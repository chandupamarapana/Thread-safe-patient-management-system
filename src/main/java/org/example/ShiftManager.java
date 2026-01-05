package org.example;

import java.util.List;

public class ShiftManager {
    private final List<Consultant> dayShiftConsultant;
    private final List<Consultant> nightShiftConsultant;
    private final long shiftDurationMs;

    private boolean isDayShift = true;
    private int shiftNumber =0 ;

    private Thread[] currentThreads;

    public ShiftManager(List<Consultant> dayShiftConsultant, List<Consultant> nightShiftConsultant, Long shiftDurationMs ){
        this.dayShiftConsultant = dayShiftConsultant;
        this.nightShiftConsultant = nightShiftConsultant;
        this.shiftDurationMs = shiftDurationMs;

    }
    //start the continuous shift rotation
    public void start() throws InterruptedException{
        startShift(dayShiftConsultant, "DAY");

        while(true){
            Thread.sleep(shiftDurationMs);
            switchSwift();
        }
    }
    //starts the shift by creating the Consultants threads
    public void startShift (List<Consultant> consultants, String shiftType){
        shiftNumber ++;
        System.out.println();
        System.out.println("Starting the " + shiftType + " Shift number "+ shiftNumber + ".");
        System.out.println();

        currentThreads = new Thread[consultants.size()];

        for(int i=0; i<consultants.size(); i++){
            currentThreads[i] = new Thread(consultants.get(i));
            currentThreads[i].start();
        }
    }

    //the methods stop the current shift and starts the next one
    public void switchSwift() throws InterruptedException {
        endCurrentShift();

        isDayShift = !isDayShift;

        if (isDayShift) {
            startShift(dayShiftConsultant, "DAY");
        } else {
            startShift(nightShiftConsultant, "NIGHT");
        }
    }
    //signalling consultants to finish work and wait for them
    public void endCurrentShift() throws InterruptedException{
        System.out.println(" Ending the current shift");

        List<Consultant> currentConsultants = isDayShift ? dayShiftConsultant : nightShiftConsultant ;

        //signalling consultants to stop accepting patients
        for (Consultant consultant : currentConsultants){
            consultant.stopShift();
        }
        //wait for all the consultants to finish
        for (Thread t : currentThreads){
            t.join();
        }
        System.out.println("All the consultants have stopped there work ");
    }
}
