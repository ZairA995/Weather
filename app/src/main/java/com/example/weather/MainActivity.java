package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView view_city;
    TextView view_temp;
    TextView view_desc;
    Button search;
    ImageView view_weather;
    EditText search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        view_city = findViewById(R.id.town);
        view_temp = findViewById(R.id.temp);
        view_desc = findViewById(R.id.describe);
        view_city.setText("");
        view_temp.setText("");
        view_desc.setText("");
        search = findViewById(R.id.button);
        view_weather = findViewById(R.id.view_weather);
        search_text = findViewById(R.id.search_town);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(), 0);
                api_key(String.valueOf(search_text.getText()));
                search_text.setText("");

            }

            private void api_key(final String City) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=20d92d14e7ab745e3ddff3a2a4bf78fe&units=metric")
                        .get()
                        .build();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try{
                    Response response = client.newCall(request).execute();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseData = response.body().string();
                            try {
                                JSONObject json = new JSONObject(responseData);
                                JSONArray array = json.getJSONArray("weather");
                                JSONObject object = array.getJSONObject(0);

                                String description = object.getString("description");
                                String icons = object.getString("icon");
                                JSONObject temp1 = json.getJSONObject("main");
                                Double Temperature = temp1.getDouble("temp");

                                setText(view_city,City);
                                String temps = (Math.round(Temperature)+" °С");
                                setText(view_temp,temps);
                                setText(view_desc,description);
                                setImage(view_weather,icons);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }
                    });
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void setText(final TextView text, final String value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(value);
                    }
                });
            }

            private void setImage(final ImageView imageView, final String value){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (value) {
                            case "01d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon1));
                                break;
                            case "01n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon1));
                                break;
                            case "02d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon2));
                                break;
                            case "02n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon2));
                                break;
                            case "03d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon3));
                                break;
                            case "03n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon3));
                                break;
                            case "04d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon4));
                                break;
                            case "04n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon4));
                                break;
                            case "09d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon5));
                                break;
                            case "09n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon5));
                                break;
                            case "10d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon6));
                                break;
                            case "10n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon6));
                                break;
                            case "11d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon7));
                                break;
                            case "11n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon7));
                                break;
                            default:
                                imageView.setImageDrawable(getResources().getDrawable(R.drawable.weather));
                        }
                    }
                });
            }
        });
    }
}

   