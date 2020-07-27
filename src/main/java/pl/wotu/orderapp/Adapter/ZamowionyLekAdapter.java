package pl.wotu.orderapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.wotu.orderapp.IloscZamActivity;
import pl.wotu.orderapp.ListModel.Lek;
import pl.wotu.orderapp.R;

public class ZamowionyLekAdapter extends RecyclerView.Adapter<ZamowionyLekAdapter.ViewHolder>  {

    private static final int EDYCJA_ZAMOWIENIA = 1;

    private final Activity kontekst;
    private List<Lek> lekList;
    private int kt;

    public ZamowionyLekAdapter(Activity kontekst, List<Lek> lekList) {
        this.lekList = lekList;
        this.kontekst=kontekst;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zamowiony_lek_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double cenaHurt_data = lekList.get(position).getCenaHurtowa();
        double cenaPoRab_data = lekList.get(position).getCenaPoRabacie();
        int id = position+1;
        int pozycja_w_ofercie_data = Integer.parseInt(lekList.get(position).getId());

        String producent_data = lekList.get(position).getProducent();
        holder.setCenaHurt_CenaPorab_Producent(cenaHurt_data,cenaPoRab_data,producent_data);

        String nazwa_data = lekList.get(position).getNazwa();
        holder.setNazwa(nazwa_data,pozycja_w_ofercie_data);

        kt = lekList.get(position).getKt();

        double cena_po_rab_data = lekList.get(position).getCenaPoRabacie();//+" zł";
        holder.setCenaPoRabacie(cena_po_rab_data);

        String ilosc_zam = String.valueOf(lekList.get(position).getIloscZamowiona());
        holder.setIloscZam(ilosc_zam);

        holder.setIDPozycji(position+1);

    }

    @Override
    public int getItemCount() {
        return lekList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private double cenaPoRab,cenaHurt;

        private ConstraintLayout kafelekConstraintLayout;
        private TextView nazwaView,cenaPoRabView,iloscZamView,pozycjaView,pozycjaWOfercieTV;
        private String producent;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCenaHurt_CenaPorab_Producent(double cenaHurt, double cenaPoRab, String producent){
            this.cenaPoRab = cenaPoRab;
            this.cenaHurt = cenaHurt;
            this.producent = producent;
        }

        public void setNazwa(String nazwa_data, final int numer_pozycji_w_zamowieniu) {
            nazwaView = mView.findViewById(R.id.nr_dokumentu_list_item);
            nazwaView.setText(nazwa_data);
            pozycjaWOfercieTV = mView.findViewById(R.id.zam_nr_w_ofercie_poz);
            pozycjaWOfercieTV.setText(numer_pozycji_w_zamowieniu+"");

            kafelekConstraintLayout = mView.findViewById(R.id.constraintLayoutListElem);
            kafelekConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                synchronized(lekList){
                    lekList.notify();
                }

                Intent iloscZamIntent = new Intent(kontekst,IloscZamActivity.class);
                iloscZamIntent.putExtra("nazwaView",nazwaView.getText().toString());
                iloscZamIntent.putExtra("iloscZamView",Integer.parseInt(iloscZamView.getText().toString()));
                iloscZamIntent.putExtra("pozycjaView",pozycjaView.getText().toString());
                iloscZamIntent.putExtra("cenaHurt",cenaHurt);
                iloscZamIntent.putExtra("cenaPoRab",cenaPoRab);
                iloscZamIntent.putExtra("producentView",producent);
                iloscZamIntent.putExtra("pozycjaView",String.valueOf(numer_pozycji_w_zamowieniu));
                iloscZamIntent.putExtra("pozycjaWOfercieTV",pozycjaWOfercieTV.getText().toString());

                iloscZamIntent.putExtra("kt",kt);
                kontekst.startActivityForResult(iloscZamIntent,EDYCJA_ZAMOWIENIA);
                }
            });

        }

        public void setCenaPoRabacie(double cena_po_rab_data) {
            cenaPoRabView = mView.findViewById(R.id.zam_cena_porab_pos_lek_list_item);
            cenaPoRabView.setText(String.format("%.2f", cena_po_rab_data)+" zł");
        }

        public void setIloscZam(String ilosc_zam) {
            iloscZamView = mView.findViewById(R.id.zam_ilosc_zam_lek_list_item);
            iloscZamView.setText(ilosc_zam);
        }

        public void setIDPozycji(int id) {
            pozycjaView = mView.findViewById(R.id.zam_nr_poz);
            pozycjaView.setText(""+id);
        }

    }
}
