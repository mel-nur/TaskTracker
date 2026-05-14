package com.mel.TaskTracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    TODO("todo", "Yapılacak"),
    IN_PROGRESS("in-progress", "Devam Ediyor"),
    DONE("done", "Tamamlandı");

    private final String value;
    private final String displayName;

    TaskStatus (String value, String displayName){
        this.value = value;
        this.displayName = displayName;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        for(TaskStatus status : values()){
            if(status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(
                "Geçersiz durum: '" + value + "'. Geçerli değerler: todo, in-progress, done"
        );
    }


}
