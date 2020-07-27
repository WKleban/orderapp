package pl.wotu.orderapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wotu.orderapp.Adapter.OrdersAdapter;
import pl.wotu.orderapp.ListModel.LiniaZPlikuOdp;
import pl.wotu.orderapp.database.DatabaseHelper;
import pl.wotu.orderapp.database.model.Order;

public class OrdersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseHelper db;
    private List<Order> ordersList = new ArrayList<>();
    private OrdersAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private long nrAptekiLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerView = findViewById(R.id.order_list_view);
        noNotesView = findViewById(R.id.empty_notes_view);

        mToolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_zamowienia));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        pref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_tag), 0); // 0 - for private mode
        editor = pref.edit();
        nrAptekiLogin = pref.getLong("nrApteki", 0);

        ArrayList<String> tempList = WymianaPlikowFTP.pobierzOdpowiedzi(OrdersActivity.this, "/mob/odp/" + nrAptekiLogin + "_odp.csv");

        Map<String, LiniaZPlikuOdp> map = new HashMap<>();
        if (tempList!=null){
            for (String tempElem:tempList){
                String[] parts = tempElem.split("\\|"); // Tak się zapisuje rozdzielenie pipe'em
    //            String nr_zam_sys = parts[0];
    //            String code = parts[1];
                String nr_zam_apt = parts[2];
                String nr_prom = parts[3];
                String kt = parts[4];
                String nazwa_leku = parts[5];
                String il_zam = parts[6];
                String il_zreal = parts[7];
                String status_odmowy = parts[8];
    //            String poprzedni_status = parts[9];
    //            String data_wyslania = parts[10];
    //            String czas_wyslania = parts[11];
                String data_pobrania = parts[12];
    //            String data_utworzenia = parts[13];
                String czas_pobrania = parts[14];
                String nazwa_pliku = parts[15];
                String oferta = parts[16];
                Float cena_p_rabat = Float.valueOf(parts[17]); //
                Float cena = Float.valueOf(parts[18]); //przekreślona

                db.removePositionFromOrder(nazwa_pliku,Integer.parseInt(kt));
                db.insertOrderPosition(nr_zam_apt,Integer.parseInt(nr_prom),Integer.parseInt(kt),Integer.parseInt(il_zam),Integer.parseInt(il_zreal),Integer.parseInt(status_odmowy),nazwa_pliku,nazwa_leku,cena_p_rabat,cena);

                //TODO TUTAJ TRZEBA ZROBIC WARUNEK DODAWANIA POZYCJI
                float nowa_wartosc_dokumentu=0f;

                nowa_wartosc_dokumentu = Float.parseFloat(il_zam)*cena_p_rabat;



                LiniaZPlikuOdp tempLiniaZPlikuOdp = map.get(nazwa_pliku);

                if (tempLiniaZPlikuOdp!=null){
                    if ((Integer.parseInt(status_odmowy)==Global.S0_OCZEKIWANIE_NA_REALIZACJE)||(tempLiniaZPlikuOdp.getStatus_odmowy()==Global.S1_W_TRAKCIE_REALIZACJI)){
                        nowa_wartosc_dokumentu = Float.parseFloat(il_zam)*cena_p_rabat;}
                    else {
                        nowa_wartosc_dokumentu = Float.parseFloat(il_zreal)*cena_p_rabat;
                    }
                    //                tempLiniaZPlikuOdp.
                    tempLiniaZPlikuOdp.setIl_zam(tempLiniaZPlikuOdp.getIl_zam()+Integer.parseInt(il_zam));
                    tempLiniaZPlikuOdp.setIl_zreal(tempLiniaZPlikuOdp.getIl_zreal()+Integer.parseInt(il_zreal));
                    tempLiniaZPlikuOdp.setStatus_odmowy(Integer.parseInt(status_odmowy));
                    tempLiniaZPlikuOdp.setNowa_wartosc_dokumentu(nowa_wartosc_dokumentu+tempLiniaZPlikuOdp.getNowa_wartosc_dokumentu());
                }else{
                    tempLiniaZPlikuOdp = new LiniaZPlikuOdp(nazwa_pliku,Integer.parseInt(status_odmowy),nowa_wartosc_dokumentu,Integer.parseInt(il_zam),Integer.parseInt(il_zreal),data_pobrania,czas_pobrania,oferta,nazwa_leku,  kt, nr_prom, nr_zam_apt);
                    map.put(nazwa_pliku,tempLiniaZPlikuOdp);
                }

                for (Map.Entry<String, LiniaZPlikuOdp> entry : map.entrySet()) {
                    tempLiniaZPlikuOdp = entry.getValue();
                    int tempStatus = tempLiniaZPlikuOdp.getStatus_odmowy();
                    int tempIlZam = 0,tempIlZreal = 0;
                    switch (tempStatus){
                        case Global.S0_OCZEKIWANIE_NA_REALIZACJE:
                        case Global.S1_W_TRAKCIE_REALIZACJI:
                        case Global.S6_BLOKADA:
                        case Global.S7_BLOKADA:
                            // nic się nie dzieje

                        case Global.S2_CZESCIOWO_ZREALIZOWANE:
                        case Global.S3_CZESCIOWO_ZREALIZOWANE:
                            // tutaj też częściowo zrealizoane, czyli nic się nie zmienia ze statusem
                            break;

                        case Global.S4_ZREALIZOWANE:
                        case Global.S5_ZREALIZOWANE:
                            tempIlZam = tempLiniaZPlikuOdp.getIl_zam();
                            tempIlZreal = tempLiniaZPlikuOdp.getIl_zreal();
                            if ((tempIlZam>tempIlZreal)&&(tempIlZam>0)){ //częściowo
                                tempLiniaZPlikuOdp.setStatus_odmowy(Global.S2_CZESCIOWO_ZREALIZOWANE); //zrealizowane
                            }else if (tempIlZam==tempIlZreal){
                                tempLiniaZPlikuOdp.setStatus_odmowy(Global.S4_ZREALIZOWANE);
                            }else if (tempIlZreal==0){ //niezrealizowane
                                tempLiniaZPlikuOdp.setStatus_odmowy(Global.S8_NIE_ZREALIZOWANE);
                            }else {
                                //błędna ilość... większa niż
                                tempLiniaZPlikuOdp.setStatus_odmowy(10);
                            }

                            break;
                        case Global.S8_NIE_ZREALIZOWANE:
                        case Global.S9_NIE_ZREALIZOWANE:
//                            tempIlZam = tempLiniaZPlikuOdp.getIl_zam();
//                            tempIlZreal = tempLiniaZPlikuOdp.getIl_zreal();
//                            if ((tempIlZam>tempIlZr/eal)&&(tempIlZam>0)){ //częściowo
//                                tempLiniaZPlikuOdp.setStatus_odmowy(Global.S2_CZESCIOWO_ZREALIZOWANE); //zrealizowane
//                            }else if (tempIlZam==tempIlZreal){
//                                tempLiniaZPlikuOdp.setStatus_odmowy(Global.S4_ZREALIZOWANE);
//                            }else if (tempIlZreal==0){ //niezrealizowane
//                                tempLiniaZPlikuOdp.setStatus_odmowy(Global.S8_NIE_ZREALIZOWANE);
//                            }else {
//                                //błędna ilość... większa niż
//                                tempLiniaZPlikuOdp.setStatus_odmowy(10);
//                            }

                            tempLiniaZPlikuOdp.setStatus_odmowy(Global.S8_NIE_ZREALIZOWANE);
                            break;
                    }
                    int dbResponse = db.updateOrderStatus(tempLiniaZPlikuOdp.getNazwa_pliku(),tempLiniaZPlikuOdp.getStatus_odmowy(),String.format("%.2f", tempLiniaZPlikuOdp.getNowa_wartosc_dokumentu())+" zł");

                    if (dbResponse == 0){
                        tempLiniaZPlikuOdp.getIl_zam();
                        tempLiniaZPlikuOdp.getIl_zreal();

                        String nr_zam = "APS";
                        String date = tempLiniaZPlikuOdp.getDate();
                        String czas = tempLiniaZPlikuOdp.getCzas();

                        int status = tempLiniaZPlikuOdp.getStatus_odmowy();
                        int il_poz = 0;

                        String wartosc =  String.format("%.2f", tempLiniaZPlikuOdp.getNowa_wartosc_dokumentu())+" zł";
                        String plik = tempLiniaZPlikuOdp.getNazwa_pliku();
                        oferta = tempLiniaZPlikuOdp.getOferta();
                        db.insertOrder(nr_zam,date,czas,status,il_poz,plik,oferta);
                    }
                    entry.setValue(tempLiniaZPlikuOdp);
                }
            }
        }

        ordersList.addAll(db.getAllOrders());

        if (ordersList.size() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }

        mAdapter = new OrdersAdapter(OrdersActivity.this,this, ordersList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

//        ordersList.
//        NAGŁÓWEK
//        Nr zamówienia
//        Data zamówienia
//        Czas
//        Status
//        Ile pozycji

//        POZYCJA
//        Nazwa leku
//        Ilość zamówiona
//        Ilość zrealizowana
//        Status

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }



}
