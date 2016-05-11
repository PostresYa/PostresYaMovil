package com.example.duvan.postresya;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import javax.net.ssl.HttpsURLConnection;


public class VerReposterias extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private android.content.Context contextReposterias=this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reposterias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        AsyncTask tarea=new MyTaskGetReposterias().execute("reposterias");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ver_reposterias, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.view_reposterias){

            AsyncTask tarea=new MyTaskGetReposterias().execute("reposterias");
        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private class MyTaskGetReposterias extends AsyncTask<String,Integer,JSONArray> {
        private String cual;
        @Override
        protected JSONArray doInBackground(String... strings) {
            JSONArray products = null;
            try {


                 Intent i=getIntent();
                cual=strings[0];

                String userPass = i.getStringExtra("user")+":"+i.getStringExtra("password");
                //String userPass = "Duvan:password";
                String basicAuth = "Basic " + new String(Base64.encodeToString(userPass.getBytes(), Base64.NO_WRAP));
                URL obj;
                if(strings[0].equals("reposterias")){
                    obj = new URL("https://projectpostresya.herokuapp.com/reposterias");
                }else{
                    obj = new URL("https://projectpostresya.herokuapp.com/postres/"+cual);
                }


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

                products = new JSONArray(response.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return products;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            TextView h = (TextView) findViewById(R.id.prueba);

            if (jsonArray == null) {
                h.setText("adasd");
            } else {
                //  h.setText(jsonArray.toString());
                try {
                    if(cual.equals("reposterias")){
                        addData(jsonArray);
                    }else{
                        addDataPostres(jsonArray);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }




    }

    private void addData(JSONArray jsonArray) throws JSONException {

        TableLayout tableproducts = (TableLayout) findViewById(R.id.listReposterias);
        tableproducts.removeAllViews();
        TableRow tr_head = new TableRow(contextReposterias);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new Toolbar.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));


        TextView label_nit = new TextView(contextReposterias);
        label_nit.setId(20);
        label_nit.setText("id");
        label_nit.setTextColor(Color.WHITE);
        label_nit.setPadding(15, 15, 15, 15);
        tr_head.addView(label_nit);// add the column to the table row here

        TextView label_name = new TextView(contextReposterias);
        label_name.setId(21);// define id that must be unique
        label_name.setText("name"); // set the text for the header
        label_name.setTextColor(Color.WHITE); // set the color
        label_name.setPadding(15, 15, 15, 15); // set the padding (if required)
        tr_head.addView(label_name); // add the column to the table row here

        TextView label_direccion = new TextView(contextReposterias);
        label_direccion.setId(21);// define id that must be unique
        label_direccion.setText("price"); // set the text for the header
        label_direccion.setTextColor(Color.WHITE); // set the color
        label_direccion.setPadding(15, 15, 15, 15); // set the padding (if required)
        tr_head.addView(label_direccion); // add the column to the table row here


        tableproducts.addView(tr_head, new TableLayout.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject product = (JSONObject) jsonArray.get(i);
            TableRow tr = new TableRow(contextReposterias);
            //  if (i % 2 != 0) {
            //     tr.setBackgroundColor(Color.BLACK);
            // } else {
            tr.setBackgroundColor(Color.GRAY);
            //}
            tr.setId(100 + i);
            tr.setClickable(true);
            tr.setPadding(30, 30, 30, 30);

            tr.setLayoutParams(new Toolbar.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));

//Create two columns to add as table data
            // Create a TextView to add date
            TextView id = new TextView(contextReposterias);
            id.setId(200 + i);
            id.setText("" + product.get("nit"));
            //id.setPadding(2, 0, 5, 0);
            id.setTextColor(Color.WHITE);
            tr.addView(id);


            TextView name = new TextView(contextReposterias);
            name.setId(200 + i);
            name.setText(product.get("name") + "");
            //name.setPadding(2, 0, 5, 0);
            name.setTextColor(Color.WHITE);
            tr.addView(name);

            TextView direccion = new TextView(contextReposterias);
            direccion.setId(200 + i);
            direccion.setText(product.get("direccion") + "");
            //price.setPadding(2, 0, 5, 0);
            direccion.setTextColor(Color.WHITE);
            tr.addView(direccion);

            id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView h = (TextView) findViewById(R.id.prueba);
                    TextView ms= (TextView) findViewById(view.getId());

                    h.setText(view.getId()+"");
                    AsyncTask tareaPostres= new MyTaskGetReposterias().execute(ms.getText()+"");

                }
            });

            tableproducts.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));


        }

    }
    private void addDataPostres(JSONArray jsonArray) throws JSONException {
        TableLayout tableproducts = (TableLayout) findViewById(R.id.listReposterias);
        tableproducts.removeAllViews();
        TableRow tr_head = new TableRow(contextReposterias);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new Toolbar.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));


      /*  TextView label_nit = new TextView(contextReposterias);
        label_nit.setId(20);
        label_nit.setText("id");
        label_nit.setTextColor(Color.WHITE);
        label_nit.setPadding(15, 15, 15, 15);
        tr_head.addView(label_nit);// add the column to the table row here
*/



        TextView label_name = new TextView(contextReposterias);
        label_name.setId(21);// define id that must be unique
        label_name.setText("name"); // set the text for the header
        label_name.setTextColor(Color.WHITE); // set the color
        label_name.setPadding(15, 15, 15, 15); // set the padding (if required)
        tr_head.addView(label_name); // add the column to the table row here

        TextView label_price= new TextView(contextReposterias);
        label_price.setId(21);// define id that must be unique
        label_price.setText("price"); // set the text for the header
        label_price.setTextColor(Color.WHITE); // set the color
        label_price.setPadding(15, 15, 15, 15); // set the padding (if required)
        tr_head.addView(label_price); // add the column to the table row here


        tableproducts.addView(tr_head, new TableLayout.LayoutParams(
                Toolbar.LayoutParams.FILL_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject product = (JSONObject) jsonArray.get(i);
            TableRow tr = new TableRow(contextReposterias);
            //  if (i % 2 != 0) {
            //     tr.setBackgroundColor(Color.BLACK);
            // } else {
            tr.setBackgroundColor(Color.GRAY);
            // }
            tr.setId(100 + i);
            tr.setClickable(true);
            tr.setPadding(30, 30, 30, 30);

            tr.setLayoutParams(new Toolbar.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));

//Create two columns to add as table data
            // Create a TextView to add date
            /*TextView id = new TextView(contextReposterias);
            id.setId(200 + i);
            id.setText("" + product.get("nit"));
            //id.setPadding(2, 0, 5, 0);
            id.setTextColor(Color.WHITE);
            tr.addView(id);
*/

            TextView name = new TextView(contextReposterias);
            name.setId(200 + i);
            name.setText(product.get("name") + "");
            //name.setPadding(2, 0, 5, 0);
            name.setTextColor(Color.WHITE);
            tr.addView(name);

            TextView price = new TextView(contextReposterias);
            price.setId(200 + i);
            price.setText(product.get("price") + "");
            //price.setPadding(2, 0, 5, 0);
            price.setTextColor(Color.WHITE);
            tr.addView(price);



            tableproducts.addView(tr, new TableLayout.LayoutParams(
                    Toolbar.LayoutParams.FILL_PARENT,
                    Toolbar.LayoutParams.WRAP_CONTENT));


        }

    }


}
