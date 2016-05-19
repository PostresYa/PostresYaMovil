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
import java.net.MalformedURLException;
import java.net.URL;
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
                tr.setPadding(30,30,30,30);
                tr.setLayoutParams(new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.FILL_PARENT,
                        Toolbar.LayoutParams.WRAP_CONTENT));

                TextView label_postre = new TextView(this);

                label_postre = new TextView(this);
                label_postre.setId(20);
                label_postre.setText(postreAtual.getString("name"));

                label_postre.setTextColor(Color.BLACK);
               // label_postre.setPadding(30, 30, 30, 30);
                tr.addView(label_postre);// add the column to the table row here

                TextView label_valor = new TextView(this);

                label_valor = new TextView(this);
                label_valor.setId(20);
                label_valor.setText(((JSONObject) value.get(index)).getInt("cantidad")+"x $ "+valor+"");

                label_valor.setTextColor(Color.BLACK);
                label_valor.setPadding(5, 30, 30, 30);
                tr.addView(label_valor);// add the column to the table row here

                ImageView cancelar= new ImageView(this);
                cancelar.setId(20);
                int res_imagen = getResources().getIdentifier("ic_cancel_black_24dp", "drawable",getPackageName());
                cancelar.setImageResource(res_imagen);
                cancelar.setPadding(5, 5, 5, 5);

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
            listaPedidos.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));



        }



    }




    public class PedidoTask extends AsyncTask<String, Void, JSONObject> {

        private final String username;
        private final String password;

        PedidoTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO: attempt authentication against a network service.


            String reqResponse = null;
            JSONObject cliente = null;

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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return cliente;
        }

        @Override
        protected void onPostExecute(JSONObject success) {
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
