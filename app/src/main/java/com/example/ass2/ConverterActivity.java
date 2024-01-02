package com.example.ass2;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ConverterActivity extends AppCompatActivity {

    private RequestQueue queue;
    private EditText edtUSD, edtEUR, edtNIS, edtEGP, edtJOD ,edtcoins;
    private Spinner spinner;
    private Button btConverter,btback ;
    private TextView tverror;
    String[] coins = {"USD", "EUR", "ILS", "EGP", "JOD"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        queue = Volley.newRequestQueue(this);

        // Initialize your EditText views
        edtUSD = findViewById(R.id.edtUSD);
        edtEUR = findViewById(R.id.edtEUR);
        edtNIS = findViewById(R.id.edtNIS);
        edtEGP = findViewById(R.id.edtEGP);
        edtJOD = findViewById(R.id.edtJOD);
        edtcoins = findViewById(R.id.edtcoins);
        spinner = findViewById(R.id.spinner);
        btConverter = findViewById(R.id.btConverter);
        btback = findViewById(R.id.btback);
        tverror = findViewById(R.id.tverror2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coins);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoinRateApi();
            }
        });
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConverterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onStop() {
        super.onStop();
        edtUSD.setText(String.valueOf(""));
        edtEUR.setText(String.valueOf(""));
        edtNIS.setText(String.valueOf(""));
        edtEGP.setText(String.valueOf(""));
        edtJOD.setText(String.valueOf(""));
        edtcoins.setText("");
    }
    private void CoinRateApi() {
        String selectedCoin = coins[spinner.getSelectedItemPosition()];
        String url = "https://v6.exchangerate-api.com/v6/66496a5a62879bf805fcb13d/latest/" + selectedCoin;
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

                    // Get the entered amount from the EditText
                    String coinsText = edtcoins.getText().toString();
                    if (!coinsText.isEmpty()) {
                        double amount = Double.parseDouble(coinsText);

                        // Update EditText views with the converted amounts
                        edtUSD.setText(String.valueOf(amount * usdRate));
                        edtEUR.setText(String.valueOf(amount * eurRate));
                        edtNIS.setText(String.valueOf(amount * nisRate));
                        edtEGP.setText(String.valueOf(amount * egpRate));
                        edtJOD.setText(String.valueOf(amount * jodRate));
                    } else {
                        // Handle the case where edtcoins is empty
                        tverror.setText("Please enter an amount");
                    }


                } catch (JSONException e) {

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
