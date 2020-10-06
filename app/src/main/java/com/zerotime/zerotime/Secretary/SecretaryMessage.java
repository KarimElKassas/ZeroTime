package com.zerotime.zerotime.Secretary;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zerotime.zerotime.Interfaces.ApiService;
import com.zerotime.zerotime.MyBroadCast;
import com.zerotime.zerotime.No_Internet_Connection;
import com.zerotime.zerotime.Notifications.Client;
import com.zerotime.zerotime.R;
import com.zerotime.zerotime.Secretary.Adapters.SecretaryMessageAdapter;
import com.zerotime.zerotime.Secretary.Pojos.SecretaryChatPojo;
import com.zerotime.zerotime.databinding.SecretaryActivityMessageBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class SecretaryMessage extends AppCompatActivity {
    private static final int GALLERY_PICK = 0;
    ApiService apiService;
    SharedPreferences prefs;
    SecretaryMessageAdapter adapter;
    List<SecretaryChatPojo> secretaryChatPojoList;
    DatabaseReference chatRef;
    ValueEventListener seenListener;
    String userId, intentFrom;
    private SecretaryActivityMessageBinding binding;

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecretaryActivityMessageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Check Internet State
        if (!haveNetworkConnection()) {
            Intent i = new Intent(SecretaryMessage.this, No_Internet_Connection.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        checkInternetConnection();
        //-----------------------------------

        apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);

        binding.nestedScroll.fullScroll(View.FOCUS_DOWN);
        binding.secretaryMessageRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        binding.secretaryMessageRecycler.setLayoutManager(linearLayoutManager);
        binding.secretaryMessageRecycler.setNestedScrollingEnabled(false);

        if (getIntent().getStringExtra("UniqueID") != null) {
            if (Objects.requireNonNull(getIntent().getStringExtra("UniqueID")).equals("DisplayChatsAdapter")) {
                intentFrom = "DisplayChatsAdapter";
                userId = getIntent().getStringExtra("UserID");

            }
        }
        prefs = getSharedPreferences("UserState", MODE_PRIVATE);
        binding.secretaryMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.secretaryMessageWriteMSGEdt.getText().toString();
                if (!msg.equals("")) {
                    if (intentFrom != null) {
                        sendMessage("Zero Time", userId, msg);
                    }


                } else
                    Toast.makeText(SecretaryMessage.this, "You Cant't Sent Empty Message!!", Toast.LENGTH_SHORT).show();
                binding.secretaryMessageWriteMSGEdt.setText("");
            }
        });
        binding.secretaryMessageSendImage.setOnClickListener(view1 -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
        });
        ReadMessages();

        seenMessage(userId);
    }

    private void seenMessage(final String userid) {
        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SecretaryChatPojo chat = snapshot.getValue(SecretaryChatPojo.class);
                    assert chat != null;
                    if (chat.getReceiver().equals("Zero Time") && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String Sender, String Receiver, String Message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Sender", Sender);
        hashMap.put("Receiver", Receiver);
        hashMap.put("Message", Message);
        hashMap.put("isSeen", false);
        hashMap.put("Type", "Text");
        reference.child("Chats").push().setValue(hashMap);

    }

    private void ReadMessages() {
        secretaryChatPojoList = new ArrayList<>();
        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                secretaryChatPojoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SecretaryChatPojo mSecretaryChatPojo = dataSnapshot.getValue(SecretaryChatPojo.class);
                    if (mSecretaryChatPojo != null && (mSecretaryChatPojo.getReceiver().equals("Zero Time") && mSecretaryChatPojo.getSender().equals(userId) ||
                            mSecretaryChatPojo.getReceiver().equals(userId) && mSecretaryChatPojo.getSender().equals("Zero Time"))) {
                        secretaryChatPojoList.add(mSecretaryChatPojo);

                    }

                }

                adapter = new SecretaryMessageAdapter(SecretaryMessage.this, secretaryChatPojoList);
                binding.secretaryMessageRecycler.setAdapter(adapter);
                binding.secretaryMessageRecycler.smoothScrollToPosition(adapter.getItemCount() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(Objects.requireNonNull(userId))
                .child("Zero Time");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("Receiver_ID").setValue("Zero Time");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child("Zero Time")
                .child(userId);
        chatRefReceiver.child("Receiver_ID").setValue(userId);

    }

    @Override
    protected void onPause() {
        super.onPause();
        chatRef.removeEventListener(seenListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            assert data != null;
            Uri imageUri = data.getData();
            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText("جارى رفع الصورة ...");

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            DatabaseReference userMessagePush = FirebaseDatabase.getInstance()
                    .getReference("Messages").child("Zero Time").child(userId).push();
            final String pushID = userMessagePush.getKey();

            StorageReference filePath = FirebaseStorage.getInstance().getReference("Messages")
                    .child("MessageImages").child(pushID + ".jpg");

            assert imageUri != null;
            //compressImage(imageUri.toString());

            filePath.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseStorage.getInstance().getReference("Messages")
                            .child("MessageImages").child(pushID + ".jpg")
                            .getDownloadUrl().addOnSuccessListener(uri -> {

                        String downloadUrl = uri.toString();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Sender", "Zero Time");
                        hashMap.put("Receiver", userId);
                        hashMap.put("Message", downloadUrl);
                        hashMap.put("isSeen", false);
                        hashMap.put("Type", "Image");
                        chatRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    pDialog.cancel();
                                    Toasty.success(getApplicationContext(),"تم رفع الصورة بنجاح",Toasty.LENGTH_SHORT,true).show();
                                }else {
                                    pDialog.cancel();
                                    Toasty.error(getApplicationContext(),"لقد حدث خطأ ما",Toasty.LENGTH_SHORT,true).show();
                                }
                            }
                        });

                    });
                }
            });
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void checkInternetConnection() {
        MyBroadCast broadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadCast, intentFilter);
    }
}