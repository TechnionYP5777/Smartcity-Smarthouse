package com.example.alex.bakshishapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import java.util.HashMap;


/**
 * Created by Alex on 02/04/2017.
 *This class is a "Back code" for content_main.xml
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Temporary database */
        users.put("Hana",new person("Hana","bakshish"));
        users.put("Yossi",new person("Yossi","gil"));
        /**********************/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

           }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem i) {
        return i.getItemId() == R.id.action_settings || super.onOptionsItemSelected(i);
    }

    public void signIn(View v) {

        String givenName = ((EditText) findViewById(R.id.nameText)).getText().toString(),
                givenPassword = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        AlertDialog.Builder aMsgBuilder = new AlertDialog.Builder(this);
        person prsn =  users.get(givenName);
        if (prsn == null) {
            aMsgBuilder.setMessage("There is no such user").setTitle("Error");
            aMsgBuilder.create().show();
        } else if (prsn.getPassword().equals(givenPassword))
            startActivity(new Intent(MainActivity.this, registeredUser.class).putExtra("user", givenName));
        else {
            aMsgBuilder.setMessage("Wrong password").setTitle("Error");
            aMsgBuilder.create().show();
        }
    }
    public void signUp(View v) {
        AlertDialog.Builder aMsgBuilder = new AlertDialog.Builder(this);
        String givenName = ((EditText)findViewById(R.id.nameText)).getText().toString();
        if(givenName.length() == 0){
            aMsgBuilder.setMessage("Choose Name please").setTitle("Error");
            aMsgBuilder.create().show();
            return;
        }
        if(users.get(givenName) != null){
            aMsgBuilder.setMessage("Name already exist \n please choose other name").setTitle("Error");
            aMsgBuilder.create().show();
            return;
        }
        String givenPassword = ((EditText)findViewById(R.id.passwordText)).getText().toString();
        if(givenPassword.length() == 0){
            aMsgBuilder.setMessage("Choose password please").setTitle("Error");
            aMsgBuilder.create().show();
            return;
        }
        users.put(givenName,new person(givenName,givenPassword));

        aMsgBuilder.setMessage("User successfuly created").setPositiveButton("sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface i, int which) {
                startActivity(new Intent(MainActivity.this,registeredUser.class).putExtra("user",givenName));
            }
        });
                aMsgBuilder.create().show();





    }
// Temporary Database
    public HashMap<String,person> users = new HashMap<String,person>() ;




}
