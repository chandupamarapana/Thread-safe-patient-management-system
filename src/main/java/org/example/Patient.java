package org.example;

import java.time.LocalDateTime;

public class Patient {
    private final int id;
    private final  long arrivalTime;
    private final String specialty;

    public Patient (int id, long arrivalTime, String specialty){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public String getSpecialty() {
        return specialty;
    }

}
