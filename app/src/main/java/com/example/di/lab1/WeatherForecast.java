package com.example.di.lab1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "WeatherForecast";

    private static final String weather_URL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";


    private TextView tv_current_temperature;
    private TextView tv_min_temperature;
    private TextView tv_max_temperature;
    private ImageView iv_weather_image;


    ProgressBar pb_load;
    //ProgressBar pb_load_h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        pb_load = (ProgressBar) findViewById(R.id.pb_loading);
        //pb_load_h = (ProgressBar) findViewById(R.id.pb_loading_h);

        tv_current_temperature = (TextView) findViewById(R.id.current_temperature);
        tv_min_temperature = (TextView) findViewById(R.id.min_temperature);
        tv_max_temperature = (TextView) findViewById(R.id.max_temperature);

        iv_weather_image = (ImageView) findViewById(R.id.weatherImage);

        Log.i(ACTIVITY_NAME, "enter onCreate!! ");

        new ForecastQuery().execute(weather_URL);

    }

    public void setPBarVisibility(boolean b) {
        if (b) {
            pb_load.setVisibility(View.VISIBLE);
            //pb_load_h.setVisibility(View.VISIBLE);
        } else {
            pb_load.setVisibility(View.INVISIBLE);
            //pb_load_h.setVisibility(View.INVISIBLE);

        }

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String current_temperature;
        private String min_temperature;
        private String max_temperature;

        private String icon_name;

        private Bitmap bm_current_weather;

        @Override
        protected String doInBackground(String... params) {

            Log.i(ACTIVITY_NAME, "enter doInBackground(String... params)!! ");

            Log.i(ACTIVITY_NAME, "URL passed:" + params[0]);

            setPBarVisibility(true);

            HttpURLConnection conn = null;
            try {
                //String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                //return conn.getInputStream();

                Log.i(ACTIVITY_NAME, "HttpURLConnection established");

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);
                parser.nextTag();
                //return readFeed(parser);


                //parser.require(XmlPullParser.START_TAG, null, "current");

                //Log.i(ACTIVITY_NAME, "XmlPullParser.END_TAG: " +XmlPullParser.END_TAG);

                // Log.i(ACTIVITY_NAME, "parser.next(): " +parser.next());
                //Log.i(ACTIVITY_NAME, "XmlPullParser.START_TAG: " +XmlPullParser.END_TAG);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    //Log.i(ACTIVITY_NAME, "parser.getName(): " +parser.getName());

                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
//                    Log.i(ACTIVITY_NAME, "Event: " +parser.getEventType());


                    String name = parser.getName();
                    //Log.i(ACTIVITY_NAME, "name: " + name);

                    // Starts by looking for the entry tag
                    if (name.equals("temperature")) {
                        current_temperature = parser.getAttributeValue(null, "value");
                        //Log.i(ACTIVITY_NAME, "Current temperature: " + current_temperature);

                        this.publishProgress(25);
                        min_temperature = parser.getAttributeValue(null, "min");
                        //Log.i(ACTIVITY_NAME, "Minimum temperature: " + min_temperature);
                        Thread.sleep(4000);
                        this.publishProgress(50);
                        max_temperature = parser.getAttributeValue(null, "max");
                        //Log.i(ACTIVITY_NAME, "Maximum temperature: " + max_temperature);
                        Thread.sleep(4000);
                        this.publishProgress(75);


                    } else if (name.equals("weather")) {
                        icon_name = parser.getAttributeValue(null, "icon");
                        //Log.i(ACTIVITY_NAME, "icon Name: " + icon_name);

                    }
                }

                String bitmapFname = icon_name + ".png";
                Log.i(ACTIVITY_NAME, "Looking for image: " + bitmapFname);

                if (fileExistance(bitmapFname)) {

                    bm_current_weather = readImage(bitmapFname);
                    Log.i(ACTIVITY_NAME, "locally read image: " + bitmapFname);

                } else {
                    String bitmapUrl = "http://openweathermap.org/img/w/" + bitmapFname;
                    bm_current_weather = getImage(new URL(bitmapUrl), bitmapFname);
                    Log.i(ACTIVITY_NAME, "Download image: " + bitmapFname);

                }

                this.publishProgress(100);

                return "success";

            } catch (MalformedURLException e) {

                return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            pb_load.setProgress(values[0]);
            //Log.i(ACTIVITY_NAME, "ProgressBar Value: " + values[0]);

            pb_load.setVisibility(View.VISIBLE);
            //pb_load_h.setProgress(values[0]);

            //pb_load_h.setVisibility(View.VISIBLE);

            //setPBarVisibility(true);
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            tv_current_temperature.setText("Current Temperature: " + current_temperature);
            tv_min_temperature.setText("Minimum Temperature: " + min_temperature);
            tv_max_temperature.setText("Maximum Temperature: " + max_temperature);
            iv_weather_image.setImageBitmap(bm_current_weather);
            pb_load.setVisibility(View.INVISIBLE);
            //pb_load_h.setVisibility(View.INVISIBLE);

        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


        public Bitmap getImage(URL url, String fName) {
            HttpURLConnection connection = null;
            Bitmap image = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return image;
        }

        public Bitmap readImage(String fname) {

            try {
                FileInputStream fis = openFileInput(fname);
                Bitmap b = BitmapFactory.decodeStream(fis);
                fis.close();
                return b;
            } catch (Exception e) {
            }
            return null;
        }
    }

}
