package com.yogeshojha.blooddonation;

import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public FragmentManager fm;
    public LatLng marker;
    public TextView txt;
    private String URL = "http://kyampus.in/blood/loc.php";
    public final ArrayList<String> latarray = new ArrayList<String>();
    public final ArrayList<String> lngarray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchWebsiteData().execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.my_profile) {
            fm.beginTransaction().replace(R.id.fragment_container, new profile()).commit();
        }
        else if(id == R.id.receive)
        {
            fm.beginTransaction().replace(R.id.fragment_container, new receive()).commit();
        }
        else if(id == R.id.donate)
        {
            fm.beginTransaction().replace(R.id.fragment_container, new donate()).commit();
        }

        else if(id == R.id.event)
        {
            fm.beginTransaction().replace(R.id.fragment_container, new campaign()).commit();
        }

        else if(id == R.id.leader)
        {
            fm.beginTransaction().replace(R.id.fragment_container, new leaderboard()).commit();
        }

        else if(id == R.id.information)
        {
            fm.beginTransaction().replace(R.id.fragment_container, new information()).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class FetchWebsiteData extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        public Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(URL).get();
                latarray.clear();
                lngarray.clear();
                for (Element table : document.select("table.locationclass")) {
                    for (Element row : table.select("tr")) {
                        Elements tds = row.select("td");
                        if (tds.size() >= 1) {
                            lngarray.add(tds.get(2).text());
                            latarray.add(tds.get(1).text());
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
        public void onPostExecute(Void result) {
            Bundle bundle=new Bundle();
            bundle.putString("name", "From Activity");
            receive fragobj=new receive();
            fragobj.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container, new receive()).commit();
        }
    }
}
