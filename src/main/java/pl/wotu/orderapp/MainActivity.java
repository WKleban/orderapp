package pl.wotu.orderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.wotu.orderapp.Adapter.LekAdapter;
import pl.wotu.orderapp.Adapter.ZamowionyLekAdapter;
import pl.wotu.orderapp.ListModel.Lek;
import pl.wotu.orderapp.database.DatabaseHelper;

import static pl.wotu.orderapp.WymianaPlikowFTP.lek;

public class MainActivity extends ParentActivity implements NavigationView.OnNavigationItemSelectedListener, LekAdapter.CallbackInterface, SearchView.OnQueryTextListener{

    //STAŁE
    private static final int MENU_ORDER_ITEM = 0;
    private static final int MENU_DOCUMENTS_ITEM = 1;
    private static final int MENU_NOTIFICATIONS_ITEM = 2;
    private static final int MENU_COMPLAINTS_ITEM = 3;
    private static final int MENU_CONTACT_ITEM = 4;
    private static final int MENU_SETTINGS_ITEM = 5;
    private static final int MENU_CONTACT_REPRESENTATIVE_ITEM = 6;

    private static final int DODAJ_LEKI_DO_ZAMOWIENIA = 1;
    private static final int EDYCJA_ZAMOWIENIA = 2;
    private static final int MENU_ACCOUNT_SETTINGS = 3;
    private static final int MENU_SETTINGS = 4;

    //INTERFACE    zamo
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private View navLayoutHeader;
    private ConstraintLayout constraintLayout;
    private Snackbar snackbar;
    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private Button zamowButton;
    private TextView labelSzczegolyZamownieniaBottom;
    private TextView wartZamBottom, sumaOszczBottom;
    private TextView aptnameNavigation, nrAptekiNavigation;

    //Adaptek, lista
    public RecyclerView lekRecyclerView, zamowionyLekRecyclerView;
    private List<Lek> lekList;
    private LekAdapter lekAdapter;
    private NavigationView navigationView;
    public List<Lek> zamowionyLekList;
    private ZamowionyLekAdapter zamowionyLekAdapter;

    //FTP
    boolean error = false;
    String protocol = "SSL";
    FTPSClient ftps;

    //SharedPref
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String licencja;
    private String miejscowoscApt;
    private String nazwPrzedst;
    private String telDoPrzedst;

    //Database
    private DatabaseHelper db;

    private long nrAptekiLogin;
    private ProgressBar progressBar;
    private long backPressedTime;
//    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INTERFACE
        zamowionyLekRecyclerView = findViewById(R.id.order_list_view_main);
        wartZamBottom = findViewById(R.id.wart_zam_bottom_tv);
        sumaOszczBottom = findViewById(R.id.suma_oszcze_bottom_tv);
        constraintLayout = findViewById(R.id.constraintLayoutMain);
        bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        zamowButton = findViewById(R.id.buttonZamow);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        lekRecyclerView = findViewById(R.id.notes_list_view);

        pref = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_tag), 0); // 0 - for private mode
        editor = pref.edit();

        nrAptekiLogin = pref.getLong("nrApteki", 0);
        licencja = pref.getString("licencja","Brak licencji");
        miejscowoscApt = pref.getString("miejscowosc","");
        nazwPrzedst = pref.getString("nazwPrzedst","");
        telDoPrzedst = pref.getString("telDoPrzedst","");




        if (nrAptekiLogin==0) {
            sendToLogin();
        }
        else {
            snackbar = Snackbar
                    .make(constraintLayout, "Nr kontrahenta" + nrAptekiLogin, Snackbar.LENGTH_INDEFINITE);

        db = new DatabaseHelper(this);

        mToolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("APT "+nrAptekiLogin);

        mDrawerLayout = findViewById(R.id.main_drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ftps = new FTPSClient(protocol);
        ftps.addProtocolCommandListener(new PrintCommandListener(new
                PrintWriter(System.out)));

        labelSzczegolyZamownieniaBottom = findViewById(R.id.label_szczegoly_zamownienia_bottom);
        labelSzczegolyZamownieniaBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehavior.getState() ==BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lekList = new ArrayList<>();
        lekAdapter = new LekAdapter(MainActivity.this,lekList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this,2);
//            RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,1);
        lekRecyclerView.setLayoutManager(mLayoutManager);
        lekRecyclerView.setAdapter(lekAdapter);
//
        zamowionyLekList = new ArrayList<>();
        zamowionyLekAdapter = new ZamowionyLekAdapter(MainActivity.this,zamowionyLekList);
            RecyclerView.LayoutManager mZamLayoutManager = new LinearLayoutManager(MainActivity.this);
//            RecyclerView.LayoutManager mZamLayoutManager = new StaggeredGridLayoutManager(2,1);
        zamowionyLekRecyclerView.setLayoutManager(mZamLayoutManager);
        zamowionyLekRecyclerView.setAdapter(zamowionyLekAdapter);
        zamowionyLekAdapter.notifyDataSetChanged();

//        zamowionyLekList.add(new Lek("82266|ASCORUTICAL * 30 kaps. _|JELFA S.A.|7.91|8.59|24.65|5.96|1|111|KWIECIEŃ 2019||79717|79722|","1"));
        //todo
//        zamowionyLekList.notifyAll();


//            synchronized(lek){
//                lek.notifyAll();
//            }

            synchronized(lekList){
                lekList.notifyAll();
            }
                synchronized(zamowionyLekList){
                    zamowionyLekList.notifyAll();
                }


            navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navLayoutHeader = navigationView.getHeaderView(0);
        aptnameNavigation = navLayoutHeader.findViewById(R.id.aptname_navigation);
        aptnameNavigation.setText("Apteka po sąsiedzku");
        nrAptekiNavigation = navLayoutHeader.findViewById(R.id.nazwaskroc_navigation);
        nrAptekiNavigation.setText("APT "+nrAptekiLogin+", "+miejscowoscApt);
        navLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent menuAccountIntent = new Intent(MainActivity.this,UserSettingsActivity.class);
                menuAccountIntent.putExtra("pref","pref_user");
                startActivityForResult(menuAccountIntent,MENU_ACCOUNT_SETTINGS);
            }
        });

        navigationView.getMenu().add(MENU_ORDER_ITEM,MENU_ORDER_ITEM,MENU_ORDER_ITEM,getString(R.string.menu_zamowienia)); //zamówienia
            navigationView.getMenu().add(MENU_CONTACT_ITEM,MENU_CONTACT_ITEM,MENU_CONTACT_ITEM,getString(R.string.menu_contact)); //kontakt
            if (!nazwPrzedst.equals("") &&!telDoPrzedst.equals("") ){
                navigationView.getMenu().add(MENU_CONTACT_REPRESENTATIVE_ITEM,MENU_CONTACT_REPRESENTATIVE_ITEM,MENU_CONTACT_REPRESENTATIVE_ITEM,"Przedstawiciel: "+nazwPrzedst); //kontakt
            }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case MENU_ORDER_ITEM:
                        mDrawerLayout.closeDrawers();
                        Intent ordersIntent = new Intent(MainActivity.this,OrdersActivity.class);
                        startActivity(ordersIntent);

                        return true;

                    case MENU_DOCUMENTS_ITEM:
                        mDrawerLayout.closeDrawers();
                        Intent documentsIntent = new Intent(MainActivity.this, OrderPosActivity.class);
                        startActivity(documentsIntent);

                        return true;

                    case MENU_NOTIFICATIONS_ITEM:
                        mDrawerLayout.closeDrawers();
                        Intent notificationsIntent = new Intent(MainActivity.this,NotificationsActivity.class);
                        startActivity(notificationsIntent);

                        return true;

//                    case MENU_COMPLAINTS_ITEM:
//                        mDrawerLayout.closeDrawers();
//                        List<Integer> jakasLista = new ArrayList<>();
//                        jakasLista.add(1);
//                        jakasLista.add(2);
//                        jakasLista.add(3);
//                        jakasLista.add(4);
//                        jakasLista.add(5);
//                        jakasLista.add(6);
//                        jakasLista.add(7);
//
//                        int wynik = 1+2*3+4*5+6*7;
//                        Integer wynikInt = ZmienneIFunkcjeStatyczne.pokazWynik(jakasLista);
//
//                        String tresctemp = "";
//                        tresctemp = tresctemp+"1+2*3+4*5+6*7\nWynik funkcji: "+wynikInt+"\nWynik: "+wynik+"\n\n";
//
//                        jakasLista=ZmienneIFunkcjeStatyczne.utworzListe(jakasLista);
//                        for(Integer elem:jakasLista){
//                            tresctemp = tresctemp + elem.toString()+ ", ";
//                        }
//
//                        ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,tresctemp,ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_GRADIENT);
//
//                        return true;

                    case MENU_SETTINGS_ITEM:
                        mDrawerLayout.closeDrawers();

                        Intent menuAccountIntent = new Intent(MainActivity.this,UserSettingsActivity.class);
                        menuAccountIntent.putExtra("pref","pref_general_settings");
                        startActivityForResult(menuAccountIntent,MENU_SETTINGS);

                        return true;

                    case MENU_CONTACT_ITEM:
                        mDrawerLayout.closeDrawers();

                        Intent intent_MENU_CONTACT_ITEM = new Intent(Intent.ACTION_DIAL);
                        intent_MENU_CONTACT_ITEM.setData(Uri.parse("tel:800000900"));
                        startActivity(intent_MENU_CONTACT_ITEM);


                        return true;
                    case MENU_CONTACT_REPRESENTATIVE_ITEM:
                        mDrawerLayout.closeDrawers();

                            Intent intent_MENU_CONTACT_REPRESENTATIVE_ITEM = new Intent(Intent.ACTION_DIAL);
                            intent_MENU_CONTACT_REPRESENTATIVE_ITEM.setData(Uri.parse("tel:" + telDoPrzedst));
                            startActivity(intent_MENU_CONTACT_REPRESENTATIVE_ITEM);

                        return true;

                }
                return false;
            }
        });



        zamowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZmienneIFunkcjeStatyczne.sprawdzInternet(MainActivity.this,constraintLayout,"Nie udało się wysłać zamówienia!");  //    "Nie udało się pobrać oferty"
                    double wartoscZamowienia=0;
                    String zamowienie=licencja+"|"+android.os.Build.MODEL+"|"+android.os.Build.SERIAL+"\r\n";

                    if (zamowionyLekList.size()==0){
                        ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Zamówienie nie zostało wysłane\n - nie wybrano leków",ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_RED);

                    }else {
                        for(int i=0;i<zamowionyLekList.size();i++){
                            zamowienie+=zamowionyLekList.get(i).getKt()+"|"+zamowionyLekList.get(i).getNrPromocji()+"|"+zamowionyLekList.get(i).getIloscZamowiona()+"\r\n";
                            wartoscZamowienia = zamowionyLekList.get(i).getCenaPoRabacie() * zamowionyLekList.get(i).getIloscZamowiona();
                        }
                        String strDouble = String.format("%.2f", wartoscZamowienia);
                        generateFile(getApplicationContext(),"zamowienie_testowe",zamowienie,zamowionyLekList.size(),strDouble);

                        if(sheetBehavior.getState() ==BottomSheetBehavior.STATE_EXPANDED) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }

                    }

                }
            });
        }


        t = new MyThread(this);
        t.attach(this);
        t.start();
//        sprawdzInternetIPobierzOferte();
    }



    public void generateFile(Context context, String sFileName, String sBody, int ilpoz, String wartosc) {
        FTPClient con = null;

        File cacheFolder = context.getCacheDir(); // context being the Activity pointer
        File gpxfile = null;
        try {
            gpxfile =  File.createTempFile(sFileName,"txt", cacheFolder);

            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();


            con = new FTPClient();
            con.connect(FTPConnectionData.server, FTPConnectionData.port);

            if (con.login(FTPConnectionData.user, FTPConnectionData.pass))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);

                con.setControlEncoding("UTF-8");
                con.setAutodetectUTF8(true);

                Long tsLong = System.currentTimeMillis()/1000;
                String filename_ts =nrAptekiLogin+"_"+tsLong.toString()+".mob";

                FileInputStream in = new FileInputStream(gpxfile);
//                Toast.makeText(MainActivity.this, gpxfile.getPath(),Toast.LENGTH_LONG).show();

                boolean result = con.storeFile("mob/zam/"+filename_ts, in);
                in.close();

                if (result) {
                    Date date= Calendar.getInstance().getTime();
                    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate=dateFormat.format(date);
                    String formattedTime=timeFormat.format(date);
                    saveZamToHist("testowe", formattedDate,formattedTime,0,ilpoz,wartZamBottom.getText().toString(),filename_ts);
                    ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Zamówienie zostało wysłane",ZmienneIFunkcjeStatyczne.ICON_CHECK,ZmienneIFunkcjeStatyczne.BG_GRADIENT);
                }
                else{
                    ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Nie udało się wysłać zamówienia",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);
                }

                con.logout();
                con.disconnect();
            }

        } catch (FileNotFoundException e){
            ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,e.toString(),ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_bar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.app_download_files:
                sprawdzInternetIPobierzOferte();
                return true;
        }

            if (mToggle.onOptionsItemSelected(item)){
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    private void sprawdzInternetIPobierzOferte() {
        progressBar.setVisibility(View.VISIBLE);

        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        boolean polaczenieZBaza=WymianaPlikowFTP.sprawdzPolaczenieZBaza(MainActivity.this);
        if(networkInfo != null && networkInfo.isConnected()==true&&polaczenieZBaza==true )
        {
//            lekList = new ArrayList<>();
//            lekRecyclerView = findViewById(R.id.notes_list_view);
//            lekAdapter = new LekAdapter(MainActivity.this,lekList);
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this,2);
//            lekRecyclerView.setLayoutManager(mLayoutManager);
//            lekRecyclerView.setAdapter(lekAdapter);
            Lek lekItem = null;
            if (WymianaPlikowFTP.sprawdzLicencje(MainActivity.this,nrAptekiLogin,licencja,getString(R.string.shared_preferences_tag))){

                if (navigationView.getMenu().findItem(MENU_CONTACT_REPRESENTATIVE_ITEM)!=null){
                    navigationView.getMenu().removeItem(MENU_CONTACT_REPRESENTATIVE_ITEM);
                    if (!nazwPrzedst.equals("") && !telDoPrzedst.equals("")) {
                        navigationView.getMenu().add(MENU_CONTACT_REPRESENTATIVE_ITEM, MENU_CONTACT_REPRESENTATIVE_ITEM, MENU_CONTACT_REPRESENTATIVE_ITEM, "Przedstawiciel: " + pref.getString("nazwPrzedst","")); //kontakt
                    }
                    nrAptekiNavigation.setText(pref.getString("nazwaSkroc","Nie wpisano nazwy")+", "+pref.getString("miejscowosc","Apteka po sąsiedzku"));

                }


                Lek[] lekTemp =  super.listaLekow;
                if (lekTemp==null){
                    lekTemp = WymianaPlikowFTP.pobierz(MainActivity.this, "/mob/ofe/oferta.txt");
                }
                else{
//                    Toast.makeText(MainActivity.this,"Liczba pozycji: "+lekTemp.length,Toast.LENGTH_SHORT).show();
                }
//                ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Pobrano najnowszą ofertę",ZmienneIFunkcjeStatyczne.ICON_CHECK,ZmienneIFunkcjeStatyczne.BG_GRADIENT);
                if (lekTemp!=null){
                    lek = lekTemp;
                    for(int i=0;i<lek.length;i++){
                        lekItem = lek[i];
                        lekList.add(lekItem);
                    }
                    ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Pobrano najnowszą ofertę",ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_GRADIENT);
                }else
                {
                    ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Trwa przygotowywanie nowej oferty\nProszę spróbować ponownie za chwilę",ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_YELLOW);
                }

//                !!!!TUTAJ KURWA BYŁO ŹLE!!!
//                zamowionyLekList = new ArrayList<>();
//                zamowionyLekAdapter = new ZamowionyLekAdapter(MainActivity.this,zamowionyLekList);
//                zamowionyLekAdapter.notifyDataSetChanged();

            }
            else{ //BRAK PLIKU Z LICENCJĄ
                lek = new Lek[0];
                lekList = new ArrayList<>();
                lekAdapter.notifyDataSetChanged();
                lekAdapter.updateList(lekList);
                snackbar = Snackbar
                        .make(constraintLayout, "Brak dostępu do systemu", Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(Color.YELLOW)
                        .setAction("Kontakt", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.setDuration(Snackbar.LENGTH_SHORT);
                            }
                        });
                snackbar.show();
                ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Brak dostępu do systemu, skontaktuj się z Hurtownią",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);
            }
        }
        else
        {
            snackbar = Snackbar
                    .make(constraintLayout, "Nie udało się pobrać oferty", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.YELLOW)

                    .setAction("Pobierz", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.setDuration(Snackbar.LENGTH_SHORT);
                            sprawdzInternetIPobierzOferte();
                        }
                    });

            snackbar.show();
            ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Brak dostępu do internetu",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);
        }


        progressBar.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("APSLubfarmPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        nrAptekiLogin = pref.getLong("nrApteki", 0);
        licencja = pref.getString("licencja","Brak licencji");
        if (nrAptekiLogin==0 ||licencja.equals("Brak licencji")){
            editor.clear();
            editor.commit();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }


        if (requestCode == DODAJ_LEKI_DO_ZAMOWIENIA) {
            if (resultCode == RESULT_OK) {
                String nazwa = data.getExtras().getString("nazwa");
                int iloscZam = Integer.parseInt(data.getExtras().getString("iloscZam"));
                int pozycja = Integer.parseInt(data.getExtras().getString("pozycjaView"));
                int pozycjaWOfercie = Integer.parseInt(data.getExtras().getString("pozycjaWOfercieTV"));
                int poprzedniaIlosc = lekList.get(pozycjaWOfercie).getIloscZamowiona();
                int ktLeku = lekList.get(pozycjaWOfercie).getKt();

                String tresc_toast = "requestCode == DODAJ_LEKI_DO_ZAMOWIENIA:\n" + lekList.get(pozycjaWOfercie).getNazwa() + "\n" + nazwa + "\nPozycja:" + pozycja + "\n";
                tresc_toast = "nazwa"+nazwa+"\niloscZam"+iloscZam+"\npozycja"+pozycja+"\npozycjaWOfercie"+pozycjaWOfercie+"\npoprzedniaIlosc"+poprzedniaIlosc+"\nktLeku"+ktLeku;

                if (lekList.get(pozycjaWOfercie).getNazwa().equals(nazwa)&&(iloscZam!=poprzedniaIlosc)){
                    lek[pozycjaWOfercie].setIloscZamowiona(iloscZam);
                    if (iloscZam==0){ //usunac pozycje z listy
                        zamowionyLekList.remove(lek[pozycjaWOfercie]);
                    }else if(iloscZam>0){ //zmiana ilosci lub dodawanie
                        if (poprzedniaIlosc>0){

                        }else if(poprzedniaIlosc ==0){
                            zamowionyLekList.add(lek[pozycjaWOfercie]);
                            if (iloscZam==0){
                                zamowionyLekList.remove(lek[pozycjaWOfercie]);
                            }
                        }
                    }
                }



//                zamowionyLekList.add(new Lek("82266|AfsfsdSCORUTICAL * 30 kaps. _|JELFA S.A.|7.91|8.59|24.65|5.96|1|111|KWIECIEŃ 2019||79717|79722|","1"));

                lekAdapter.notifyDataSetChanged();
                zamowionyLekAdapter.notifyDataSetChanged();

                synchronized(lek){
                    lek.notifyAll();
                }

                synchronized(lekList){
                    lekList.notifyAll();
                }
                //todo
                synchronized(zamowionyLekList){
                    zamowionyLekList.notifyAll();
                }
//                Toast.makeText(MainActivity.this,"lekList.size()"+lekList.size()+"\nzamowionyLekList.size()"+zamowionyLekList.size()+"\nzamowionyLekAdapter.getItemCount()"+zamowionyLekAdapter.getItemCount()+"\nlekAdapter.getItemCount()"+lekAdapter.getItemCount(),Toast.LENGTH_SHORT).show();



                double wartoscZamowienia=0,sumaOszczednosci=0;

                for(int i=0;i<zamowionyLekList.size();i++){
                    wartoscZamowienia+=zamowionyLekList.get(i).getCenaPoRabacie() * zamowionyLekList.get(i).getIloscZamowiona();
                    sumaOszczednosci+=(zamowionyLekList.get(i).getCenaHurtowa()-zamowionyLekList.get(i).getCenaPoRabacie())*zamowionyLekList.get(i).getIloscZamowiona();
                }
                wartZamBottom.setText(String.format("%.2f", wartoscZamowienia)+" zł");
                sumaOszczBottom. setText(String.format("%.2f", sumaOszczednosci)+" zł");
            }
        }
        else if (requestCode == EDYCJA_ZAMOWIENIA) {
//           Toast.makeText(MainActivity.this,"requestCode=EDYCJA_ZAMOWIENIA",Toast.LENGTH_LONG).show();

            if (resultCode == RESULT_OK) {

            }
        }
    }



    private void saveZamToHist(String nr_zam,String date,String czas,int status,int ilpoz,String wartosc,String plik) {
            int ostatniaPozycjaZListy;
            double wartoscZamowienia=0,sumaOszczednosci=0;

            int il_zam=0,il_zreal=0, nr_prom=0, kt=0;
            String nazwa_leku="";


            for(int i=0;zamowionyLekList.size()>0;){
                ostatniaPozycjaZListy = zamowionyLekList.size()-1;
                il_zam= lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].getIloscZamowiona();
                nr_prom = lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].getNrPromocji();
                kt = lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].getKt();
                nazwa_leku = lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].getNazwa();
                float cena_p_rabat = (float) lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].getCenaPoRabacie();
                float cena = (float) lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].getCenaDetaliczna();


                lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].setIloscZamowiona(0);
                zamowionyLekList.remove(ostatniaPozycjaZListy);
                i++;
//                if (i==lek.length){
//                    break;
//                }
//                lek[Integer.parseInt(zamowionyLekList.get(ostatniaPozycjaZListy).getId())].setIloscZamowiona(0);
                db.insertOrderPosition(nr_zam,nr_prom,kt,il_zam,il_zreal,status,plik,nazwa_leku,cena_p_rabat,cena);
            }

//        ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Zamówienie zostało wysłane \nsaveZamToHist\nzamowionyLekList.size()="+zamowionyLekList.size(),ZmienneIFunkcjeStatyczne.ICON_CHECK,ZmienneIFunkcjeStatyczne.BG_GRADIENT);

            wartZamBottom.setText(String.format("%.2f", wartoscZamowienia)+" zł");
            sumaOszczBottom. setText(String.format("%.2f", sumaOszczednosci)+" zł");

            lekAdapter.notifyDataSetChanged();
            zamowionyLekAdapter.notifyDataSetChanged();

            synchronized(lek){
                lek.notifyAll();
            }

            synchronized(lekList){
                lekList.notifyAll();
            }

            String nazwaOferty=""; //TODO nazwa oferty [?]
            long id = db.insertOrder(nr_zam,date,czas,status,ilpoz,wartosc,plik, nazwaOferty);


    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Lek> newList = new ArrayList<>();
        for(Lek spr_lek: lek){
            if(spr_lek.getNazwa().toLowerCase().contains(userInput)){
                newList.add(spr_lek);
            }
        }
        lekAdapter.updateList(newList);
        return true;
    }



    public class MyThread extends Thread{
        private MainActivity mActivity;

        public MyThread(MainActivity activity){
            mActivity = activity;
        }
        public void attach(MainActivity activity){
            mActivity = activity;
        }
        @Override
        public void run(){
            mActivity.downloadOffer();
        }

    }

    private void downloadOffer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sprawdzInternetIPobierzOferte();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    public static MyThread t;

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
//        return super.onRetainCustomNonConfigurationInstance();
        if(t!=null&&t.isAlive()){
            return t;
        }
        return null;
    }



    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
//        super.onBackPressed();
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }else{
            if (zamowionyLekList.size()>0) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
        ZmienneIFunkcjeStatyczne.showToast(MainActivity.this,"Naciśnij ponownie klawisz WSTECZ,\naby zamknąć program",ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_GRADIENT);
        backPressedTime = System.currentTimeMillis();

    }

}
