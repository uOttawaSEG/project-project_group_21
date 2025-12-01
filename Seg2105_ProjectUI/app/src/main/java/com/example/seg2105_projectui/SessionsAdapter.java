package com.example.seg2105_projectui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//this entire class is for the viewing requested sessions listview
public class SessionsAdapter extends ArrayAdapter<Sessions> {
    public SessionsAdapter(Context context, ArrayList<Sessions> sessions) {
        super(context, 0, sessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sessions session = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_sessionitem, parent, false);
        }

        //for getting the information for each session box
        TextView tvCourse = (TextView) convertView.findViewById(R.id.tvCourse);
        TextView tvDate= (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvTutor= (TextView) convertView.findViewById(R.id.tvTutor);
        TextView tvStatus= (TextView) convertView.findViewById(R.id.tvStatus);

        //set the text like usual
        String temp = "Session for " + session.getCourse();
        tvCourse.setText(temp);
        temp = "Date: " + session.getDate();
        tvDate.setText(temp);
        temp = "Time: " + session.getStartTime();
        tvTime.setText(temp);
        temp = "Tutor: " + session.getTutorUsername();
        tvTutor.setText(temp);
        temp = "Status: " + session.getStatus();
        tvStatus.setText(temp);

        return convertView;
    }
}