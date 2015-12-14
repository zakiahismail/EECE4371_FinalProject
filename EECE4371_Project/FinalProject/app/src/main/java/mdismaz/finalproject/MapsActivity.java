package mdismaz.finalp;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available
        Button bGet;
        EditText max;
        double maxVar = 0.0;
        LatLng curPoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setOnMapClickListener(this);
        bGet = (Button) findViewById(R.id.get);
        max = (EditText) findViewById(R.id.maxDistance);


        bGet.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        if(max.toString().equals("0")){
                            Toast.makeText(MapsActivity.this, "Please enter a valid maximum distance and click \"Get Airports\"", Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(MapsActivity.this, "Sending", Toast.LENGTH_SHORT).show();
                            maxVar = Double.parseDouble(max.getText().toString());
                            connect(curPoint, maxVar);
                        }
                    }
                }
        );
    }

    public void onMapClick(LatLng point) {
        addMarker(point.latitude,point.longitude);
        Toast.makeText(MapsActivity.this, point.latitude + " " + point.longitude, Toast.LENGTH_SHORT).show();
        curPoint = point;
        Toast.makeText(MapsActivity.this, "Please enter a valid maximum distance and click \"Get Airports\"", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        }
    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.Bzoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.Bzoomout)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void changeType(View view)
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }

    // called inside connect()
    private void addMarker(double lat, double lng){
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Closest airport!"));
    }

    // called inside connect()
    private void addLines(double lat1, double lng1, double lat2, double lng2){
        LatLng current = new LatLng(lat1,lng1);
        LatLng dest = new LatLng(lat2,lng2);
       mMap.addPolyline((new PolylineOptions())
               .add(current, dest).width(5).color(Color.BLUE)
               .geodesic(true));
    }

    protected void connect(LatLng point, double maxVar){
        Log.i("Final Project: ", "connect() starts");
        final double lng = point.longitude;
        final double lat = point.latitude;
        final double max = maxVar;

        new AsyncTask<Void,Void,Void>(){

            protected Void doInBackground(Void... args){
                Log.i("Final Project: ", "in doInBackground");
                String urlString = "http://10.66.105.212:8080/m/test?&lat=" +lat+ "&long=" +lng+ "&max=" + max;
                try{
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    Log.i("Final Project: ", "connected");
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder out = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }

                    Log.d("Content from server", out.toString());
                    String response = out.toString();
                    String[] info = response.split(",");
                    double latitude = Double.parseDouble(info[0]);
                    double longitude = Double.parseDouble(info[1]);
                    addMarker(latitude, longitude);
                    addLines(lat,lng,latitude,longitude);


                } catch (MalformedURLException e){

                } catch (IOException e){

                }

                return null;
            }


        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
