package org.example;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientArrivalSystem implements Runnable{
    // this is the producer of the system
    // this produces the patients
    private final BlockingQueue<Patient> pediatricQueue;
    private final BlockingQueue<Patient> surgeryQueue;
    private final BlockingQueue<Patient> cardiologyQueue;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final Random random = new Random();
    private volatile boolean running = true;

    public PatientArrivalSystem( BlockingQueue<Patient> pediatricQueue,
                                 BlockingQueue<Patient> surgeryQueue,
                                 BlockingQueue<Patient> cardiologyQueue){
        this.pediatricQueue = pediatricQueue;
        this.surgeryQueue = surgeryQueue;
        this.cardiologyQueue = cardiologyQueue;
    }

    public void run(){
        while(running){
            try{
                Thread.sleep(500 + random.nextInt(1000)); //random arrivals

                String speciality = pickRandomSpeciality();
                Patient patient = new Patient(counter.incrementAndGet(),
                        System.currentTimeMillis(),
                        speciality);
                routePatient(patient);

                System.out.println("Patient "+ patient.getId() + " arrived for "+speciality);
            } catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
    public String pickRandomSpeciality(){
        int x = random.nextInt(3);
        if (x == 0 ) return "PEDIATRIC";
        if (x == 1) return "SURGERY";
        return "CARDIOLOGY";
    }
    public void routePatient(Patient patient) throws InterruptedException{
        if (patient.getSpecialty().equals("PEDIATRIC")){
            pediatricQueue.put(patient);
        } else if (patient.getSpecialty().equals("SURGERY")) {
            surgeryQueue.put(patient);
        } else if (patient.getSpecialty().equals("CARDIOLOGY")) {
            cardiologyQueue.put(patient);
        }
    }
    public void stop(){
        running = false;
    }

}
