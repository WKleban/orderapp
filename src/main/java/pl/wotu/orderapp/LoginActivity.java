package pl.wotu.orderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button zalogujBtn,telPrzedstBtn;
    private EditText nrAptekiEditText,nrLicencjiEditText;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private long nrAptekiLogin;
    private String licencja;
//    private String telDoPrzedst;
//    private String nazwPrzedst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.logoIV);

        nrAptekiEditText = findViewById(R.id.nr_apt_et);

        nrLicencjiEditText = findViewById(R.id.nr_licencji_et);

        zalogujBtn = findViewById(R.id.btn_zaloguj);
        telPrzedstBtn = findViewById(R.id.btn_tel_przedst);

        pref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_tag), 0); // 0 - for private mode
        editor = pref.edit();

        nrAptekiLogin = pref.getLong("nrApteki", 0);
        licencja = pref.getString("licencja","Brak licencji");
//        nazwPrzedst = pref.getString("nazwPrzedst","");
//        telDoPrzedst = pref.getString("telDoPrzedst","");

        if (nrAptekiLogin>0){
            nrAptekiEditText.setText(""+nrAptekiLogin);
            nrLicencjiEditText.setText(licencja);
            zalogujBtn.setText("Zaloguj się ponownie");
            telPrzedstBtn.setVisibility(View.VISIBLE);
            telPrzedstBtn.setText("Zadzwoń: Dział telemarketingu");
            telPrzedstBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_MENU_CONTACT_REPRESENTATIVE_ITEM = new Intent(Intent.ACTION_DIAL);
                    intent_MENU_CONTACT_REPRESENTATIVE_ITEM.setData(Uri.parse("tel:" + "800000900"));
                    startActivity(intent_MENU_CONTACT_REPRESENTATIVE_ITEM);
                }
            });

        }else{
            telPrzedstBtn.setVisibility(View.GONE);
        }

        zalogujBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        Picasso.get().load("https://i1.wp.com/aptekaposasiedzku.pl/wp-content/uploads/2018/07/logo_apteka-SASIEDZKA.png").into(imageView);

        zalogujBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
//                    System.out.println("Numer apteki:"+nrAptekiEditText.getText().toString());
                    int nrApteki = Integer.parseInt(nrAptekiEditText.getText().toString());
//                    System.out.println("Licencja:"+nrLicencjiEditText.getText().toString());
                    String nrLicencji = nrLicencjiEditText.getText().toString();

                    if (WymianaPlikowFTP.sprawdzLicencje(LoginActivity.this,nrApteki,nrLicencji,getString(R.string.shared_preferences_tag))) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putLong("nrApteki", nrApteki);
                        editor.putString("licencja",nrLicencji);
                        editor.putString("nazwaApteki","APT "+nrApteki);
                        editor.commit();

                        Toast.makeText(LoginActivity.this,"Wpisano nr apteki: "+nrApteki+"\nlicencja: "+nrLicencji,Toast.LENGTH_LONG).show();

                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                        finish();

                    }else{
                        Toast.makeText(LoginActivity.this,"Logowanie nie powiodło się!",Toast.LENGTH_LONG).show();
                    }
                }
                catch (NumberFormatException exOb) {
                    Toast.makeText(LoginActivity.this,"Podaj numer apteki!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

