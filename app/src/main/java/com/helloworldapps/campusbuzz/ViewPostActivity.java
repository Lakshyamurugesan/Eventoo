package com.helloworldapps.campusbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewPostActivity extends AppCompatActivity {

    ImageView eventPoster;
    TextView eventHeadline,eventSubject,eventType,eventVenue,eventStartDate,eventEndDate,eventLocation,lastRegistrationDate,registrationFees,registerButton;
    Data data;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        eventPoster = findViewById(R.id.eventPoster);
        eventHeadline = findViewById(R.id.eventHeadline);
        eventSubject = findViewById(R.id.eventSubject);
        eventType = findViewById(R.id.eventType);
        eventVenue = findViewById(R.id.eventVenue);
        eventStartDate = findViewById(R.id.eventStartDate);
        eventEndDate = findViewById(R.id.eventEndDate);
        eventLocation = findViewById(R.id.eventLocation);
        lastRegistrationDate = findViewById(R.id.lastRegistrationDate);
        registrationFees = findViewById(R.id.registrationFees);
        registerButton = findViewById(R.id.registerButton);

        Intent intent = getIntent();
        if (intent != null) {
            data = (Data) intent.getSerializableExtra("clickedPost");
            from = intent.getStringExtra("from");

            if(from!=null) {
                if (from.equals("closed")) {

                    registerButton.setEnabled(false);
                    registerButton.setText("Closed");
                    Toast.makeText(this, "Registration Closed", Toast.LENGTH_SHORT).show();
                }
            }

            if (data != null) {

                eventHeadline.setText(data.getSubject());
                applytextcolor(data.getSubject(),data.getSubject(),eventHeadline);
                eventSubject.setText("About Event : "+data.getMessage());
                applytextcolor("About Event : "+data.getMessage(),"About Event : ",eventSubject);
                Glide.with(this).load(data.getImageurl()).into(eventPoster);
                if (data.getEventplace().isEmpty() || data.getEventplace().equals("Online")) {
                    eventType.setText("EventType : "+data.getEventplace());
                    applytextcolor("EventType : "+data.getEventplace(),"EventType : ",eventType);
                    eventVenue.setText("EventVenue : Online");
                    applytextcolor("EventVenue : Online","EventVenue : ",eventVenue);

                }
                else{
                    eventType.setText("EventType : Venue");
                    applytextcolor("EventType : Venue","EventType : ",eventType);
                    eventVenue.setText("EventVenue : "+data.getEventplace());
                    applytextcolor("EventVenue : "+data.getEventplace(),"EventVenue : ",eventVenue);
                }
            }

            eventStartDate.setText("EventStartDate : "+data.getEventstartdate());
            applytextcolor("EventStartDate : "+data.getEventstartdate(),"EventStartDate : ",eventStartDate);
            eventEndDate.setText("EventEndDate : "+data.getEventenddate());
            applytextcolor("EventEndDate : "+data.getEventenddate(),"EventEndDate : ",eventEndDate);
            eventLocation.setText("Location : "+data.getLocation());
            applytextcolor("Location : "+data.getLocation(),"Location : ",eventLocation);
            lastRegistrationDate.setText("Last Date for Registration : "+ DateConversion(data.getRegistrationenddate()));
            applytextcolor("Last Date for Registration : "+DateConversion(data.getRegistrationenddate()),"Last Date for Registration : ",lastRegistrationDate);
            registrationFees.setText("Registration Fees : "+"₹"+data.getRegistrationfees());
            applytextcolor("Registration Fees : "+"₹"+data.getRegistrationfees(),"Registration Fees : ",registrationFees);

        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ensureHttpOrHttps(data.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private static void applytextcolor(String arg,String ptext, TextView textview){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(arg);

        int startIndex = arg.indexOf(ptext);
        int endIndex = arg.indexOf(ptext) + ptext.length();
        //int endIndex = startIndex + ptext.length();

        // Apply color to the text
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), startIndex, endIndex, 0);

        // Apply a bold style to the text
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, 0);

        textview.setText(spannableStringBuilder);
    }

    public String ensureHttpOrHttps(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url;
        }
        return url;
    }

    private static String DateConversion(String arg) {
        String inputDate = arg;
        String outputDate = null;

        try {
            // Define the input date format
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Parse the input date string into a Date object
            Date date = inputFormat.parse(inputDate);

            // Define the desired output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd' 'MMM yyyy");

            // Format the Date object into the desired output format
            outputDate = outputFormat.format(date);

            // Convert the month abbreviation to title case
            outputDate = titleCaseMonth(outputDate);
            outputDate = addOrdinalSuffix(outputDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    private static String titleCaseMonth(String date) {
        String[] parts = date.split(" ");
        if (parts.length == 3) {
            String day = parts[0];
            String month = parts[1];
            String year = parts[2];
            return day + " " + month.substring(0, 1).toUpperCase() + month.substring(1) + " " + year;
        }
        return date;
    }

    private static String addOrdinalSuffix(String date) {
        String[] parts = date.split(" ");
        if (parts.length == 3) {
            String day = parts[0];
            String month = parts[1];
            String year = parts[2];
            int dayInt = Integer.parseInt(day);
            if (dayInt >= 11 && dayInt <= 13) {
                return day + "th " + month + " " + year;
            } else {
                switch (dayInt % 10) {
                    case 1:
                        return day + "st " + month + " " + year;
                    case 2:
                        return day + "nd " + month + " " + year;
                    case 3:
                        return day + "rd " + month + " " + year;
                    default:
                        return day + "th " + month + " " + year;
                }
            }
        }
        return date;
    }
}