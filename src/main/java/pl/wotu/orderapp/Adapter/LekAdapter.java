package pl.wotu.orderapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.wotu.orderapp.IloscZamActivity;
import pl.wotu.orderapp.ListModel.Lek;
import pl.wotu.orderapp.R;

public class LekAdapter extends RecyclerView.Adapter<LekAdapter.ViewHolder> implements Filterable {

    private static final int DODAJ_LEKI_DO_ZAMOWIENIA = 1;

    private final Activity kontekst;
    private List<Lek> lekList;
    private List<Lek> lekListFiltered;
    private int kt;

    public LekAdapter(Activity kontekst,List<Lek> lekList) {
        this.lekList = lekList;
        this.kontekst=kontekst;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lek_list_item,parent,false);

        return new ViewHolder(view);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    lekListFiltered = lekList;
                } else {
                    List<Lek> filteredList = new ArrayList<>();
                    for (Lek row : lekList) {
                        if (row.getNazwa().toLowerCase().contains(charString.toLowerCase())||row.getProducent().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }
                    lekListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = lekListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                lekListFiltered = (ArrayList<Lek>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String nazwa_data = lekList.get(position).getNazwa();
//        nazwa_data = ZmienneIFunkcjeStatyczne.usunPodkreslenieZKoncaLinii(nazwa_data);

        String id_data = lekList.get(position).getId();
        holder.setNazwa(nazwa_data);
        holder.setIDPozycji(id_data);

        kt = lekList.get(position).getKt();

        String producent_data = lekList.get(position).getProducent();
        holder.setProducent(producent_data);

        double cena_po_rab_data = lekList.get(position).getCenaPoRabacie();//+" zł";
        holder.setCenaPoRabacie(cena_po_rab_data);

        double cena_hurt_data = lekList.get(position).getCenaHurtowa();//+" zł";
        holder.setCenaHurt(cena_hurt_data);

        String ilosc_zam = String.valueOf(lekList.get(position).getIloscZamowiona());
        holder.setIloscZam(ilosc_zam);

    }

    @Override
    public int getItemCount() {
        return lekList.size();
    }

    public interface CallbackInterface {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView nazwaView,producentView,cenaPoRabView,cenaHurtView;
        private TextView iloscZamView;
        private TextView pozycjaWOfercieTV;
        private double cenaPoRab,cenaHurt;


        private ConstraintLayout kafelekConstraintLayout,kafelekConstraintLayoutBorder;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNazwa(String nazwa_data) {
            nazwaView = mView.findViewById(R.id.nr_dokumentu_list_item);
            nazwaView.setTypeface(Typeface.DEFAULT);
//            nazwaView.setFon
            nazwaView.setText(nazwa_data);


        }

        public void setCenaPoRabacie(double cena_po_rab_data) {
            cenaPoRabView = mView.findViewById(R.id.zam_cena_porab_pos_lek_list_item);
            cenaPoRabView.setText(String.format("%.2f", cena_po_rab_data)+" zł");
            cenaPoRab = cena_po_rab_data;
        }

        public void setProducent(String producent_data) {
            producentView = mView.findViewById(R.id.producent_lek_list_item);
            producentView.setText(producent_data);

        }

        public void setCenaHurt(double cena_hurt_data) {
            cenaHurtView = mView.findViewById(R.id.cena_hurt_lek_list_item);
            cenaHurtView.setText(String.format("%.2f", cena_hurt_data)+" zł");
            cenaHurt = cena_hurt_data;
        }

        public void setIloscZam(String ilosc_zam) {
            iloscZamView = mView.findViewById(R.id.zam_ilosc_zam_lek_list_item);
            kafelekConstraintLayoutBorder = mView.findViewById(R.id.constraintLayoutListElemBorder);
            kafelekConstraintLayout = mView.findViewById(R.id.constraintLayoutListElem);


            iloscZamView.setText(ilosc_zam);
            if (Integer.parseInt(ilosc_zam)>0){
//                kafelekConstraintLayout.setBackgroundResource(R.color.colorLightGreen); //.setBackgroundResource(R.color.myGreen);
                kafelekConstraintLayoutBorder.setBackgroundResource(R.drawable.round_list_elem_selected);
                kafelekConstraintLayout.setBackgroundResource(R.color.zielonyAPS);
                nazwaView.setTypeface(nazwaView.getTypeface(), Typeface.BOLD);
                iloscZamView.setVisibility(View.VISIBLE);
            }else {
                kafelekConstraintLayout.setBackgroundResource(R.color.colorWhite);
                nazwaView.setTypeface(nazwaView.getTypeface(), Typeface.NORMAL);
                kafelekConstraintLayoutBorder.setBackgroundResource(R.drawable.simple_list_elem);
//                kafelekConstraintLayout.setBackgroundResource(R.color.colorWhite);
                iloscZamView.setVisibility(View.GONE);
            }

            kafelekConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iloscZamIntent = new Intent(kontekst,IloscZamActivity.class);
                    iloscZamIntent.putExtra("nazwaView",nazwaView.getText().toString());
                    iloscZamIntent.putExtra("iloscZamView",Integer.parseInt(iloscZamView.getText().toString()));
                    iloscZamIntent.putExtra("cenaHurt",cenaHurt);
                    iloscZamIntent.putExtra("cenaPoRab",cenaPoRab);
                    iloscZamIntent.putExtra("producentView",producentView.getText().toString());
                    iloscZamIntent.putExtra("pozycjaView",pozycjaWOfercieTV.getText().toString());
                    iloscZamIntent.putExtra("pozycjaWOfercieTV",pozycjaWOfercieTV.getText().toString());
                    iloscZamIntent.putExtra("kt",kt);
                    kontekst.startActivityForResult(iloscZamIntent,DODAJ_LEKI_DO_ZAMOWIENIA);
                }
            });
        }


        public void setIDPozycji(String id) {
            pozycjaWOfercieTV = mView.findViewById(R.id.zam_pozycja_w_ofercieTV);
            pozycjaWOfercieTV.setVisibility(View.INVISIBLE);
            pozycjaWOfercieTV.setText(""+id);
        }
    }

    public void updateList(List<Lek> newList){
        lekList = new ArrayList<>();
        lekList.addAll(newList);
        notifyDataSetChanged();
    }
}
