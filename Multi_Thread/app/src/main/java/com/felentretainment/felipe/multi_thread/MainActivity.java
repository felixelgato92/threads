package com.felentretainment.felipe.multi_thread;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void loadFile(View view){
        Back_Thread runner = new Back_Thread();
        //String sleepTime = time.getText().toString();
        runner.doInBackground();
    }
    public void load() {
        List<String> myList = new ArrayList<>();
        try {
            //String filename = this.getFilesDir() + "/" + "numbers.txt";

            FileInputStream in = openFileInput("numbers.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                myList.add(line);
                Thread.sleep(250);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
    }


    public void writeFile(View view) {

        Back_Thread runner = new Back_Thread();
        //String sleepTime = time.getText().toString();
        runner.doInBackground(this.getFilesDir());


    }

    public void clearListView(View view) {
        List<String> myList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
    }

    private class Back_Thread extends AsyncTask<File,Integer,Void>{
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        public void write(File main_file){
            File file = new File(String.valueOf(main_file), "numbers.txt");
            //Outp fileOutputStream;

            Context context = getApplicationContext();
            CharSequence str = main_file.toString();
            int len = Toast.LENGTH_SHORT;
            Toast.makeText(context, str, len).show();

            try {
                FileWriter fileWriter = new FileWriter(file);
                for (int i = 1; i <= 10; i++) {
                    fileWriter.append(i + "\n");
                    publishProgress(i);
                    Thread.sleep(250);
                }
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(File... params) {
            if (params.length != 0) {
                progressBar.setMax(10);
                write(params[0]);
            }
            else {
                Context context = getApplicationContext();
                CharSequence str = "here";
                int len = Toast.LENGTH_SHORT;
                Toast.makeText(context, str, len).show();
                load();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(10);
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setProgress(progressBar.getMax());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }
}