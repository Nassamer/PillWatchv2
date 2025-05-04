package com.example.pillwatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static java.util.regex.Pattern.matches;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 30, manifest = Config.NONE)
public class PillWatchTest {

    @Rule
    public ActivityScenarioRule<PillWatch> activityScenarioRule =
            new ActivityScenarioRule<>(PillWatch.class);


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGoBackButton_navigatesToDirection() {
        Intents.init();

        onView(withId(R.id.goBackToDirFromPillWatchBtn)).perform(click());
        intended(hasComponent(Direction.class.getName()));

        Intents.release();
    }

    // This test simulates the adapter receiving a new list of items
    @Test
    public void testRecyclerView_updatesData() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            List<Database> testData = new ArrayList<>();
            Database testEntry = new Database("ON", "FULL", "08:00", 100L, 50L);
            testEntry.setDayOfWeek("Hétfő");
            testEntry.setPeriod("Reggel");
            testData.add(testEntry);

            DatabaseAdapter adapter = new DatabaseAdapter(activity, (ArrayList<Database>) testData);
            activity.recyclerView.setAdapter(adapter);

            assertEquals(1, adapter.getItemCount());
        });
    }
}