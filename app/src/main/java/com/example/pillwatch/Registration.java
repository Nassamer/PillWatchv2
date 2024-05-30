package com.example.pillwatch;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registration extends AppCompatActivity {

    EditText createEmail, createPassword;
    Button registrationButton;

    //Autentikáció
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    //Firebase kapcsolat
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationButton = findViewById(R.id.registrationButton);
        createEmail = findViewById(R.id.registrationEmail);
        createPassword = findViewById(R.id.registrationPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

                //Checking if the current user is already logged in
                if(firebaseUser != null){

                }else{
                    //The west HAS fallen.....
                }
            }
        };

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(createEmail.getText()) && !TextUtils.isEmpty(createPassword.getText())){
                    CreateUserWithEmail(createEmail.getText().toString().trim(), createPassword.getText().toString().trim());
                }
                else{
                    Toast.makeText(Registration.this, "Kérjük minden mezőt töltsön ki!", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void CreateUserWithEmail(String email, String password){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this, "Sikeres regisztráció", Toast.LENGTH_LONG);
                    }
                }
            });
        }
    }
}