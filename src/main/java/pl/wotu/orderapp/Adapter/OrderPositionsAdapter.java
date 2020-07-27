package pl.wotu.orderapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.wotu.orderapp.Global;
import pl.wotu.orderapp.R;
import pl.wotu.orderapp.ZmienneIFunkcjeStatyczne;
import pl.wotu.orderapp.database.DatabaseHelper;
import pl.wotu.orderapp.database.model.OrderPosition;


public class OrderPositionsAdapter extends RecyclerView.Adapter<OrderPositionsAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<OrderPosition> orderPositionsList;
    private OrderPosition orderPositions;
    private int id;
    private int il_zam;
    private int il_zreal;
    private int kt;
    private String nazwa_leku;
    private int nr_prom;
    private String nr_zam;
    private String plik;
    private int status;
    private String timestamp;
    private Float cena;
    private Float cena_po_rabacie;




    public String getZamNr() {
        return nr_zam;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private DatabaseHelper db;
        private int nrID;

        private ConstraintLayout cLOrderPositionsBorder;
        private ConstraintLayout clOrdersBorder2;
        private TextView kt_item_pos;
        private TextView nazwa_list_item_pos;
        private TextView nr_zam_list_item_pos;
        private TextView zam_zreal_list_item;
        private TextView status_list_item_pos;

        private TextView cena_pos_item;
        private TextView cena_porab_pos_item;
//        private TextView plik_list_item_pos;


        public MyViewHolder(final View view) {
            super(view);
            db = new DatabaseHelper(activity);

            cLOrderPositionsBorder = view.findViewById(R.id.cLOrderPositionsBorder);
            clOrdersBorder2 = view.findViewById(R.id.clOrdersBorder2);
            kt_item_pos = view.findViewById(R.id.kt_item_pos);
            nazwa_list_item_pos = view.findViewById(R.id.nazwa_list_item_pos);
            nr_zam_list_item_pos = view.findViewById(R.id.nr_zam_list_item_pos);
            zam_zreal_list_item = view.findViewById(R.id.zam_zreal_list_item);
            status_list_item_pos = view.findViewById(R.id.status_list_item_pos);

            cena_pos_item = view.findViewById(R.id.cena_hurt_lek_pos_list_item);
            cena_porab_pos_item = view.findViewById(R.id.zam_cena_porab_pos_lek_list_item);

//            plik_list_item_pos = view.findViewById(R.id.plik_list_item_pos);

            nrID = 0;


        }
    }


    public OrderPositionsAdapter(Activity activity, Context context, List<OrderPosition> orderPositionsList) {
        this.orderPositionsList = orderPositionsList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_positions_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        orderPositions = orderPositionsList.get(position);
//        String status;

        id = orderPositions.getId();
        il_zam = orderPositions.getIl_zam();
        il_zreal = orderPositions.getIl_zreal();
        kt = orderPositions.getKt();
        nazwa_leku = orderPositions.getNazwa_leku();
        nr_prom = orderPositions.getNr_prom();
        nr_zam = orderPositions.getNr_zam();
        plik = orderPositions.getPlik();
        status = orderPositions.getStatus();
        timestamp = orderPositions.getTimestamp();
        cena = orderPositions.getCena();
        cena_po_rabacie = orderPositions.getCena_p_rab();





        nazwa_leku = ZmienneIFunkcjeStatyczne.usunPodkreslenieZKoncaLinii(nazwa_leku);

//        holder.nazwa_list_item_pos.setText(nazwa_leku+"\nnr_zam "+nr_zam+"\nplik "+plik);
        holder.nazwa_list_item_pos.setText(nazwa_leku);

        holder.kt_item_pos.setText(kt+"");
        holder.nr_zam_list_item_pos.setText("nr_zam "+nr_zam);
//        holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+")" );
        holder.zam_zreal_list_item.setText("");

        if (cena != null || cena_po_rabacie != null){
            holder.cena_pos_item.setVisibility(View.VISIBLE);
            holder.cena_porab_pos_item.setVisibility(View.VISIBLE);
            holder.cena_pos_item.setText(String.format("%.2f", cena)+" zł");
            holder.cena_porab_pos_item.setText(String.format("%.2f", cena_po_rabacie)+" zł");
        }
        else {
            holder.cena_pos_item.setVisibility(View.GONE);
            holder.cena_porab_pos_item.setVisibility(View.GONE);
            holder.cena_pos_item.setText("");
            holder.cena_porab_pos_item.setText("");
        }


//        holder.plik_list_item_pos.setText("plik "+plik);
        holder.cLOrderPositionsBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String stringToast = "id: "+id;
//                stringToast+="\n"+orderPositions.getStatus();
//                stringToast+="\n"+orderPositions.getPlik();
//                stringToast+="\n"+orderPositions.getNr_zam();
//                stringToast+="\n"+orderPositions.getNr_prom();
//                stringToast+="\n"+orderPositions.getNazwa_leku();
//                stringToast+="\n"+orderPositions.getKt();
//                stringToast+="\n"+orderPositions.getIl_zreal();
//                stringToast+="\n"+orderPositions.getIl_zam();
//                stringToast+="\n"+orderPositions.getId();
//                stringToast+="\n"+orderPositions.getCena();
//                stringToast+="\n"+orderPositions.getCena_p_rab();

//                ZmienneIFunkcjeStatyczne.showToast(activity,stringToast,ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_YELLOW);


            }
        });


//        if (status==0)

//        orderStatus = MainActivity;





        switch (status){
            case Global.S0_OCZEKIWANIE_NA_REALIZACJE:
//                if (statusStarszyNizDwaDni(order.getTimestamp())){
//                    holder.thisLayout.setBackgroundResource(R.color.colorRed);
//                    status = "Nie zrealizowano";
                holder.status_list_item_pos.setText("Oczekiwanie na realizację");
//                }else{
                    holder.clOrdersBorder2.setBackgroundResource(R.color.colorWhite);
                holder.zam_zreal_list_item.setText("(Zamówiono " + il_zam+" szt.)" );
//                    status = "Oczekiwanie na realizację";
//                }

                break;

            case Global.S1_W_TRAKCIE_REALIZACJI:
//                if (statusStarszyNizDwaDni(order.getTimestamp())){
//                    holder.clOrdersBorder2.setBackgroundResource(R.color.colorRed);
//                    status = "Nie zrealizowano";
                holder.status_list_item_pos.setText("W trakcie realizacji");
                holder.zam_zreal_list_item.setText("(Zamówiono " + il_zam+" szt.)" );
//                }else{
                    holder.clOrdersBorder2.setBackgroundResource(R.color.colorWhite);
//                    status = "W trakcie realizacji";
//                }
                break;

            case Global.S2_CZESCIOWO_ZREALIZOWANE:
            case Global.S3_CZESCIOWO_ZREALIZOWANE:

                if ((il_zreal ==0) ){
                    holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_red);
                    holder.status_list_item_pos.setText("Niezrealizowane");
                    holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                }
                else if (il_zreal!=il_zam){
                    holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_yellow);
                    holder.status_list_item_pos.setText("Częściowo zrealizowane");
                    holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                }else{
                    holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_green);
                    holder.status_list_item_pos.setText("Zrealizowane");
                    holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                }

//                status = "Częściowo zrealizowane";
                break;

            case Global.S4_ZREALIZOWANE:
            case Global.S5_ZREALIZOWANE:
//                status = "Zrealizowane";
                holder.status_list_item_pos.setText("Zrealizowane");
                holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_green);
                break;

            case Global.S6_BLOKADA:
            case Global.S7_BLOKADA:
                holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_red);
//                status = "Blokada";
                holder.status_list_item_pos.setText("Blokada");
                holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                break;

            case Global.S8_NIE_ZREALIZOWANE:
            case Global.S9_NIE_ZREALIZOWANE:
                holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_red);
//                status = "Nie zrealizowano";
                holder.status_list_item_pos.setText("Nie zrealizowano");
                holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                break;
            case Global.S10_BRAK_DOSTEPU:
                holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_red);
//                status = "Brak dostępu do systemu";
                holder.status_list_item_pos.setText("Brak dostępu do systemu");
                holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                break;
            case Global.S15_NIEAKTUALNA_OFERTA:
                holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_red);
                holder.status_list_item_pos.setText("Nie zrealizowane - Nieaktualna oferta");
                holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
//                status = "Nie zrealizowane - Nieaktualna oferta";
                break;
            default:
                holder.clOrdersBorder2.setBackgroundResource(R.drawable.custom_toast_yellow);
//                status = "Nieznany status ["+order.getStatus()+"]";
                holder.status_list_item_pos.setText("Nieznany status ["+orderPositions.getStatus()+"]");
                holder.zam_zreal_list_item.setText("("+il_zreal+" z " + il_zam+" szt.)" );
                break;

        }

//        Toast.makeText(context,nazwa_leku,Toast.LENGTH_SHORT).show();

        }


    private boolean statusStarszyNizDwaDni(String statusTimestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(System.currentTimeMillis());
        String date = DateFormat.format("yyyy-MM-dd hh:mm:ss",
                cal).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date statusDate=null,actualDate = null;

        try {
            statusDate = sdf.parse(statusTimestamp.substring(0,10));
            actualDate = sdf.parse(date.substring(0,10));
            if (statusDate.compareTo(actualDate) < 2) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public int getItemCount() {
        return orderPositionsList.size();
    }


}

