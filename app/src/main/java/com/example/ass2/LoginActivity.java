package com.example.ass2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btLogin, btRegister;
    private CheckBox checkBox;
    private TextView tverror;
    private boolean isLoggedIn = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        checkBox = findViewById(R.id.checkBox);
        tverror = findViewById(R.id.tverror1);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Load saved data if Rememberme was checked
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);

////////////////////////////////////////////////////////////////
       // SharedPreferences.Editor editor = preferences.edit();
       // editor.clear();
       // editor.apply();
////////////////////////////////////////////////////////////////

        boolean rememberMeChecked = preferences.getBoolean("remember_me", false);

        if (rememberMeChecked) {
            String savedUsername = preferences.getString("saved_username", "");
            String savedPassword = preferences.getString("saved_password", "");

            edtUsername.setText(savedUsername);
            edtPassword.setText(savedPassword);
            checkBox.setChecked(true);
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = edtUsername.getText().toString();
                String enteredPassword = edtPassword.getText().toString();

                if (checkBox.isChecked()) {
                    // Save entered data if "Remember Me" is checked
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("remember_me", true);
                    editor.putString("saved_username", enteredUsername);
                    editor.putString("saved_password", enteredPassword);
                    editor.apply();
                } else {
                    // Clear saved data if "Remember Me" is unchecked
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("remember_me");
                    editor.remove("saved_username");
                    editor.remove("saved_password");
                    editor.apply();
                }

                // Continue with the rest of the login logic
                try {
                    JSONObject userData = new JSONObject(preferences.getString("user_data", ""));
                    String savedUsername = userData.getString("username");
                    String savedPassword = userData.getString("password");

                    if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                        tverror.setText("Login Successful");
                        isLoggedIn = true;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        tverror.setText("Incorrect Username or Password");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
