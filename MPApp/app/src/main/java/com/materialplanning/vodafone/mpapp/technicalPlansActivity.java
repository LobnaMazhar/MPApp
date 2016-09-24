package com.materialplanning.vodafone.mpapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class technicalPlansActivity extends AppCompatActivity {

    ArrayList<prvm> prvmArrayList = new ArrayList<>();

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet;

    int huaweiTotal = 0, ericssonTotal = 0, totalTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_plans);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Technical plan");
        setSupportActionBar(toolbar);

      //  getTechnicalPlans();
        getProjectsInPRVMs();
    }

    public void getProjectsInPRVMs(){
        HashMap<String, String> params = new HashMap<String, String>();
        Connection conn = new Connection(params, new ConnectionPostListener() {
            @Override
            public void doSomething(String result) {
                try {
                    final JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); ++i) {
                        JSONObject data = reader.getJSONObject(i);

                        prvm prvmObject = new prvm();

                        prvmObject.prvmID = data.getInt("prvmID");
                        prvmObject.prvmProjectID = data.getInt("prvmProjectID");
                        prvmObject.projectName = data.getString("projectName");
                        prvmObject.prvmRegionID = data.getInt("prvmRegionID");
                        prvmObject.prvmVendorID = data.getInt("prvmVendorID");
                        prvmObject.prvmYearTarget = data.getInt("prvmYearTarget");

                        prvmArrayList.add(prvmObject);
                    }

                    final ArrayAdapter<prvm> prvmAdapter = new prvmAdapter(technicalPlansActivity.this, prvmArrayList);
                    // Connect list and adapter
                    ListView projectsInTechnicalPlanListView = (ListView) findViewById(R.id.projectsInTechnicalPlanListView);
                    projectsInTechnicalPlanListView.setAdapter(prvmAdapter);

                    // On item click listener
                    projectsInTechnicalPlanListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try{
                                        Intent goToPRVM = new Intent(technicalPlansActivity.this, prvmViewActivity.class);
                                        goToPRVM.putExtra("projectID", prvmArrayList.get(position).getPrvmProjectID());
                                        goToPRVM.putExtra("projectName", prvmArrayList.get(position).getProjectName());
                                        startActivity(goToPRVM);
                                    }
                                    catch (Exception e){}
                                }
                            }
                    );
                } catch (JSONException e) {
                }
            }
        });
        conn.execute(conn.URL + "/getPRVMs");
    }

    public void addProject(View view) {
        Intent intent = new Intent(technicalPlansActivity.this, addProjectToTechnicalPlanActivity.class);
        startActivity(intent);
        finish();
    }

    public void generateTechnicalPlansReport(View view){
        // Fill sheet with defaults
        sheet = workbook.createSheet("Technical Plans");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Project");

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
        cell.setCellValue("Grand Total");
        sheet.addMergedRegion(CellRangeAddress.valueOf("N1:P1"));
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
        cell = row.createCell(13);
        cell.setCellValue("Huawei");
        cell = row.createCell(14);
        cell.setCellValue("Ericsson");
        cell = row.createCell(15);
        cell.setCellValue("Total");

        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:A2"));

        int prevProjectID = 0, projectsCount = 1;
        for(int i=0; i<prvmArrayList.size(); ++i){
            if(i == 0 || prevProjectID != prvmArrayList.get(i).getPrvmProjectID()){
                row = sheet.createRow(++projectsCount);
                cell = row.createCell(0);
                cell.setCellValue(prvmArrayList.get(i).getProjectName());
                sheet.setColumnWidth(0, 10000);

                prevProjectID = prvmArrayList.get(i).getPrvmProjectID();
            }

            // GET CELL ACCORDING TO REGION AND VENDOR
            int cellIndex = prvmArrayList.get(i).getPrvmRegionID()*2 - 1; // *2 -1 3shan 3la asas l specified cells fo2
            if(prvmArrayList.get(i).getPrvmVendorID() == 2)
                ++cellIndex;

            cell = row.createCell(cellIndex);
            int yearTarget = prvmArrayList.get(i).getPrvmYearTarget();
            cell.setCellValue(Integer.toString(yearTarget));

            if(prvmArrayList.get(i).getPrvmVendorID() == 1){ // NOTE :: Huawei (in database)
                huaweiTotal += yearTarget;
            }else if(prvmArrayList.get(i).getPrvmVendorID() == 2){ // NOTE :: Ericsson (in database)
                ericssonTotal += yearTarget;
            }

            // NEXT PROJECT WON"T BE THE SAME || LAST PROJECT,, THEN, SET TOTAL
            if((i != prvmArrayList.size()-1 && prvmArrayList.get(i+1).getPrvmProjectID() != prevProjectID) || i == prvmArrayList.size()-1){
                cell = row.createCell(13);
                cell.setCellValue(huaweiTotal);
                cell = row.createCell(14);
                cell.setCellValue(ericssonTotal);
                cell = row.createCell(15);
                totalTotal = huaweiTotal + ericssonTotal;
                cell.setCellValue(totalTotal);

                huaweiTotal = 0; ericssonTotal = 0; totalTotal = 0;
            }
        }
        saveOnStorage();
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

                File file = new File(dir, "Technical plan.xls");

                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Toast.makeText(technicalPlansActivity.this, "Report is generated.", Toast.LENGTH_LONG).show();
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
