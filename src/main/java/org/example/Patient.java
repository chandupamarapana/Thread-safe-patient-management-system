package org.example;

import java.time.LocalDateTime;

public class Patient {
    private final int id;
    private final  LocalDateTime arrivalTime;
    private final Specialty specialty;

    public Patient (int id, LocalDateTime arrivalTime, Specialty specialty){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

}
