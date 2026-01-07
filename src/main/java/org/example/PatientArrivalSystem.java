package org.example;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientArrivalSystem implements Runnable {

    private final BlockingQueue<Patient> paediatricQueue;
    private final BlockingQueue<Patient> surgeryQueue;
    private final BlockingQueue<Patient> cardiologyQueue;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final Random random = new Random();
    private volatile boolean running = true;

    public PatientArrivalSystem(
            BlockingQueue<Patient> paediatricQueue,
            BlockingQueue<Patient> surgeryQueue,
            BlockingQueue<Patient> cardiologyQueue
    ) {
        this.paediatricQueue = paediatricQueue;
        this.surgeryQueue = surgeryQueue;
        this.cardiologyQueue = cardiologyQueue;
    }

    @Override
    public void run() {
        System.out.println("Patient arrival generator started.");

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500 + random.nextInt(1000)); // 500ms to 1500ms

                Speciality speciality = Speciality.random(random);
                Patient patient = new Patient(counter.incrementAndGet(), System.currentTimeMillis(), speciality);

                routePatient(patient);

                System.out.println("Patient " + patient.getId() + " arrived for " + speciality);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // keep interrupt status
                break;
            }
        }

        System.out.println("Patient arrival generator stopped. Total patients: " + counter.get());
    }

    private void routePatient(Patient patient) throws InterruptedException {
        switch (patient.getSpeciality()) {
            case PAEDIATRIC -> paediatricQueue.put(patient);
            case SURGERY -> surgeryQueue.put(patient);
            case CARDIOLOGY -> cardiologyQueue.put(patient);
        }
    }

    public void stop() {
        running = false;
    }
}
