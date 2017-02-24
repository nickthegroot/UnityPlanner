package com.nbdeg.unityplanner.data;

@SuppressWarnings("ALL")
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
    public String getClassEndDate() {
        return this.endDate;
    }
    public int getClassRoomNumber() {
        return this.roomNumber;
    }
    public String setClassBuildingName() {
        return this.buildingName;
    }
}


