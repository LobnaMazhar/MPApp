package com.materialplanning.vodafone.mpapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class siteActivity extends AppCompatActivity {

    final ArrayList<site> siteList = new ArrayList<site>();

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(getIntent().getExtras().get("rollout/expansion").equals("1"))
            toolbar.setTitle("Rollout");
        else if(getIntent().getExtras().get("rollout/expansion").equals("2"))
            toolbar.setTitle("Expansions");
        setSupportActionBar(toolbar);

        getRollout_Expansions();
    }

    public void getRollout_Expansions(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("rollout/expansion", getIntent().getExtras().get("rollout/expansion").toString());
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        site siteObject = new site();

                        siteObject.siteID = data.getString("siteID");
                        siteObject.siteProjectID = data.getInt("projectID");
                        siteObject.siteProjectName = data.getString("projectName");
                        siteObject.siteDate = data.getInt("date");
                        siteObject.siteMonthName = data.getString("monthName");
                        siteObject.siteRegionName = data.getString("regionName");

                        siteList.add(siteObject);
                    }

                    ArrayAdapter<site> sitesListAdapter = new siteAdapter(siteActivity.this, siteList);
                    // Connect list and adapter
                    ListView siteListView = (ListView) findViewById(R.id.siteListView);
                    siteListView.setAdapter(sitesListAdapter);

                    // TODO hn7tag item click listener ??

                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getSites");
    }

    public void generateReport(View view){

        if(getIntent().getExtras().get("rollout/expansion").toString().equals("1")) {
            sheet = workbook.createSheet("Rollout sheet");
        }else{
            sheet = workbook.createSheet("Expansion sheet");
        }

        // IDs ll columns
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("Month");

        cell = row.createCell(1);
        cell.setCellValue("Site ID");

        cell = row.createCell(2);
        cell.setCellValue("Project name");

        cell = row.createCell(3);
        cell.setCellValue("Region");

        // assign values to the sheet
        for(int rownum = 0; rownum<siteList.size(); ++rownum){
            row = sheet.createRow(rownum+1); // 3shan l row l ana mzwdah bara 3shan l IDs ll columns ,,, msh 3mla +1 fl ba2e 3shan dol indices

            cell = row.createCell(0);
            cell.setCellValue(siteList.get(rownum).getSiteMonthName());

            cell = row.createCell(1);
            cell.setCellValue(siteList.get(rownum).getSiteID());

            cell = row.createCell(2);
            cell.setCellValue(siteList.get(rownum).getSiteProjectName());

            cell = row.createCell(3);
            cell.setCellValue(siteList.get(rownum).getSiteRegionName());
        }

        saveOnStorage();
    }

    public void getProjectName(int projectID, final Row row){
        HashMap<String, String> params = new HashMap<>();
        params.put("projectID", Integer.toString(projectID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONObject reader = new JSONObject(result);
                    String projectName = reader.getString("projectName");

                    Cell cell = row.createCell(2);
                    cell.setCellValue(projectName);

                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getProjectName");
    }

    public void saveOnStorage(){
        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            try {
                // CREATE FILE
                File root = Environment.getExternalStorageDirectory();
                // CREATE DIRECTORY
                File dir = new File(root.getAbsolutePath() + "/MPApp");
                if (!dir.exists()) {
                    dir.mkdir();
                }

                File file;
                if(getIntent().getExtras().get("rollout/expansion").toString().equals("1")) {
                    file = new File(dir, "Rollout.xls");
                }else{
                    file = new File(dir, "Expansion.xls");
                }

                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();
                Toast.makeText(siteActivity.this, "Report is generated.", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
