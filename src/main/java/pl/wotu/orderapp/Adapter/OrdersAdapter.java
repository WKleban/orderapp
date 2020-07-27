package pl.wotu.orderapp.Adapter;

/**
 * Created by ravi on 20/02/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.wotu.orderapp.Global;
import pl.wotu.orderapp.OrderPosActivity;
import pl.wotu.orderapp.R;
import pl.wotu.orderapp.ZmienneIFunkcjeStatyczne;
import pl.wotu.orderapp.database.DatabaseHelper;
import pl.wotu.orderapp.database.model.Order;



public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {



    private Context context;
    private Activity activity;
    private List<Order> ordersList;
    private Order order;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final DatabaseHelper db;
        private int nrID;
        public ImageView iv_device;
//        public TextView nrOLI;
        public TextView ofertaOLI;
        public TextView dateOLI;
        public TextView nr_zam_list_item_pos;
        public TextView statusOLI;
        public TextView wartDokOLI;
        public ConstraintLayout thisLayout;
        public TextView plikOLI;
        public ConstraintLayout thisRow;
        public TextView czas_pole;

        public MyViewHolder(final View view) {
            super(view);
            db = new DatabaseHelper(activity);

            thisRow = view.findViewById(R.id.cLOrdersBorder);
            thisLayout = view.findViewById(R.id.clOrdersBorder2List);
            ofertaOLI = view.findViewById(R.id.kt_item_pos);
            dateOLI = view.findViewById(R.id.nazwa_list_item_pos);

            nr_zam_list_item_pos = view.findViewById(R.id.nr_zam_list_item_pos);
            statusOLI = view.findViewById(R.id.status_list_item_pos);
            wartDokOLI = view.findViewById(R.id.wart_dok_order_list_item);
            plikOLI = view.findViewById(R.id.plik_order_list_item);
            iv_device = view.findViewById(R.id.iv_pc_or_phone);

            czas_pole = view.findViewById(R.id.nr_zam_list_item_pos);
            nrID = 0;

            thisRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ZmienneIFunkcjeStatyczne.showToast(activity,"Oferta "+ofertaOLI.getText().toString(),ZmienneIFunkcjeStatyczne.ICON_INFO,ZmienneIFunkcjeStatyczne.BG_YELLOW);
                    Intent intentDetails = new Intent(activity, OrderPosActivity.class);
                    intentDetails.putExtra("order_plik",plikOLI.getText().toString());
                    intentDetails.putExtra("order_nr_zam",nr_zam_list_item_pos.getText().toString());
//                    intentDetails.putExtra("order_plik",.getText().toString());
                    context.startActivity(intentDetails);
                }
            });

            thisRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Order order = new Order(nrID);
                    db.deleteOrder(order);
                    return false;
                }
            });

        }
    }


    public OrdersAdapter(Activity activity, Context context, List<Order> ordersList) {
        this.ordersList = ordersList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        order = ordersList.get(position);
        String status;
//        statusStarszyNizDwaDni(order.getTimestamp());
        switch (order.getStatus()){
            case Global.S0_OCZEKIWANIE_NA_REALIZACJE:
                if (ZmienneIFunkcjeStatyczne.statusStarszyNizDwaDni(order.getTimestamp())){
                    holder.thisLayout.setBackgroundResource(R.color.colorRed);
                    status = "Nie zrealizowano";
                }else{
                    holder.thisLayout.setBackgroundResource(R.color.colorWhite);
                    status = "Oczekiwanie na realizację";
                }

                break;

            case Global.S1_W_TRAKCIE_REALIZACJI:
                if (ZmienneIFunkcjeStatyczne.statusStarszyNizDwaDni(order.getTimestamp())){
                    holder.thisLayout.setBackgroundResource(R.color.colorRed);
                    status = "Nie zrealizowano";
                }else{
                    holder.thisLayout.setBackgroundResource(R.color.colorWhite);
                    status = "W trakcie realizacji";
                }
                break;

            case Global.S2_CZESCIOWO_ZREALIZOWANE:
            case Global.S3_CZESCIOWO_ZREALIZOWANE:
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_yellow);
                status = "Częściowo zrealizowane";
                break;

            case Global.S4_ZREALIZOWANE:
            case Global.S5_ZREALIZOWANE:
                status = "Zrealizowane";
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_green);
                break;

            case Global.S6_BLOKADA:
            case Global.S7_BLOKADA:
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_red);
                status = "Blokada";
                break;

            case Global.S8_NIE_ZREALIZOWANE:
            case Global.S9_NIE_ZREALIZOWANE:
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_red);
                status = "Nie zrealizowane";
                break;
            case Global.S10_BRAK_DOSTEPU:
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_red);
                status = "Brak dostępu do systemu";
                break;
            case Global.S15_NIEAKTUALNA_OFERTA:
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_red);
                status = "Nie zrealizowane - Nieaktualna oferta";
                break;
            default:
                holder.thisLayout.setBackgroundResource(R.drawable.custom_toast_yellow);
                status = "Nieznany status ["+order.getStatus()+"]";
                break;

        }

//        holder.nrOLI.setText("Nr "+order.getNr_zam());


        if(order.getOferta().equals("")){
            holder.ofertaOLI.setText("");
        }else {
            holder.ofertaOLI.setText("Oferta: "+order.getOferta());
        }

        holder.dateOLI.setText(""+order.getDate());
//        holder.czasOLI.setText(""+order.getCzas());
        holder.statusOLI.setText(""+status);
        holder.wartDokOLI.setText(String.valueOf(order.getWartosc()));
        holder.plikOLI.setText(String.valueOf(order.getPlik()));
        holder.czas_pole.setText(String.valueOf(order.getCzas()));




        String[] parts = String.valueOf(order.getPlik()).split("\\."); // Tak się zapisuje rozdzielenie kropką
//        String rozszerzenie ="";
        if (parts[1].equals("mob")) {
            holder.iv_device.setImageResource(R.drawable.ic_phone_android_black_24dp);
            holder.iv_device.setVisibility(View.VISIBLE);
        }
        else if (parts[1].equals("txt")){
            holder.iv_device.setImageResource(R.drawable.ic_computer_black_24dp);
            holder.iv_device.setVisibility(View.VISIBLE);
        }
        else {
            holder.iv_device.setVisibility(View.GONE);
        }

        holder.nrID = order.getId();

    }



    @Override
    public int getItemCount() {
        return ordersList.size();
    }

}