package com.materialplanning.vodafone.mpapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO check this link :: https://rogerkeays.com/simple-android-file-chooser
// TODO remove first part of the URL and add it to Connection class 3shan lw et3'yr yt3'yr fe mkan wa7d
public class fileChooserActivity extends ListActivity {

    private File currentDir;
    private fileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this,"Started",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/"); // TODO MPApp

        fill(currentDir);
    }
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<option> dir = new ArrayList<option>();
        List<option>fls = new ArrayList<option>();
        try{
            for(File ff: dirs)
            {
                if(ff.isDirectory())
                    dir.add(new option(ff.getName(),"Folder",ff.getAbsolutePath()));
                else
                {
                    fls.add(new option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
                }
            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new option("..","Parent Directory",f.getParent()));
        adapter = new fileArrayAdapter(fileChooserActivity.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        option o = adapter.getItem(position);
        if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else {
            onFileClick(o);
        }
    }
    private void onFileClick(option o) {
        Intent intent = new Intent(this, reportsActivity.class);
        intent.putExtra("filename", o.getName());
        startActivity(intent);
    }
}
