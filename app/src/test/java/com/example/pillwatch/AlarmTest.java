package com.example.pillwatch;

import android.content.Intent;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 31, manifest = Config.NONE)
public class AlarmTest {

    private Alarm activity;

    @Before
    public void setUp() {
        // Mock FirebaseDatabase and DatabaseReference to avoid actual calls
        FirebaseDatabase mockDatabase = Mockito.mock(FirebaseDatabase.class);
        DatabaseReference mockRef = Mockito.mock(DatabaseReference.class);
        Mockito.when(mockDatabase.getReference(Mockito.anyString())).thenReturn(mockRef);

        try (MockedStatic<FirebaseDatabase> mockedStatic = Mockito.mockStatic(FirebaseDatabase.class)) {
            mockedStatic.when(FirebaseDatabase::getInstance).thenReturn(mockDatabase);

            activity = Robolectric.buildActivity(Alarm.class)
                    .create()
                    .start()
                    .resume()
                    .get();
        }
    }

    @Test
    public void activityIsNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void alarmSwitch_initializesCorrectly() {
        Switch alarmSwitch = activity.findViewById(R.id.alarmSwitch);
        assertNotNull(alarmSwitch);
    }

    @Test
    public void timePicker_is24HourMode() {
        TimePicker timePicker = activity.findViewById(R.id.timePicker);
        assertTrue(timePicker.is24HourView());
    }

    @Test
    public void binSelector_populatedCorrectly() {
        Spinner binSelector = activity.findViewById(R.id.binSelector);
        assertEquals(21, binSelector.getAdapter().getCount());
    }
}