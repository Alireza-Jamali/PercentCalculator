package org.aeza.percentor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Cal {

    public static int hekmatPercentage;
    public static int creditPercentage;
    public static int viraPercentage;
    public static int colonyPercentage;
    public static int colonyPrecisionPercentage;
    public static int bimePercentage;
    public static int bimePrecisionPercentage;
    public static int brandPercentage ;


    String roundedContent;
    String rawContent;
    BigDecimal brandPercent;
    public BigDecimal brand;
    public BigDecimal creditCard;
    public BigDecimal colony;
    public BigDecimal bime;
    public BigDecimal vira;
    public BigDecimal hekmat;


    public String roundedBrandForExcel;
    public String roundedCreditCardForExcel;
    public String roundedColonyForExcel;
    public String roundedBimeForExcel;
    public String roundedViraForExcel;
    public String roundedHekmatForExcel;

    public BigDecimal rawBrandForExcel;
    public BigDecimal rawCreditCardForExcel;
    public BigDecimal rawColonyForExcel;
    public BigDecimal rawBimeForExcel;
    public BigDecimal rawViraForExcel;
    public BigDecimal rawHekmatForExcel;


    public String writeRounded() {

        rawBrandForExcel = brand;
        rawCreditCardForExcel = creditCard;
        rawColonyForExcel = colony;
        rawBimeForExcel = bime;
        rawViraForExcel = vira;
        rawHekmatForExcel = hekmat;


        brandPercent = brandPercent.setScale(0, RoundingMode.HALF_EVEN);
        creditCard = creditCard.setScale(0, RoundingMode.HALF_EVEN);
        brand = brand.setScale(0, RoundingMode.HALF_EVEN);
        colony = colony.setScale(0, RoundingMode.HALF_EVEN);
        bime = bime.setScale(0, RoundingMode.HALF_EVEN);
        vira = vira.setScale(0, RoundingMode.HALF_EVEN);
        hekmat = hekmat.setScale(0, RoundingMode.HALF_EVEN);

        roundedContent = "colony: " + colony + "\n";
        roundedContent += "vira: " + vira + "\n";
        roundedContent += "bime: " + bime + "\n";
        roundedContent += "hekmat: " + hekmat + "\n";
        roundedContent += "brand: " + brand + "\n";
        roundedContent += "CreditCard: " + creditCard + "\n\n";

        return roundedContent;
    }

    public String writeRaw() {

        brand = brand.setScale(2, RoundingMode.HALF_EVEN);
        creditCard = creditCard.setScale(2, RoundingMode.HALF_EVEN);
        colony = colony.setScale(2, RoundingMode.HALF_EVEN);
        bime = bime.setScale(2, RoundingMode.HALF_EVEN);
        vira = vira.setScale(2, RoundingMode.HALF_EVEN);
        hekmat = hekmat.setScale(2, RoundingMode.HALF_EVEN);

        rawContent = "colony: " + colony.setScale(2, RoundingMode.HALF_EVEN) + "\n";
        rawContent += "vira: " + vira.setScale(2, RoundingMode.HALF_EVEN) + "\n";
        rawContent += "bime: " + bime.setScale(2, RoundingMode.HALF_EVEN) + "\n";
        rawContent += "hekmat: " + hekmat.setScale(2, RoundingMode.HALF_EVEN) + "\n";
        rawContent += "brand: " + brand.setScale(2, RoundingMode.HALF_EVEN) + "\n";
        rawContent += "CreditCard: " + creditCard.setScale(2, RoundingMode.HALF_EVEN) + "\n\n";

        return rawContent;
    }

    void calculate(double num) {

        BigDecimal input = new BigDecimal(num);
        brandPercent = input.multiply(new BigDecimal(brandPercentage)).divide(new BigDecimal(10));
        creditCard = input.multiply(new BigDecimal(creditPercentage)).divide(new BigDecimal(10));
        BigDecimal creditSumPercent = brandPercent.add(creditCard);
        brand = input.multiply(new BigDecimal(10)).subtract(creditSumPercent);
        colony = brandPercent.multiply(new BigDecimal((float) colonyPercentage > 0 ? ((float)colonyPercentage + (float)colonyPrecisionPercentage/10) : 1)).divide(new BigDecimal(100));
        bime = brandPercent.multiply(new BigDecimal((float) bimePercentage > 0 ? ((float)bimePercentage + (float)bimePrecisionPercentage/10) : 1)).divide(new BigDecimal(100));
        BigDecimal vk = brandPercent.subtract(colony.add(bime));
        vira = vk.multiply(new BigDecimal(viraPercentage)).divide(new BigDecimal(100));
        hekmat = vk.multiply(new BigDecimal(hekmatPercentage)).divide(new BigDecimal(100));
    }

    String singleFactorCalculate(double num) {

        String output = "";
        BigDecimal input = new BigDecimal(num);
        BigDecimal brandPercent = input.multiply(new BigDecimal(brandPercentage)).divide(new BigDecimal(10));
        BigDecimal creditCard = input.multiply(new BigDecimal(creditPercentage)).divide(new BigDecimal(10));
        BigDecimal creditSumPercent = brandPercent.add(creditCard);
        BigDecimal brand = input.multiply(new BigDecimal(10)).subtract(creditSumPercent);
        BigDecimal colony = brandPercent.multiply(new BigDecimal((float) colonyPercentage > 0 ? ((float)colonyPercentage + (float)colonyPrecisionPercentage/10) : 1)).divide(new BigDecimal(100));
        BigDecimal bime = brandPercent.multiply(new BigDecimal((float) bimePercentage > 0 ? ((float)bimePercentage + (float)bimePrecisionPercentage/10) : 1)).divide(new BigDecimal(100));
        BigDecimal vk = brandPercent.subtract(colony.add(bime));
        BigDecimal vira = vk.multiply(new BigDecimal(viraPercentage)).divide(new BigDecimal(100));
        BigDecimal hekmat = vk.multiply(new BigDecimal(hekmatPercentage)).divide(new BigDecimal(100));

        brand = brand.setScale(0, RoundingMode.HALF_EVEN);
        creditCard = creditCard.setScale(0, RoundingMode.HALF_EVEN);
        colony = colony.setScale(0, RoundingMode.HALF_EVEN);
        bime = bime.setScale(0, RoundingMode.HALF_EVEN);
        vira = vira.setScale(0, RoundingMode.HALF_EVEN);
        hekmat = hekmat.setScale(0, RoundingMode.HALF_EVEN);

        output = "colony: " + colony + "\n";
        output += "vira: " + vira + "\n";
        output += "bime: " + bime + "\n";
        output += "hekmat: " + hekmat + "\n";
        output += "brand: " + brand + "\n";
        output += "creditCard: " + creditCard + "\n";

        return output;
    }

    //calculate and roundup
    public void singleFactorCalculateForExcel(double num) {

        BigDecimal input = new BigDecimal(num);
        BigDecimal brandPercent = input.multiply(new BigDecimal(brandPercentage)).divide(new BigDecimal(10));
        BigDecimal creditCard = input.multiply(new BigDecimal(creditPercentage)).divide(new BigDecimal(10));
        BigDecimal creditSumPercent = brandPercent.add(creditCard);
        BigDecimal brand = input.multiply(new BigDecimal(10)).subtract(creditSumPercent);
        BigDecimal colony = brandPercent.multiply(new BigDecimal((float) colonyPercentage > 0 ? ((float)colonyPercentage + (float)colonyPrecisionPercentage/10) : 1)).divide(new BigDecimal(100));
        BigDecimal bime = brandPercent.multiply(new BigDecimal((float) bimePercentage > 0 ? ((float)bimePercentage + (float)bimePrecisionPercentage/10) : 1)).divide(new BigDecimal(100));
        BigDecimal vk = brandPercent.subtract(colony.add(bime));
        BigDecimal vira = vk.multiply(new BigDecimal(viraPercentage)).divide(new BigDecimal(100));
        BigDecimal hekmat = vk.multiply(new BigDecimal(hekmatPercentage)).divide(new BigDecimal(100));

        brand = brand.setScale(0, RoundingMode.HALF_EVEN);
        creditCard = creditCard.setScale(0, RoundingMode.HALF_EVEN);
        colony = colony.setScale(0, RoundingMode.HALF_EVEN);
        bime = bime.setScale(0, RoundingMode.HALF_EVEN);
        vira = vira.setScale(0, RoundingMode.HALF_EVEN);
        hekmat = hekmat.setScale(0, RoundingMode.HALF_EVEN);


        roundedBrandForExcel = brand.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
        roundedCreditCardForExcel = creditCard.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
        roundedColonyForExcel = colony.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
        roundedBimeForExcel = bime.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
        roundedViraForExcel = vira.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
        roundedHekmatForExcel = hekmat.toString().replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");


    }
}
