package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class reportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reports");
        setSupportActionBar(toolbar);

        getReports();

      /*     String filename = getIntent().getExtras().getString("filename");
        if(!filename.equals(""))
            saveBOM(filename);*/
    }
    /*
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
/*
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
        Intent intent = new Intent(this, siteActivity.class);
        startActivity(intent);
    }
*/
    public void getReports(){
        final String[] reportsList = getResources().getStringArray(R.array.reports);

        ListView reportsListView = (ListView) findViewById(R.id.reportsListView);
        final ArrayAdapter<String> reportsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reportsList);
        reportsListView.setAdapter(reportsAdapter);

        // TODO on click
        reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(reportsList[position].equals("Technical Plan")){
                    Intent goToTechnicalPlan = new Intent(reportsActivity.this, technicalPlansActivity.class);
                    startActivity(goToTechnicalPlan);
                }else if(reportsList[position].equals("New Rollout")){
                    Intent goToRollout = new Intent(reportsActivity.this, siteActivity.class);
                    goToRollout.putExtra("rollout/expansion", "1");
                    startActivity(goToRollout);
                }else if(reportsList[position].equals("Expansions")){
                    Intent goToExpansions = new Intent(reportsActivity.this, siteActivity.class);
                    goToExpansions.putExtra("rollout/expansion", "2");
                    startActivity(goToExpansions);
                }
            }
        });
    }

    public void getBOM(View view){
        Intent goToBOM = new Intent(reportsActivity.this, billOfMaterialActivity.class);
        startActivity(goToBOM);
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
