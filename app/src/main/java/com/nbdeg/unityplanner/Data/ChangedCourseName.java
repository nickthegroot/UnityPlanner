package com.nbdeg.unityplanner.Data;

/**
 * Class that stores old and new course names. Useful when adding new assignments from Classroom that
 * have had a name changed.
 */
public class ChangedCourseName {

    String oldCourseName;
    String newCourseName;

    /*
    Required empty constructor
     */
    public ChangedCourseName() {
    }

    public ChangedCourseName(String oldCourseName, String newCourseName) {
        this.oldCourseName = oldCourseName;
        this.newCourseName = newCourseName;
    }

    /* -- Getters and setters -- */

    public String getOldCourseName() {
        return oldCourseName;
    }
    public void setOldCourseName(String oldCourseName) {
        this.oldCourseName = oldCourseName;
    }

    public String getNewCourseName() {
        return newCourseName;
    }
    public void setNewCourseName(String newCourseName) {
        this.newCourseName = newCourseName;
    }
}
