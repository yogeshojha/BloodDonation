package com.yogeshojha.blooddonation;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class receive extends Fragment implements OnMapReadyCallback {
    public TextView tvLat;
    public LatLng marker;
    public TextView txt;
    private String URL = "http://kyampus.in/blood/loc.php";
    public final ArrayList<String> latarray = new ArrayList<String>();
    public final ArrayList<String> lngarray = new ArrayList<String>();
    public final ArrayList<String> namearray = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vvn = inflater.inflate(R.layout.fragment_receive, container, false);
        Spinner spinner = (Spinner) vvn.findViewById(R.id.bld);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.blood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        new FetchWebsiteData().execute();
        return vvn;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                            namearray.add("Name: "+ tds.get(3).text() + "\nEmail: HIDDEN\nPhone: HIDDEN\nBlood Type:"+tds.get(7).text());
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
        public void onPostExecute(Void result) {
            receive.this.postmarkers(latarray,lngarray,namearray);
        }
    }
    public void postmarkers(ArrayList latarray, ArrayList lngarray, ArrayList namearray)
    {
        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        marker = new LatLng(12.886982, 77.641395);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                View va = getActivity().getLayoutInflater().inflate(R.layout.windowlayout, null);
                tvLat = (TextView) va.findViewById(R.id.tv_lat);
                for(int i = 1; i < namearray.size(); i++ ) {
                    tvLat.setText(namearray.get(i));
                }
                return va;

            }
        });
        // Adding and showing marker while touching the GoogleMap
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            GoogleMap googleMap;
            @Override
            public void onMapClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
                googleMap.clear();

                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(arg0);

                // Animating to the currently touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                Marker marker = googleMap.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();

            }
        });
        int size = latarray.size();
        for (int i = 0; i < size; i++)
        {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(latarray.get(i)), Double.parseDouble(lngarray.get(i))))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_room)));

        }
    }
}