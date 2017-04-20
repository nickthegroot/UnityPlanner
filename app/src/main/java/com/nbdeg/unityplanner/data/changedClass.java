package com.nbdeg.unityplanner.data;

/**
 * Created by nbdeg on 4/19/2017.
 */

public class changedClass {
    String originalName;
    String newName;

    public changedClass() {
    }

    public changedClass(String originalName, String newName) {
        this.originalName = originalName;
        this.newName = newName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
