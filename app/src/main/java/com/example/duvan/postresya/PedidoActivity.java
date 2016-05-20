package com.example.duvan.postresya;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PedidoActivity extends AppCompatActivity {

    private SingletonPedido pedido;
    private Context contextPedido=this;
    private View viewPedidos;
    private View progressPedidos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        try {
            viewPedidos= findViewById(R.id.scrollViewPedidos);
            progressPedidos=findViewById(R.id.pedido_progress);
            actualizadarDatos();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizadarDatos() throws JSONException {
        TextView e=(TextView) findViewById(R.id.textView2);
        Intent i=getIntent();
        String cual=i.getStringExtra("cual");

        if(cual.equals("agregar")){
           JSONObject actual= new JSONObject(i.getStringExtra("postreCantidad")) ;


            JSONObject llave = (JSONObject) ((JSONObject) actual.get("postre")).get("id");

            pedido.getInstance().addPedido(llave.getString("reposteriaNit"),actual);

        }

        e.setText(pedido.getInstance().getDireccion());

        llenarTabla();

    }


    private void llenarTabla() throws JSONException {
        TableLayout listaPedidos = (TableLayout) findViewById(R.id.listaPedidos);

        listaPedidos.removeAllViews();



        int total=0;
        for (Map.Entry entry : pedido.getInstance().getPedido().entrySet()) {
            final String key = (String) entry.getKey();
            JSONArray value = (JSONArray) entry.getValue();
            System.out.println(key+":"+value);
            TableRow tr = new TableRow(this);

            tr.setId(10);
            tr.setBackgroundColor(Color.BLACK);
            tr.setLayoutParams(new Toolbar.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));


            TextView label_nit = new TextView(this);

            label_nit = new TextView(this);
            label_nit.setId(20);
            label_nit.setText(((JSONObject)((JSONObject)value.get(0)).get("postre")).getString("nombreReposteria"));

            label_nit.setTextColor(Color.WHITE);
            label_nit.setPadding(15, 15, 15, 15);
            tr.addView(label_nit);// add the column to the table row here

            listaPedidos.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));
            int totalReposteria=0;
            for(int index=0;index<value.length();index++){
                final JSONObject postreAtual= (JSONObject) ((JSONObject) value.get(index)).get("postre");
                final JSONObject postreEliminar= ((JSONObject) value.get(index));
                final String llaveActual=key;
                System.out.println(postreAtual.toString());
                int valor=  postreAtual.getInt("price") * ((JSONObject) value.get(index)).getInt("cantidad");
                totalReposteria+=valor;

                tr = new TableRow(this);

                tr.setId(10);
                tr.setBackgroundColor(Color.WHITE);
                //tr.setPadding(30,30,30,30);
                tr.setLayoutParams(new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.FILL_PARENT,
                        Toolbar.LayoutParams.WRAP_CONTENT));

                TextView label_postre = new TextView(this);

                label_postre = new TextView(this);
                label_postre.setId(20);
                label_postre.setText(postreAtual.getString("name"));

                label_postre.setTextColor(Color.BLACK);
                label_postre.setPadding(30, 30, 30, 3);

                tr.addView(label_postre);// add the column to the table row here

                TextView label_valor = new TextView(this);

                label_valor = new TextView(this);
                label_valor.setId(20);
                label_valor.setText(((JSONObject) value.get(index)).getInt("cantidad")+"x $ "+valor+"");

                label_valor.setTextColor(Color.BLACK);
                label_valor.setPadding(3, 30, 30, 3);
                tr.addView(label_valor);// add the column to the table row here

                ImageView cancelar= new ImageView(this);
                cancelar.setId(20);
                int res_imagen = getResources().getIdentifier("ic_cancel_black_24dp", "drawable",getPackageName());
                cancelar.setImageResource(res_imagen);
                cancelar.setPadding(5, 30, 30, 30);

                final ImageView cakesad= new ImageView(this);
                cakesad.setImageResource(R.drawable.cakesad);

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(contextPedido);

                       // ImageView imageView = new ImageView(contextPedido);

                       dialogo1.setIcon(R.drawable.cakesad);

                        dialogo1.setTitle("Confirmación");
                        //dialogo1.setView(cakesad);

                        dialogo1.setMessage("¿ Desea eliminar el postre del pedido ?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                try {
                                    pedido.getInstance().removePostre(llaveActual,postreEliminar);
                                    llenarTabla();
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });



                        dialogo1.show();




                    }
                });
                tr.addView(cancelar);// add the column to the table row here



                listaPedidos.addView(tr, new TableLayout.LayoutParams(
                        Toolbar.LayoutParams.FILL_PARENT,
                        Toolbar.LayoutParams.WRAP_CONTENT));



            }


            tr = new TableRow(this);

            tr.setId(10);
            tr.setBackgroundColor(Color.parseColor("#DE813E"));
            tr.setLayoutParams(new Toolbar.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));


            TextView label_total = new TextView(this);

            label_total = new TextView(this);
            label_total.setId(20);
            label_total.setText("Total "+((JSONObject)((JSONObject)value.get(0)).get("postre")).getString("nombreReposteria")+": $"+totalReposteria);

            label_total.setTextColor(Color.BLACK);
            label_total.setPadding(30, 30, 30, 30);
            tr.addView(label_total);// add the column to the table row here

            ImageView enviar= new ImageView(this);
            enviar.setId(20);
            int res_imagen = getResources().getIdentifier("ic_send_black_24dp", "drawable",getPackageName());
            enviar.setImageResource(res_imagen);
            enviar.setPadding(30, 30, 30, 30);
            JSONObject totalPed=new JSONObject();
            totalPed.put("total",totalReposteria);
            final JSONArray pedidoReposteriaActual=new JSONArray();
            pedidoReposteriaActual.put(totalPed);
            pedidoReposteriaActual.put(value);
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(contextPedido);

                   // dialogo1.setIcon(R.drawable.cakesad);

                    dialogo1.setTitle("Confirmación");

                    try {
                        dialogo1.setMessage("¿ Desea confirmar el pedido unicamente para "+pedidoReposteriaActual.getJSONArray(1).getJSONObject(0).getJSONObject("postre").getString("nombreReposteria")+" ?");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                           AsyncTask task=new PedidoTask(SingletonUser.getInstance().getUser(),SingletonUser.getInstance().getPassword()).execute(pedidoReposteriaActual);

                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });



                    dialogo1.show();

                }
            });
            tr.addView(enviar);

            listaPedidos.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));

            total+=totalReposteria;
        }

        if(total>0){
            TableRow tr = new TableRow(this);

            tr.setId(10);
            tr.setBackgroundColor(Color.parseColor("#DD592A"));
            tr.setLayoutParams(new Toolbar.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));


            TextView label_total = new TextView(this);

            label_total = new TextView(this);
            label_total.setId(20);
            label_total.setText("Total: $"+total);

            label_total.setTextColor(Color.BLACK);
            label_total.setPadding(30, 30, 30, 30);
            tr.addView(label_total);// add the column to the table row here

            ImageView enviarTodo= new ImageView(this);
            enviarTodo.setId(20);
            int res_imagen = getResources().getIdentifier("ic_send_black_24dp", "drawable",getPackageName());
            enviarTodo.setImageResource(res_imagen);
            enviarTodo.setPadding(30, 30, 30, 30);
            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(contextPedido);

                    // dialogo1.setIcon(R.drawable.cakesad);

                    dialogo1.setTitle("Confirmación");

                    dialogo1.setMessage("¿ Desea confirmar la totalidad del pedido ?");

                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                          /*  try {
                              //  pedido.getInstance().removePostre(llaveActual,postreEliminar);
                               // llenarTabla();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }*/

                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });



                    dialogo1.show();

                }
            });

            tr.addView(enviarTodo);



            listaPedidos.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));



        }



    }




    public class PedidoTask extends AsyncTask<JSONArray, Void, Void> {

        private final String username;
        private final String password;

        PedidoTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Void doInBackground(JSONArray... params) {
            // TODO: attempt authentication against a network service.


            String reqResponse = null;
            JSONObject cliente = null;
            JSONObject pedidoSend= new JSONObject();
            JSONArray postresCantidad= new JSONArray();
            try {

                JSONArray actual= (JSONArray) params[0].get(1);

                pedidoSend.put("direccion",SingletonPedido.getInstance().getDireccion());
                pedidoSend.put("estado","en espera");
                pedidoSend.put("precio",((JSONObject)params[0].get(0)).getInt("total"));
                final Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                Date date = cal.getTime();
                pedidoSend.put("fecha",date);
                JSONArray postres= new JSONArray();


                for(int index=0;index<actual.length();index++){
                    JSONObject postreCant=new JSONObject();
                    JSONObject postreInfo=new JSONObject();
                    JSONObject primaria= new JSONObject();
                    JSONObject indexActual= new JSONObject();
                    indexActual= ((JSONObject) actual.get(index)).getJSONObject("postre");
                    primaria.put("codigoPostre",indexActual.getJSONObject("id").getString("code"));
                    primaria.put("reposteriaNit",indexActual.getJSONObject("id").getString("reposteriaNit"));

                    postreInfo.put("id",indexActual.getJSONObject("id"));
                    postreInfo.put("name",indexActual.getString("name"));
                    postreInfo.put("price",indexActual.getInt("price"));
                    postreInfo.put("description",indexActual.getString("description"));

                    postreCant.put("postreCantId",primaria);
                    postreCant.put("postre",postreInfo);
                    postreCant.put("cant",actual.getJSONObject(index).getInt("cantidad"));
                    postres.put(postreCant);
                }
                pedidoSend.put("postres",postres);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {




                String userPass = username + ":" + password;
                String basicAuth = "Basic " + new String(Base64.encodeToString(userPass.getBytes(), Base64.NO_WRAP));
                URL obj = new URL("https://projectpostresya.herokuapp.com/cliente/" + username);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Authorization", basicAuth);
                int rc = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                reqResponse = ((HttpsURLConnection) con).getResponseMessage();

                cliente = new JSONObject(response.toString());

                pedidoSend.put("cliente",cliente);







                obj = new URL("https://products-catalog-api.herokuapp.com/pedidos");
                HttpsURLConnection conp = (HttpsURLConnection) obj.openConnection();
                conp.setRequestMethod("POST");
                conp.setRequestProperty("Content-Type","application/json");
                conp.setRequestProperty("Authorization",basicAuth);
                OutputStream out = conp.getOutputStream();

                out.write(pedidoSend.toString().getBytes());

                out.flush();
                out.close();

//Mensaje de respuesta
                reqResponse = ((HttpsURLConnection)con).getResponseMessage();
//Código HTTP de respuesta
                int restcode=con.getResponseCode();

                System.out.println("-----------------------"+restcode+"---------------");





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            //return cliente;

            return null;
        }

        @Override
        protected void onPostExecute(Void success) {
            showProgress(false);


            /*
            if (success!=null) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }*/
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            viewPedidos.setVisibility(show ? View.GONE : View.VISIBLE);
            viewPedidos.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewPedidos.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressPedidos.setVisibility(show ? View.VISIBLE : View.GONE);
            progressPedidos.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressPedidos.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressPedidos.setVisibility(show ? View.VISIBLE : View.GONE);
            progressPedidos.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




}
