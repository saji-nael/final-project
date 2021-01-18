package com.example.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CaptionedImageAdapter extends RecyclerView.Adapter<CaptionedImageAdapter.ViewHolder> {
    ArrayList<weather> arrayWeather;

    public CaptionedImageAdapter(ArrayList<weather> array) {
        arrayWeather = array;

    }

    @Override
    public CaptionedImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CaptionedImageAdapter.ViewHolder holder, final int position) {
        //current card view
        final CardView cardView = holder.cardView;
        //set day
        final TextView tx1 = (TextView) cardView.findViewById(R.id.day_tv);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String input_date = arrayWeather.get(position).getDate();
            Date dt1 = format1.parse(input_date);
            String split[] = dt1.toString().split(" ");
            tx1.setText(split[0] + " " + arrayWeather.get(position).getDate());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        final ImageView iv = (ImageView) cardView.findViewById(R.id.day_status_image);
        final String imgURL = "https://www.weatherbit.io/static/img/icons/" + arrayWeather.get(position).getImageCode() + ".png";
        new DownLoadImageTask(iv).execute(imgURL);
        //set day
        final TextView tx2 = (TextView) cardView.findViewById(R.id.temp_tv);
        tx2.setText(Integer.toString(arrayWeather.get(position).getMaxTemp()));


    }


    @Override
    public int getItemCount() {
        return arrayWeather.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
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
