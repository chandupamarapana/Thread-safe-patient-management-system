package org.example;

public class Patient {
    private final int id;
    private final long arrivalTime;
    private final Speciality speciality;

    public Patient(int id, long arrivalTime, Speciality speciality) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public Speciality getSpeciality() {
        return speciality;
    }
}
