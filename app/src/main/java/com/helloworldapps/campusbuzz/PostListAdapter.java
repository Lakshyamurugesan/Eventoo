package com.helloworldapps.campusbuzz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostListAdapter extends ArrayAdapter<Data> {

    private Context mContext;
    private List<Data> mPostList;
    private String outputDate;

    public PostListAdapter( Context context, List<Data> postList) {
        super(context,0,postList);
        mContext = context;
        mPostList = postList;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        Data currentPost = mPostList.get(position);

        ImageView image = listItem.findViewById(R.id.post_image);
        TextView eventplaceanddate = listItem.findViewById(R.id.eventplaceanddate);
        // Use Glide or Picasso library to load the image from the URL
         Glide.with(mContext).load(currentPost.getImageurl()).into(image);
        // Picasso.get().load(currentPost.getImageUrl()).into(image);

        TextView text = listItem.findViewById(R.id.post_sub);
        text.setText(currentPost.getSubject());

        String fullText = currentPost.getEventplace()+" - "+DateConversion(currentPost.getEventstartdate());
        String targetText = DateConversion(currentPost.getEventstartdate());

        // Find the starting index of the target text in the full text
        int startIndex = fullText.indexOf(targetText);

        if (startIndex != -1) {

            eventplaceanddate.setText(fullText, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) eventplaceanddate.getText();

            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, startIndex + targetText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannable.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + targetText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data clickedPost = mPostList.get(position);
                Toast.makeText(mContext, position+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ViewPostActivity.class);
                //intent.putExtra("postdata", clickedPost);
                intent.putExtra("clickedPost",clickedPost);
                mContext.startActivity(intent);

            }
        });

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

}