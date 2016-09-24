package com.materialplanning.vodafone.mpapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
      //  startActivity(new Intent(loginActivity.this, homeActivity.class));
        // TODO delete above line and uncomment the next block

        HashMap<String, String> params = new HashMap<String, String>();

        EditText usernameText = (EditText) findViewById(R.id.loginUsernameTextField);
        String username = usernameText.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_LONG).show();
            return;
        }
        params.put("userName", username);

        EditText passwordText = (EditText) findViewById(R.id.loginPasswordTextField);
        String password = passwordText.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
            return;
        }
        params.put("userPassword", password);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    JSONObject reader = new JSONObject(result);
                    Intent intent = new Intent(loginActivity.this, homeActivity.class);
                    intent.putExtra("userID", reader.getInt("userID"));
                    intent.putExtra("userName", reader.getString("userName"));
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                   Toast.makeText(loginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
        conn.execute(conn.URL + "/login");
    }
}
