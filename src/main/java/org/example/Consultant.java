package org.example;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consultant implements Runnable{
    // this is the consumer class of the project
    private final int id;
    private final String speciality;
    private final BlockingQueue<Patient> queue;
    private volatile boolean onShift = true;
    private int patientsServed;
    private Random random;

    public Consultant(int id, String speciality, BlockingQueue<Patient> queue){
        this.id = id;
        this.speciality = speciality;
        this.queue =queue;
        this.patientsServed = 0;
        this.random = new Random();
    }
    @Override
    public void run(){
        try{
            while (onShift || !queue.isEmpty()){
                Patient patient = queue.take(); //get the patient from the queue

                System.out.println("Consultant "+ id + " with "+ speciality+ " treating patient "+ patient.getId());

                int treatmentTime = 1000 + random.nextInt(2000); //simulate a tratment time of 1 to 3 s
                Thread.sleep(treatmentTime);

                patientsServed++;
                System.out.println("Consultant "+ id + " finished treating patient "+ patient.getId());


            }
        } catch (Exception e){
            System.out.println("Consultant "+id+ " shift ended. patients served "+ patientsServed);
        }
    }
    public void stopShift(){
        onShift = false;
    }
    public int getPatientsServed(){
        return patientsServed;
    }
    public int getId(){
        return id;
    }
    public String getSpeciality(){
        return speciality;
    }
}
