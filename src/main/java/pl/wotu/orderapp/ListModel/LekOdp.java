package pl.wotu.orderapp.ListModel;

public class LekOdp {

//    private final int iloscZamowiona;

    private String id;
    private int nr_zam_sys;;
    private int code;
    private int nr_zam_apt;
    private int nr_prom;
    private int kt;
    private String nazwa_leku;
    private int il_zam;
    private int il_zreal;
    private int status_odmowy;
    private int poprzedni_status;
    private String data_wyslania;
    private String czas_wyslania;
    private String data_pobrania;
    private String data_utworzenia;
    private String czas_pobrania;
    private String nazwa_pliku;
    private String oferta;

    public LekOdp() {

    }
    //nr_zam_sys|code|nr_zam_apt|nr_prom|kt|nazwa_leku|il_zam|il_zreal|status_odmowy|poprzedni_status|data_wyslania|xpo:czas_wyslania|data_pobrania|data_utworzenia|czas_pobrania|nazwa_pliku
    //64146|4|739|111|70587|PROTEVASC SR 35mg * 60 szt|1|1|5|1|2019-04-05|17:07:38|2019-04-05|2019-04-05|16:52:22|apt4_739.txt

    public LekOdp(String linia,String id){
        String temp;
        int palka ;
        temp = linia;

        this.id = id;

        palka = temp.indexOf("|");
        nr_zam_sys = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        code = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        nr_zam_apt = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        nr_prom = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        kt = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        nazwa_leku = temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        il_zam = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        il_zreal = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        status_odmowy = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        poprzedni_status = Integer.parseInt(temp.substring(0, palka));
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        data_wyslania = temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        czas_wyslania = temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        data_pobrania =temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        data_utworzenia =temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        czas_pobrania = temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        nazwa_pliku = temp.substring(0, palka);
        temp=temp.substring(palka+1,temp.length());

        palka = temp.indexOf("|");
        oferta = temp.substring(0, palka);

//        iloscZamowiona =0;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNr_zam_sys() {
        return nr_zam_sys;
    }

    public void setNr_zam_sys(int nr_zam_sys) {
        this.nr_zam_sys = nr_zam_sys;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNr_zam_apt() {
        return nr_zam_apt;
    }

    public void setNr_zam_apt(int nr_zam_apt) {
        this.nr_zam_apt = nr_zam_apt;
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

    public int getStatus_odmowy() {
        return status_odmowy;
    }

    public void setStatus_odmowy(int status_odmowy) {
        this.status_odmowy = status_odmowy;
    }

    public int getPoprzedni_status() {
        return poprzedni_status;
    }

    public void setPoprzedni_status(int poprzedni_status) {
        this.poprzedni_status = poprzedni_status;
    }

    public String getData_wyslania() {
        return data_wyslania;
    }

    public void setData_wyslania(String data_wyslania) {
        this.data_wyslania = data_wyslania;
    }

    public String getCzas_wyslania() {
        return czas_wyslania;
    }

    public void setCzas_wyslania(String czas_wyslania) {
        this.czas_wyslania = czas_wyslania;
    }

    public String getData_pobrania() {
        return data_pobrania;
    }

    public void setData_pobrania(String data_pobrania) {
        this.data_pobrania = data_pobrania;
    }

    public String getData_utworzenia() {
        return data_utworzenia;
    }

    public void setData_utworzenia(String data_utworzenia) {
        this.data_utworzenia = data_utworzenia;
    }

    public String getCzas_pobrania() {
        return czas_pobrania;
    }

    public void setCzas_pobrania(String czas_pobrania) {
        this.czas_pobrania = czas_pobrania;
    }

    public String getNazwa_pliku() {
        return nazwa_pliku;
    }

    public void setNazwa_pliku(String nazwa_pliku) {
        this.nazwa_pliku = nazwa_pliku;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }
}
