package com.nbdeg.unityplanner.Data;

public class Course {

    private String name;
    private String teacher;
    private String roomNumber;
    private String description;
    private Time time;
    private int color;
    private String ID;
    private com.google.api.services.classroom.model.Course classroomCourse;

    /**
     * Empty Constructor
     */
    public Course() {
    }


    /**
     * Constructor for Google Classroom assignments
     * @param name Name of class
     * @param teacher Full name of teacher
     * @param roomNumber Room number of classroom
     * @param description Description of class
     * @param classroomCourse Google Classroom course
     */
    public Course(String name, String teacher, Time time, String roomNumber, String description, com.google.api.services.classroom.model.Course classroomCourse, int color) {
        this.name = name;
        this.teacher = teacher;
        this.time = time;
        this.roomNumber = roomNumber;
        this.description = description;
        this.classroomCourse = classroomCourse;
        this.color = color;
    }


    /* -- Getters and setters -- */

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public com.google.api.services.classroom.model.Course getClassroomCourse() {
        return classroomCourse;
    }
    public void setClassroomCourse(com.google.api.services.classroom.model.Course classroomCourse) {
        this.classroomCourse = classroomCourse;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
}
