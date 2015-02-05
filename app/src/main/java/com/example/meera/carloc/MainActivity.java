package com.example.meera.carloc;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;


public class MainActivity extends ActionBarActivity  {

    public static final String LOCPREFERENCES = "LocPrefs" ;
    public static final String LocationKey = "LocationKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sharedpreferences = getSharedPreferences(LOCPREFERENCES, Context.MODE_PRIVATE);
                String loc_json = sharedpreferences.getString(LocationKey,null);
                if (loc_json != null) {
                    Intent i = new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(i);
                }
                else {
                    displayDialog();
                }

            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sharedpreferences = getSharedPreferences(LOCPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(LOCPREFERENCES);
        editor.clear();
        editor.commit();
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


    public void carParked (View v) {

        Intent i = new Intent(this,SaveLocActivity.class);
        startActivity(i);
    }

    public void directionsToCar (View v){
        sharedpreferences = getSharedPreferences(LOCPREFERENCES, Context.MODE_PRIVATE);
        String loc_json = sharedpreferences.getString(LocationKey,null);
        if (loc_json != null) {

            Gson gson = new Gson();
            Location location = gson.fromJson(loc_json, Location.class);
            String url = "http://maps.google.com/maps?daddr="+location.getLatitude()+","+location.getLongitude();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
            startActivity(intent);
        }
        else {
            displayDialog();
        }

    }

    public void displayDialog(){

        Log.i("MainActivity", "Creating dialog ");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
