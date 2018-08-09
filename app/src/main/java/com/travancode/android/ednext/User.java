package com.travancode.android.ednext;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;

public class User {

    static String user_Name;
    static String class_Name;
    static String mobile_No;
    static Uri pro_Image;
    static String parent_Mobile_No;
    static String role;
    static String roll_No;
    static String userId;
    static String dob;
    static String branch;
    static String batch;
    static String admno;
    static String[] subjects;
    static String total_Percentage;
    static String[] total_classes;
    static String[] attended_classes;
    static String[] per_subjects;

    public User() {
    }

    public static String getUser_Name() {
        return user_Name;
    }

    public static void setUser_Name(String user_Name) {
        User.user_Name = user_Name;
    }

    public static String getClass_Name() {
        return class_Name;
    }

    public static void setClass_Name(String class_Name) {
        User.class_Name = class_Name;
    }

    public static String getMobile_No() {
        return mobile_No;
    }

    public static void setMobile_No(String mobile_No) {
        User.mobile_No = mobile_No;
    }

    public static Uri getPro_Image() {
        return pro_Image;
    }

    public static void setPro_Image(Uri pro_Image) {
        User.pro_Image = pro_Image;
    }

    public static String getParent_Mobile_No() {
        return parent_Mobile_No;
    }

    public static void setParent_Mobile_No(String parent_Mobile_No) {
        User.parent_Mobile_No = parent_Mobile_No;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        User.role = role;
    }

    public static String getRoll_No() {
        return roll_No;
    }

    public static void setRoll_No(String roll_No) {
        User.roll_No = roll_No;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public static String getDob() {
        return dob;
    }

    public static void setDob(String dob) {
        User.dob = dob;
    }

    public static String getBranch() {
        return branch;
    }

    public static void setBranch(String branch) {
        User.branch = branch;
    }

    public static String getBatch() {
        return batch;
    }

    public static void setBatch(String batch) {
        User.batch = batch;
    }

    public static String getAdmno() {
        return admno;
    }

    public static void setAdmno(String admno) {
        User.admno = admno;
    }

    public static String[] getSubjects() {
        return subjects;
    }

    public static void setSubjects(String[] subjects) {
        User.subjects = subjects;
    }

    public static String getTotal_Percentage() {
        return total_Percentage;
    }

    public static void setTotal_Percentage(String total_Percentage) {
        User.total_Percentage = total_Percentage;
    }

    public static String[] getTotal_classes() {
        return total_classes;
    }

    public static void setTotal_classes(String[] total_classes) {
        User.total_classes = total_classes;
    }

    public static String[] getAttended_classes() {
        return attended_classes;
    }

    public static void setAttended_classes(String[] attended_classes) {
        User.attended_classes = attended_classes;
    }

    public static String[] getPer_subjects() {
        return per_subjects;
    }

    public static void setPer_subjects(String[] per_subjects) {
        User.per_subjects = per_subjects;
    }
}
