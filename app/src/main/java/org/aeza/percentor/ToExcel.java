package org.aeza.percentor;

//import java.awt.Desktop;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ToExcel {
    String filePath;

    public FileOutputStream init(MainActivity mainActivity) {

        String fileName = "DID" + mainActivity.didForm.getText().toString().replaceAll(" ", " DID");
        String filePath = Environment.getExternalStorageDirectory() + "/ExcelOutputs";

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet(fileName);

        //This data needs to be written (Object[])
        Map<Integer, String[]> data = new TreeMap<>();
        int rowCounter = 1;
        if(fileName.length() > 85) {
            data.put(rowCounter++, new String[]{fileName.substring(0, 83)});
            data.put(rowCounter++, new String[]{fileName.substring(84, fileName.length())});
        }else {
            data.put(rowCounter++, new String[]{fileName});
        }
        data.put(rowCounter++, new String[]{"Total/Rial", mainActivity.tempSumForExcel});
        data.put(rowCounter++, new String[]{
                "colony: " + Cal.colonyPercentage + "." + Cal.colonyPrecisionPercentage + "%",
                "vira: " + Cal.viraPercentage + "%",
                "bime: " + Cal.bimePercentage + "." + Cal.bimePrecisionPercentage + "%",
                "hekmat: " + Cal.hekmatPercentage + "%",
                "brand: " + Cal.brandPercentage + "%",
                "CreditCard: " + Cal.creditPercentage + "%"});
        data.put(rowCounter++, new String[]{
                mainActivity.tempCalForExcel.colony.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,"),
                mainActivity.tempCalForExcel.vira.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,"),
                mainActivity.tempCalForExcel.bime.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,"),
                mainActivity.tempCalForExcel.hekmat.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,"),
                mainActivity.tempCalForExcel.brand.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,"),
                mainActivity.tempCalForExcel.creditCard.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,")});
        data.put(rowCounter++, new String[]{
                mainActivity.tempCalForExcel.rawColonyForExcel.toString(),
                mainActivity.tempCalForExcel.rawViraForExcel.toString(),
                mainActivity.tempCalForExcel.rawBimeForExcel.toString(),
                mainActivity.tempCalForExcel.rawHekmatForExcel.toString(),
                mainActivity.tempCalForExcel.rawBrandForExcel.toString(),
                mainActivity.tempCalForExcel.rawCreditCardForExcel.toString()});
        data.put(rowCounter++, new String[]{"" ,"" ,"" ,""});
        data.put(rowCounter++, new String[]{"" , "" ,"Colony" ,"Vira" ,"Bime" ,"Hekmat" ,"Brand" ,"CreditCard"});

        for (int i=0; i < mainActivity.inputs.size(); i++) {
            mainActivity.tempCalForExcel.singleFactorCalculateForExcel(Double.valueOf(mainActivity.inputs.get(i).getText().toString().replace(",", "")));
            data.put(rowCounter++, new String[]{i+1 + ") " +
                    mainActivity.inputs.get(i).getText().toString(),
                    mainActivity.tempCalForExcel.roundedColonyForExcel,
                    mainActivity.tempCalForExcel.roundedViraForExcel,
                    mainActivity.tempCalForExcel.roundedBimeForExcel,
                    mainActivity.tempCalForExcel.roundedHekmatForExcel,
                    mainActivity.tempCalForExcel.roundedBrandForExcel,
                    mainActivity.tempCalForExcel.roundedCreditCardForExcel});
        }
        data.put(rowCounter++, new String[]{""});

        //Iterate over data and write to sheet
        //Set<String> keyset = data.keySet();

        int rownum = 0;
        int[] columnWidthList = new int[8];
        for (int key : data.keySet()) {
            //create a row of excelsheet
            Row row = sheet.createRow(rownum++);

            //get object array of prerticuler key
            String[] objArr = data.get(key);

            int cellnum = 0;

            for (int i=0; i<objArr.length; i++) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(objArr[i]);
                int cellLentgh = objArr[i].length();
                if (cellLentgh>columnWidthList[i]) {
                    columnWidthList[i] = cellLentgh;
                }
            }
        }
        for (int j = 0; j < 8; j++) {
            if(j == 0){
                sheet.setColumnWidth(0, (mainActivity.tempCalForExcel.rawColonyForExcel.toString().length() < 12 ? 13 : mainActivity.tempCalForExcel.rawColonyForExcel.toString().length()) * 240);
                continue;
            }
            sheet.setColumnWidth(j, columnWidthList[j]*270);
        }
        try {
            File excelFileDirectory = new File(filePath);
            if (!excelFileDirectory.exists()) {
                if (!excelFileDirectory.mkdirs()) {
                    return null;
                }
            }
            int counter = 2;
            for (int i = 0; i< excelFileDirectory.listFiles().length; i++) {
                if(fileName.equals(excelFileDirectory.listFiles()[i].getName().replace(".xlsx", ""))) {
                    fileName = fileName.replaceAll("-.*", "");
                    fileName += "-" + counter++;
                    i = 0;
            }
            }
            File excelFile = new File(excelFileDirectory , fileName + ".xlsx");
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(excelFile);
            workbook.write(out);
            System.out.println("file written successfully on disk.");
            this.filePath = filePath;
//            mainActivity.printFile(excelFile);

//            Desktop.getDesktop().open(new File(excelFile.getParent()));
//            Desktop.getDesktop().open(excelFile);
            return out;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
