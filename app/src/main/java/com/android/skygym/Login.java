package com.android.skygym;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText eml = findViewById(R.id.email);
        final EditText pword = findViewById(R.id.password);
        final Button bLogin = findViewById(R.id.sign_in_button);

        final TextView signup = findViewById(R.id.create_account);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup.setTextColor(Color.parseColor("#ff4081"));
                signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = eml.getText().toString().trim();
                final String password = pword.getText().toString().trim();

                if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    eml.setError("Invalid email address!");
                }
                else if(password.isEmpty()){
                    pword.setError("Field is required!");
                }
                else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    final String first_name = jsonResponse.getString("first_name");
                                    final String last_name = jsonResponse.getString("last_name");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setMessage("Welcome to Sky Gym.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Login.this, Home.class);
                                                    intent.putExtra("first_name", first_name);
                                                    intent.putExtra("last_name", last_name);
                                                    intent.putExtra("email", email);
                                                    Login.this.startActivity(intent);
                                                }
                                            })
                                            .setTitle("Success!")
                                            .setIcon(R.drawable.ic_success)
                                            .create()
                                            .show();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setMessage("Incorrect email or password.")
                                            .setNegativeButton("Retry", null)
                                            .setTitle("Alert!")
                                            .setIcon(R.drawable.ic_error)
                                            .create()
                                            .show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);
                }
            }
        });
    }
}

