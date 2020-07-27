package pl.wotu.orderapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.wotu.orderapp.ListModel.Lek;

public class ZmienneIFunkcjeStatyczne {
    public Lek[] lek;
    private static Snackbar snackbar;
//    Snackbar snackbar;

    //TOAST SETTINGS
    public static final int ICON_CHECK = R.drawable.ic_check_white_24dp;
    public static final int ICON_X = R.drawable.ic_close_white_24dp;
    public static final int ICON_INFO = R.drawable.ic_info_white_24dp;
    public static final int ICON_WARNING = R.drawable.ic_warning_white_24dp;
    public static final int ICON_NOICON = 0;

    public static final int BG_GRADIENT = R.drawable.custom_toast_gradient;
    public static final int BG_RED = R.drawable.custom_toast_red;
    public static final int BG_GREEN = R.drawable.custom_toast_green;
    public static final int BG_YELLOW = R.drawable.custom_toast_yellow;


    //  public static String pass = "test";
    public static String usunPodkreslenieZKoncaLinii(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '_') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static void showToast(Activity context, String text, int imageResource, int backgroundResource) {

        LayoutInflater inflater = context.getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.custom_toast_text_image, (ViewGroup) context.findViewById(R.id.toast_green_text));
        TextView toastText = toastLayout.findViewById(R.id.textToast);
        ImageView toastImage = toastLayout.findViewById(R.id.ivToast);

        toastText.setText(text);
        if (imageResource==0){
            toastImage.setVisibility(View.GONE);
        }else {
            toastImage.setVisibility(View.VISIBLE);
            toastImage.setImageResource(imageResource);
        }

//        createToast(context, backgroundResource, toastLayout);

        Toast toast = new Toast(context.getApplicationContext());

        toastLayout.setBackgroundResource(backgroundResource);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();

    }

//    private static void createToast(Activity context, int backgroundResource, View toastLayout) {

//        runOnUiThread(new Runnable(){
//            public void run() {
//                Toast.makeText(getApplicationContext(), "Status = " + message.getBody() , Toast.LENGTH_LONG).show();
//            }
//        });

//    }

//    public static void showToast(Activity context, String text, int backgroundResource) {
//
//        LayoutInflater inflater = context.getLayoutInflater();
//        View toastLayout = inflater.inflate(R.layout.custom_toast_text_image, (ViewGroup) context.findViewById(R.id.toast_green_text));
//        TextView toastText = toastLayout.findViewById(R.id.textToast);
//        ImageView toastImage = toastLayout.findViewById(R.id.ivToast);
//
//        toastText.setText(text);
//            toastImage.setVisibility(View.GONE);
////        createToast(context, backgroundResource, toastLayout);
//
//
//        Toast toast = new Toast(context.getApplicationContext());
//
//        toastLayout.setBackgroundResource(backgroundResource);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(toastLayout);
//        toast.show();
//
//    }


    public static boolean sprawdzInternet(final Activity context, final ConstraintLayout constraintLayout, final String komunikat) {  //    "Nie udało się pobrać oferty"
        ConnectivityManager ConnectionManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            return true;
        }
        else
        {

             snackbar = Snackbar
                    .make(constraintLayout, komunikat, Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.RED)

                    .setAction("Zamknij", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.setDuration(Snackbar.LENGTH_SHORT);
//                            sprawdzInternet(context,constraintLayout,komunikat);
                        }
                    });

            snackbar.show();

            ZmienneIFunkcjeStatyczne.showToast(context,"Brak dostępu do internetu",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);
            return false;

        }
    }

//    public static List<Integer> utworzListe(List<Integer> lista){
//        List<Integer> listaL=new ArrayList<>();
//        List<Integer> listaP=new ArrayList<>();
////        [1,2,3,4,5,6,7]
//        int i=0;
//        for (Integer elem:lista){
//            if (i % 2 == 0){
//                listaL.add(elem);
//            }else{
//                listaP.add(elem);
//            }
//            i+=1;
//        }
//        listaL.addAll(listaP);
//
//        return listaL;
//    }

//    public static Integer pokazWynik(List<Integer> lista){
//        int wynikMn=1,wynik=0;
//        boolean parzysta=true;
//        for(Integer elem:lista) {
//
//            if (parzysta) {
//                wynikMn=wynikMn*elem;
//                wynik = wynik + wynikMn;
//                wynikMn=0;
//                parzysta = false;
//            }
//            else {
//                wynikMn=elem;
//                parzysta = true;
//            }
//        }
//        wynik = wynik + wynikMn;
//
//        return wynik;
//    }

    public static boolean statusStarszyNizDwaDni(String statusTimestamp) {
        Calendar cal = Calendar.getInstance(Locale.forLanguageTag("pl"));
        cal.setTimeInMillis(System.currentTimeMillis());
        String date = DateFormat.format("yyyy-MM-dd hh:mm:ss", cal).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date statusDate=null,actualDate = null;
        boolean starszy = false;
        try {
            statusDate = sdf.parse(statusTimestamp.substring(0,10));
            actualDate = sdf.parse(date.substring(0,10));

//            long roznica = (actualDate.getTime() - statusDate.getTime()) / 1000*60*60*24;
            long roznica = (actualDate.getTime() - statusDate.getTime())/(1000*60*60*24);

            if (roznica < 2) {
                starszy = false;
            } else {
                starszy = true;
            }

//            Toast.makeText(activity.getApplicationContext(),"czy statusStarszyNizDwaDni?\nstatusDate="+statusDate+"\nactualDate="+actualDate+"\nróżnica:"+roznica+"\nstarszy:"+starszy,Toast.LENGTH_LONG).show();

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return starszy;
    }

}
