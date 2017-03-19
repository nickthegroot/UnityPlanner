package com.nbdeg.unityplanner.data;

@SuppressWarnings("ALL")
public class Classes {
    private String className;
    private String classTeacher;
    private Long startDate;
    private Long endDate;
    private int roomNumber;
    private String buildingName;

    // Empty Constructor
    public Classes() {
    }

    // Adding all info in one call
    public Classes(String className, String classTeacher, Long startDate, Long endDate, int roomNumber, String buildingName) {
        this.className = className;
        this.classTeacher = classTeacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
        this.buildingName = buildingName;
    }

    /* --- Getters --- */

    public String getClassName() {
        return this.className;
    }
    public String getClassTeacher() {
        return this.classTeacher;
    }
    public Long getClassStartDate() {
        return this.startDate;
    }
    public Long getClassEndDate() {
        return this.endDate;
    }
    public int getClassRoomNumber() {
        return this.roomNumber;
    }
    public String setClassBuildingName() {
        return this.buildingName;
    }

    /* --- Setters -- */

    public void setClassName(String className) {
        this.className = className;
    }
    public void setClassTeacher(String classTeacher) {
        this.classTeacher = classTeacher;
    }
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}


