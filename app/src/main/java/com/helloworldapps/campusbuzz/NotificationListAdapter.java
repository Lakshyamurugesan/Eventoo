package com.helloworldapps.campusbuzz;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationListAdapter extends ArrayAdapter<Data> {

    private Context mContext;
    private List<Data> mnotificationList;

    public NotificationListAdapter( Context context, List<Data> postList) {
        super(context,0,postList);
        mContext = context;
        mnotificationList = postList;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        Data currentPost = mnotificationList.get(position);

        TextView title = listItem.findViewById(R.id.title);
        TextView date = listItem.findViewById(R.id.date);
        //TextView time = listItem.findViewById(R.id.time);

        title.setText(currentPost.getSubject());

        String dates = "Posted : "+DateConversion(currentPost.getDate());
        date.setText(dates);
        applytextcolor(dates,DateConversion(currentPost.getDate()),date);

        return listItem;
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


}