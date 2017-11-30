package helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by jemarinero on 20/11/2017.
 */

public class Configuracion {

    public static final String TABLE = "Configuracion";
    public static final String ID = "_id";
    public static final String FECHA_TRABAJO = "FechaTrabajo";
    public static final String URL_SERVIDOR = "UrlServidor";


    public Configuracion() {
    }




//    public int update(SQLiteDatabase db,String fechaTrabajo, String urlServidor,String where, String[] params){
//        ContentValues values = new ContentValues();
//        values.put(FECHA_TRABAJO, fechaTrabajo);
//        values.put(URL_SERVIDOR, urlServidor);
//
//        return db.update(TABLE,values,where,params);
//
//    }
//    public int deleteById(SQLiteDatabase db,String[] id){
//        return db.delete(TABLE,"_id = ?",id);
//    }


}
