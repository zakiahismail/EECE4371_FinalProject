package mdismaz.finalproject;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap; // Mighet be null if Google Play services APK is not available


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
       // mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);


    }

    public void onMapLongClick(LatLng point) {
        Toast.makeText(MapsActivity.this, point.latitude + " " + point.longitude, Toast.LENGTH_SHORT).show();
        connect(point);
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
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
//        Location myLoc = mMap.getMyLocation();
//        double lat = myLoc.getLatitude();
//        double lng = myLoc.getLongitude();
//        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Your current location"));
    }

    protected void connect(LatLng point){
        Log.i("Final Project: ", "connect() starts");
        final double lng = point.longitude;
        final double lat = point.latitude;

        new AsyncTask<Void,Void,Void>(){

            protected Void doInBackground(Void... args){
                Log.i("Final Project: ", "in doInBackground");
                String urlString = "http://10.66.105.212:8080/m/test?lat=" + lat + "&long=" + lng + "&max=23";
                try{
                    URL url = new URL(urlString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    StringBuilder out = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }

                    Log.d("Content from server", out.toString());
                    reader.close();
                } catch (MalformedURLException e){

                } catch (IOException e){

                }

                return null;
            }


        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
