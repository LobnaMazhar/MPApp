package com.materialplanning.vodafone.mpapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class reportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String filename = getIntent().getExtras().getString("filename");
        if(!filename.equals(""))
            saveBOM(filename);
    }

    public void getBOM(View view){
        startActivity(new Intent(reportsActivity.this, fileChooserActivity.class));
    }

    public void saveBOM(String filename){
        String cellContents="";
        try{

           /* BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("rollout.xls")));
            StringBuffer stringBuffer = new StringBuffer();
            while ((cellContents = inputReader.readLine()) != null) {
                Toast.makeText(this, "READING", Toast.LENGTH_SHORT).show();
                stringBuffer.append(cellContents + "\n");
            }*/

            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard,filename);
            FileInputStream myInput = new FileInputStream(file);

// TODO -> Google :: Read excel sheet on device using android studio
           Workbook workbook = Workbook.getWorkbook(myInput);
            Sheet sheet = workbook.getSheet(0);

            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            for(int r=0; r<rows; ++r){
                for(int c=0; c<columns; ++c){
                    Cell cell = sheet.getCell(r,c);
                    cellContents += cell.getContents();
                }
                cellContents+="\n";
            }
        }catch(Exception e){
        }

        Toast.makeText(this, "S " + cellContents + " E.", Toast.LENGTH_SHORT).show();
    }

    public void rollout(View view){
        Intent intent = new Intent(this, rolloutActivity.class);
        startActivity(intent);
    }

}
