package com.example.pillwatch;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)  // Replace with your target SDK version
public class LoginTest {

    private Login loginActivity;

    @Mock
    FirebaseAuth mockAuth;

    @Mock
    Task<AuthResult> mockTask;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use Robolectric to create the activity
        loginActivity = Robolectric.buildActivity(Login.class).create().get();

        // Inject mock FirebaseAuth
        loginActivity.firebaseAuth = mockAuth;
    }

    @Test
    public void login_withValidCredentials_callsFirebase() {
        String email = "test@gmail.com";
        String password = "123456789";

        // Simulate Firebase sign-in
        when(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);
        when(mockTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
            invocation.<com.google.android.gms.tasks.OnSuccessListener<AuthResult>>getArgument(0).onSuccess(mock(AuthResult.class));
            return mockTask;
        });

        // Act
        loginActivity.logEmailPass(email, password);

        // Assert
        verify(mockAuth).signInWithEmailAndPassword(email, password);
    }
}