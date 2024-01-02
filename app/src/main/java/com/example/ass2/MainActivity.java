package com.example.ass2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText edtUSD, edtEUR, edtNIS, edtEGP, edtJOD,edtBTC,edtETH;
    private Button btConverter;

    protected void onPause() {
        super.onPause();
        boolean isLoggedIn = checkIfLoggedIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isLoggedIn = checkIfLoggedIn();
    }
    private boolean checkIfLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return preferences.getBoolean("is_logged_in", false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);

        // Initialize your EditText views
        edtUSD = findViewById(R.id.edtUSD);
        edtEUR = findViewById(R.id.edtEUR);
        edtNIS = findViewById(R.id.edtNIS);
        edtEGP = findViewById(R.id.edtEGP);
        edtJOD = findViewById(R.id.edtJOD);
        edtBTC = findViewById(R.id.edtBTC);
        edtETH = findViewById(R.id.edtETH);
        btConverter=findViewById(R.id.btConverter);
        CoinRateApi();
        CryptoRateApi("ethereum", edtETH);
        CryptoRateApi("bitcoin", edtBTC);

        Button btnLogout = findViewById(R.id.btLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLoggedInStatus(false);
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        btConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConverterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void setLoggedInStatus(boolean isLoggedIn) {

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_logged_in", isLoggedIn);
        editor.apply();
    }
    private void CoinRateApi() {
        String url = "https://v6.exchangerate-api.com/v6/66496a5a62879bf805fcb13d/latest/USD";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rates = response.getJSONObject("conversion_rates");

                    // Retrieve exchange rates
                    double usdRate = rates.getDouble("USD");
                    double eurRate = rates.getDouble("EUR");
                    double nisRate = rates.getDouble("ILS");
                    double egpRate = rates.getDouble("EGP");
                    double jodRate = rates.getDouble("JOD");

                    // Update EditText views with the exchange rates
                    edtUSD.setText(String.valueOf(usdRate));
                    edtEUR.setText(String.valueOf(eurRate));
                    edtNIS.setText(String.valueOf(nisRate));
                    edtEGP.setText(String.valueOf(egpRate));
                    edtJOD.setText(String.valueOf(jodRate));

                    Log.d("Response", response.toString());
                } catch (JSONException e) {
                    Log.d("json_error", e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", error.toString());

            }
        });

        queue.add(request);
    }
    private void CryptoRateApi(String coin, final EditText editText) {
        String url = "https://api.coincap.io/v2/rates/" + coin;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    double rate = data.getDouble("rateUsd");
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    String formattedRate = decimalFormat.format(rate);
                    // Update the EditText view with the exchange rate
                    editText.setText(String.valueOf(formattedRate));
                } catch (JSONException e) {
                    Log.d("json_error", e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", error.toString());

            }
        });

        queue.add(request);
    }



}
