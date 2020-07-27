package pl.wotu.orderapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.wotu.orderapp.Adapter.OrderPositionsAdapter;
import pl.wotu.orderapp.database.DatabaseHelper;
import pl.wotu.orderapp.database.model.Order;
import pl.wotu.orderapp.database.model.OrderPosition;

public class OrderPosActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseHelper db;
    private TextView mNrZam;
    private TextView emptyPosView;
    private RecyclerView recyclerView;
    private List<OrderPosition> ordersPositionsList = new ArrayList<>();
    private OrderPositionsAdapter mAdapter;
    private TextView tvNrZamPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_position);

        recyclerView = findViewById(R.id.order_pos_list_view);
        emptyPosView = findViewById(R.id.empty_pos_view);

        tvNrZamPos = findViewById(R.id.tv_nr_zam_pos);
        tvNrZamPos.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();

//        String order_plik = b.getString("order_plik");

        recyclerView.setVisibility(View.VISIBLE);
        emptyPosView.setVisibility(View.GONE);

        mToolbar = findViewById(R.id.order_pos_toolbar);
        mNrZam = findViewById(R.id.tv_nr_zam_pos);
//        mPlik = findViewById(R.id.tv_plik_pos);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.order_pos));
        mNrZam.setText(b.getString("order_plik"));
//        mPlik.setText(b.getString("order_plik"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        List<OrderPosition> lista = db.getOrderPositionsByFile(b.getString("order_plik"));
        ordersPositionsList.addAll(lista);

        List<Order> orders = db.getOrderWithFilename(b.getString("order_plik"));
//        mNrZam.setText("");
        recyclerView.setVisibility(View.GONE);
        emptyPosView.setVisibility(View.VISIBLE);

        try {
            ZmienneIFunkcjeStatyczne.statusStarszyNizDwaDni(orders.get(0).getTimestamp());
            if (lista.size()>0){

                recyclerView.setVisibility(View.VISIBLE);
                emptyPosView.setVisibility(View.GONE);

                String nr_zam = lista.get(0).getNr_zam();
//                mNrZam.setText(lista.get(0).getNr_zam());
                for(OrderPosition elem:lista){
                    if (!nr_zam.equals(elem.getNr_zam())){
//                        ZmienneIFunkcjeStatyczne.showToast((Activity) getBaseContext(),"",ZmienneIFunkcjeStatyczne.ICON_WARNING,ZmienneIFunkcjeStatyczne.BG_RED);

                    }
                }

                mAdapter = new OrderPositionsAdapter(OrderPosActivity.this,this, ordersPositionsList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
                recyclerView.setAdapter(mAdapter);

//                mNrZam.setText(mAdapter.getZamNr()+" NOWA METODA");
            }
        }
        catch (IndexOutOfBoundsException exOb) {

        }








    }


}
