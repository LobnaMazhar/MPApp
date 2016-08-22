package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
        Intent intent = new Intent(homeActivity.this, technicalPlansActivity.class);
        startActivity(intent);
    }

    public void reports(View view){
        Intent intent = new Intent(homeActivity.this, reportsActivity.class);
        intent.putExtra("filename", "");
        startActivity(intent);
    }

}
