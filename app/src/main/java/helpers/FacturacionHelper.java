package helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by jemarinero on 20/11/2017.
 */

public class FacturacionHelper extends SQLiteOpenHelper {
    //variables locales
    private static final String DATABASE_NAME = "Facturacion";
    private static final int DATABASE_VERSION = 1;
    private Configuracion config = new Configuracion();
    private Empresa empr = new Empresa();
    private Clientes clie = new Clientes();
    private Numeradores num = new Numeradores();
    private Servicios ser = new Servicios();

    //constructor
    public FacturacionHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        CreateTableConfiguracion(db);
        CreateTableEmpresa(db);
        CreateTableCliente(db);
        CreateTableNumerador(db);
        CreateTableServicios(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DropTableConfiguracion(db);
        DropTableEmpresa(db);
        DropTableNumerador(db);
        DropTableServicios(db);

        this.onCreate(db);
    }

    //=================================================
    //CONFIGURACION
    //=================================================
    public void CreateTableConfiguracion(SQLiteDatabase db){
        String query = "create table "+config.TABLE +"( "+
                config.ID+" integer primary key AUTOINCREMENT, "+
                config.FECHA_TRABAJO+" text null, "+
                config.URL_SERVIDOR+" text null "+
                ")";
        db.execSQL(query);
    }

    public void DropTableConfiguracion(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+config.TABLE);
    }

    public long insertConfiguracion(String fechaTrabajo, String urlServidor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(config.FECHA_TRABAJO, fechaTrabajo);
        values.put(config.URL_SERVIDOR, urlServidor);
        long rs = db.insert(config.TABLE, null, values);
        db.close();
        return rs;
    }
    public void deleteAllConfiguracion(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(config.TABLE,null,null);
        db.close();
    }
    public Cursor selectByIdConfiguracion(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {config.FECHA_TRABAJO, config.URL_SERVIDOR};
        Cursor mCursor = db.query(true, config.TABLE,cols,where
                , params, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        db.close();
        return mCursor; // iterate to get each value.
    }
    public Cursor selectAllConfiguracion(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {config.FECHA_TRABAJO, config.URL_SERVIDOR};
        Cursor mCursor = db.query(true, config.TABLE,cols,null
                , null, null, null, null, null);

        return mCursor; // iterate to get each value.
    }

    //=================================================
    //EMPRESA
    //=================================================

    public void CreateTableEmpresa(SQLiteDatabase db){
        String query = "create table "+empr.TABLE +"( "+
                empr.ID+" integer primary key AUTOINCREMENT, "+
                empr.NOMBRE+" text null, "+
                empr.DIRECCION+" text null, "+
                empr.RTN+" text null, "+
                empr.TELEFONO+" text null "+
                ")";
        db.execSQL(query);
    }

    public void DropTableEmpresa(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+empr.TABLE);
    }

    public long insertEmpresa(String nombre, String direccion, String rtn, String telefono){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(empr.NOMBRE, nombre);
        values.put(empr.DIRECCION, direccion);
        values.put(empr.RTN, rtn);
        values.put(empr.TELEFONO, telefono);
        return db.insert(empr.TABLE, null, values);
    }
    public int deleteAllEmpresa(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(empr.TABLE,null,null);
    }

    public Cursor selectAllEmpresa(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {empr.ID,empr.NOMBRE, empr.DIRECCION,empr.RTN,empr.TELEFONO};
        Cursor mCursor = db.query(true, empr.TABLE,cols,null
                , null, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public long updateEmpresa(String nombre, String direccion, String rtn, String telefono,String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(empr.NOMBRE, nombre);
        values.put(empr.DIRECCION, direccion);
        values.put(empr.RTN, rtn);
        values.put(empr.TELEFONO, telefono);

        return db.update(empr.TABLE,values,where,params);

    }

    //=================================================
    //CLIENTE
    //=================================================

    public void CreateTableCliente(SQLiteDatabase db){
        String query = "create table "+clie.TABLE +"( "+
                clie.ID+" integer primary key AUTOINCREMENT, "+
                clie.NOMBRE+" text null, "+
                clie.DIRECCION+" text null, "+
                clie.TELEFONO+" text null, "+
                clie.CORREO+" text null, "+
                clie.IDENTIDAD+" text null, "+
                clie.RTN+" text null, "+
                clie.LATITUD+" text null, "+
                clie.LONGITUD+" text null, "+
                clie.EXENTO_IMPUESTO+" integer null "+
                ")";
        db.execSQL(query);
    }
    public void DropTableCliente(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+clie.TABLE);
    }

    public long insertCliente(String nombre, String direccion, String telefono,String correo, String identidad, String rtn, String latitud, String longitud, String exento){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(clie.NOMBRE, nombre);
        values.put(clie.DIRECCION, direccion);
        values.put(clie.TELEFONO, telefono);
        values.put(clie.CORREO, correo);
        values.put(clie.IDENTIDAD, identidad);
        values.put(clie.RTN, rtn);
        values.put(clie.LATITUD, latitud);
        values.put(clie.LONGITUD, longitud);
        values.put(clie.EXENTO_IMPUESTO, exento);
        return db.insert(clie.TABLE, null, values);
    }
    public int deleteAllCliente(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(clie.TABLE,null,null);
    }

    public int deleteCliente(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(clie.TABLE,where,params);
    }

    public Cursor selectAllCliente(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {clie.ID,clie.NOMBRE, clie.DIRECCION,clie.TELEFONO,clie.CORREO,clie.IDENTIDAD,clie.RTN,clie.LATITUD,clie.LONGITUD,clie.EXENTO_IMPUESTO};
        Cursor mCursor = db.query(true, clie.TABLE,cols,null
                , null, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public Cursor selectCliente(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {clie.ID,clie.NOMBRE, clie.DIRECCION,clie.TELEFONO,clie.CORREO,clie.IDENTIDAD,clie.RTN,clie.LATITUD,clie.LONGITUD,clie.EXENTO_IMPUESTO};
        Cursor mCursor = db.query(true, clie.TABLE,cols,where
                , params, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public long updateCliente(String nombre, String direccion, String telefono,String correo, String identidad, String rtn, String exento,String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(clie.NOMBRE, nombre);
        values.put(clie.DIRECCION, direccion);
        values.put(clie.TELEFONO, telefono);
        values.put(clie.CORREO, correo);
        values.put(clie.IDENTIDAD, identidad);
        values.put(clie.RTN, rtn);
        values.put(clie.EXENTO_IMPUESTO, exento);

        return db.update(clie.TABLE,values,where,params);

    }
    public long updateClienteGPS(String latitud, String longitud, String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(clie.LATITUD, latitud);
        values.put(clie.LONGITUD, longitud);

        return db.update(clie.TABLE,values,where,params);

    }

    //=================================================
    //Numeradores
    //=================================================

    public void CreateTableNumerador(SQLiteDatabase db){
        String query = "create table "+num.TABLE +"( "+
                num.ID+" integer primary key AUTOINCREMENT, "+
                num.NUMERADOR+" text null, "+
                num.SERIE+" text null, "+
                num.NUMERO_INICIO+" integer null, "+
                num.NUMERO_FIN+" integer null, "+
                num.FECHA_INICIO+" text null, "+
                num.FECHA_FIN+" text null, "+
                num.CAI+" text null, "+
                num.ULTIMO_USADO+" integer null, "+
                num.ESTADO+" integer null "+
                ")";
        db.execSQL(query);
    }
    public void DropTableNumerador(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+num.TABLE);
    }

    public long insertNumerador(String numerador, String serie, String numeroInicio, String numeroFin, String fechaInicio, String fechaFin, String cai, String ultimo,String estado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(num.NUMERADOR, numerador);
        values.put(num.SERIE, serie);
        values.put(num.NUMERO_INICIO, numeroInicio);
        values.put(num.NUMERO_FIN, numeroFin);
        values.put(num.FECHA_INICIO, fechaInicio);
        values.put(num.FECHA_FIN, fechaFin);
        values.put(num.CAI, cai);
        values.put(num.ULTIMO_USADO, ultimo);
        values.put(num.ESTADO, estado);
        return db.insert(num.TABLE, null, values);
    }
    public int deleteAllNumeradores(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(num.TABLE,null,null);
    }

    public int deleteNumerador(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(num.TABLE,where,params);
    }

    public Cursor selectAllNumeradores(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {num.ID,num.NUMERADOR, num.SERIE,num.NUMERO_INICIO,num.NUMERO_FIN,num.FECHA_INICIO,num.FECHA_FIN,num.CAI,num.ULTIMO_USADO,num.ESTADO};
        Cursor mCursor = db.query(true, num.TABLE,cols,null
                , null, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public Cursor selectNumerador(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {num.ID,num.NUMERADOR, num.SERIE,num.NUMERO_INICIO,num.NUMERO_FIN,num.FECHA_INICIO,num.FECHA_FIN,num.CAI,num.ULTIMO_USADO,num.ESTADO};
        Cursor mCursor = db.query(true, num.TABLE,cols,where
                , params, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public long updateNumerador(String numerador, String serie, String numeroInicio, String numeroFin, String fechaInicio, String fechaFin, String cai, String ultimo,String estado,String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(num.NUMERADOR, numerador);
        values.put(num.SERIE, serie);
        values.put(num.NUMERO_INICIO, numeroInicio);
        values.put(num.NUMERO_FIN, numeroFin);
        values.put(num.FECHA_INICIO, fechaInicio);
        values.put(num.FECHA_FIN, fechaFin);
        values.put(num.CAI, cai);
        values.put(num.ULTIMO_USADO, ultimo);
        values.put(num.ESTADO, estado);

        return db.update(num.TABLE,values,where,params);

    }

    //=================================================
    //Servicios
    //=================================================

    public void CreateTableServicios(SQLiteDatabase db){
        String query = "create table "+ser.TABLE +"( "+
                ser.ID+" integer primary key AUTOINCREMENT, "+
                ser.NOMBRE+" text null, "+
                ser.PRECIO+" real null, "+
                ser.IMPUESTO+" real null "+
                ")";
        db.execSQL(query);
    }
    public void DropTableServicios(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+ser.TABLE);
    }

    public long insertServicios(String nombre, String precio, String impuesto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ser.NOMBRE, nombre);
        values.put(ser.PRECIO, precio);
        values.put(ser.IMPUESTO, impuesto);
        return db.insert(ser.TABLE, null, values);
    }
    public int deleteAllServicios(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ser.TABLE,null,null);
    }

    public int deleteServicio(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ser.TABLE,where,params);
    }

    public Cursor selectAllServicios(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {ser.ID,ser.NOMBRE, ser.PRECIO,ser.IMPUESTO};
        Cursor mCursor = db.query(true, ser.TABLE,cols,null
                , null, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public Cursor selectServicio(String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] cols = new String[] {ser.ID,ser.NOMBRE, ser.PRECIO,ser.IMPUESTO};
        Cursor mCursor = db.query(true, ser.TABLE,cols,where
                , params, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

    public long updateServicios(String nombre, String precio, String impuesto,String where, String[] params){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ser.NOMBRE, nombre);
        values.put(ser.PRECIO, precio);
        values.put(ser.IMPUESTO, impuesto);

        return db.update(clie.TABLE,values,where,params);

    }
    
}
