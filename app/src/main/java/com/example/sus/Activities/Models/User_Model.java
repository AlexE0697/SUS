package com.example.sus.Activities.Models;

public class User_Model {

    private String full_name;
    private String student_no;
    private String email;
    private String access_level;
    private String profile_photo_url = "";

    public User_Model() {
    }

    public String getaccess_level() {
        return access_level;
    }

    public void setaccess_level(String access_level) {
        this.access_level = access_level;
    }

    public String getprofile_photo_url() {
        return profile_photo_url;
    }

    public void setprofile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
    }

    public String getfull_name() {
        return full_name;
    }

    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getstudent_no() {
        return student_no;
    }

    public void setstudent_no(String student_no) {
        this.student_no = student_no;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }
}
