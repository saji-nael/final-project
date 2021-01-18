package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity4 extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        recyclerView = (RecyclerView) findViewById(R.id.weather_recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        String message = intent.getStringExtra("city");
        getCityWeather(message);

    }





    static ArrayList<weather> arrayWeather = new ArrayList<>();

    public void getCityWeather(String city) {
        Toast.makeText(getApplicationContext(), city, Toast.LENGTH_LONG).show();

          String url = "https://api.weatherbit.io/v2.0/forecast/daily?city="+city+"&key=dae41d9b3b4c49deb2022bba958bf38b";

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
            final TextView tx1 = (TextView) findViewById(R.id.current_tv1);
            tx1.setText(Integer.toString(arrayWeather.get(counter).getMaxTemp()));
            //set status
            final TextView tx2 = (TextView) findViewById(R.id.status_tv1);
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dt1=format1.parse(date);
                String split[]=dt1.toString().split(" ");
                tx2.setText(split[0]+" "+date+"\n   "+desc);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final ImageView iv = (ImageView) findViewById(R.id.current_day1);
            final String imgURL = "https://www.weatherbit.io/static/img/icons/" + arrayWeather.get(counter).getImageCode() + ".png";
            new DownLoadImageTask(iv).execute(imgURL);
        }
    }

    class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}