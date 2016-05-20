package com.example.duvan.postresya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PostreCantidadActivity extends AppCompatActivity {

    private JSONObject postre;
    private TextView nombreReposteria;
    private TextView nombrePostre;
    private TextView descripcionPostre;
    private TextView precioPostre;
    private ImageView imagenPostre;
    private TextView cantidadPostre;
    private Context contextPostreCantidad=this;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postre_cantidad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(contextPostreCantidad,PedidoActivity.class);

                i.putExtra("cual","ver");
                startActivity(i);
            }
        });

        actualizarDatos();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void actualizarDatos() {
        Intent i = getIntent();
        try {
            postre = new JSONObject(i.getStringExtra("postre"));

            nombreReposteria = (TextView) findViewById(R.id.nombreReposteria);
            nombrePostre = (TextView) findViewById(R.id.nombrePostre);
            descripcionPostre = (TextView) findViewById(R.id.descripcionPostre);
            precioPostre = (TextView) findViewById(R.id.precioPostre);
            imagenPostre = (ImageView) findViewById(R.id.imagenPostre);
            cantidadPostre=(TextView) findViewById(R.id.cantidadPedido);
            nombreReposteria.setText(postre.getString("nombreReposteria"));
            nombrePostre.setText(postre.getString("name"));
            descripcionPostre.setText(postre.getString("description"));
            precioPostre.setText(postre.getString("price"));
            cantidadPostre.setText("1");
            //Bitmap algo= new Bitmap(postre.get("image"));
            imagenPostre.setImageBitmap(SingletonUser.getInstance().getImagePostreActual());







        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddCantidad(View v){
        cantidadPostre.setText((Integer.parseInt(""+cantidadPostre.getText())+1)+"");
    }

    public void LessCantidad(View v){
        int cantidad=Integer.parseInt(""+cantidadPostre.getText());
        if(cantidad>1){
            cantidadPostre.setText((cantidad-1)+"");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PostreCantidad Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.duvan.postresya/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PostreCantidad Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.duvan.postresya/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public boolean existePostre(JSONObject postre){
        boolean respuesta=false;
        try {
            System.out.println(postre.getJSONObject("postre").getJSONObject("id").toString());
            if(SingletonPedido.getInstance().existReposteria(postre.getJSONObject("postre").getJSONObject("id").getString("reposteriaNit"))){
                JSONArray existentes= SingletonPedido.getInstance().getPedido().get(postre.getJSONObject("postre").getJSONObject("id").getString("reposteriaNit"));
                for(int index=0;index<existentes.length();index++){
                    JSONObject actualexistente= (JSONObject) existentes.get(index);
                    System.out.println(postre.getJSONObject("postre").getJSONObject("id").getString("code")+"    sdfsdf   "+ actualexistente.getJSONObject("postre").getJSONObject("id").getString("code"));
                    if(postre.getJSONObject("postre").getJSONObject("id").getString("code").equals(actualexistente.getJSONObject("postre").getJSONObject("id").getString("code"))){
                        respuesta=true;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
    public void AgregarCarrito(View v) throws JSONException {



        JSONObject postreCantidad= new JSONObject();

        postreCantidad.put("postre",postre);
        postreCantidad.put("cantidad",cantidadPostre.getText());

        if(existePostre(postreCantidad)){

            AlertDialog.Builder alertBuilder= new AlertDialog.Builder(contextPostreCantidad);
            alertBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alertDialog= alertBuilder.create();
            alertDialog.setTitle("Postre repetido");
            alertDialog.setMessage("el postre que desea solicitar, ya se encuentra en el carrito");
            alertDialog.show();


        }else{
            Intent i= new Intent(this,PedidoActivity.class);
            i.putExtra("postreCantidad",postreCantidad.toString());
            i.putExtra("cual","agregar");
            System.out.println("postre cantidad"+postreCantidad.toString());
            startActivity(i);
        }





    }

}
