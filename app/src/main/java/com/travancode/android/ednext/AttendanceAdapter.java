package com.travancode.android.ednext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;

import at.grabner.circleprogress.CircleProgressView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleProgressView circleProgressView;
        public TextView gName, gPercent, gTotal, gAttended;

        public MyViewHolder(View itemView) {
            super(itemView);

            circleProgressView = itemView.findViewById(R.id.gCircleProgressView);
            gName = itemView.findViewById(R.id.grid_name);
            gPercent = itemView.findViewById(R.id.grid_percent);
            gTotal = itemView.findViewById(R.id.grid_total);
            gAttended = itemView.findViewById(R.id.grid_attended);
        }
    }

    public AttendanceAdapter()
    {

    }

    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.MyViewHolder holder, int position) {

        Float percent = Float.valueOf(User.getPer_subjects()[position]);
        String name = User.getSubjects()[position];
        String total = User.getTotal_classes()[position];
        String attended = User.getAttended_classes()[position];

        holder.circleProgressView.setValue(percent);
        holder.gName.setText(name);
        holder.gPercent.setText(String.format("%.2f Percentage", percent));
        holder.gTotal.setText(String.format("%s classes in total", total));
        holder.gAttended.setText(String.format("%s classes attended", attended));

    }

    @Override
    public int getItemCount() {
        return User.getSubjects().length;
    }
}
