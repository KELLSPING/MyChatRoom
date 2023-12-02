package com.example.mychatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychatroom.databinding.ActivityAuthenticationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";
    ActivityAuthenticationBinding binding;
    String name, email, password;

    DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        binding.login.setOnClickListener(v -> {
            Log.d(TAG, "login onClick");
            email = binding.email.getText().toString();
            password = binding.password.getText().toString();
            login();
        });

        binding.signUp.setOnClickListener(v -> {
            Log.d(TAG, "signUp onClick");
            name = binding.name.getText().toString();
            email = binding.email.getText().toString();
            password = binding.password.getText().toString();
            signUp();
        });
    }

    private void signUp() {
        Toast.makeText(this, "SignUp", Toast.LENGTH_SHORT).show();
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(authResult -> {
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updateProfile(userProfileChangeRequest);
                    UserModel userModel = new UserModel(FirebaseAuth.getInstance().getUid(), name, email, password);



                    databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e(TAG, "Data could not be saved. " + databaseError.getMessage());
                            } else {
                                Log.d(TAG, "Data saved successfully.");
                            }
                        }
                    });
                    binding.progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                    finish();
                });
    }

    private void login() {
        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(authResult -> {
                    binding.progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                    finish();
                });
    }

}