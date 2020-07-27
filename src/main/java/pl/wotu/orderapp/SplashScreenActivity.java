package pl.wotu.orderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreenActivity extends ParentActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private long nrAptekiLogin;
    private String licencja;
    private TextView tvSplash;
    private ProgressBar pbSplash;
    private LinearLayout layoutTextSplash;

    private String komunikat="";
    private Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make us non-modal, so that others can receive touch events.
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        setContentView(R.layout.activity_splash_screen);


        tvSplash = findViewById(R.id.textViewSplash);
        pbSplash = findViewById(R.id.progressBarSplash);
        layoutTextSplash = findViewById(R.id.layoutTextSplash);

//        tvSplash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        pref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_tag), 0); // 0 - for private mode


        editor = pref.edit();

        nrAptekiLogin = pref.getLong("nrApteki", 0);
        licencja = pref.getString("licencja","Brak licencji");

//        System.out.println("Nr apteki [Splash]"+nrAptekiLogin);

        if (nrAptekiLogin==0){
            Intent loginIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();

        }
        else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            startDownloadOfferThread();
        }
    }


    protected void startDownloadOfferThread() {
        t = new Thread() {
            public void run() {
                sprawdzInternetIPobierzOferte();
                SystemClock.sleep(3500);
                finish();
            }
        };
        t.start();
    }

    private void sprawdzInternetIPobierzOferte() {
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        boolean polaczenieZBaza=WymianaPlikowFTP.sprawdzPolaczenieZBaza(SplashScreenActivity.this);
        if(networkInfo != null && networkInfo.isConnected()==true&&polaczenieZBaza==true )
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    komunikat="Łączenie z serwerem...";
                    tvSplash.setText(komunikat);
                }
            });
            if (WymianaPlikowFTP.sprawdzLicencje(SplashScreenActivity.this,nrAptekiLogin,licencja,getString(R.string.shared_preferences_tag))){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pbSplash.setVisibility(View.VISIBLE);
                        komunikat="Pobieranie oferty...";
                        tvSplash.setText(komunikat);
                    }
                });
                super.listaLekow = WymianaPlikowFTP.pobierz(SplashScreenActivity.this, "/mob/ofe/oferta.txt");

                Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            else//BRAK PLIKU Z LICENCJĄ
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        komunikat="Brak dostępu do systemu, skontaktuj się z Hurtownią";
                        pbSplash.setVisibility(View.GONE);
                        tvSplash.setText(komunikat);
                        Intent loginIntent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                });
            }
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    komunikat="Brak dostępu do internetu.\nPołącz z internetem i spróbuj ponownie.";
                    pbSplash.setVisibility(View.GONE);
                    tvSplash.setText(komunikat);
                }
            });
        }
    }
}
