package org.example;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consultant implements Runnable {

    private final int id;
    private final Speciality speciality;
    private final BlockingQueue<Patient> queue; // Shared resource used by both producer and consumer threads

    private final Random random = new Random();
    private volatile boolean onShift = false;  // Volatile ensures visibility of shift stop signal across threads

    private int patientsServed = 0;
//Consultant is the CONSUMER in the Producer–Consumer pattern.
    public Consultant(int id, Speciality speciality, BlockingQueue<Patient> queue) {
        this.id = id;
        this.speciality = speciality;
        this.queue = queue;
    }

    /** Called by ShiftManager right before thread starts */
    public void beginShift() {
        onShift = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // If shift ended, stop accepting NEW patients (leave remaining for next shift)
                if (!onShift) break;

                // poll with timeout so we can re-check onShift frequently
                Patient patient = queue.poll(250, TimeUnit.MILLISECONDS);
                if (patient == null) continue;

                System.out.println("Consultant " + id + " (" + speciality + ") treating patient " + patient.getId());

                int treatmentTime = 1000 + random.nextInt(2000); // 1 to 3 seconds
                Thread.sleep(treatmentTime);

                patientsServed++;
                System.out.println("Consultant " + id + " (" + speciality + ") finished patient " + patient.getId());
            }
        } catch (InterruptedException e) {
            // if interrupted, finish thread gracefully
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Consultant " + id + " (" + speciality + ") shift ended. Patients served: " + patientsServed);
        }
    }

    public void stopShift() {
        onShift = false;
    }

    public int getPatientsServed() {
        return patientsServed;
    }

    public int getId() {
        return id;
    }

    public Speciality getSpeciality() {
        return speciality;
    }
}
