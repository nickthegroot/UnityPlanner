package com.nbdeg.unityplanner.Data;

import com.google.api.services.calendar.model.Event;

import java.io.Serializable;

public class Time implements Serializable {
    private Event calEvent;
    private boolean isBlockSchedule;
    private boolean isADay;
    private boolean isBDay;
    private boolean isDaySchedule;

    private String days;
    private Long startLong;
    private Long endLong;
    private Long finish;

    /**
     * Required empty constructor
     */
    public Time() {
    }

    /**
     * Google Calendar Constructor
     * @param calEvent Google Calendar Event
     * @param isBlockSchedule true if event is block schedule
     * @param isADay true if event lies on A days
     * @param isBDay true if event lies on B days
     * @param isDaySchedule true if event is day schedule
     * @param days days event falls on
     * @param startLong millisecond value of start
     * @param endLong milliseconds value of end
     * @param finish milliseconds value of when event finishes repetition
     */
    public Time(Event calEvent, boolean isBlockSchedule, boolean isADay, boolean isBDay, boolean isDaySchedule, String days, Long startLong, Long endLong, Long finish) {
        this.calEvent = calEvent;
        this.isBlockSchedule = isBlockSchedule;
        this.isADay = isADay;
        this.isBDay = isBDay;
        this.isDaySchedule = isDaySchedule;
        this.days = days;
        this.startLong = startLong;
        this.endLong = endLong;
        this.finish = finish;
    }

    /**
     * Google Classroom Constructor
     * @param startLong millisecond value of when event starts
     */
    public Time(Long startLong) {
        this.startLong = startLong;
    }

    /**
     * Intent constructor
     * This constructor is used for passing on infomation from CourseAddTime.java to CreateCourse.java
     * @param isBlockSchedule true if event is block schedule
     * @param isADay true if event lies on A days
     * @param isBDay true if event lies on B days
     * @param isDaySchedule true if event is day schedule
     * @param days Days event lies on, used in conjunction with day schedules.
     * @param startLong millisecond value of start
     * @param endLong milliseconds value of end
     * @param finish milliseconds value of when event finishes repetition
     */
    public Time(boolean isBlockSchedule, boolean isADay, boolean isBDay, boolean isDaySchedule, String days, Long startLong, Long endLong, Long finish) {
        this.isBlockSchedule = isBlockSchedule;
        this.isADay = isADay;
        this.isBDay = isBDay;
        this.isDaySchedule = isDaySchedule;
        this.days = days;
        this.startLong = startLong;
        this.endLong = endLong;
        this.finish = finish;
    }

    public Event getCalEvent() {
        return calEvent;
    }
    public void setCalEvent(Event calEvent) {
        this.calEvent = calEvent;
    }

    public boolean isBlockSchedule() {
        return isBlockSchedule;
    }
    public void setBlockSchedule(boolean blockSchedule) {
        isBlockSchedule = blockSchedule;
    }

    public boolean isADay() {
        return isADay;
    }
    public void setADay(boolean ADay) {
        isADay = ADay;
    }

    public boolean isBDay() {
        return isBDay;
    }
    public void setBDay(boolean BDay) {
        isBDay = BDay;
    }

    public boolean isDaySchedule() {
        return isDaySchedule;
    }
    public void setDaySchedule(boolean daySchedule) {
        isDaySchedule = daySchedule;
    }

    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }

    public Long getStartLong() {
        return startLong;
    }
    public void setStartLong(Long startLong) {
        this.startLong = startLong;
    }

    public Long getEndLong() {
        return endLong;
    }
    public void setEndLong(Long endLong) {
        this.endLong = endLong;
    }

    public Long getFinish() {
        return finish;
    }
    public void setFinish(Long finish) {
        this.finish = finish;
    }
}
