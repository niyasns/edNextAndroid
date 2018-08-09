package com.travancode.android.ednext;

import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
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
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;


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

    static private Context mContext;

    static ProgressDialog dialog;

    HomeActivity parentActivity;

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
                isStoragePermissionGranted();
                DownloadFileFromURL loader = new DownloadFileFromURL(getActivity());
                loader.execute(regulations, "regulations.pdf");
            }
        });

        sClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
                DownloadFileFromURL loader = new DownloadFileFromURL(getActivity());
                loader.execute(syllabus, "syllabus.pdf");
            }
        });

        aClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
                DownloadFileFromURL loader = new DownloadFileFromURL(getActivity());
                loader.execute(activity_points, "activity_points.pdf");
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
        parentActivity = (HomeActivity) getActivity();
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


    private static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        public DownloadFileFromURL (Context context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(mContext, "",
                    "Downloading. Please wait...", true);
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(strings[0]);
            String fileName = strings[1];

            DownloadManager.Request r = new DownloadManager.Request(uri);
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            if (dm != null) {
                dm.enqueue(r);
            } else {
                return "failed";
            }
            return "success";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s.equals("failed")) {
                Toast.makeText(mContext, "Download Failed", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(mContext, "Download complete. Check Downloads folder.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionChecker.checkSelfPermission(getActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}