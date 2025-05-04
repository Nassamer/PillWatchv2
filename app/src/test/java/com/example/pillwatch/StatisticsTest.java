package com.example.pillwatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.S}, manifest = Config.NONE)
public class StatisticsTest {

    private Statistics activity;
    private StatisticsDbHelper mockDbHelper;

    @Before
    public void setUp() {
        // Mock the DB helper
        mockDbHelper = Mockito.mock(StatisticsDbHelper.class);

        // Mock the DB return values
        MatrixCursor mockCursor = new MatrixCursor(new String[]{"alarmState", "binState", "alarmTime", "currentValue", "baseValue"});
        mockCursor.addRow(new Object[]{"ON", "FULL", "08:00", 5L, 5L});

        Mockito.when(mockDbHelper.getAllStats()).thenReturn(mockCursor);
        Mockito.when(mockDbHelper.getFullCountForWeek(0)).thenReturn(10);
        Mockito.when(mockDbHelper.getFullCountForWeek(-1)).thenReturn(5);
        Mockito.when(mockDbHelper.getFullChangePercentageWeekOverWeek()).thenReturn(100f);

        // Inject mock via shadowing
        activity = Robolectric.buildActivity(Statistics.class)
                .create()
                .get();
    }

    @Test
    public void testRecyclerViewIsPopulated() {
        RecyclerView recyclerView = activity.findViewById(R.id.statisticsRecyclerView);
        assertNotNull("RecyclerView should not be null", recyclerView);
        assertTrue("RecyclerView should have adapter set", recyclerView.getAdapter() != null);
    }

    @Test
    public void testWeeklyComparisonTextDisplayed() {
        TextView textView = activity.findViewById(R.id.weeklyComparisonText);
        String expected = "Több teljes kuka volt ezen a héten (+ 100.0%).";
        assertEquals(expected, textView.getText().toString());
    }

    @Test
    public void testGoBackButtonNavigatesToAlarm() {
        Button backButton = activity.findViewById(R.id.goBackToAlarmFromStatisticsBtn);
        backButton.performClick();

        Intent expectedIntent = new Intent(activity, Alarm.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();

        assertNotNull("Expected intent to start Alarm activity", actual);
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @After
    public void tearDown() {
        activity.finish();
    }
}