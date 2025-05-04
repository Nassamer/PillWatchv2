package com.example.pillwatch;

import android.content.Intent;
import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;

import com.example.pillwatch.Alarm;
import com.example.pillwatch.Direction;
import com.example.pillwatch.PillWatch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowIntent;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30, manifest = Config.NONE)
public class DirectionTest {

    private Direction activity;
    private Button databaseButton;
    private Button alarmButton;

    @Before
    public void setUp() {
        ActivityController<Direction> controller = Robolectric.buildActivity(Direction.class).create().start().resume();
        activity = controller.get();
        databaseButton = activity.findViewById(R.id.databaseDirectionButton);
        alarmButton = activity.findViewById(R.id.alarmDirectionButton);
    }

    @Test
    public void clickingDatabaseButton_shouldStartPillWatchActivity() {
        databaseButton.performClick();

        Intent expectedIntent = new Intent(activity, PillWatch.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();

        assertNotNull("Intent should be fired", actual);
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void clickingAlarmButton_shouldStartAlarmActivity() {
        alarmButton.performClick();

        Intent expectedIntent = new Intent(activity, Alarm.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();

        assertNotNull("Intent should be fired", actual);
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
