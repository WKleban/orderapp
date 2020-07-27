package pl.wotu.orderapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IloscZamActivity extends AppCompatActivity {

    private String nazwa;
    private String producent;
    private String pozycja;
    private String cenaHurt;
    private double cenaPoRab;
    private int iloscZam;
    private TextView producentView,nazwaView,pozycjaView,cenaRazyIloscView,wartoscZamView;
    private FloatingActionButton minusView,plusView;
    private Button zatwierdzButton;
    private int kt;
    private double wartoscZam;
    private String pozycjaWOfercie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilosc_zam);
        Bundle bundle = getIntent().getExtras();

        kt = bundle.getInt("kt");
        nazwa = bundle.getString("nazwaView");
        iloscZam = bundle.getInt("iloscZamView");
        pozycja = bundle.getString("pozycjaView");
        pozycjaWOfercie =  bundle.getString("pozycjaWOfercieTV");
//        cenaHurt = bundle.getString("cenaHurtView");
        cenaPoRab = bundle.getDouble("cenaPoRab");
        producent = bundle.getString("producentView");

        nazwaView = findViewById(R.id.nazwa_lek_details_item);
        producentView = findViewById(R.id.producent_lek_details_item);
        cenaRazyIloscView = findViewById(R.id.cena_razy_ilosc_details_item);
        wartoscZamView = findViewById(R.id.wartosc_textView);
//        cenaHurtView = findViewById(R.id.cena_hurt_lek_details_item);
//        cenaPoRabView = findViewById(R.id.cena_porab_lek_details_item);
//        iloscZamView = findViewById(R.id.ilosc_zam_lek_details_item);
        cenaRazyIloscView.setText(iloscZam+" * "+String.format("%.2f", round(cenaPoRab,2))+" zł");

        wartoscZam = iloscZam*cenaPoRab;

        wartoscZamView.setText("Wartość: "+String.format("%.2f", round(wartoscZam,2))+" zł");

        minusView = findViewById(R.id.minus_lek_details_item);
        plusView = findViewById(R.id.plus_lek_details_item);
        pozycjaView = findViewById(R.id.pozycja_detailsTV);

        zatwierdzButton = findViewById(R.id.zatwierdzIloscDoZamButton);

        if (iloscZam==0) {

            iloscZam +=1;
            wartoscZam = iloscZam*cenaPoRab;
            cenaRazyIloscView.setText(iloscZam+" * "+String.format("%.2f", round(cenaPoRab,2))+" zł");
            wartoscZamView.setText("Wartość: "+String.format("%.2f", round(wartoscZam,2))+" zł");

            minusView.setVisibility(View.VISIBLE);
//            minusView.setVisibility(View.INVISIBLE);

//            zatwierdzButton.setBackgroundColor(getResources().getColor(R.color.colorMaterialGreen));
        }else{
            minusView.setVisibility(View.VISIBLE);
        }
        zatwierdzButton.setText("Dodaj do zamówienia "+iloscZam+ " szt.");
        zatwierdzButton.setBackgroundColor(getResources().getColor(R.color.colorMaterialGreen));


        nazwaView.setText(nazwa);
        producentView.setText(producent);
//        cenaHurtView.setText(cenaHurt);
//        cenaPoRabView.setText(cenaPoRab);
//        iloscZamView.setText(iloscZam+"");
        pozycjaView.setText(pozycja+"");

        plusView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                iloscZam +=1;
                wartoscZam = iloscZam*cenaPoRab;
                cenaRazyIloscView.setText(iloscZam+" * "+String.format("%.2f", round(cenaPoRab,2))+" zł");
                wartoscZamView.setText("Wartość: "+String.format("%.2f", round(wartoscZam,2))+" zł");
                minusView.setVisibility(View.VISIBLE);

                zatwierdzButton.setBackgroundColor(getResources().getColor(R.color.colorMaterialGreen));
                zatwierdzButton.setText("Dodaj do zamówienia "+iloscZam+ " szt.");


            }
        });

        minusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iloscZam -=1;
                wartoscZam = iloscZam*cenaPoRab;
                cenaRazyIloscView.setText(iloscZam+" * "+String.format("%.2f", round(cenaPoRab,2))+" zł");
                wartoscZamView.setText("Wartość: "+round(wartoscZam,2)+" zł");
                if (iloscZam==0) {
                    minusView.setVisibility(View.INVISIBLE);

                    zatwierdzButton.setBackgroundColor(getResources().getColor(R.color.colorMaterialRed));
                    zatwierdzButton.setText("Nie chcę teraz zamawiać tego leku");
                }
                else {
                    zatwierdzButton.setBackgroundColor(getResources().getColor(R.color.colorMaterialGreen));
                    zatwierdzButton.setText("Dodaj do zamówienia "+iloscZam+ " szt.");

                }
            }
        });

        zatwierdzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nazwa",nazwaView.getText().toString());
                resultIntent.putExtra("kt",kt);
                resultIntent.putExtra("iloscZam",String.valueOf(iloscZam));
                resultIntent.putExtra("pozycjaView",pozycjaView.getText().toString());
                resultIntent.putExtra("pozycjaWOfercieTV",pozycjaWOfercie);
                resultIntent.putExtra("cenaPoRabacie",cenaPoRab);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
