package com.helloworldapps.campusbuzz;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "SaveToFirebase";
    private EditText subject, message,link,location,fees;
    private TextView startDate,endDate,regendDate;
    private Uri imageUri;
    private String imageUrl,selectedValue;
    private Task<Uri> filedownloadurl;
    private String sub1, msg1, link1, sDate, eDate,regDate,loc,fee;
    private Button submitButton, fileupload;
    private Data data;
    private ImageView imageView;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PICK_PDF_FILES = 2;
    private DatabaseReference databaseReference;
    private NotificationManager notificationManager;
    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    private static final String NOTIFICATION_CHANNEL_NAME = "Firebase Notification Channel";
    private List<Uri> selectedFiles = new ArrayList<>();
    private List<String> DownloadedUrls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FirebaseApp.initializeApp(this);

        databaseReference = FirebaseDatabase.getInstance("https://campus-buzz-7528e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("data");


        submitButton = findViewById(R.id.submitButton);

        imageView = findViewById(R.id.imageView);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        regendDate = findViewById(R.id.regendDate);
        location = findViewById(R.id.location);
        fees = findViewById(R.id.fees);

        Spinner spinner = findViewById(R.id.spinner);

        // Create a list of items for the Spinner
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Choose Event Place");
        spinnerItems.add("Online");
        spinnerItems.add("MCA Dept");
        spinnerItems.add("Mini Seminar Hall");
        spinnerItems.add("Prahallad Hall");

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerItems
        );

        // Set the dropdown layout style for the Spinner
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the ArrayAdapter to the Spinner
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item here (e.g., display a Toast)
                if (position == 0) {
                    selectedValue = spinnerItems.get(position);
                } else {
                    selectedValue = spinnerItems.get(position);
                    Toast.makeText(PostActivity.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostActivity.this, "Posting...", Toast.LENGTH_SHORT).show();
                saveData();
            }
        });


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String formattedMonth = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

                        startDate.setText(formattedDay + "/" + formattedMonth + "/" + year);

                    }
                },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String formattedMonth = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

                        endDate.setText(formattedDay + "/" + formattedMonth + "/" + year);

                    }
                },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        regendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String formattedDay = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String formattedMonth = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

                        regendDate.setText(formattedDay + "/" + formattedMonth + "/" + year);

                    }
                },
                        year, month, day);
                datePickerDialog.show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery();
            }
        });
    }

    private void selectImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uploadImagesToStorage(imageUri);
        }

        if (requestCode == REQUEST_CODE_PICK_PDF_FILES && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        selectedFiles.add(uri);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    selectedFiles.add(uri);
                }

                for (Uri pdfUri : selectedFiles) {
                    String filePath = pdfUri.toString();
                    Toast.makeText(this, "Selected PDF: " + filePath, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void saveData() {

        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        link = findViewById(R.id.link);

        sub1 = subject.getText().toString();
        msg1 = message.getText().toString();
        sDate = startDate.getText().toString();
        eDate = endDate.getText().toString();
        link1 = link.getText().toString();
        fee = fees.getText().toString();
        loc = location.getText().toString();
        regDate = regendDate.getText().toString();

        // Reference to the root node of the Firebase Realtime Database
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://campus-buzz-7528e-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("data");

        // Reference to the child node where the data will be stored
        DatabaseReference dataRef = rootRef;

        // Create a unique key for the new data
        String key = dataRef.push().getKey();


        /*
        Task<Uri> uploadTask1 = uploadfilesToStorage(imageUri);
        uploadTask1.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri imageUri) {
                imageUrl = imageUri.toString();
            }
        });


        ArrayList<Task<Uri>> uploadTasks = new ArrayList<>();
        for (int i = 0; i < selectedFiles.size(); i++) {
            Uri fileUri = selectedFiles.get(i);
            //String fileName = dataRef.getKey() + "_image_" + i + ".jpg"; // Unique file name
            Task<Uri> uploadTask = uploadfilesToStorage(fileUri);
            uploadTasks.add(uploadTask);
        }

        // Wait for all image uploads to complete
        Task<List<Uri>> allUploads = Tasks.whenAllSuccess(uploadTasks);

        allUploads.addOnSuccessListener(new OnSuccessListener<List<Uri>>() {
            @Override
            public void onSuccess(List<Uri> fileUrls) {

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String currentDate = sdf1.format(new Date());

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String currentTime = sdf2.format(new Date());

                for (Uri uri : fileUrls) {
                    DownloadedUrls.add(uri.toString());
                }

                if (!sub1.isEmpty() && !msg1.isEmpty() && !fileUrls.isEmpty() && !sDate.isEmpty() && !eDate.isEmpty() && isValidUrl(link1)) {
                    // Create a new object to store the data
                    Data data = new Data(sub1, msg1, DownloadedUrls, currentDate, currentTime, sDate, eDate, link1,imageUrl);

                    // Update the data in the Firebase Realtime Database
                    dataRef.child(key).setValue(data);


                    Log.d(TAG, "Message Published!");
                    Toast.makeText(PostActivity.this, "Message Published!", Toast.LENGTH_SHORT).show();
                    dosomework();
                    finish();
                } else {
                    Toast.makeText(PostActivity.this, "Provide proper Date!", Toast.LENGTH_SHORT).show();
                }
            }
        });

*/

        // Reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique child name for the file
        String fileName = key + ".jpg";
        StorageReference imageRef = storageRef.child("images").child(fileName);

        UploadTask uploadTask = imageRef.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String currentDate = sdf1.format(new Date());

                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String currentTime = sdf2.format(new Date());


                    if (!sub1.isEmpty() && !msg1.isEmpty() && !imageUrl.isEmpty() && !sDate.isEmpty() && !eDate.isEmpty() && isValidUrl(link1) && !selectedValue.isEmpty() && !regDate.isEmpty() && !loc.isEmpty() && !fee.isEmpty()) {

                        // Create a new object to store the data
                        data = new Data(sub1, msg1, imageUrl, currentDate, currentTime,sDate,eDate,link1,selectedValue,regDate,loc,fee);

                        // Add the data to the Firebase Realtime Database
                        dataRef.child(key).setValue(data);

                        Log.d(TAG, "Message Published!");
                        Toast.makeText(PostActivity.this, "Message Published!", Toast.LENGTH_SHORT).show();
                        dosomework();
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "Provide proper Date!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PostActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void dosomework()
    {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
                if (dataSnapshot.exists()) {
                    //triggerLocalNotification();
                }
            }

            @Override
            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onChildRemoved( DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    private void triggerLocalNotification()
    {
        System.out.println("Entered trigger method"+"======================================");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("New Data!")
                .setContentText("New data has been added to Firebase.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        Intent intent = new Intent(this, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        notificationBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void createNotificationChannel()
    {
            System.out.println("Create Notification channel method"+"======================================");

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("Firebase Notification Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    public boolean isValidUrl(String url) {
        if (url.isEmpty()) {
            return false;
        }
        boolean isUrlValid = Patterns.WEB_URL.matcher(url).matches();

        return isUrlValid;
    }

    private Task<Uri> uploadfilesToStorage(Uri filesUri)
    {
           FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference imageRef = storageRef.child("files/" + System.currentTimeMillis() + "." + getFileExtension(filesUri));

            UploadTask uploadTask = imageRef.putFile(imageUri);

            // Return a Task that resolves with the download URL when the upload is complete
            return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageRef.getDownloadUrl();
                }
            });
        }

    private Task<Uri> uploadImagesToStorage(Uri imageUri)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique child name for the file
        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Return a Task that resolves with the download URL when the upload is complete
        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}