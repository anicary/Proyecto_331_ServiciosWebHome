package mx.edu.ittepic.proyecto_331_serviciosweb.Utilidades;


import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConexionWeb extends AsyncTask<URL,String,String> {
    List<String[]> variables;
    AsyncResponse delegado;
    String json="";
    String metodo="";
    public ConexionWeb(AsyncResponse p){
        delegado = p;
        variables = new ArrayList<>();
    }

    public void metodo(String metodo){
        this.metodo=metodo;
    }

    public void agregarVariables(String nombreVariable, String contenidoVariable){
        String[] temporal ={nombreVariable,contenidoVariable};
        variables.add(temporal);
    }
    public void agregarJSON(String nombreVariable){
        json=nombreVariable;
    }

    private String generarCadenaPost(){
        String post="";
        try {
            for (int i = 0; i < variables.size(); i++) {
                String[] temporal = variables.get(i);
                post += temporal[0] + "=" + URLEncoder.encode(temporal[1], "UTF-8") + " ";
            }
        }catch (Exception e){

        }
        post=post.trim();
        post = post.replaceAll(" ","&");
        return post;
    }
    @Override
    protected String doInBackground(URL... urls)
    {
       String respuesta="";
       if (metodo.equals("GET")){
           String inputLine;
           String POST =  json;
           try {
               HttpURLConnection conexion =(HttpURLConnection) urls[0].openConnection();
               conexion.setRequestMethod("GET");
               conexion.setReadTimeout(1500);
               conexion.setConnectTimeout(1500);

               conexion.connect();

               InputStreamReader streamReader = new  InputStreamReader(conexion.getInputStream());
               BufferedReader reader = new BufferedReader(streamReader);
               StringBuilder stringBuilder = new StringBuilder();
               while((inputLine = reader.readLine()) != null){
                   stringBuilder.append(inputLine);
               }
               reader.close();
               streamReader.close();
               respuesta = stringBuilder.toString();
           }
           catch(IOException e){
               e.printStackTrace();
               respuesta = null;
           }
       }else
       {
           String POST =  json;
           HttpURLConnection conexion = null;
           try{
               conexion = (HttpURLConnection) urls[0].openConnection();
               conexion.setDoOutput(true);
               conexion.setDoInput(true);
               conexion.setFixedLengthStreamingMode(POST.length());
               publishProgress("Enviando datos...");
               //conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
               conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
             //  conexion.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
              // conexion.setRequestProperty("Accept","application/json");
               OutputStream flujoSalida = new BufferedOutputStream(conexion.getOutputStream());
               flujoSalida.write(POST.getBytes());
               flujoSalida.flush();
               flujoSalida.close();
               if (conexion.getResponseCode()==200){
                   publishProgress("recibiendo respuesta del servidor");
                   InputStreamReader entrada = new InputStreamReader(conexion.getInputStream(),"UTF-8");
                   BufferedReader flujoEntrada = new BufferedReader(entrada);
                   String temp = "";
                   do{
                       temp = flujoEntrada.readLine();
                       if (temp !=null){
                           respuesta+=temp;
                       }
                   }while (temp!=null);
                   flujoEntrada.close();

               }else{
                   return "ERROR_400";
               }
           }catch (UnknownHostException e){
               respuesta = "ERROR_404";
           }catch (IOException e){
               respuesta = "ERROR_405";
           }finally {
               if (conexion !=null){
                   conexion.disconnect();
               }
           }
       }
        return respuesta;
    }
    public void onPostExecute(String r){

        delegado.procesarRespuesta(r);
    }
}