package com.materialplanning.vodafone.mpapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class monthlyPhasingPerProjectActivity extends AppCompatActivity {

    int selectedProjectID = 0;
    String selectedProjectName;

    ArrayList<monthlyPhasing> monthlyPhasingPerProjectList = new ArrayList<>();

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_phasing_per_project);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Monthly phasing per project");
        setSupportActionBar(toolbar);

        prepareProjects();
    }

    public void prepareProjects(){
        final ArrayList<project> projectsList = new ArrayList<project>();
        project projectObject = new project();
        projectObject.projectName = "";
        projectsList.add(projectObject);

        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        project projectObject = new project();
                        projectObject.projectID = data.getInt("projectID");
                        projectObject.projectName = data.getString("projectName");
                        projectsList.add(projectObject);
                    }
                }catch (JSONException e){
                }

                Spinner projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
                projectSpinnerAdapter projectSpinnerAdapter = new projectSpinnerAdapter(monthlyPhasingPerProjectActivity.this, projectsList);
                projectSpinner.setAdapter(projectSpinnerAdapter);

                projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        String projectName = projectsList.get(position).getProjectName();
                        if(!projectName.equals("")) {
                            Toast.makeText(monthlyPhasingPerProjectActivity.this, projectName, Toast.LENGTH_SHORT).show();
                            selectedProjectID = projectsList.get(position).getProjectID();
                            selectedProjectName = projectsList.get(position).getProjectName();
                          //  getMonthlyPhasing();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        });
        conn.execute(conn.URL + "/getProjectsInPRVMs");
    }

    public void getMonthlyPhasing(){
        HashMap<String, String> params = new HashMap<>();
        params.put("projectID", Integer.toString(selectedProjectID));
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try{
                    JSONArray reader = new JSONArray(result);
                    for(int i=0; i<reader.length(); ++i){
                        JSONObject data = reader.getJSONObject(i);

                        monthlyPhasing monthlyPhasingObject = new monthlyPhasing();

                        monthlyPhasingObject.projectID = selectedProjectID;
                        monthlyPhasingObject.projectName = data.getString("projectName");
                        monthlyPhasingObject.regionID = data.getInt("regionID");
                        monthlyPhasingObject.regionName = data.getString("regionName");
                        monthlyPhasingObject.vendorID = data.getInt("vendorID");
                        monthlyPhasingObject.vendorName = data.getString("vendorName");
                        monthlyPhasingObject.month = data.getInt("monthID");
                        monthlyPhasingObject.monthName = data.getString("monthName");
                        monthlyPhasingObject.phasing = data.getInt("monthPhase");

                        monthlyPhasingPerProjectList.add(monthlyPhasingObject);
                    }
                    generateReport();
                }catch (JSONException e){
                }
            }
        });
        conn.execute(conn.URL + "/getMonthlyPhasingPerProject");
    }

    public void generateReport(View view){
        getMonthlyPhasing();
    }

    public void generateReport(){
        if(selectedProjectID == 0){
            Toast.makeText(monthlyPhasingPerProjectActivity.this, "Choose project", Toast.LENGTH_LONG).show();
        }else {
            sheet = workbook.createSheet(selectedProjectName);

            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Month");

            // REGIONS
            CellStyle regionStyle = workbook.createCellStyle();
            regionStyle.setAlignment(regionStyle.ALIGN_CENTER);

            cell = row.createCell(1);
            cell.setCellValue("Alex");
            sheet.addMergedRegion(CellRangeAddress.valueOf("B1:C1"));
            cell.setCellStyle(regionStyle);
            cell = row.createCell(3);
            cell.setCellValue("Cairo");
            sheet.addMergedRegion(CellRangeAddress.valueOf("D1:E1"));
            cell.setCellStyle(regionStyle);
            cell = row.createCell(5);
            cell.setCellValue("Giza");
            sheet.addMergedRegion(CellRangeAddress.valueOf("F1:G1"));
            cell.setCellStyle(regionStyle);
            cell = row.createCell(7);
            cell.setCellValue("Delta");
            sheet.addMergedRegion(CellRangeAddress.valueOf("H1:I1"));
            cell.setCellStyle(regionStyle);
            cell = row.createCell(9);
            cell.setCellValue("Upper Egypt");
            sheet.addMergedRegion(CellRangeAddress.valueOf("J1:K1"));
            cell.setCellStyle(regionStyle);
            cell = row.createCell(11);
            cell.setCellValue("Canal");
            sheet.addMergedRegion(CellRangeAddress.valueOf("L1:M1"));
            cell.setCellStyle(regionStyle);
            cell = row.createCell(13);
            cell.setCellValue("Target");
            cell.setCellStyle(regionStyle);

            // VENDORS
            row = sheet.createRow(1);
            cell = row.createCell(1);
            cell.setCellValue("Huawei");
            cell = row.createCell(2);
            cell.setCellValue("Ericsson");
            cell = row.createCell(3);
            cell.setCellValue("Huawei");
            cell = row.createCell(4);
            cell.setCellValue("Ericsson");
            cell = row.createCell(5);
            cell.setCellValue("Huawei");
            cell = row.createCell(6);
            cell.setCellValue("Ericsson");
            cell = row.createCell(7);
            cell.setCellValue("Huawei");
            cell = row.createCell(8);
            cell.setCellValue("Ericsson");
            cell = row.createCell(9);
            cell.setCellValue("Huawei");
            cell = row.createCell(10);
            cell.setCellValue("Ericsson");
            cell = row.createCell(11);
            cell.setCellValue("Huawei");
            cell = row.createCell(12);
            cell.setCellValue("Ericsson");

            sheet.addMergedRegion(CellRangeAddress.valueOf("A1:A2"));
            sheet.addMergedRegion(CellRangeAddress.valueOf("N1:N2"));

            ArrayList<Pair<String, Integer>> months = new ArrayList<>();
            months.add(new Pair<String, Integer>("April", 0));
            months.add(new Pair<String, Integer>("May", 0));
            months.add(new Pair<String, Integer>("June", 0));
            months.add(new Pair<String, Integer>("July", 0));
            months.add(new Pair<String, Integer>("August", 0));
            months.add(new Pair<String, Integer>("September", 0));
            months.add(new Pair<String, Integer>("October", 0));
            months.add(new Pair<String, Integer>("November", 0));
            months.add(new Pair<String, Integer>("December", 0));
            months.add(new Pair<String, Integer>("January", 0));
            months.add(new Pair<String, Integer>("February", 0));
            months.add(new Pair<String, Integer>("March", 0));

            for(int i=0; i<12; ++i) {
                row = sheet.createRow(i+2);
                cell = row.createCell(0);
                cell.setCellValue(months.get(i).first);
            }
            row = sheet.createRow(14);
            cell = row.createCell(0);
            cell.setCellValue("Total target");

            for(int i=0; i<monthlyPhasingPerProjectList.size(); ++i){
                row = sheet.getRow(monthlyPhasingPerProjectList.get(i).getMonth()+1);

                // INCREMENT TARGET OF MONTH TODO
                //months.get(monthlyPhasingPerProjectList.get(i).getMonth()-2).second += 1;

                // GET CELL ACCORDING TO REGION AND VENDOR
                int cellIndex = monthlyPhasingPerProjectList.get(i).getRegionID()*2 - 1; // *2 -1 3shan 3la asas l specified cells fo2
                if(monthlyPhasingPerProjectList.get(i).getVendorID() == 2)
                    ++cellIndex;
                cell = row.createCell(cellIndex);

                cell.setCellValue(monthlyPhasingPerProjectList.get(i).getPhasing());
            }

            // Add total target for each month
            for(int i=0; i<12; ++i) {
                row = sheet.getRow(i+2);
                cell = row.createCell(13);
                cell.setCellValue(months.get(i).second);
            }
            saveOnStorage();
        }
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

                File file = new File(dir, "Monthly phasing for " + selectedProjectName + ".xls");

                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();
                Toast.makeText(monthlyPhasingPerProjectActivity.this, "Report is generated.", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
