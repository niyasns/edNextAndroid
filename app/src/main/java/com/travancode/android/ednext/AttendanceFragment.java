package com.travancode.android.ednext;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.special.ResideMenu.ResideMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import at.grabner.circleprogress.CircleProgressView;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

public class AttendanceFragment extends android.app.Fragment {

    private View parentView;
    private ResideMenu resideMenu;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private String uid;

    private Boolean isSubAvail = false;

    TextView user_name;
    TextView user_branch;
    TextView user_number;
    TextView user_attended;
    TextView user_total;
    TextView attendance_title;

    CircleProgressView circleProgressView;
    ProgressDialog dialog;

    Integer total_classes;
    Integer total_attended;

    RecyclerView recyclerView;
    AttendanceAdapter attendanceAdapter;

    TextView heading;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_attendance, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            uid = currentUser.getUid();
        } else {
            Log.d("Attendance Fragment", "No user detected");
        }

        total_attended = 0;
        total_classes = 0;

        recyclerView = parentView.findViewById(R.id.recyclerAttendanceList);
        attendanceAdapter = new AttendanceAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setupViews();

        AsyncAttendanceLoader loader = new AsyncAttendanceLoader();
        loader.execute(uid);

        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupViews() {
        Typeface raleway_bold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Raleway-Bold.ttf" );
        Typeface raleway_regular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Raleway-Regular.ttf" );
        HomeActivity parentActivity = (HomeActivity) getActivity();
        Button button = parentActivity.findViewById(R.id.menu_button);
        heading = parentActivity.findViewById(R.id.heading);

        circleProgressView = parentView.findViewById(R.id.circleProgressView);
        attendance_title = parentView.findViewById(R.id.attendanceTitle);
        user_name = parentView.findViewById(R.id.user_name);
        user_branch = parentView.findViewById(R.id.user_branch);
        user_number = parentView.findViewById(R.id.user_number);
        user_attended = parentView.findViewById(R.id.user_attended_classes);
        user_total = parentView.findViewById(R.id.user_total_classes);

        heading.setText(R.string.home);
        heading.setTypeface(raleway_bold);
        attendance_title.setTypeface(raleway_bold);
        user_name.setTypeface(raleway_bold);
        user_branch.setTypeface(raleway_regular);
        user_attended.setTypeface(raleway_regular);
        user_number.setTypeface(raleway_regular);
        user_total.setTypeface(raleway_regular);


        resideMenu = parentActivity.getResideMenu();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    public class AsyncAttendanceLoader extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "",
                    "Loading. Please wait...", true);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("https://us-central1-ucek-app.cloudfunctions.net/getAttendanceStudent");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("studentId", strings[0]);
                Log.e("Params", postDataParams.toString());

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {

                    return "Ferror : " + responseCode;

                }
            } catch (Exception e) {
                return "Ferror: " + e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Result Firebase", s);

            if(s.contains("Ferror")) {

                Toast.makeText(getActivity(), "Data Not Available", Toast.LENGTH_SHORT).show();
                circleProgressView.setValue(0.0f);
                user_name.setText("No data");
                user_branch.setText("Will be updated");
                user_number.setText("");
                user_total.setText("");
                user_attended.setText("");

            }else {

                try {

                    JSONObject data = new JSONObject(s);
                    User.setAdmno(data.getString("admn"));
                    User.setPro_Image(Uri.parse(data.getString("image")));
                    User.setBatch(data.getString("batch"));
                    User.setRoll_No(data.getString("rollNo"));
                    User.setUserId(data.getString("user_Id"));
                    User.setMobile_No(data.getString("contactNo"));
                    User.setRole(data.getString("role"));
                    User.setParent_Mobile_No(data.getString("parentsNo"));
                    User.setClass_Name(data.getString("className"));
                    User.setDob(data.getString("dob"));
                    User.setUser_Name(data.getString("name"));
                    User.setBranch(data.getString("branch"));
                    User.setTotal_Percentage(data.getString("totalPercentage"));

                    JSONArray d = data.getJSONArray("subjects");

                    if(d.length() != 0) {

                        d.getJSONObject(0);

                        String[] subjects = new String[d.length()];
                        String[] total = new String[d.length()];
                        String[] attended = new String[d.length()];
                        String[] percentage = new String[d.length()];

                        isSubAvail = true;

                        for (int i = 0; i < d.length(); i++) {

                            subjects[i] = d.getJSONObject(i).getString("subjectName");
                            total[i] = d.getJSONObject(i).getString("total");
                            attended[i] = d.getJSONObject(i).getString("attended");
                            percentage[i] = d.getJSONObject(i).getString("percentage");
                            total_classes = total_classes + parseInt(total[i]);
                            total_attended = total_attended + parseInt(attended[i]);
                        }

                        User.setSubjects(subjects);
                        User.setTotal_classes(total);
                        User.setAttended_classes(attended);
                        User.setPer_subjects(percentage);

                        Log.d("Subjects Array : ", Arrays.toString(subjects));
                    }

                } catch (JSONException e) {

                    Log.e(TAG,"Conversion of string to json object failed" );
                    e.printStackTrace();

                }

                if(isSubAvail) {
                    circleProgressView.setValue(Float.valueOf(User.getTotal_Percentage()));
                    user_name.setText(User.getUser_Name());
                    user_branch.setText(User.getBranch());
                    user_number.setText(User.getAdmno());
                    user_total.setText(total_classes + " classes in total");
                    user_attended.setText(total_attended + " classes attended");
                    recyclerView.setAdapter(attendanceAdapter);
                } else {

                    Toast.makeText(getActivity(), "Attendance data not available", Toast.LENGTH_SHORT).show();
                    circleProgressView.setValue(0.0f);
                    user_name.setText("No data");
                    user_branch.setText("Will be updated");
                    user_number.setText("");
                    user_total.setText("");
                    user_attended.setText("");
                }

                dialog.cancel();

            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuffer result = new StringBuffer();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
