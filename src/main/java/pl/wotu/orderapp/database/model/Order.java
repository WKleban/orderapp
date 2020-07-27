package pl.wotu.orderapp.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Order {
    public static final String TABLE_NAME = "orders";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NR_ZAM = "nr_zam";//        Nr zamówienia
    public static final String COLUMN_DATE = "date";    //        Data zamówienia
    public static final String COLUMN_CZAS = "czas";    //        Czas
    public static final String COLUMN_STATUS = "status";//        Status
    public static final String COLUMN_IL_POZ = "il_poz";//        Ilość pozycji na dokumencie
    public static final String COLUMN_WARTOSC = "wartosc";//      Wartość
    public static final String COLUMN_PLIK = "plik";    //        Nazwa pliku
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_OFERTA = "oferta";

    private int id;
    private String nr_zam;
    private String date;
    private String czas;
    private int status;
    private int il_poz;
    private String wartosc;
    private String plik;
    private String timestamp;
    private String oferta;

    // Create table SQL query

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NR_ZAM + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_CZAS + " TEXT,"
                    + COLUMN_STATUS + " INTEGER,"
                    + COLUMN_IL_POZ + " INTEGER,"
                    + COLUMN_WARTOSC + " TEXT,"
                    + COLUMN_PLIK + " TEXT,"
                    + COLUMN_OFERTA + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Order() {
    }

    public Order(int id, String nr_zam, String date, String czas, int status, int il_poz,String wartosc, String plik, String timestamp,String oferta) {
        this.id = id;
        this.nr_zam = nr_zam;
        this.date = date;
        this.czas = czas;
        this.status = status;
        this.il_poz = il_poz;
        this.wartosc = wartosc;
        this.oferta = oferta;
        this.timestamp = timestamp;
    }

    public Order(int nrID) {
        this.id = nrID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNr_zam() {
        return nr_zam;
    }

    public void setNr_zam(String nr_zam) {
        this.nr_zam = nr_zam;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCzas() {
        return czas;
    }

    public void setCzas(String czas) {
        this.czas = czas;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIl_poz() {
        return il_poz;
    }

    public void setIl_poz(int il_poz) {
        this.il_poz = il_poz;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlik() {
        return plik;
    }

    public void setPlik(String plik) {
        this.plik = plik;
    }

    public String getWartosc() {
        return wartosc;
    }

    public void setWartosc(String wartosc) {
        this.wartosc = wartosc;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }
}
