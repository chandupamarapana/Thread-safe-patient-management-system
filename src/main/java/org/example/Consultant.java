package org.example;

import java.util.concurrent.BlockingQueue;

public class Consultant implements Runnable{
    private final int id;
    private final String speciality;
    private final BlockingQueue<Patient> queue;
    private volatile boolean onShift = true;

    public Consultant(int id, String speciality, BlockingQueue<Patient> queue){
        this.id = id;
        this.speciality = speciality;
        this.queue =queue;
    }
    @Override
    public void run(){
        try{
            while (onShift || !queue.isEmpty()){
                Patient patient = queue.take();

                System.out.println("Consultant "+ id + " with "+ speciality+ " treating patient "+ patient.getId());

                Thread.sleep(2000); //simulate the 2 hour treatment

                System.out.println("Consultant "+ id + " finished treating patient "+ patient.getId());

                patient.getId();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void stopShift(){
        onShift = false;
    }
}
