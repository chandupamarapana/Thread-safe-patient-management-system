package org.example;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HospitalSimulation {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Patient> peadiactricQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Patient> surgeryQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Patient> cardiologyQueue = new LinkedBlockingQueue<>();

        PatientArrivalSystem arrivalSystem = new PatientArrivalSystem(peadiactricQueue, surgeryQueue, cardiologyQueue);
        Thread arrivalThread = new Thread(arrivalSystem);
        arrivalThread.start();
//        new Thread(arrivalSystem).start();

        List<Consultant> dayShift = List.of(
                new Consultant(1, "PAEDIATRIC",peadiactricQueue),
                new Consultant(2,"SURGERY", surgeryQueue),
                new Consultant(3,"CARDIOLOGY",cardiologyQueue)
        );
        List<Consultant> nightShift = List.of(
                new Consultant(1, "PAEDIATRIC",peadiactricQueue),
                new Consultant(2,"SURGERY", surgeryQueue),
                new Consultant(3,"CARDIOLOGY",cardiologyQueue)
        );
        long shiftDurationMs = 12000; //simulate 12 seconds per shift
        int totalShifts = 2; // simulate a 2 shift system
        ShiftManager manager = new ShiftManager(dayShift,nightShift,shiftDurationMs, totalShifts);
        manager.start();

        //stop patient arrivals after simulation completes
        arrivalSystem.stop();
        arrivalThread.interrupt();

        try{
            arrivalThread.join();
        } catch (InterruptedException e) {
            System.out.println("Error waiting for arrival thread. ");
        }
        System.out.println();
        System.out.println("__________________________________________");
        System.out.println("            Simulation completed          ");
        System.out.println("Summary of each consultant queues ");
        System.out.println("__________________________________");
        int dayShiftTotal = 0;
        for (Consultant consultant : dayShift){
            int served = consultant.getPatientsServed();
            dayShiftTotal = dayShiftTotal+ served;
            System.out.println("Consultant "+ consultant.getId() + "( "+ consultant.getSpeciality() + "): "+ served + " patients");
        }
        System.out.println("Day shift total: "+ dayShiftTotal+ " patients");
        System.out.println();

        int nightShiftTotal = 0;
        for (Consultant consultant: nightShift){
            int served = consultant.getPatientsServed();
            nightShiftTotal = nightShiftTotal + served;
            System.out.println("Consultant "+ consultant.getId() + "( "+ consultant.getSpeciality() + "): "+ served + " patients");
        }
        System.out.println("Night shift toal: "+ nightShiftTotal+ " patients");
        System.out.println();

        System.out.println("Remaining patients in paediatric queue: "+ peadiactricQueue.size());
        System.out.println("Remaining patients in surgery queue: "+ surgeryQueue.size());
        System.out.println("Remaining patients in Cardiology queue: "+ cardiologyQueue.size());
        System.out.println("___________________________________________");


    }

}
