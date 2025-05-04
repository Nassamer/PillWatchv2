package com.example.pillwatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;

@LargeTest
@RunWith(AndroidJUnit4.class)
@Config(sdk = 30, manifest = Config.NONE)
public class RegistrationTest {

    @Rule
    public ActivityScenarioRule<Registration> activityRule =
            new ActivityScenarioRule<>(Registration.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testUIElementsVisible() {
        onView(withId(R.id.registrationEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.registrationPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.registrationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.goBackToLoginFromRegistrationBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationToLoginActivity() {
        onView(withId(R.id.goBackToLoginFromRegistrationBtn)).perform(click());
        intended(hasComponent(Login.class.getName()));
    }

    @Test
    public void testEmptyFieldPreventsRegistration() {
        onView(withId(R.id.registrationEmail)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.registrationPassword)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.registrationButton)).perform(click());
    }

    @Test
    public void testValidRegistrationTriggersFirebaseCall() {
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        Task<AuthResult> mockTask = Mockito.mock(Task.class);

        Mockito.when(mockTask.isSuccessful()).thenReturn(true);
        Mockito.when(mockAuth.createUserWithEmailAndPassword(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockTask);

        onView(withId(R.id.registrationEmail)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.registrationPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.registrationButton)).perform(click());

        intended(hasComponent(Direction.class.getName()));
    }
}