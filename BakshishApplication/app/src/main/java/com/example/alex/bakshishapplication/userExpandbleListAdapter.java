package com.example.alex.bakshishapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 11/04/2017.
 * This Class implements the ExpandableListAdapter interface that
 * we need in order to fill emails and phones in our user
 * " Expandable List view "
 */

public class userExpandbleListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> userProps;
    Map<String , List<String>> userSubProps;


    public userExpandbleListAdapter(registeredUser registeredUser, List<String> userProps, Map<String, List<String>> userSubProps) {

        this.context = registeredUser;
        this.userProps = userProps;
        this.userSubProps = userSubProps;
    }


    @Override
    public int getGroupCount() {
        return userProps.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return userSubProps.get(userProps.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return userProps.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return userSubProps.get(userProps.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String userPropsTitle =(String) getGroup(groupPosition);

        if ( convertView == null   ){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
            convertView = inflater.inflate(R.layout.list_user_properties,null);
        }
        TextView txtUserProps = (TextView) convertView.findViewById(R.id.userProperties);
        txtUserProps.setText(userPropsTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        String userSubPropsTitle =(String) getChild(groupPosition,childPosition);

        if ( convertView == null   ){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
            convertView = inflater.inflate(R.layout.list_subproperties,null);
        }

        TextView txtUserSubProps = (TextView) convertView.findViewById(R.id.userSubproperties);
        txtUserSubProps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =  userSubProps.get(userProps.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                                /************************************************
                                 REMOVE FROM DATA BASE !!!!!!!!!!!!!!!!!!!!!!!!!!
                                 ************************************************/
                            }
                        });
                builder.setNegativeButton("No",  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        txtUserSubProps.setText(userSubPropsTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }





}
