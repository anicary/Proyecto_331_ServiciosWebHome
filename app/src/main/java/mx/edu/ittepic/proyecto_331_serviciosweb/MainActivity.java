package mx.edu.ittepic.proyecto_331_serviciosweb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import mx.edu.ittepic.proyecto_331_serviciosweb.Clases.Alumno;
import mx.edu.ittepic.proyecto_331_serviciosweb.Utilidades.AdaptadorAlumno;
import mx.edu.ittepic.proyecto_331_serviciosweb.Utilidades.AsyncResponse;
import mx.edu.ittepic.proyecto_331_serviciosweb.Utilidades.ConexionWeb;

public class MainActivity extends AppCompatActivity implements AsyncResponse, SwipeRefreshLayout.OnRefreshListener {
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Button btnAgregar, btnRecargar, btnBuscar;
    EditText nombreedti, direccionEdit, editIDalumno;
    LinearLayout linear1, linear2, linearBusqueda, linearBusqueda2, linearBusqueda3;
    ConexionWeb conexionWeb;
    ArrayList<Alumno> listaAlumnos;
    RecyclerView listaObjetos;
    private RecyclerView.LayoutManager mLayoutManager;
    AdaptadorAlumno adapter;
    SwipeRefreshLayout swipe;
    LinearLayout recargar;
    TextView busquedaNombre, BusquedaDomicilio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Administracion");

        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        listaObjetos = (RecyclerView) findViewById(R.id.lista);
        listaObjetos.setHasFixedSize(true);
        listaObjetos.setLayoutManager(new LinearLayoutManager(this));
        listaAlumnos = new ArrayList<>();
        recargar = (LinearLayout) findViewById(R.id.linearRecargar);
        btnRecargar = (Button) findViewById(R.id.btnRecargr);
        cargarAlumnos();
        swipe.setOnRefreshListener(this);
        btnRecargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarAlumnos();
            }
        });
    }

    public void cargarAlumnos() {
        try {
            listaAlumnos = new ArrayList<>();
            conexionWeb = new ConexionWeb(MainActivity.this);
            URL direccion = new URL("http://carostore.x10host.com/android/obtener_alumnos.php");
            conexionWeb.metodo("GET");
            conexionWeb.execute(direccion);
        } catch (MalformedURLException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principl, menu);

        MenuItem search = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        busqueda(searchView);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sobre) {
            Intent a = new Intent(MainActivity.this, Sobre.class);
            startActivity(a);
            return true;
        }
        if (id == R.id.buscar) {
            dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alerta_id_buscar, null);
            dialogBuilder.setView(dialogView);

            linearBusqueda = (LinearLayout) dialogView.findViewById(R.id.linearBusqueda);
            linearBusqueda2 = (LinearLayout) dialogView.findViewById(R.id.linearCargando);
            linearBusqueda3 = (LinearLayout) dialogView.findViewById(R.id.linearResultado);

            busquedaNombre = (TextView) dialogView.findViewById(R.id.busquedanNombre);
            BusquedaDomicilio = (TextView) dialogView.findViewById(R.id.busquedaDomicilo);

            editIDalumno = (EditText) dialogView.findViewById(R.id.idalumno);
            btnBuscar = (Button) dialogView.findViewById(R.id.btnbuscar);

            btnBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject post_agregar_alumno = new JSONObject();
                    try {
                        conexionWeb = new ConexionWeb(MainActivity.this);
                        conexionWeb.metodo("GET");
                        conexionWeb.agregarJSON(String.valueOf(post_agregar_alumno));
                        try {
                            linearBusqueda.setVisibility(View.GONE);
                            linearBusqueda3.setVisibility(View.GONE);
                            linearBusqueda2.setVisibility(View.VISIBLE);
                            URL direccion = new URL("http://carostore.x10host.com/android/obtener_alumno_por_id.php?idalumno=" + editIDalumno.getText().toString());
                            conexionWeb.execute(direccion);
                        } catch (MalformedURLException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toasty.error(MainActivity.this, "Ocurrio un error al generar el JSON_POST", Toast.LENGTH_SHORT, true).show();
                    }
                }
            });

            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
        if (id == R.id.agregar) {
            dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alerta_agregar_alumno, null);
            dialogBuilder.setView(dialogView);

            linear1 = (LinearLayout) dialogView.findViewById(R.id.linear1);
            linear2 = (LinearLayout) dialogView.findViewById(R.id.linear2);

            nombreedti = (EditText) dialogView.findViewById(R.id.nombre);
            direccionEdit = (EditText) dialogView.findViewById(R.id.dirrecion);
            btnAgregar = (Button) dialogView.findViewById(R.id.btnAgregar);
            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject post_agregar_alumno = new JSONObject();
                    try {
                        conexionWeb = new ConexionWeb(MainActivity.this);
                        conexionWeb.metodo("POST");
                        post_agregar_alumno.put("nombre",   URLEncoder.encode(nombreedti.getText().toString(), "utf-8"));
                        post_agregar_alumno.put("direccion",           URLEncoder.encode(direccionEdit.getText().toString(), "utf-8"));

                        // System.out.println("JSON:  "+String.valueOf(post_agregar_alumno));
                        try {
                            conexionWeb.agregarJSON(String.valueOf(post_agregar_alumno));
                            btnAgregar.setEnabled(false);
                            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            URL direccion = new URL("http://carostore.x10host.com/android/insertar_alumno.php");
                            conexionWeb.execute(direccion);
                        } catch (MalformedURLException e) {
                            System.out.println("Error:"+e.getMessage());
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Error:"+e.getMessage());
                        Toasty.error(MainActivity.this, "Ocurrio un error al generar el JSON_POST", Toast.LENGTH_SHORT, true).show();
                    }catch (UnsupportedEncodingException e){
                        System.out.println("Error:"+e.getMessage());
                        Toasty.error(MainActivity.this, "Ocurrio un error al generar el JSON_POST", Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
            alertDialog = dialogBuilder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void procesarRespuesta(String r) {
        try {
            System.out.println("JSON:  " + r);
            if (!r.equals("") || r != null) {
                try {
                    JSONObject respuesta = new JSONObject(r);
                    if (respuesta.getInt("estado") == 1) {
                        if (respuesta.has("alumnos")) {
                            JSONArray Alumnos = respuesta.getJSONArray("alumnos");
                            for (int i = 0; i < Alumnos.length(); i++) {
                                JSONObject Alumno = Alumnos.getJSONObject(i);

                                try {

                                    listaAlumnos.add(new Alumno(
                                            Alumno.getString("idalumno"),
                                            URLDecoder.decode(Alumno.getString("nombre"),"utf-8"),
                                            URLDecoder.decode(Alumno.getString("direccion"),"utf-8")));
                                }catch (UnsupportedEncodingException e){
                                    listaAlumnos.add(new Alumno(
                                            Alumno.getString("idalumno"),
                                            "Error",
                                            "Direccion invalida"));

                                }
                            }
                            if (listaAlumnos.size() != 0) {
                                adapter = new AdaptadorAlumno(listaAlumnos, getApplicationContext(), MainActivity.this);
                                adapter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                                swipe.setRefreshing(false);
                                listaObjetos.setAdapter(adapter);
                                recargar.setVisibility(View.GONE);
                            } else {
                                recargar.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (respuesta.has("alumno")) {
                                linearBusqueda2.setVisibility(View.GONE);
                                JSONObject obejto = respuesta.getJSONObject("alumno");

                                busquedaNombre.setText(obejto.getString("nombre"));
                                BusquedaDomicilio.setText(obejto.getString("direccion"));

                                linearBusqueda.setVisibility(View.VISIBLE);
                                linearBusqueda3.setVisibility(View.VISIBLE);
                            } else {
                                if (respuesta.getString("mensaje").equals("Creacion correcta")) {
                                    cargarAlumnos();
                                    linear1.setVisibility(View.GONE);
                                    linear2.setVisibility(View.VISIBLE);
                                    CountDownTimer contador = new CountDownTimer(2700, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                        }

                                        @Override
                                        public void onFinish() {
                                            alertDialog.dismiss();
                                        }
                                    }.start();
                                    Toasty.success(MainActivity.this, "Agreado corretamente  !", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    if (respuesta.getString("mensaje").equals("Eliminacion exitosa")) {
                                        cargarAlumnos();
                                        Toasty.error(MainActivity.this, "Se elimino correctamente  !", Toast.LENGTH_LONG, true).show();
                                    } else {
                                        if (respuesta.getString("mensaje").equals("Actualizacion correcta")) {
                                            cargarAlumnos();
                                            Toasty.success(MainActivity.this, "Se Actualizo Correctamente  !", Toast.LENGTH_LONG, true).show();
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (respuesta.getInt("estado") == 2) {
                            if (respuesta.getString("mensaje").equals("No se obtuvo el registro")) {
                                linearBusqueda2.setVisibility(View.GONE);
                                busquedaNombre.setText("No se obtuvo el registro :(");
                                BusquedaDomicilio.setText("");

                                linearBusqueda.setVisibility(View.VISIBLE);
                                linearBusqueda3.setVisibility(View.VISIBLE);
                            }
                            Toasty.error(MainActivity.this, "" + respuesta.getString("mensaje"), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(MainActivity.this, "Ocurrio un error al parsear el JSON_POST", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                Toasty.error(MainActivity.this, "Error en la peticion", Toast.LENGTH_SHORT, true).show();
            }
        } catch (Exception e) {
            Toasty.error(MainActivity.this, "Error en la CONEXION", Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onRefresh() {
        Toasty.success(MainActivity.this, "CARGANDO... !", Toast.LENGTH_LONG, true).show();
        cargarAlumnos();
    }
    public void elimnarAlumno(String idalumn, String nombre) {
        final String tempid = idalumn;
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Eliminar Alumno");
        dialogo1.setMessage("¿ Desea Elimnar este Alumno " + nombre + " ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                JSONObject post_agregar_alumno = new JSONObject();
                try {
                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.metodo("POST");
                    post_agregar_alumno.put("idalumno", "" + tempid + "");
                    conexionWeb.agregarJSON(String.valueOf(post_agregar_alumno));
                    try {
                        URL direccion = new URL("http://carostore.x10host.com/android/borrar_alumno.php");
                        conexionWeb.execute(direccion);
                    } catch (MalformedURLException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(MainActivity.this, "Ocurrio un error al generar el JSON_POST", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
    public void editarAlumno(String idalumn, String nombre, String direccion) {
        final String tempid = idalumn;
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alerta_editar_alumno, null);
        dialogBuilder.setView(dialogView);

        linear1 = (LinearLayout) dialogView.findViewById(R.id.linear1);
        linear2 = (LinearLayout) dialogView.findViewById(R.id.linear2);

        nombreedti = (EditText) dialogView.findViewById(R.id.nombre);
        direccionEdit = (EditText) dialogView.findViewById(R.id.dirrecion);
        btnAgregar = (Button) dialogView.findViewById(R.id.btnAgregar);

        nombreedti.setText(nombre);
        direccionEdit.setText(direccion);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject post_agregar_alumno = new JSONObject();
                try {
                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.metodo("POST");
                    post_agregar_alumno.put("idalumno", tempid);
                    post_agregar_alumno.put("nombre", URLEncoder.encode( nombreedti.getText().toString(), "utf-8"));
                    post_agregar_alumno.put("direccion",  URLEncoder.encode( direccionEdit.getText().toString(), "utf-8"));
                    conexionWeb.agregarJSON(String.valueOf(post_agregar_alumno));
                    try {
                        btnAgregar.setEnabled(false);
                        URL direccion = new URL("http://carostore.x10host.com/android/actualizar_alumno.php");
                        conexionWeb.execute(direccion);
                    } catch (MalformedURLException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(MainActivity.this, "Ocurrio un error al generar el JSON_POST", Toast.LENGTH_SHORT, true).show();
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                    Toasty.error(MainActivity.this, "Ocurrio un error al generar el JSON_POST", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    private void busqueda(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
