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

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fname = findViewById(R.id.firstname);
        final EditText lname = findViewById(R.id.lastname);
        final EditText eml = findViewById(R.id.email);
        final EditText pwd = findViewById(R.id.password);
        final EditText cnfmPwd = findViewById(R.id.confirmPassword);
        final Button bRegister = findViewById(R.id.sign_up_button);

        final TextView login = findViewById(R.id.login_to_account);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setTextColor(Color.parseColor("#ff4081"));
                login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String first_name = fname.getText().toString().trim();
                final String last_name = lname.getText().toString().trim();
                final String email = eml.getText().toString().trim();
                final String password = pwd.getText().toString().trim();
                final String confirmPassword = cnfmPwd.getText().toString().trim();

                if(first_name.isEmpty()){
                    fname.setError("Field is required!");
                }
                else if(last_name.isEmpty()){
                    lname.setError("Field is required!");
                }
                else if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    eml.setError("Enter a valid Email address!");
                }
                else if(password.isEmpty()){
                    pwd.setError("Field is required!");
                }
                else if(confirmPassword.isEmpty()){
                    cnfmPwd.setError("Please re-enter your Password!");
                }
                else if(!password.equals(confirmPassword)){
                    cnfmPwd.setError("Passwords do not match!");
                }
                else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("Registration successful!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    Register.this.startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("Registration failed!")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(first_name, last_name, email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
