package com.travancode.android.ednext;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DocsFragment extends android.app.Fragment {

    private View parentView;
    private ResideMenu resideMenu;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private String uid;

    private String activity_points = "https://firebasestorage.googleapis.com/v0/b/ucek-app.appspot.com/o/ACTIVITY%20POINTS%20-%20UCE%20document%20_corrected.pdf?alt=media&token=10973f2a-efec-4c20-967e-b10cf764bab8";
    private String regulations = "https://firebasestorage.googleapis.com/v0/b/ucek-app.appspot.com/o/REGULATIONS%202018%20UCEK%2CKVTM28-05-18.pdf?alt=media&token=0eff5022-d5c8-475c-b727-c69c1cba919c";
    private String syllabus = "https://firebasestorage.googleapis.com/v0/b/ucek-app.appspot.com/o/Syllabus2018%20Btech%2CUCE%2CKVTM%20Samitha28-05-18.pdf?alt=media&token=b6c63c7b-bef5-4fa9-8b9f-dad328eae11f";

    TextView Tregulation;
    TextView Tsyllabus;
    TextView Tactivity;

    ProgressDialog dialog;

    Button rClick;
    Button sClick;
    Button aClick;

    Button button;

    TextView heading;

    public DocsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_docs, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            uid = currentUser.getUid();
        } else {
            Log.d("Attendance Fragment", "No user detected");
        }
        setupViews();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        rClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadFileFromURL loader = new DownloadFileFromURL();
                loader.execute(regulations, "regulations");
            }
        });

        sClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadFileFromURL loader = new DownloadFileFromURL();
                loader.execute(syllabus, "syllabus");
            }
        });

        aClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFileFromURL loader = new DownloadFileFromURL();
                loader.execute(activity_points, "activity_points");
            }
        });

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
        Typeface raleway_bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface raleway_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
        HomeActivity parentActivity = (HomeActivity) getActivity();
        button = parentActivity.findViewById(R.id.menu_button);
        heading = parentActivity.findViewById(R.id.heading);

        Tregulation = parentView.findViewById(R.id.regulation);
        Tsyllabus = parentView.findViewById(R.id.syllabus);
        Tactivity = parentView.findViewById(R.id.activity);

        rClick = parentView.findViewById(R.id.regulationDownload);
        sClick = parentView.findViewById(R.id.syllabusDownload);
        aClick = parentView.findViewById(R.id.activityDownload);

        heading.setText(R.string.docs);
        heading.setTypeface(raleway_bold);
        Tregulation.setTypeface(raleway_regular);
        Tsyllabus.setTypeface(raleway_regular);
        Tactivity.setTypeface(raleway_regular);

        resideMenu = parentActivity.getResideMenu();
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog = ProgressDialog.show(getActivity(), "",
                    "Downloading. Please wait...", true);
        }

        @Override
        protected String doInBackground(String... strings) {

            int count;

            try {
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lengthOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                        + "/" + strings[1] + ".pdf");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("Error : ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Toast.makeText(getActivity(), "Download complete. Check Downloads folder.", Toast.LENGTH_SHORT).show();
        }
    }

}