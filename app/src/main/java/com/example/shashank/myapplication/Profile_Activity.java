package com.example.shashank.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Profile_Activity extends AppCompatActivity {


    private static final int CHOOSE_IMAGE =101 ;
    ImageView profilepicture;
    EditText editTextName;
    Uri uriprofileimage;
    String ProfileImageUrl;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_);




        editTextName=findViewById(R.id.editTextName);
        profilepicture=findViewById(R.id.profilepicture);
        mAuth=FirebaseAuth.getInstance();



        final StorageReference profileimagerefrence= FirebaseStorage.getInstance().getReference();
        profileimagerefrence.child("profilepics/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .apply(RequestOptions.circleCropTransform())
                        .into(profilepicture);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });











        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();

            }
        });


        findViewById(R.id.savebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveuserinformation();

            }
        });
    }



    private void saveuserinformation() {

        final String name=editTextName.getText().toString().trim();

        if(name.isEmpty())
        {
            editTextName.setError("Name Required");
            editTextName.requestFocus();
            return;
        }
        FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(name);
        uploadimagetofirebasestorage();
        Toast.makeText(Profile_Activity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainHome_Activity.class));

    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            uriprofileimage=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uriprofileimage);


                //////
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);

                profilepicture.setImageBitmap(resized);

                /////



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadimagetofirebasestorage() {

        final StorageReference profileimagerefrence= FirebaseStorage.getInstance().getReference("profilepics/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
        if(uriprofileimage!=null)
        {
            profileimagerefrence.putFile(uriprofileimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                }
            })

            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


    }

    private void showImageChooser()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);
    }
}
