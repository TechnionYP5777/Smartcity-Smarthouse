package com.example.alex.bakshishapplication;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Alex on 04/04/2017.
 * This class is a "Back code" for activity_registered_user.xml
 */

public class registeredUser extends AppCompatActivity {

    String userName;
    ExpandableListView expandableListView;
    List<String> userProps;
    Map<String , List<String>> userSubProps ;
    List<String> phones;
    List<String> emails;
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_user);
        userName = getIntent().getStringExtra("user");
       ((TextView)findViewById(R.id.userNameText)).setText("Welcome " + userName);


        userProps = new ArrayList<String>();
        userProps.add("Phones");
        userProps.add("Emails");
        getUserData();
        AlertDialog.Builder aMsgBuilder = new AlertDialog.Builder(this);

        expandableListView = (ExpandableListView)findViewById(R.id.usersContacts);
        listAdapter = new userExpandbleListAdapter(this,userProps,userSubProps);
        expandableListView.setAdapter(listAdapter);

    }


    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    private void getUserData(){
        phones = new ArrayList<>();
        emails = new ArrayList<>();
        userSubProps = new HashMap<>();
        phones.add("0541234567");
        phones.add("0547654321");
        emails.add("mashuMashu@gmail.com");
        emails.add("havalAlAzman@gmail.com");
        userSubProps.put(userProps.get(0),phones);
        userSubProps.put(userProps.get(1),emails);
    }

    private String m_Text = "";
    public void addEmailClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Email Please");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                emails.add(m_Text);
                expandableListView.setAdapter(listAdapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    public void addPhoneClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Phone Number Please");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                phones.add(m_Text);
                expandableListView.setAdapter(listAdapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
