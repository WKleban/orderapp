package pl.wotu.orderapp;import android.app.Activity;import android.content.Context;import android.content.SharedPreferences;import android.os.AsyncTask;import android.os.PowerManager;import android.os.StrictMode;import android.widget.ProgressBar;import org.apache.commons.net.ftp.FTPClient;import org.apache.commons.net.ftp.FTPFile;import org.apache.commons.net.ftp.FTPReply;import java.io.BufferedReader;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.net.SocketException;import java.text.DateFormat;import java.text.SimpleDateFormat;import java.util.ArrayList;import pl.wotu.orderapp.ListModel.Lek;public class WymianaPlikowFTP {    private static Lek[] lek_temp;    static Lek[] lek=null;    private static FTPClient ftpClient = new FTPClient();    private static FTPFile[] file = null;    private static void showServerReply(FTPClient ftpClient) {        String[] replies = ftpClient.getReplyStrings();        if (replies != null && replies.length > 0) {            for (String aReply : replies) {               // System.out.println("SERVER: " + aReply);            }        }    }    public static boolean sprawdzPolaczenieZBaza(Activity context){        ftpClient.setControlEncoding("UTF-8");        ftpClient.setAutodetectUTF8(true);        boolean success = false;        try {            ftpClient.connect(FTPConnectionData.server, FTPConnectionData.port);            int replyCode = ftpClient.getReplyCode();            if (!FTPReply.isPositiveCompletion(replyCode)) {                ////System.out.println("Operation failed. Server reply code: " + replyCode);                //  return;            }            success = ftpClient.login(FTPConnectionData.user, FTPConnectionData.pass);            if (!success) {                ZmienneIFunkcjeStatyczne.showToast(context,"Brak połączenia z serwerem",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            }        } catch (IOException ex) {            ZmienneIFunkcjeStatyczne.showToast(context,"Brak połączenia z serwerem",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            ex.printStackTrace();            return false;        }        return success;    }    public static boolean sprawdzLicencje(Activity context,long numerApteki,String numerLicencji,String sharedTag){        if (android.os.Build.VERSION.SDK_INT > 9) {            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()                    .permitAll().build();            StrictMode.setThreadPolicy(policy);        }        ftpClient.setControlEncoding("UTF-8");        ftpClient.setAutodetectUTF8(true);        boolean success = false;        try {            ftpClient.connect(FTPConnectionData.server, FTPConnectionData.port);            int replyCode = ftpClient.getReplyCode();            if (!FTPReply.isPositiveCompletion(replyCode)) {                ////System.out.println("Operation failed. Server reply code: " + replyCode);                //  return;            }             success = ftpClient.login(FTPConnectionData.user, FTPConnectionData.pass);            if (!success) {//                System.out.println("Could not login to the server");                ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać plików z serwera",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            } else {                System.out.println("Połączono z serwerem");                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                FTPFile[] file = ftpClient.listFiles("/mob/lic/"+numerApteki+"_"+numerLicencji+".lic");                System.out.println("/mob/lic/"+numerApteki+"_"+numerLicencji+".lic");                SharedPreferences pref = context.getApplicationContext().getSharedPreferences(sharedTag, 0); // 0 - for private mode                SharedPreferences.Editor editor = pref.edit();                System.out.println("file.length = "+file.length);                if (file.length==0){//                    ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać pliku /mob/lic/"+numerApteki+"_"+numerLicencji+".lic",ZmienneIFunkcjeStatyczne.ICON_CLOSE,ZmienneIFunkcjeStatyczne.BG_RED);//                    ZmienneIFunkcjeStatyczne.showToast(context, "Brak dostępu do systemu, skontaktuj się z Przestawicielem Handlowym", ZmienneIFunkcjeStatyczne.ICON_X, ZmienneIFunkcjeStatyczne.BG_RED);                    success = false;//                    System.out.println("file.length==0");//                    editor.putString("nazwaSkroc","");                    editor.putString("nazwa","");                    editor.putString("nazwa2","");                    editor.putString("kodPoczt","");                    editor.putString("miejscowosc","");                    editor.putString("ul","");                    editor.putString("telefon","");                    editor.putString("email","");                    editor.putString("nazwPrzedst","");                    editor.putString("telDoPrzedst","");                    editor.commit();                }else {//                    System.out.println("file.length OK");                    String details1 = file[0].getName();                    //for (FTPFile file : files) {//                    System.out.println(file[0].getSize());                    if (file[0].isDirectory()) {                        details1 = "[" + details1 + "]";                    }                    details1 += "\t\t" + file[0].getSize();                    details1 += "\t\t" + dateFormater.format(file[0].getTimestamp().getTime());//                    System.out.println(details1);                    //}//                    System.out.println("Nazwa pliku = " + file[0].getName());//                    System.out.println("Rozmiar pliku = " + file[0].getSize());                    BufferedReader reader = null;                    String firstLine = null;                    ArrayList<String> list = new ArrayList<String>();                    try {                        InputStream stream = ftpClient.retrieveFileStream(file[0].getName());                        reader = new BufferedReader(new InputStreamReader(stream, "Cp1250")); //Cp1250 - to jest kodowanie windows1250                        editor.putString("nazwaSkroc",reader.readLine());                        editor.putString("nazwa",reader.readLine());                        editor.putString("nazwa2",reader.readLine());                        editor.putString("kodPoczt",reader.readLine());                        editor.putString("miejscowosc",reader.readLine());                        editor.putString("ul",reader.readLine());                        editor.putString("telefon",reader.readLine());                        editor.putString("email",reader.readLine());                        editor.putString("nazwPrzedst",reader.readLine());                        editor.putString("telDoPrzedst",reader.readLine());                        editor.commit();                    }  catch (NullPointerException ex) {                        ZmienneIFunkcjeStatyczne.showToast(context, "Brak dostępu, skontaktuj się z Hurtownią Lubfarm", ZmienneIFunkcjeStatyczne.ICON_X, ZmienneIFunkcjeStatyczne.BG_RED);                    }                    finally {                        if (reader != null) try {                            reader.close();                        } catch (IOException logOrIgnore) {                        }                    }                }            }        } catch (IOException ex) {            System.out.println("Oops! Something wrong happened");            ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            ex.printStackTrace();        }//        ZmienneIFunkcjeStatyczne.showToast(context,"success="+success,ZmienneIFunkcjeStatyczne.ICON_CLOSE,ZmienneIFunkcjeStatyczne.BG_YELLOW);        return success;    }    public static String sprawdzDatePliku(Activity context,String fileFromFTP) {        ftpClient.setControlEncoding("UTF-8");        ftpClient.setAutodetectUTF8(true);        String wynik="";        try {            ftpClient.connect(FTPConnectionData.server, FTPConnectionData.port);            int replyCode = ftpClient.getReplyCode();            if (!FTPReply.isPositiveCompletion(replyCode)) {                ////System.out.println("Operation failed. Server reply code: " + replyCode);                //  return;            }            boolean success = ftpClient.login(FTPConnectionData.user, FTPConnectionData.pass);            if (!success) {                System.out.println("Could not login to the server");                ZmienneIFunkcjeStatyczne.showToast(context, "Nie udało się pobrać oferty", ZmienneIFunkcjeStatyczne.ICON_X, ZmienneIFunkcjeStatyczne.BG_RED);            } else {                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                FTPFile[] file = ftpClient.listFiles(fileFromFTP);                String details1 = file[0].getName();                //for (FTPFile file : files) {//                System.out.println(file[0].getSize());                if (file[0].isDirectory()) {                    details1 = "[" + details1 + "]";                }                details1 += "\t\t" + file[0].getSize();                details1 += "\t\t" + dateFormater.format(file[0].getTimestamp().getTime());//                System.out.println(details1);                //}//                System.out.println("Nazwa pliku = " + file[0].getName());//                System.out.println("Rozmiar pliku = " + file[0].getSize());                wynik = file[0].getTimestamp().toString();//                BufferedReader reader = null;//                String firstLine = null;//                ArrayList<String> list = new ArrayList<String>();            }        } catch (SocketException e) {            e.printStackTrace();        } catch (IOException e) {            e.printStackTrace();        }        return wynik;    }    public static Lek[] pobierz(Activity context,String fileFromFTP) {        ftpClient.setControlEncoding("UTF-8");        ftpClient.setAutodetectUTF8(true);        try {            ftpClient.connect( FTPConnectionData.server, FTPConnectionData.port);            int replyCode = ftpClient.getReplyCode();            if (!FTPReply.isPositiveCompletion(replyCode)) {                ////System.out.println("Operation failed. Server reply code: " + replyCode);                //  return;            }            boolean success = ftpClient.login(FTPConnectionData.user, FTPConnectionData.pass);            if (!success) {                System.out.println("Could not login to the server");                ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            } else {                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                file = ftpClient.listFiles(fileFromFTP);                System.out.println("Liczba pobranych plików: "+file.length);                if (file.length>0) {                    String details1 = file[0].getName();                    //for (FTPFile file : files) {//                System.out.println(file[0].getSize());                    if (file[0].isDirectory()) {                        details1 = "[" + details1 + "]";                    }                    details1 += "\t\t" + file[0].getSize();                    details1 += "\t\t" + dateFormater.format(file[0].getTimestamp().getTime());//                System.out.println(details1);                    //}//                System.out.println("Nazwa pliku = "+file[0].getName());//                System.out.println("Rozmiar pliku = "+file[0].getSize());                    BufferedReader reader = null;                    String firstLine = null;                    ArrayList<String> list = new ArrayList<String>();                    try {                        InputStream stream = ftpClient.retrieveFileStream(file[0].getName());                        reader = new BufferedReader(new InputStreamReader(stream, "Cp1250")); //Cp1250 - to jest kodowanie windows1250//                   reader = new BufferedReader(new InputStreamReader(stream));                        firstLine = reader.readLine(); //naglowek//                    System.out.println(firstLine); //naglowek                        //System.console().writer();                        String readerTemp = "";                        while (readerTemp != null) {                            readerTemp = reader.readLine();                            // if (readerTemp.length()>5){                            list.add(readerTemp);                            // }                        }                        // firstLine = reader.readLine();                        // System.out.println(firstLine);                        //Lek lek = new Lek(firstLine);                        //Lek[] lek = list.toArray(new Lek[list.size()]);                        //list.get(index)                        lek_temp = new Lek[list.size() - 1];                        //   for (int i=0;i<10;i++){                        for (int i = 0; i < list.size() - 1; i++) {                            // System.out.println("Nowy element nr "+i+" dlugosc: "+list.get(i).length());                            lek_temp[i] = new Lek(list.get(i), String.valueOf(i));                        }                    } finally {                        if (reader != null) try {                            reader.close();                        } catch (IOException logOrIgnore) {                        }                    }                }else{ // brak pliku                    lek_temp=null;                }                }        } catch (IOException ex) {            System.out.println("Oops! Something wrong happened");            ex.printStackTrace();        }        //Lek[] lek;        return lek_temp;    }    public static ArrayList<String> pobierzOdpowiedzi(Activity context, String fileFromFTP) {        ftpClient.setControlEncoding("UTF-8");        ftpClient.setAutodetectUTF8(true);        ArrayList <String> list = new ArrayList<String>();        try {            ftpClient.connect( FTPConnectionData.server, FTPConnectionData.port);            int replyCode = ftpClient.getReplyCode();            if (!FTPReply.isPositiveCompletion(replyCode)) {                ////System.out.println("Operation failed. Server reply code: " + replyCode);                //  return;            }            boolean success = ftpClient.login(FTPConnectionData.user, FTPConnectionData.pass);            if (!success) {                System.out.println("Could not login to the server");                ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            } else {                DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                file = ftpClient.listFiles(fileFromFTP);                if (file.length<1){                    return null;                }                String details1 = file[0].getName();                //for (FTPFile file : files) {//                System.out.println(file[0].getSize());                if (file[0].isDirectory()) {                    details1 = "[" + details1 + "]";                }                details1 += "\t\t" + file[0].getSize();                details1 += "\t\t" + dateFormater.format(file[0].getTimestamp().getTime());//                System.out.println(details1);                //}//                System.out.println("Nazwa pliku = "+file[0].getName());//                System.out.println("Rozmiar pliku = "+file[0].getSize());                BufferedReader reader = null;                String firstLine = null;                try {                    InputStream stream = ftpClient.retrieveFileStream(file[0].getName());                    reader = new BufferedReader(new InputStreamReader(stream, "Cp1250")); //Cp1250 - to jest kodowanie windows1250                    firstLine = reader.readLine(); //naglowek//                    System.out.println(firstLine); //naglowek                    String readerTemp="";                    while (readerTemp != null) {                        readerTemp=reader.readLine();                        if (readerTemp!=null) {                            list.add(readerTemp);//                            System.out.println(readerTemp);                        }                    }//                    ZmienneIFunkcjeStatyczne.showToast(context,"liczba_pozycji="+list.size(),ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_YELLOW);//                    ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);//                    lek_temp = new Lek[list.size()-1];//                    for (int i=0;i<list.size()-1;i++){//                        lek_temp[i]=new Lek(list.get(i),String.valueOf(i));//                    }                } finally {                    if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}                }            }        } catch (IOException ex) {            System.out.println("Oops! Something wrong happened");            ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);            ex.printStackTrace();        }        //Lek[] lek;//        return lek_temp;        return list;    }    // usually, subclasses of AsyncTask are declared inside the activity class.// that way, you can easily modify the UI thread from here    private class DownloadTask extends AsyncTask<String, Integer, String> {        private ProgressBar mProgressDialog;        private Activity context;        private Lek[] lek_temp;        private PowerManager.WakeLock mWakeLock;        private String fileFromFTP;        public DownloadTask(Activity context, ProgressBar mProgressDialog) {            this.context = context;            this.mProgressDialog=mProgressDialog;        }        @Override        protected String doInBackground(String... sUrl) {            fileFromFTP = sUrl[0];            ftpClient.setControlEncoding("UTF-8");            ftpClient.setAutodetectUTF8(true);            try {                ftpClient.connect( FTPConnectionData.server, FTPConnectionData.port);                int replyCode = ftpClient.getReplyCode();                if (!FTPReply.isPositiveCompletion(replyCode)) {                    ////System.out.println("Operation failed. Server reply code: " + replyCode);                    //  return;                }                boolean success = ftpClient.login(FTPConnectionData.user, FTPConnectionData.pass);                if (!success) {                    System.out.println("Could not login to the server");                    ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);                } else {                    DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                    file = ftpClient.listFiles(fileFromFTP);                    String details1 = file[0].getName();                    //for (FTPFile file : files) {//                    System.out.println(file[0].getSize());                    if (file[0].isDirectory()) {                        details1 = "[" + details1 + "]";                    }                    details1 += "\t\t" + file[0].getSize();                    details1 += "\t\t" + dateFormater.format(file[0].getTimestamp().getTime());//                    System.out.println(details1);                    //}//                    System.out.println("Nazwa pliku = "+file[0].getName());//                    System.out.println("Rozmiar pliku = "+file[0].getSize());                    BufferedReader reader = null;                    String firstLine = null;                    ArrayList <String> list = new ArrayList<String>();                    try {                        InputStream stream = ftpClient.retrieveFileStream(file[0].getName());                        reader = new BufferedReader(new InputStreamReader(stream, "Cp1250")); //Cp1250 - to jest kodowanie windows1250//                   reader = new BufferedReader(new InputStreamReader(stream));                        firstLine = reader.readLine(); //naglowek//                        System.out.println(firstLine); //naglowek                        //System.console().writer();                        String readerTemp="";                        while (readerTemp != null) {                            readerTemp=reader.readLine();                            // if (readerTemp.length()>5){                            list.add(readerTemp);                            // }                        }                        // firstLine = reader.readLine();                        // System.out.println(firstLine);                        //Lek lek = new Lek(firstLine);                        //Lek[] lek = list.toArray(new Lek[list.size()]);                        //list.get(index)                        lek_temp = new Lek[list.size()-1];                        //   for (int i=0;i<10;i++){                        for (int i=0;i<list.size()-1;i++){                            // System.out.println("Nowy element nr "+i+" dlugosc: "+list.get(i).length());                            lek_temp[i]=new Lek(list.get(i),String.valueOf(i));                        }                    } finally {                        if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}                    }                }            } catch (IOException ex) {                System.out.println("Oops! Something wrong happened");                ZmienneIFunkcjeStatyczne.showToast(context,"Nie udało się pobrać oferty",ZmienneIFunkcjeStatyczne.ICON_X,ZmienneIFunkcjeStatyczne.BG_RED);                ex.printStackTrace();            }            //Lek[] lek;//            return lek_temp;            return "";        }        @Override        protected void onPreExecute() {            super.onPreExecute();            // take CPU lock to prevent CPU from going off if the user            // presses the power button during download            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,                    getClass().getName());            mWakeLock.acquire();//            mProgressDialog.show();        }//        @Override//        protected void onProgressUpdate(Integer... progress) {//            super.onProgressUpdate(progress);//            // if we get here, length is known, now set indeterminate to false//            mProgressDialog.setIndeterminate(false);//            mProgressDialog.setMax(100);//            mProgressDialog.setProgress(progress[0]);//        }//        @Override        protected void onPostExecute(Lek[] result) {            mWakeLock.release();//            mProgressDialog.dismiss();//            if (result != null)//                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();//            else//                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();        }    }}