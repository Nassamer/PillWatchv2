package com.example.pillwatch;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Database extends BaseObservable {
    String alarmState;
    String binState;
    String alarmTime;
    Long currentValue;
    Long baseValue;

    public Database(String alarmState, String binState, String alarmTime, Long currentValue, Long baseValue) {
        this.alarmState = alarmState;
        this.binState = binState;
        this.alarmTime = alarmTime;
        this.currentValue = currentValue;
        this.baseValue = baseValue;
    }

    public Database() {
    }

    @Bindable
    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
        notifyPropertyChanged(BR.alarmState);
    }

    @Bindable
    public String getBinState() {
        return binState;
    }

    public void setBinState(String binState) {
        this.binState = binState;
        notifyPropertyChanged(BR.binState);
    }

    @Bindable
    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
        notifyPropertyChanged(BR.alarmTime);
    }

    @Bindable
    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
        notifyPropertyChanged(BR.currentValue);
    }

    @Bindable
    public Long getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Long baseValue) {
        this.baseValue = baseValue;
        notifyPropertyChanged(BR.baseValue);
    }
}
