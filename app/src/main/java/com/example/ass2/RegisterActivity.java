package com.example.ass2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUser, edtPassword,edtEmail;
    private Button btSignIn, btRegister;

    private TextView tverror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        btSignIn = findViewById(R.id.btSignIn);
        btRegister = findViewById(R.id.btRegister2);
        tverror = findViewById(R.id.tverror);


        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUser.getText().toString();
                String password = edtPassword.getText().toString();
                String email = edtEmail.getText().toString();

                if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    String savedUserData = preferences.getString("user_data", "");

                    try {
                        JSONObject savedUserJSON = new JSONObject(savedUserData);

                        // Check if the username or email is already registered
                        if (savedUserJSON.has("username") && savedUserJSON.getString("username").equals(username)) {
                            tverror.setText("Username is already taken");
                        } else if (savedUserJSON.has("email") && savedUserJSON.getString("email").equals(email)) {
                            tverror.setText("Email is already registered");
                        } else {
                            // User is not registered, proceed with registration
                            JSONObject userData = new JSONObject();
                            userData.put("username", username);
                            userData.put("password", password);
                            userData.put("email", email);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("user_data", userData.toString());
                            editor.apply();

                            tverror.setText("Registration Successful");

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tverror.setText("Please enter all data correctly");
                }
            }
        });

    }
}



