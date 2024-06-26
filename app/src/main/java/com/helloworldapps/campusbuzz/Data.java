package com.helloworldapps.campusbuzz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Data implements Serializable {
    public String subject;
    public String message,imageurl;
    public String date,time,eventstartdate,eventenddate,link,eventplace,registrationenddate,location,registrationfees;

    public Data() {
    }

    public Data(String subject, String message, String imageurl, String date, String time, String eventstartdate, String eventenddate, String link, String eventplace, String registrationenddate, String location, String registrationfees) {
        this.subject = subject;
        this.message = message;
        this.imageurl = imageurl;
        this.date = date;
        this.time = time;
        this.eventstartdate = eventstartdate;
        this.eventenddate = eventenddate;
        this.link = link;
        this.eventplace = eventplace;
        this.registrationenddate = registrationenddate;
        this.location = location;
        this.registrationfees = registrationfees;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getEventplace() {
        return eventplace;
    }

    public void setEventplace(String eventplace) {
        this.eventplace = eventplace;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegistrationfees() {
        return registrationfees;
    }

    public void setRegistrationfees(String registrationfees) {
        this.registrationfees = registrationfees;
    }

    public String getRegistrationenddate() {
        return registrationenddate;
    }

    public void setRegistrationenddate(String registrationenddate) {
        this.registrationenddate = registrationenddate;
    }
}