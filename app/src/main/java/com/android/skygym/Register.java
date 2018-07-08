package com.android.skygym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText fname, lname, eml, usr, pwd, cnfm;
    private ProgressDialog progressDialog;
    private Button bRegister;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = findViewById(R.id.login_to_account);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setTextColor(Color.parseColor("#ff4081"));
                login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Home.class));
            return;
        }

        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        eml = findViewById(R.id.email);
        usr = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        cnfm = findViewById(R.id.confirmPassword);

        progressDialog = new ProgressDialog(this);

        bRegister = findViewById(R.id.sign_up_button);
        bRegister.setOnClickListener(this);
    }

    private void registerUser() {
        final String firstname = fname.getText().toString().trim();
        final String lastname = lname.getText().toString().trim();
        final String email = eml.getText().toString().trim();
        final String username = usr.getText().toString().trim();
        final String password = pwd.getText().toString().trim();

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            startActivity(new Intent(getApplicationContext(), Login.class));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
         RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == bRegister){
            registerUser();
        }
    }
}
