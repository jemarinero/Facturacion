package helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jemarinero on 04/05/2017.
 */

public class AsyncTaskPost extends AsyncTask<Void, Void, String>{

    String UrlPost, JsonPost;
    Context context;
    //FacturacionHelper helper;

    public AsyncTaskPost(String UrlPost, String JsonPost, Context context){
        this.JsonPost = JsonPost;
        this.UrlPost = UrlPost;
        this.context = context;
    }


    @Override
    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(Void... params) {
        //helper = new FacturacionHelper(context);
        return getServerResponse(JsonPost);
    }

    @Override
    protected void onPostExecute(String s){
//        if(CargaData==false){
//            AlertDialog.Builder alert = new AlertDialog.Builder(context);
//            alert.setTitle("Advertencia")
//                    .setMessage("Error al enviar los datos");
//            alert.show();
//        }else{
//            if(subidaFinal.equals("true")){
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setTitle("Advertencia")
//                        .setMessage("Datos enviados exitosamente");
//                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                        ((Activity)context).finish();
//                    }
//                });
//                alert.show();
//                helper.BorrarDatos();
//
//
//
//            }else if(subidaFinal.equals("false")){
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setTitle("Advertencia")
//                        .setMessage("Sincronización Exitosa");
//                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(context, mainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                        ((Activity)context).finish();
//                    }
//                });
//                alert.show();
//            }else{
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setTitle("Advertencia")
//                        .setMessage("Sincronización Exitosa");
//                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                alert.show();
//            }
//
//
//        }
        //Toast.makeText(context,"Proceso de sincronización finalizado",Toast.LENGTH_SHORT).show();
    }

    private String getServerResponse(String jsonss) {
        try {

            URL object = new URL(UrlPost);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(jsonss);
            wr.flush();
            wr.close();

            String response = con.getResponseMessage();
            Toast.makeText(context,response,Toast.LENGTH_SHORT).show();

            if(con.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT || response == null){
                con.disconnect();
            }else{
                con.disconnect();
            }

            return response;

        }catch (Exception ex){
            //ex.printStackTrace();
            //Log.e("JSON", ex.getMessage());
            //CargaData=false;
            return  null;
        }
    }

}