package com.example.duvan.postresya;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class PedidoActivity extends AppCompatActivity {

    private SingletonPedido pedido;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        try {
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

        e.setText("cargando");

        llenarTabla();

    }


    private void llenarTabla() throws JSONException {
        TableLayout listaPedidos = (TableLayout) findViewById(R.id.listaPedidos);

        listaPedidos.removeAllViews();
       /* TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new Toolbar.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));


        TextView label_nit = new TextView(this);
        label_nit.setId(20);
        label_nit.setText("Postre");
        label_nit.setTextColor(Color.WHITE);
        label_nit.setPadding(15, 15, 15, 15);
        tr_head.addView(label_nit);// add the column to the table row here

        TextView label_name = new TextView(this);
        label_name.setId(21);// define id that must be unique
        label_name.setText("Reposteria"); // set the text for the header
        label_name.setTextColor(Color.WHITE); // set the color
        label_name.setPadding(15, 15, 15, 15); // set the padding (if required)
        tr_head.addView(label_name); // add the column to the table row here

        TextView label_direccion = new TextView(this);
        label_direccion.setId(21);// define id that must be unique
        label_direccion.setText("Total"); // set the text for the header
        label_direccion.setTextColor(Color.WHITE); // set the color
        label_direccion.setPadding(15, 15, 15, 15); // set the padding (if required)
        tr_head.addView(label_direccion); // add the column to the table row here




        TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new Toolbar.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));


        TextView label_nit = new TextView(this);
        label_nit.setId(20);
        label_nit.setText("key.getKey()"+"");
        label_nit.setTextColor(Color.WHITE);
        label_nit.setPadding(15, 15, 15, 15);
        tr_head.addView(label_nit);// add the column to the table row here
        listaPedidos.addView(tr_head, new TableLayout.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));*/

        TextView e=(TextView) findViewById(R.id.textView2);




        for (Map.Entry entry : pedido.getInstance().getPedido().entrySet()) {
            String key = (String) entry.getKey();
            JSONArray value = (JSONArray) entry.getValue();

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

            for(int index=0;index<value.length();index++){
                JSONObject postreAtual= (JSONObject) ((JSONObject) value.get(index)).get("postre");
                System.out.println(postreAtual.toString());
                int valor=  postreAtual.getInt("price");


                tr = new TableRow(this);

                tr.setId(10);
                tr.setBackgroundColor(Color.WHITE);
                tr.setLayoutParams(new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.FILL_PARENT,
                        Toolbar.LayoutParams.WRAP_CONTENT));

                TextView label_postre = new TextView(this);

                label_postre = new TextView(this);
                label_postre.setId(20);
                label_postre.setText(postreAtual.getString("name"));

                label_postre.setTextColor(Color.BLACK);
                label_postre.setPadding(30, 30, 30, 30);
                tr.addView(label_postre);// add the column to the table row here

                TextView label_valor = new TextView(this);

                label_valor = new TextView(this);
                label_valor.setId(20);
                label_valor.setText("$ "+valor+"");

                label_valor.setTextColor(Color.BLACK);
                label_valor.setPadding(30, 30, 30, 30);
                tr.addView(label_valor);// add the column to the table row here

                ImageView cancelar= new ImageView(this);
                cancelar.setId(20);
                int res_imagen = getResources().getIdentifier("ic_cancel_black_24dp", "drawable",getPackageName());
                cancelar.setImageResource(res_imagen);
                cancelar.setPadding(30, 30, 30, 30);
                tr.addView(cancelar);// add the column to the table row here



                listaPedidos.addView(tr, new TableLayout.LayoutParams(
                        Toolbar.LayoutParams.FILL_PARENT,
                        Toolbar.LayoutParams.WRAP_CONTENT));



            }


        }

        /*while(iterator.hasNext()){

            String key= (String) iterator.next();
            e.setText(key+" "+ e.getText());
            //JSONArray array= (JSONArray) key.getValue();
            TableRow tr = new TableRow(this);

            TextView label_nit = new TextView(this);

            label_nit = new TextView(this);
            label_nit.setId(20);
          //  label_nit.setText(key.getKey()+"");
            label_nit.setTextColor(Color.WHITE);
            label_nit.setPadding(15, 15, 15, 15);
            tr.addView(label_nit);// add the column to the table row here

            listaPedidos.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));
           // for(int index=0;index<array.length();index++){

            //}
        }

*/


    }





}
