package com.example.pillwatch;

import androidx.databinding.Observable;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class DatabaseTest extends TestCase {
    private Database database;

    @Before
    public void setUp() {
        database = new Database();
    }

    @Test
    public void testSetAndGetAlarmState() {
        database.setAlarmState("ON");
        assertEquals("ON", database.getAlarmState());
    }

    @Test
    public void testSetAndGetBinState() {
        database.setBinState("Full");
        assertEquals("Full", database.getBinState());
    }

    @Test
    public void testSetAndGetAlarmTime() {
        database.setAlarmTime("08:00");
        assertEquals("08:00", database.getAlarmTime());
    }

    @Test
    public void testSetAndGetCurrentValue() {
        database.setCurrentValue(100L);
        assertEquals(Long.valueOf(100), database.getCurrentValue());
    }

    @Test
    public void testSetAndGetBaseValue() {
        database.setBaseValue(50L);
        assertEquals(Long.valueOf(50), database.getBaseValue());
    }

    @Test
    public void testSetAndGetDayOfWeek() {
        database.setDayOfWeek("Hétfő");
        assertEquals("Hétfő", database.getDayOfWeek());
    }

    @Test
    public void testSetAndGetPeriod() {
        database.setPeriod("Reggel");
        assertEquals("Reggel", database.getPeriod());
    }

    @Test
    public void testPropertyChangeNotifications() {
        final boolean[] notified = {false};

        database.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                notified[0] = true;
            }
        });

        database.setAlarmState("OFF");
        assertTrue("Property change should have triggered", notified[0]);
    }
}