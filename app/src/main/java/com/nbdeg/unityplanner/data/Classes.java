package com.nbdeg.unityplanner.data;

public class Classes {
    private String className;
    private String classTeacher;
    private String startDate;
    private String endDate;
    private int roomNumber;
    private String buildingName;

    // Empty Constructor
    public Classes() {
    }

    // Adding all info in one call
    public Classes(String className, String classTeacher, String startDate, String endDate, int roomNumber, String buildingName) {
        this.className = className;
        this.classTeacher = classTeacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
        this.buildingName = buildingName;
    }

    /* --- Setters --- */

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }

    public void setClassStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setClassEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setClassRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setClassBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }


    /* --- Getters --- */

    public String getClassName() {
        return this.className;
    }

    public String getClassTeacher() {
        return this.classTeacher;
    }

    public String getClassStartDate() {
        return this.startDate;
    }

    public String setClassEndDate() {
        return this.endDate;
    }

    public int setClassRoomNumber() {
        return this.roomNumber;
    }

    public String setClassBuildingName() {
        return this.buildingName;
    }
}


