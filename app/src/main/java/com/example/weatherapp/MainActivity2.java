package com.example.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    private static final String error_mesg = "Google Play Services are unavailable";
    private static final int LOCATION_PERMISSON_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_page);
        recyclerView = (RecyclerView) findViewById(R.id.weather_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GoogleApiAvailability availability=GoogleApiAvailability.getInstance();
        int result=availability.isGooglePlayServicesAvailable(this);
        if (result!= ConnectionResult.SUCCESS){
            if (!availability.isUserResolvableError(result)){
                Toast.makeText(this, error_mesg, Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        int permission = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        if (permission == PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSON_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSON_REQUEST) {
            if (grantResults[0] != PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Location Permission denied", Toast.LENGTH_LONG).show();
            } else {
                getLastLocation();
            }
        }
    }

    public void getLastLocation() {
        FusedLocationProviderClient fusedLocationClient ;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    getCityWeather(location);
                }
            });
        }
    }

    static ArrayList<weather> arrayWeather = new ArrayList<>();

    public void getCityWeather(Location location) {

      //  String url = "https://api.weatherbit.io/v2.0/forecast/daily?city=Ramallah&key=dae41d9b3b4c49deb2022bba958bf38b";
        String url = "http://api.weatherbit.io/v2.0/forecast/daily?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&key=dae41d9b3b4c49deb2022bba958bf38b";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray array = response.getJSONArray("data");
                            for (int x = 0; x < array.length(); x++) {
                                JSONObject obj = array.getJSONObject(x);
                                int minTemp = (int) Double.parseDouble(obj.getString("max_temp"));
                                int maxTemp = (int) Double.parseDouble(obj.getString("min_temp"));
                                String weather = obj.getString("weather");
                                //split weather
                                weather = weather.replace("{", "");
                                weather = weather.replace("}", "");
                                String split[] = weather.split(",");
                                //get image icon
                                String splitIconLine[] = split[0].split(":");
                                String icon = splitIconLine[1];
                                //delete "" from string
                                icon = icon.substring(1);
                                icon = icon.substring(0, icon.length() - 1);
                                //get description icon
                                String splitDescLine[] = split[2].split(":");
                                String desc = splitDescLine[1];
                                //delete "" from string
                                desc = desc.substring(1);
                                desc = desc.substring(0, desc.length() - 1);
                                String date = obj.getString("datetime");
                                //split weather
                                addWeatherStatus(minTemp, maxTemp, desc, icon,date, x);
                            }
                            CaptionedImageAdapter adapter = new CaptionedImageAdapter(arrayWeather);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void addWeatherStatus(int minTemp, int maxTemp, String desc, String icon,String date, int counter) {
        arrayWeather.add(new weather(minTemp, maxTemp, desc, icon,date));
        if (counter == 0) {
            //set day
            final TextView tx1 = (TextView) findViewById(R.id.current_tv);
            tx1.setText(Integer.toString(arrayWeather.get(counter).getMaxTemp()));
            //set status
            final TextView tx2 = (TextView) findViewById(R.id.status_tv);
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dt1=format1.parse(date);
                String split[]=dt1.toString().split(" ");
                tx2.setText(split[0]+" "+date+"\n   "+desc);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final ImageView iv = (ImageView) findViewById(R.id.current_day);
            final String imgURL = "https://www.weatherbit.io/static/img/icons/" + arrayWeather.get(counter).getImageCode() + ".png";
            new DownLoadImageTask(iv).execute(imgURL);
        }
    }

    class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }


        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }


        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}