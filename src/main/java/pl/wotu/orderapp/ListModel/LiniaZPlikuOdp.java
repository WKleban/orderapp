package pl.wotu.orderapp.ListModel;

public class LiniaZPlikuOdp {

//    nr_zam_sys|
// code
// nr_zam_apt
// nr_prom
// kt
//// nazwa_leku
//// il_zam
//// il_zreal
//// status_odmowy

//// poprzedni_status
//// data_wyslania
//// xpo:czas_wyslania
//// data_pobrania
//// data_utworzenia
//// czas_pobrania
//// nazwa_pliku

    private final String date;
    private final String czas;
    private int status_odmowy;
    private float nowa_wartosc_dokumentu;
    private int il_zam;
    private int il_zreal;
    private final String nazwa_pliku;
    private final String oferta;
    private final String nazwa_leku;
    private final String kt;
    private final String nr_prom;
    private final String nr_zam_apt;

    public LiniaZPlikuOdp(String nazwa_pliku, int status_odmowy, float nowa_wartosc_dokumentu, int il_zam, int il_zreal,String date,String czas,String oferta,String nazwa_leku, String kt,String nr_prom,String nr_zam_apt) {
        this.nazwa_pliku = nazwa_pliku;
        this.status_odmowy = status_odmowy;
        this.nowa_wartosc_dokumentu = nowa_wartosc_dokumentu;
        this.il_zam = il_zam;
        this.il_zreal = il_zreal;
        this.date = date;
        this.czas = czas;
        this.oferta = oferta;
        this.nazwa_leku=nazwa_leku;
        this.kt = kt;
        this.nr_prom = nr_prom;
        this.nr_zam_apt = nr_zam_apt;
    }

    public int getStatus_odmowy() {
        return status_odmowy;
    }

    public void setStatus_odmowy(int status_odmowy) {
        this.status_odmowy = status_odmowy;
    }

    public float getNowa_wartosc_dokumentu() {
        return nowa_wartosc_dokumentu;
    }

    public void setNowa_wartosc_dokumentu(float nowa_wartosc_dokumentu) {
        this.nowa_wartosc_dokumentu = nowa_wartosc_dokumentu;
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

    public String getNazwa_pliku() {
        return nazwa_pliku;
    }

    public String getDate() {
        return date;
    }

    public String getCzas() {
        return czas;
    }

    public String getOferta() {
        return oferta;
    }

    public String getNazwa_leku() {
        return nazwa_leku;
    }

    public String getKt() {
        return kt;
    }

    public String getNr_prom() {
        return nr_prom;
    }

    public String getNr_zam_apt() {
        return nr_zam_apt;
    }

//    504 162 760
}
