package com.travancode.android.ednext;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenu;

public class AttendanceFragment extends android.app.Fragment {

    private View parentView;
    private ResideMenu resideMenu;

    TextView user_name;
    TextView user_sem;
    TextView user_number;
    TextView user_attended;
    TextView user_total;
    TextView user_most_attended;
    TextView user_least_attended;
    TextView attendance_title;

    TextView heading;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_attendance, container, false);
        setupViews();
        return parentView;
    }

    private void setupViews() {
        Typeface raleway_bold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Raleway-Bold.ttf" );
        Typeface raleway_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Raleway-Regular.ttf" );
        HomeActivity parentActivity = (HomeActivity) getActivity();
        Button button = parentActivity.findViewById(R.id.menu_button);
        heading = parentActivity.findViewById(R.id.heading);

        attendance_title = parentView.findViewById(R.id.attendanceTitle);
        user_name = parentView.findViewById(R.id.user_name);
        user_sem = parentView.findViewById(R.id.user_semester);
        user_number = parentView.findViewById(R.id.user_number);
        user_attended = parentView.findViewById(R.id.user_attended_classes);
        user_total = parentView.findViewById(R.id.user_total_classes);
        user_most_attended = parentView.findViewById(R.id.user_most_attended_class);
        user_least_attended = parentView.findViewById(R.id.user_most_missed_class);

        heading.setText(R.string.home);
        heading.setTypeface(raleway_bold);
        attendance_title.setTypeface(raleway_bold);
        user_name.setTypeface(raleway_bold);
        user_sem.setTypeface(raleway_regular);
        user_attended.setTypeface(raleway_regular);
        user_number.setTypeface(raleway_regular);
        user_total.setTypeface(raleway_regular);
        user_most_attended.setTypeface(raleway_regular);
        user_least_attended.setTypeface(raleway_regular);


        resideMenu = parentActivity.getResideMenu();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }
}
