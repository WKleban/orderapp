package pl.wotu.orderapp.database.model;

public class OrderPosition {




    public static final String TABLE_NAME = "order_positions";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NR_ZAM = "nr_zam";//        Nr zamówienia
    public static final String COLUMN_NR_PROM = "nr_prom";
    public static final String COLUMN_KT = "kt";
    public static final String COLUMN_NAZWA_LEKU = "nazwa_leku";
    public static final String COLUMN_IL_ZAM = "il_zam";
    public static final String COLUMN_IL_ZREAL = "il_zreal";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_PLIK = "plik";    //        Nazwa pliku
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_CENA = "cena";
    public static final String COLUMN_CENA_PORAB = "cena_p_rab";


    private int id;
    private String nr_zam;
    private int nr_prom;
    private int kt;
    private String nazwa_leku;
    private int il_zam;
    private int il_zreal;
    private int status;
    private String plik;
    private String timestamp;
    private Float cena;
    private Float cena_p_rab;

    // Create table SQL query

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NR_ZAM + " TEXT,"
                    + COLUMN_NR_PROM + " INTEGER,"
                    + COLUMN_KT + " INTEGER,"
                    + COLUMN_NAZWA_LEKU + " TEXT,"
                    + COLUMN_IL_ZAM + " INTEGER,"
                    + COLUMN_IL_ZREAL + " INTEGER,"
                    + COLUMN_STATUS + " INTEGER,"
                    + COLUMN_PLIK + " TEXT,"
                    + COLUMN_CENA + " REAL,"
                    + COLUMN_CENA_PORAB + " REAL,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public OrderPosition(int id, String nr_zam, int nr_prom, int kt, String nazwa_leku, int il_zam, int il_zreal, int status, String plik, String timestamp, Float cena, Float cena_p_rab) {
        this.id = id;//
        this.nr_zam = nr_zam;//
        this.nr_prom = nr_prom;//
        this.kt = kt; //
        this.nazwa_leku = nazwa_leku;//
        this.il_zam = il_zam;//
        this.il_zreal = il_zreal;//
        this.status = status;//
        this.plik = plik;//
        this.timestamp = timestamp;
        this.cena = cena;
        this.cena_p_rab = cena_p_rab;
    }

    public OrderPosition() {
    }


    public OrderPosition(int nrID) {
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

    public int getNr_prom() {
        return nr_prom;
    }

    public void setNr_prom(int nr_prom) {
        this.nr_prom = nr_prom;
    }

    public int getKt() {
        return kt;
    }

    public void setKt(int kt) {
        this.kt = kt;
    }

    public String getNazwa_leku() {
        return nazwa_leku;
    }

    public void setNazwa_leku(String nazwa_leku) {
        this.nazwa_leku = nazwa_leku;
    }

    public int getIl_zam() {
        return il_zam;
    }

    public void setIl_zam(int il_zam) {
        this.il_zam = il_zam;
    }

    public int getIl_zreal() {
        return il_zreal;
    }

    public void setIl_zreal(int il_zreal) {
        this.il_zreal = il_zreal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlik() {
        return plik;
    }

    public void setPlik(String plik) {
        this.plik = plik;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Float getCena() {
        return cena;
    }

    public void setCena(Float cena) {
        this.cena = cena;
    }

    public Float getCena_p_rab() {
        return cena_p_rab;
    }

    public void setCena_p_rab(Float cena_p_rab) {
        this.cena_p_rab = cena_p_rab;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}