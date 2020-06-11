package com.example.shashank.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private EditText editTextName;
    private EditText editTextContact;
    private FirebaseAuth firebaseauth;


    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseauth.getCurrentUser()!=null)
        {
            // handle the already login
            finish();
            startActivity(new Intent(getApplicationContext(),MainHome_Activity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog=new ProgressDialog(this);

        firebaseauth= FirebaseAuth.getInstance();


        if(firebaseauth.getCurrentUser()!=null)
        {
            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),MainHome_Activity.class));

        }

        buttonRegister=findViewById(R.id.buttonRegister);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextName=findViewById(R.id.editTextName);
        editTextContact=findViewById(R.id.editTextContact);

        textViewSignin=findViewById((R.id.textViewSignin));

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


    }
    private void registerUser()
    {
      final String email =editTextEmail.getText().toString().trim();
      String password=editTextPassword.getText().toString().trim();
      final String name=editTextName.getText().toString().trim();
      final String contact=editTextContact.getText().toString().trim();

      if(TextUtils.isEmpty(email))
      {
          //email is empty
          Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
          return;
      }

      if(TextUtils.isEmpty(password))
      {
          // password is empty
          Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
          return;
      }

      if(TextUtils.isEmpty(name))
        {
            // password is empty
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(contact))
        {
            // password is empty
            Toast.makeText(this, "Please enter Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
      progressDialog.setMessage("Registering User...");
      progressDialog.show();

      firebaseauth.createUserWithEmailAndPassword(email,password)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isComplete()){
                              //start profile activity
                          progressDialog.hide();
                          User user=new User(name,email,contact);


                          FirebaseDatabase.getInstance().getReference("User")
                                  .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                  .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isComplete())
                                  {
                                      Toast.makeText(SignupActivity.this, "Registered Successfully ",Toast.LENGTH_LONG).show();
                                  }
                                  else
                                  {
                                      //faliure
                                  }
                              }
                          });
                          finish();
                          startActivity(new Intent(getApplicationContext(),MainHome_Activity.class));
                      }
                      else{
                          if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                              Toast.makeText(SignupActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();
                          }
                      }
                  }
              });

    }
    @Override
    public void onClick(View v) {

        if(v==buttonRegister)
        {
            registerUser();
        }
        if(v==textViewSignin)
        {
            //open login activity
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
