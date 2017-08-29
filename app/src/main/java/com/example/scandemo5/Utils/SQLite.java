package com.example.scandemo5.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.scandemo5.MyApp;
import com.google.gson.Gson;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Sam on 2017/8/28.
 */

public class SQLite {

    private class SqlOpenHelper extends SQLiteOpenHelper{

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "DMS.db";
        public static final String GOODS_TABLE_NAME = "Goods";
        public static final String Procure_TABLE_NAME = "procure";


        public SqlOpenHelper(Context context) {
            super(context, DB_NAME, null , DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String DropGoodsTable = "DROP TABLE IF EXISTS " + GOODS_TABLE_NAME +";";
            String DropProcureTable = "DROP TABLE IF EXISTS " + Procure_TABLE_NAME +";";
            String CreateGoodsTable = "create table " + GOODS_TABLE_NAME + " (goods_no TEXT primary key," +
                                                                                    " goods_name TEXT," +
                                                                                    " barcode TEXT," +
                                                                                    " box_barcode TEXT," +
                                                                                    " goods_spce TEXT," +
                                                                                    " unit TEXT," +
                                                                                    " pack_quantity TEXT," +
                                                                                    " ex_day TEXT," +
                                                                                    " single_weight TEXT," +
                                                                                    " pack_weight TEXT," +
                                                                                    " single_vol TEXT," +
                                                                                    " pack_vol TEXT);";
            String CreateProcureTable = "create table " + Procure_TABLE_NAME + " (procure_no TEXT primary key,client_name TEXT);";
            Log.d(TAG, "onCreate: 执行"+DropGoodsTable);
            db.execSQL(DropGoodsTable);
            Log.d(TAG, "onCreate: 执行"+CreateGoodsTable);
            db.execSQL(CreateGoodsTable);
            Log.d(TAG, "onCreate: 执行"+DropProcureTable);
            db.execSQL(DropProcureTable);
            Log.d(TAG, "onCreate: 执行"+CreateProcureTable);
            db.execSQL(CreateProcureTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onCreate(db);
        }
    }

    public class Goods{
        public String goods_no;
        public String barcode;
        public String box_barcode;
        public String unit;
        public String goods_spce;
        public String pack_quantity;
        public String ex_day;
        public String single_weight;
        public String pack_weight;
        public String pack_vol;
        public String single_vol;
        public String goods_name;
    }

    public class Procure{
        public String procure_no;
        public String client_name;
    }

    private static SqlOpenHelper help;
    private static SQLiteDatabase  Rdb;
    private static SQLiteDatabase  Wdb;
    private static SQLite sqlite;
    private SQLite(){
        help = new SqlOpenHelper(MyApp.getContext());
        Wdb = help.getWritableDatabase();
        Rdb = help.getReadableDatabase();
    }
    public static SQLite getInstance() {
        if(sqlite == null){
            synchronized (MyApp.getContext()){
                sqlite = new SQLite();
            }
        }
        return sqlite;
    }

    public boolean InsertGoodsAll(String string){
        boolean isSuccess = false;
        List<Goods> goods = DJson.JsonToList(string,Goods.class);
        Wdb.beginTransaction();
        help.onCreate(Rdb);
        try {
            for (Goods good : goods) {
                String insert_sql = "insert into " + help.GOODS_TABLE_NAME + " values(\""+ good.goods_no + "\",\""
                                                                                        + good.goods_name + "\",\""
                                                                                        + good.barcode + "\",\""
                                                                                        + good.box_barcode + "\",\""
                                                                                        + good.goods_spce + "\",\""
                                                                                        + good.unit + "\",\""
                                                                                        + good.pack_quantity + "\",\""
                                                                                        + good.ex_day + "\",\""
                                                                                        + good.single_weight + "\",\""
                                                                                        + good.pack_weight + "\",\""
                                                                                        + good.single_vol + "\",\""
                                                                                        + good.pack_vol
                                                                                        +"\");";
                Log.d("1235", "InsertAll: " + insert_sql);
                Wdb.execSQL(insert_sql);
            }
            Wdb.setTransactionSuccessful();
            isSuccess = true;
        }catch (Exception e){
            Log.d(TAG, "InsertAll: 插入失败" + e.getMessage());
        }finally {
            Wdb.endTransaction();
        }
        return isSuccess;
    }

    public Goods getGoods(String barcode){
//        String get_sql = "select * from "+ help.GOODS_TABLE_NAME + " where goods_no = \""+ barcode +"\";";
        String get_sql = "select * from "+ help.GOODS_TABLE_NAME + " where barcode = \""+ barcode +"\";";
        Cursor cursor = null;
        Log.d("1235", "get: " + get_sql);
        cursor = Rdb.rawQuery(get_sql,null);
        if(cursor.getCount() > 0){

            cursor.moveToNext();
            Goods good = new Goods();
            good.goods_no = cursor.getString(0);
            good.goods_name = cursor.getString(1);
            good.barcode = cursor.getString(2);
            good.box_barcode = cursor.getString(3);
            good.goods_spce = cursor.getString(4);
            good.unit = cursor.getString(5);
            good.pack_quantity = cursor.getString(6);
            good.ex_day = cursor.getString(7);
            good.single_weight = cursor.getString(8);
            good.pack_weight = cursor.getString(9);
            good.single_vol = cursor.getString(10);
            good.pack_vol = cursor.getString(11);
            return good;
        }else {
            return null;
        }
    }

    public boolean InsertProcureAll(String string){
        boolean isSuccess = false;
        List<Procure> Procures = DJson.JsonToList(string,Procure.class);
        Wdb.beginTransaction();
        try {
            for (Procure procure : Procures) {
                String insert_sql = "insert into " + help.Procure_TABLE_NAME + " values(\""+ procure.procure_no + "\",\"" + procure.client_name +"\");";
                Log.d("1235", "InsertAll: " + insert_sql);
                Wdb.execSQL(insert_sql);
            }
            Wdb.setTransactionSuccessful();
            isSuccess = true;
        }catch (Exception e){
            Log.d(TAG, "InsertAll: 插入失败" + e.getMessage());
        }finally {
            Wdb.endTransaction();
        }
        return isSuccess;
    }

    public Procure getProcure(String procure_no){
        String get_sql = "select * from "+ help.Procure_TABLE_NAME + " where procure_no = \""+ procure_no +"\";";
        Cursor cursor = null;
        Log.d("1235", "get: " + get_sql);
        cursor = Rdb.rawQuery(get_sql,null);
        if(cursor.getCount() > 0){

            cursor.moveToNext();
            Procure pro = new Procure();
            pro.procure_no = cursor.getString(0);
            pro.client_name = cursor.getString(1);
            return pro;
        }else {
            return null;
        }
    }
}
