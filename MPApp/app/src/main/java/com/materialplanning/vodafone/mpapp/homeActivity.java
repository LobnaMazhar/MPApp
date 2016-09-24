package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       /*
        if(!getIntent().getExtras().getString("userName").equals("admin")){
            Button addUserButton = (Button) findViewById(R.id.addUser_button);
            addUserButton.setVisibility(View.INVISIBLE);
        }
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
    }

    public void itemsList(View view){
        Intent intent = new Intent(homeActivity.this, itemsActivity.class);
        startActivity(intent);
    }

    public void scenarios (View view){
        Intent intent = new Intent(homeActivity.this, scenariosActivity.class);
        startActivity(intent);
    }

    public void technicalPlans(View view){
        Intent intent = new Intent(homeActivity.this, projectsActivity.class);
        startActivity(intent);
    }

    public void reports(View view){
        Intent intent = new Intent(homeActivity.this, reportsActivity.class);
   //     intent.putExtra("filename", "");
        startActivity(intent);
    }

    //Dot Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dot, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       int itemId = item.getItemId();
        switch(itemId){
            case R.id.addUser_button:
                Intent addNewUser = new Intent(homeActivity.this, addUserActivity.class);
                startActivity(addNewUser);
                break;
            case R.id.action_logout:
                startActivity(new Intent(homeActivity.this, loginActivity.class));
                finish(); //so it won't be stacked behind called activity
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
