package com.materialplanning.vodafone.mpapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class addUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add user");
        setSupportActionBar(toolbar);
    }

    public void addUser(View view){
        HashMap<String,String> params = new HashMap<String, String>();

        EditText addUserNameEditText = (EditText) findViewById(R.id.addUserNameEditText);
        final String username = addUserNameEditText.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(this, "Enter a username", Toast.LENGTH_LONG).show();
            return;
        }
        params.put("userName", username);

        EditText addUserPasswordEditText = (EditText) findViewById(R.id.addUserPasswordEditText);
        String password = addUserPasswordEditText.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(this, "Enter a password", Toast.LENGTH_LONG).show();
            return;
        }
        params.put("userPassword", password);

        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    if(reader.getBoolean("added")){
                        String msg = "User " + username + " is added successfully";
                        Toast.makeText(addUserActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/addUser");
    }

    /*
    //Dot Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dot, menu);
        return true;
    }
     */

}
