package com.zerotime.zerotime.Moderator.Pojos;
 public class Complaint_Pojo {
    String name,phone,complaint,date;

    public Complaint_Pojo(String name, String phone, String complaint, String date) {
        this.name = name;
        this.phone = phone;
        this.complaint = complaint;
        this.date = date;
    }

    public Complaint_Pojo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
