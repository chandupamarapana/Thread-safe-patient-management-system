package org.example;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HospitalSimulation {
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Patient> paediatricQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Patient> surgeryQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Patient> cardiologyQueue = new LinkedBlockingQueue<>();

        PatientArrivalSystem arrivalSystem = new PatientArrivalSystem(paediatricQueue, surgeryQueue, cardiologyQueue);
        Thread arrivalThread = new Thread(arrivalSystem, "PatientArrivalThread");
        arrivalThread.start();

        List<Consultant> dayShift = List.of(
                new Consultant(1, Speciality.PAEDIATRIC, paediatricQueue),
                new Consultant(2, Speciality.SURGERY, surgeryQueue),
                new Consultant(3, Speciality.CARDIOLOGY, cardiologyQueue)
        );

        List<Consultant> nightShift = List.of(
                new Consultant(4, Speciality.PAEDIATRIC, paediatricQueue),
                new Consultant(5, Speciality.SURGERY, surgeryQueue),
                new Consultant(6, Speciality.CARDIOLOGY, cardiologyQueue)
        );

        long shiftDurationMs = 12000; // 12 seconds
        int totalShifts = 2;

        ShiftManager manager = new ShiftManager(dayShift, nightShift, shiftDurationMs, totalShifts);
        manager.start();

        // Stop arrivals after simulation
        arrivalSystem.stop();
        arrivalThread.interrupt();
        arrivalThread.join();

        System.out.println("\n__________________________________________");
        System.out.println("            Simulation completed          ");
        System.out.println("__________________________________________");

        int dayTotal = 0;
        for (Consultant c : dayShift) {
            dayTotal += c.getPatientsServed();
            System.out.println("Day Consultant " + c.getId() + " (" + c.getSpeciality() + "): " + c.getPatientsServed());
        }
        System.out.println("Day shift total: " + dayTotal);

        System.out.println();

        int nightTotal = 0;
        for (Consultant c : nightShift) {
            nightTotal += c.getPatientsServed();
            System.out.println("Night Consultant " + c.getId() + " (" + c.getSpeciality() + "): " + c.getPatientsServed());
        }
        System.out.println("Night shift total: " + nightTotal);

        System.out.println();
        System.out.println("Remaining in paediatric queue: " + paediatricQueue.size());
        System.out.println("Remaining in surgery queue: " + surgeryQueue.size());
        System.out.println("Remaining in cardiology queue: " + cardiologyQueue.size());
        System.out.println("__________________________________________\n");
    }
}
