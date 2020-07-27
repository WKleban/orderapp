package pl.wotu.orderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import pl.wotu.orderapp.database.model.Order;
import pl.wotu.orderapp.database.model.OrderPosition;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "orders_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Order.CREATE_TABLE);
        db.execSQL(OrderPosition.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Order.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderPosition.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void czyscBazePrzyWylogowaniu(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Order.TABLE_NAME);
        db.execSQL("delete from "+ OrderPosition.TABLE_NAME);

    }

    public long insertOrder(String nr_zam, String date, String czas, int status, int il_poz, String wartosc, String plik, String oferta) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(Order.COLUMN_NR_ZAM, nr_zam);
        values.put(Order.COLUMN_DATE, date);
        values.put(Order.COLUMN_CZAS, czas);
        values.put(Order.COLUMN_STATUS, status);
        values.put(Order.COLUMN_IL_POZ, il_poz);
        values.put(Order.COLUMN_WARTOSC,wartosc);
        values.put(Order.COLUMN_PLIK, plik);
        values.put(Order.COLUMN_OFERTA, oferta);


        // insert row
        long id = db.insert(Order.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertOrder(String nr_zam, String date, String czas, int status, int il_poz, String plik, String oferta) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(Order.COLUMN_NR_ZAM, nr_zam);
        values.put(Order.COLUMN_DATE, date);
        values.put(Order.COLUMN_CZAS, czas);
        values.put(Order.COLUMN_STATUS, status);
        values.put(Order.COLUMN_IL_POZ, il_poz);
        values.put(Order.COLUMN_PLIK, plik);
        values.put(Order.COLUMN_OFERTA, oferta);


        // insert row
        long id = db.insert(Order.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertOrderPosition(String nr_zam,int nr_prom,int kt,int il_zam,int il_zreal,int status,String plik,String nazwa_leku,Float cena_p_rabat,Float cena){

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(OrderPosition.COLUMN_NR_ZAM, nr_zam);
        values.put(OrderPosition.COLUMN_NR_PROM, nr_prom);
        values.put(OrderPosition.COLUMN_KT, kt);
        values.put(OrderPosition.COLUMN_IL_ZAM, il_zam);
        values.put(OrderPosition.COLUMN_IL_ZREAL, il_zreal);
        values.put(OrderPosition.COLUMN_STATUS,status);
        values.put(OrderPosition.COLUMN_PLIK, plik);

        values.put(OrderPosition.COLUMN_CENA, cena);
        values.put(OrderPosition.COLUMN_CENA_PORAB, cena_p_rabat);

        values.put(OrderPosition.COLUMN_NAZWA_LEKU, nazwa_leku);
//        values.put(OrderPositions.COLUMN_TIMESTAMP, timestamp);

//        public static final String COLUMN_NAZWA_LEKU = "nazwa_leku";
//        public static final String COLUMN_TIMESTAMP = "timestamp";

        // insert row
        long id = db.insert(OrderPosition.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;

    }

    public Order getOrders(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Order.TABLE_NAME,
                new String[]{Order.COLUMN_ID, Order.COLUMN_NR_ZAM,Order.COLUMN_DATE,Order.COLUMN_CZAS,Order.COLUMN_STATUS,Order.COLUMN_IL_POZ,Order.COLUMN_WARTOSC,Order.COLUMN_PLIK, Order.COLUMN_TIMESTAMP},
                Order.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
//        Order(int id, String nr_zam, String date, String czas, int status, int il_poz, String plik, String timestamp)
        Order orders = new Order(
                cursor.getInt(cursor.getColumnIndex(Order.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_NR_ZAM)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_CZAS)),
                cursor.getInt(cursor.getColumnIndex(Order.COLUMN_STATUS)),
                cursor.getInt(cursor.getColumnIndex(Order.COLUMN_IL_POZ)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_WARTOSC)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_PLIK)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(Order.COLUMN_OFERTA)));

        // close the db connection
        cursor.close();

        return orders;
    }

    public OrderPosition getOrderPositionss(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(OrderPosition.TABLE_NAME,
                new String[]{OrderPosition.COLUMN_ID, OrderPosition.COLUMN_NR_ZAM, OrderPosition.COLUMN_NR_PROM, OrderPosition.COLUMN_KT, OrderPosition.COLUMN_NAZWA_LEKU, OrderPosition.COLUMN_IL_ZAM, OrderPosition.COLUMN_IL_ZREAL, OrderPosition.COLUMN_STATUS, OrderPosition.COLUMN_PLIK, OrderPosition.COLUMN_TIMESTAMP},
                OrderPosition.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
//        Order(int id, String nr_zam, String date, String czas, int status, int il_poz, String plik, String timestamp)
        OrderPosition orderPositions = new OrderPosition(
                cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_NR_ZAM)),
                cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_NR_PROM)),
                cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_KT)),
                cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_NAZWA_LEKU)),
                cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_IL_ZAM)),
                cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_IL_ZREAL)),
                cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_PLIK)),
                cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_TIMESTAMP)),
                cursor.getFloat(cursor.getColumnIndex(OrderPosition.COLUMN_CENA)),
                cursor.getFloat(cursor.getColumnIndex(OrderPosition.COLUMN_CENA_PORAB)));

        // close the db connection
        cursor.close();

        return orderPositions;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Order.TABLE_NAME + " ORDER BY " +
                Order.COLUMN_DATE+" DESC , "+ Order.COLUMN_PLIK + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndex(Order.COLUMN_ID)));
                order.setNr_zam(cursor.getString(cursor.getColumnIndex(Order.COLUMN_NR_ZAM)));
                order.setDate(cursor.getString(cursor.getColumnIndex(Order.COLUMN_DATE)));
                order.setCzas(cursor.getString(cursor.getColumnIndex(Order.COLUMN_CZAS)));
                order.setStatus(cursor.getInt(cursor.getColumnIndex(Order.COLUMN_STATUS)));
                order.setIl_poz(cursor.getInt(cursor.getColumnIndex(Order.COLUMN_IL_POZ)));
                order.setWartosc(cursor.getString(cursor.getColumnIndex(Order.COLUMN_WARTOSC)));
                order.setPlik(cursor.getString(cursor.getColumnIndex(Order.COLUMN_PLIK)));
                order.setTimestamp(cursor.getString(cursor.getColumnIndex(Order.COLUMN_TIMESTAMP)));
                order.setOferta(cursor.getString(cursor.getColumnIndex(Order.COLUMN_OFERTA)));
                orders.add(order);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return orders;
    }

    public List<OrderPosition> getAllOrderPositions() {
        List<OrderPosition> orderPositions = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + OrderPosition.TABLE_NAME + " ORDER BY " +
                OrderPosition.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderPosition orderPosition = new OrderPosition();
                orderPosition.setId(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_ID)));
                orderPosition.setNr_zam(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_NR_ZAM)));
                orderPosition.setNr_prom(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_NR_PROM)));
                orderPosition.setKt(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_KT)));
                orderPosition.setNazwa_leku(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_NAZWA_LEKU)));
                orderPosition.setIl_zam(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_IL_ZAM)));
                orderPosition.setIl_zreal(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_IL_ZREAL)));
                orderPosition.setStatus(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_STATUS)));
                orderPosition.setPlik(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_PLIK)));
                orderPosition.setTimestamp(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_TIMESTAMP)));
                orderPosition.setCena_p_rab(cursor.getFloat(cursor.getColumnIndex(OrderPosition.COLUMN_CENA_PORAB)));
                orderPosition.setCena(cursor.getFloat(cursor.getColumnIndex(OrderPosition.COLUMN_CENA)));
                orderPositions.add(orderPosition);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return orderPositions;
    }

    public List<OrderPosition> getOrderPositionsByFile(String plik) {
        List<OrderPosition> orderPositions = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + OrderPosition.TABLE_NAME +" WHERE  "+ OrderPosition.COLUMN_PLIK + " = \""+plik + "\" ORDER BY " +
                OrderPosition.COLUMN_NAZWA_LEKU + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderPosition orderPosition = new OrderPosition();
                orderPosition.setId(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_ID)));
                orderPosition.setNr_zam(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_NR_ZAM)));
                orderPosition.setNr_prom(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_NR_PROM)));
                orderPosition.setKt(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_KT)));
                orderPosition.setNazwa_leku(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_NAZWA_LEKU)));
                orderPosition.setIl_zam(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_IL_ZAM)));
                orderPosition.setIl_zreal(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_IL_ZREAL)));
                orderPosition.setStatus(cursor.getInt(cursor.getColumnIndex(OrderPosition.COLUMN_STATUS)));
                orderPosition.setPlik(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_PLIK)));
                orderPosition.setTimestamp(cursor.getString(cursor.getColumnIndex(OrderPosition.COLUMN_TIMESTAMP)));
                orderPosition.setCena_p_rab(cursor.getFloat(cursor.getColumnIndex(OrderPosition.COLUMN_CENA_PORAB)));
                orderPosition.setCena(cursor.getFloat(cursor.getColumnIndex(OrderPosition.COLUMN_CENA)));
                orderPositions.add(orderPosition);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return orderPositions;
    }

    public int getOrdersCount() {
        String countQuery = "SELECT  * FROM " + Order.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getOrderPositionsCount(int nr_zam) {
        String countQuery = "SELECT  * FROM " + Order.TABLE_NAME+ " WHERE "+ OrderPosition.COLUMN_NR_ZAM + " = "+nr_zam;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getOrderPositionsCount() {
        String countQuery = "SELECT  * FROM " + OrderPosition.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateOrderStatus(String plik, int status,String wartosc){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(Order.COLUMN_NR_ZAM, order.getIl_poz());
//        values.put(Order.COLUMN_DATE, order.getDate());
//        values.put(Order.COLUMN_CZAS, order.getCzas());
        values.put(Order.COLUMN_STATUS, status);
//        values.put(Order.COLUMN_IL_POZ, order.getIl_poz());
//        values.put(Order.COLUMN_WARTOSC,order.getWartosc());
        values.put(Order.COLUMN_WARTOSC,wartosc);
//        values.put(Order.COLUMN_PLIK, plik);

//        System.out.println("Order.TABLE_NAME="+Order.TABLE_NAME);
        // updating row
        return db.update(Order.TABLE_NAME, values, Order.COLUMN_PLIK + " = ?",
                new String[]{plik});
    }

    public int updateOrderPositionStatus(String plik, int status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(Order.COLUMN_NR_ZAM, order.getIl_poz());
//        values.put(Order.COLUMN_DATE, order.getDate());
//        values.put(Order.COLUMN_CZAS, order.getCzas());
        values.put(OrderPosition.COLUMN_STATUS, status);
//        values.put(Order.COLUMN_IL_POZ, order.getIl_poz());
//        values.put(Order.COLUMN_WARTOSC,order.getWartosc());
//        values.put(Order.COLUMN_PLIK, plik);

        // updating row
        return db.update(OrderPosition.TABLE_NAME, values, OrderPosition.COLUMN_PLIK + " = ?",
                new String[]{plik});
    }

    public int updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_NR_ZAM, order.getNr_zam());
        values.put(Order.COLUMN_DATE, order.getDate());
        values.put(Order.COLUMN_CZAS, order.getCzas());
        values.put(Order.COLUMN_STATUS, order.getStatus());
        values.put(Order.COLUMN_IL_POZ, order.getIl_poz());
        values.put(Order.COLUMN_WARTOSC,order.getWartosc());
        values.put(Order.COLUMN_PLIK, order.getPlik());
        values.put(Order.COLUMN_OFERTA, order.getOferta());


        // updating row
        return db.update(Order.TABLE_NAME, values, Order.COLUMN_ID + " = ?",
                new String[]{String.valueOf(order.getId())});


    }

    public int updateOrderPositions(OrderPosition orderPositions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderPosition.COLUMN_NR_ZAM, orderPositions.getNr_zam());
        values.put(OrderPosition.COLUMN_NR_PROM, orderPositions.getNr_prom());
        values.put(OrderPosition.COLUMN_KT, orderPositions.getKt());
        values.put(OrderPosition.COLUMN_IL_ZAM, orderPositions.getIl_zam());
        values.put(OrderPosition.COLUMN_IL_ZREAL,orderPositions.getIl_zreal());
        values.put(OrderPosition.COLUMN_STATUS, orderPositions.getStatus());
        values.put(OrderPosition.COLUMN_PLIK, orderPositions.getPlik());
        values.put(OrderPosition.COLUMN_CENA, orderPositions.getCena());
        values.put(OrderPosition.COLUMN_CENA_PORAB, orderPositions.getCena_p_rab());

        // updating row
        return db.update(OrderPosition.TABLE_NAME, values, OrderPosition.COLUMN_ID + " = ?",
                new String[]{String.valueOf(orderPositions.getId())});
    }

    public void deleteOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Order.TABLE_NAME, Order.COLUMN_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        db.close();
    }



    public void deleteOrderPosition(OrderPosition orderPositions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(OrderPosition.TABLE_NAME, OrderPosition.COLUMN_ID + " = ?",
                new String[]{String.valueOf(orderPositions.getId())});
        db.close();
    }

//    public Order getOrderWithFilename
//
//
//        return null;
//    }



    public List<Order> getOrderWithFilename(String nazwa_pliku) {
        List<Order> orders = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Order.TABLE_NAME + " WHERE TRIM(plik) = '"+nazwa_pliku.trim()+"'" + " ORDER BY " +
                Order.COLUMN_TIMESTAMP + " DESC";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndex(Order.COLUMN_ID)));
                order.setNr_zam(cursor.getString(cursor.getColumnIndex(Order.COLUMN_NR_ZAM)));
                order.setDate(cursor.getString(cursor.getColumnIndex(Order.COLUMN_DATE)));
                order.setCzas(cursor.getString(cursor.getColumnIndex(Order.COLUMN_CZAS)));
                order.setStatus(cursor.getInt(cursor.getColumnIndex(Order.COLUMN_STATUS)));
                order.setIl_poz(cursor.getInt(cursor.getColumnIndex(Order.COLUMN_IL_POZ)));
                order.setWartosc(cursor.getString(cursor.getColumnIndex(Order.COLUMN_WARTOSC)));
                order.setPlik(cursor.getString(cursor.getColumnIndex(Order.COLUMN_PLIK)));
                order.setTimestamp(cursor.getString(cursor.getColumnIndex(Order.COLUMN_TIMESTAMP)));
                order.setOferta(cursor.getString(cursor.getColumnIndex(Order.COLUMN_OFERTA)));
                orders.add(order);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

//        System.out.println( "--->SELECT  * FROM " + Order.TABLE_NAME + "WHERE TRIM(plik) = '"+nazwa_pliku.trim()+"'" + " ORDER BY " +
//                Order.COLUMN_TIMESTAMP + " DESC");
//        System.out.println("--->Liczba wyników: "+orders.size());

        return orders;
    }

//    public void getOrderPositions(String nazwa_leku, int parseInt) {
//
//    }

    public int checkPosition(String nazwa_pliku, int kt) {

        String countQuery = "SELECT  * FROM " + OrderPosition.TABLE_NAME + " WHERE "+ OrderPosition.COLUMN_PLIK+" = '"+nazwa_pliku.trim()+ "' AND "+ OrderPosition.COLUMN_KT+" = " + kt ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

//        System.out.println(countQuery);
//        System.out.println("--->checkPosition: "+count);

        return count;
    }


    public void removePositionFromOrder(String nazwa_pliku, int kt){

        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ Order.TABLE_NAME);
        db.execSQL("delete from "+ OrderPosition.TABLE_NAME  + " WHERE "+ OrderPosition.COLUMN_PLIK+" = '"+nazwa_pliku.trim()+ "' AND "+ OrderPosition.COLUMN_KT+" = " + kt);

    }


}
