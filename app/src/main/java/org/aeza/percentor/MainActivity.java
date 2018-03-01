package org.aeza.percentor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View mainView;

    Button addButton;
    Button excelBtn;
    LinearLayout linearLayout;
    EditText didForm;
    EditText tempEditTextView;
    boolean isAlreadyDone = true;

    SeekBar hekmatSeekerBar;
    SeekBar viraSeekerBar;
    SeekBar colonySeekerBar;
    SeekBar colonyPrecisionSeekerBar;
    SeekBar bimeSeekerBar;
    SeekBar bimePrecisionSeekerBar;
    SeekBar brandSeekerBar;
    SeekBar creditSeekerBar;

    TextView hekmatEditText;
    TextView viraEditText;
    TextView colonyEditText;
    TextView colonyPrecisionEditText;
    TextView bimeEditText;
    TextView bimePrecisionEditText;
    TextView brandEditText;
    TextView creditEditText;

    Cal cal;
    ArrayList<EditText> inputs = new ArrayList<>();
    public String tempSumForExcel;
    public Cal tempCalForExcel;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 777;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onPause() {
        editor = preferences.edit();
        editor.putInt("hekmatPercentage", Cal.hekmatPercentage);
        editor.putInt("creditPercentage", Cal.creditPercentage);
        editor.putInt("viraPercentage", Cal.viraPercentage);
        editor.putInt("colonyPercentage", Cal.colonyPercentage);
        editor.putInt("colonyPrecisionPercentage", Cal.colonyPrecisionPercentage);
        editor.putInt("bimePercentage", Cal.bimePercentage);
        editor.putInt("bimePrecisionPercentage", Cal.bimePrecisionPercentage);
        editor.putInt("brandPercentage", Cal.brandPercentage);
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getPreferences(MODE_PRIVATE);

        Cal.hekmatPercentage = preferences.getInt("hekmatPercentage", 49);
        Cal.creditPercentage = preferences.getInt("creditPercentage", 0);
        Cal.viraPercentage = preferences.getInt("viraPercentage", 51);
        Cal.colonyPercentage = preferences.getInt("colonyPercentage", 25);
        Cal.colonyPrecisionPercentage = preferences.getInt("colonyPrecisionPercentage", 4);
        Cal.bimePercentage = preferences.getInt("bimePercentage", 4);
        Cal.bimePrecisionPercentage = preferences.getInt("bimePrecisionPercentage", 6);
        Cal.brandPercentage = preferences.getInt("brandPercentage", 1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage(initCal())
                            .show();
                } catch (Exception ex) {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("ورودی ای یافت نشد")
                            .show();
                }
            }
        });


        handlePercentage();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addButton = (Button) findViewById(R.id.add);
        excelBtn = (Button) findViewById(R.id.excelBtn);
        linearLayout = (LinearLayout) findViewById(R.id.mainLayout);
        didForm = (EditText) findViewById(R.id.didForm);
        didForm.setOnEditorActionListener(mainFormListener);
        didForm.setSelection(didForm.getText().length());
        mainView = findViewById(R.id.mainMenu);

        createField(linearLayout, savedInstanceState != null ? savedInstanceState.getString("inputs0", "") : "");

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                createField(v, "");
            }
        });

        excelBtn.setOnClickListener(new ToExcelClass());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        for (int i = 0; i < inputs.size(); i++) {
            outState.putString("inputs" + i, inputs.get(i).getText().toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        for (int i = 1; i < savedInstanceState.size() - 1; i++) {
            createField(linearLayout, savedInstanceState.getString("inputs" + i, ""));
        }
    }

    private String initCal() {
        cal = new Cal();
        StringBuilder sb = new StringBuilder();
        double tempSum = 0;
        int i = 1;
        for (EditText ins : inputs) {
            String tempIn = ins.getText().toString();
            double tempDb = Double.valueOf(tempIn.replace(",", ""));
            sb.append(i++ + ") " + tempIn + "\n" + cal.singleFactorCalculate(tempDb) + "\n");
            tempSum += tempDb;
        }

        cal.calculate(tempSum);
        cal.writeRaw();
        cal.writeRounded();
        sb.insert(0, "Total in Rial: "
                + String.valueOf((int) tempSum * 10)
                .replaceAll("(\\d)(?=(\\d{3})+$)", "$1,")
                + "\nbrand: " + Cal.brandPercentage + "% credit: " + Cal.creditPercentage + "% \ncolony: " + Cal.colonyPercentage + "% bime: " + Cal.bimePercentage + "% \nvira: " + Cal.viraPercentage + "% hekmat: " + Cal.hekmatPercentage + "%"
                + "\n-----------------------------------\n"
                + cal.roundedContent + cal.rawContent);

        return sb.toString();
    }

    private void handlePercentage() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        hekmatSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarHekmat);
        viraSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarVira);
        colonySeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarColony);
        colonyPrecisionSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarColonyPrecision);
        bimeSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarBime);
        bimePrecisionSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarBimePrecision);
        brandSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarBrand);
        creditSeekerBar = (SeekBar) headerView.findViewById(R.id.seekBarCredit);

        hekmatEditText = (TextView) headerView.findViewById(R.id.editTextHekmat);
        viraEditText = (TextView) headerView.findViewById(R.id.editTextVira);
        colonyEditText = (TextView) headerView.findViewById(R.id.editTextColony);
        colonyPrecisionEditText = (TextView) headerView.findViewById(R.id.editTextColonyPrecision);
        bimeEditText = (TextView) headerView.findViewById(R.id.editTextBime);
        bimePrecisionEditText = (TextView) headerView.findViewById(R.id.editTextBimePrecision);
        brandEditText = (TextView) headerView.findViewById(R.id.editTextBrand);
        creditEditText = (TextView) headerView.findViewById(R.id.editTextCredit);

        hekmatSeekerBar.setMax(100);
        viraSeekerBar.setMax(100);
        colonySeekerBar.setMax(50);
        colonyPrecisionSeekerBar.setMax(9);
        bimeSeekerBar.setMax(20);
        bimePrecisionSeekerBar.setMax(9);
        brandSeekerBar.setMax(30);
        creditSeekerBar.setMax(30);

        hekmatSeekerBar.setProgress(Cal.hekmatPercentage);
        viraSeekerBar.setProgress(Cal.viraPercentage);
        colonySeekerBar.setProgress((Cal.colonyPercentage));
        colonyPrecisionSeekerBar.setProgress((Cal.colonyPrecisionPercentage));
        bimeSeekerBar.setProgress(Cal.bimePercentage);
        bimePrecisionSeekerBar.setProgress(Cal.bimePrecisionPercentage);
        brandSeekerBar.setProgress(Cal.brandPercentage);
        creditSeekerBar.setProgress(Cal.creditPercentage);

        hekmatEditText.setText(hekmatSeekerBar.getProgress() + "% Hekmat");
        viraEditText.setText(viraSeekerBar.getProgress() + "% Vira");
        colonyEditText.setText("" + Cal.colonyPercentage + "." + Cal.colonyPrecisionPercentage + "% Colony");
        colonyPrecisionEditText.setText(" ." + colonyPrecisionSeekerBar.getProgress());
        bimeEditText.setText("" + Cal.bimePercentage + "." + Cal.bimePrecisionPercentage + "% Bime");
        bimePrecisionEditText.setText(" ." + bimePrecisionSeekerBar.getProgress());
        brandEditText.setText(brandSeekerBar.getProgress() + "% Brand");
        creditEditText.setText(creditSeekerBar.getProgress() + "% Credit");

        hekmatSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        viraSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        colonySeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        colonyPrecisionSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        bimeSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        bimePrecisionSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        brandSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        creditSeekerBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // TODO Auto-generated method stub

            switch (seekBar.getId()) {
                case R.id.seekBarHekmat:
                    Cal.hekmatPercentage = progress;
                    Cal.viraPercentage = 100 - progress;
                    hekmatEditText.setText(progress + "% Hekmat");
                    viraSeekerBar.setProgress(100 - progress);
                    break;
                case R.id.seekBarVira:
                    Cal.viraPercentage = progress;
                    Cal.hekmatPercentage = 100 - progress;
                    viraEditText.setText(progress + "% Vira");
                    hekmatSeekerBar.setProgress(100 - progress);
                    break;
                case R.id.seekBarColony:
                    if (progress > Cal.colonyPrecisionPercentage && progress < colonySeekerBar.getMax()) {
                        Cal.colonyPercentage = Cal.colonyPrecisionPercentage + progress;
                        colonyEditText.setText("" + Cal.colonyPercentage + "." + Cal.colonyPrecisionPercentage + "% Colony");
                    }else {
                        Cal.colonyPercentage = progress;
                        colonyEditText.setText("" + Cal.colonyPercentage + "." + Cal.colonyPrecisionPercentage + "% Colony");
                    }
                    break;
                case R.id.seekBarColonyPrecision:
                    Cal.colonyPrecisionPercentage = progress;
                    colonyEditText.setText("" + Cal.colonyPercentage + "." + Cal.colonyPrecisionPercentage + "% Colony");
                    colonyPrecisionEditText.setText(" ." + progress);
                    break;
                case R.id.seekBarBime:
                    if (progress > Cal.bimePrecisionPercentage && progress < bimeSeekerBar.getMax()) {
                        Cal.bimePercentage = Cal.bimePrecisionPercentage + progress;
                        bimeEditText.setText("" + Cal.bimePercentage + "." + Cal.bimePrecisionPercentage + "% Bime");
                    }else {
                        Cal.bimePercentage = progress;
                        bimeEditText.setText("" + Cal.bimePercentage + "." + Cal.bimePrecisionPercentage + "% Bime");
                    }
                    break;
                case R.id.seekBarBimePrecision:
                    Cal.bimePrecisionPercentage = progress;
                    bimeEditText.setText("" + Cal.bimePercentage + "." + Cal.bimePrecisionPercentage + "% Bime");
                    bimePrecisionEditText.setText(" ." + progress);
                    break;
                case R.id.seekBarBrand:
                    Cal.brandPercentage = progress;
                    brandEditText.setText(progress + "% Brand");
                    break;
                case R.id.seekBarCredit:
                    Cal.creditPercentage = progress;
                    creditEditText.setText(progress + "% Credit");
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up addButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Seyyed.Alireza.Jamali@gmail.com", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createField(View v, String text) {

        final EditText editTextView = new EditText(v.getContext());
        tempEditTextView = editTextView;
        editTextView.setOnEditorActionListener(mainFormListener);
        editTextView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextView.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        editTextView.setHeight(70);
        editTextView.setPadding(10, 0, 0, 0);

        editTextView.setGravity(Gravity.TOP);
        editTextView.setBackgroundColor(Color.parseColor("#CFDBCC"));
        editTextView.requestFocus();
        editTextView.setText(text);
        editTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isAlreadyDone) {
                    isAlreadyDone = false;
                    try {
                        editTextView.setText(NumberFormat.getIntegerInstance().format(Long.valueOf(s.toString().replaceAll("[^0-9]+", ""))));
                        editTextView.setSelection(editTextView.getText().length());
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isAlreadyDone = true;
            }
        });

        Space space = new Space(v.getContext());
        space.setMinimumHeight(10);
        linearLayout.addView(space);
        linearLayout.addView(editTextView);

        inputs.add(editTextView);
    }

    TextView.OnEditorActionListener mainFormListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                //I had to use this way to Select DID form, couldn't find another solution
                if ("DID کد".equals(view.getHint())) {
                    createField(view, "");
                    tempEditTextView.requestFocus();
                } else {
                    didForm.requestFocus();
                    if (!didForm.getText().toString().equals("")) {
                        didForm.append(" ");
                    }
                }
            }
            return true;
        }
    };

    class ToExcelClass implements View.OnClickListener {

        @Override
        public void onClick(View v) {

//            Cal.erase();
//            arCal.clear();
//            arInputs.clear();

            cal = new Cal();
            StringBuilder sb = new StringBuilder();
            double tempSum = 0;
            int i = 1;
            for (EditText ins : inputs) {
                String tempIn = ins.getText().toString();
                double tempDb = Double.valueOf(tempIn.replace(",", ""));
                sb.append(i++ + ") " + tempIn + "\n" + cal.singleFactorCalculate(tempDb) + "\n");
                tempSum += tempDb;
            }

            cal.calculate(tempSum);
            cal.writeRaw();
            cal.writeRounded();
            tempSumForExcel = String.valueOf((int) tempSum * 10).replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");

            tempCalForExcel = cal;
            requestPermission(v);
        }
    }

    private void requestPermission(View v) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(v, "اجازه برای ایجاد فولدر در حافظه داخلی", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            createExcel();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    createExcel();

                } else {
                    new AlertDialog.Builder(mainView.getContext())
                            .setMessage("بدون اجازه ذخیره فایل ممکن نخواهد شد.")
                            .show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void createExcel() {

        try {
            ToExcel toExcel = new ToExcel();
            toExcel.init(MainActivity.this).close();
            Snackbar.make(mainView, toExcel.filePath + " :محل ذخیره فایل", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            try {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("nextapp.fx");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            } catch (Exception ex) {
                System.out.println("can't open filemanager app");
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

}
