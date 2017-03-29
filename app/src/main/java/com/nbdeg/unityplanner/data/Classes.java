package com.nbdeg.unityplanner.data;

@SuppressWarnings("ALL")
public class Classes {
    private String name;
    private String teacher;
    private Long startDate;
    private Long endDate;
    private String roomNumber;
    private String buildingName;
    private String ID;
    private String description;
    private String section;

    // Empty Constructor
    public Classes() {
    }

    // Adding all info in one call
    public Classes(String name, String teacher, Long startDate, Long endDate, String roomNumber, String buildingName, String ID) {
        this.name = name;
        this.teacher = teacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
        this.buildingName = buildingName;
        this.ID = ID;
    }

    // Adding all common info in one call
    public Classes(String name, String teacher, Long startDate, Long endDate, String roomNumber, String buildingName) {
        this.name = name;
        this.teacher = teacher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
        this.buildingName = buildingName;
    }

    // Google Classroom intergration
    public Classes(String name, String teacher, Long startDate, String roomNumber, String description, String section) {
        this.name = name;
        this.teacher = teacher;
        this.startDate = startDate;
        this.roomNumber = roomNumber;
        this.description = description;
        this.section = section;
    }

    /* -- Getters and Setters -- */

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

    public Long getStartDate() {
        return startDate;
    }
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }
}


