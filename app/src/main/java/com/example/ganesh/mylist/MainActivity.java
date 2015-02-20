package com.example.ganesh.mylist;


import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {
    String [] colourNames;
    String [] colourCode;
    String   sText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colourNames = getResources().getStringArray(R.array.listArray);
        colourCode = getResources().getStringArray(R.array.listValues);
        final RelativeLayout relativelay =  ( RelativeLayout) findViewById(R.id.relativel);
        final ListView lv = (ListView) findViewById(R.id.listView);
         final  ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);
        ReadSD();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                sText = colourCode[position];
                sText = sText.substring(2);
                relativelay.setBackgroundColor(Color.parseColor("#"+ sText));
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        registerForContextMenu(lv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select SD card");
        menu.add(0, v.getId(), 0, "Write to SD CARD");
        menu.add(0, v.getId(), 0, "Read to SD CARD");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Write to SD CARD") {
            try {
                Toast.makeText(getApplicationContext(), "Writing to SD Card....", Toast.LENGTH_LONG).show();
         writetoSD();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (item.getTitle() == "Read to SD CARD") {
            Toast.makeText(getApplicationContext(), "Reading from SD Card....", Toast.LENGTH_LONG).show();
            ReadSD();
        } else {
            return false;
        }
        return true;
    }

   public void writetoSD()throws IOException{
        File card = Environment.getExternalStorageDirectory();
        File Directory = new File(card.getAbsolutePath() + "/dir1");
        Directory.mkdir();
        File newfile = new File(Directory, "MyFileColor.txt");
        FileOutputStream fileoutput = new FileOutputStream(newfile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fileoutput);
        myOutWriter.append(sText);
        myOutWriter.close();
        fileoutput.close();
    }

    public void ReadSD(){
        try {
            File card = Environment.getExternalStorageDirectory();
            File Directory = new File(card.getAbsolutePath()+ "/dir1");
            File newfile = new File(Directory, "MyFileColor.txt");
            if(newfile.exists()) {
                FileInputStream FileInput = new FileInputStream(newfile);
                InputStreamReader inputReader = new InputStreamReader(FileInput);
                BufferedReader BReader = new BufferedReader(inputReader);
                String getString;
                StringBuilder sBuilder = new StringBuilder();
                while ((getString = BReader.readLine()) != null) {
                    sBuilder.append(getString);
                }
                FileInput.close();
                String StringInput = sBuilder.toString();
                final RelativeLayout relativelay = (RelativeLayout) findViewById(R.id.relativel);
                relativelay.setBackgroundColor(Color.parseColor("#"+StringInput));
            }
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage()+ "ERROR, Program can't write to file...", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
