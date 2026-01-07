package org.example;

import java.util.List;

public class ShiftManager {
    private final List<Consultant> dayShiftConsultants;
    private final List<Consultant> nightShiftConsultants;
    private final long shiftDurationMs;
    private final int maxShifts;

    private boolean isDayShift = true;
    private int shiftNumber = 0;

    private Thread[] currentThreads;

    public ShiftManager(List<Consultant> dayShiftConsultants,
                        List<Consultant> nightShiftConsultants,
                        long shiftDurationMs,
                        int maxShifts) {
        this.dayShiftConsultants = dayShiftConsultants;
        this.nightShiftConsultants = nightShiftConsultants;
        this.shiftDurationMs = shiftDurationMs;
        this.maxShifts = maxShifts;
    }

    public void start() throws InterruptedException {
        for (int i = 0; i < maxShifts; i++) {

            List<Consultant> consultants = isDayShift ? dayShiftConsultants : nightShiftConsultants;
            String type = isDayShift ? "DAY" : "NIGHT";

            startShift(consultants, type);

            Thread.sleep(shiftDurationMs);

            endCurrentShift();

            isDayShift = !isDayShift;
        }

        System.out.println("Simulation completed.");
        System.out.println("Total shifts completed: " + maxShifts);
    }


    private void startShift(List<Consultant> consultants, String shiftType) {
        shiftNumber++;
        System.out.println("\nStarting " + shiftType + " shift #" + shiftNumber + "\n");

        currentThreads = new Thread[consultants.size()];

        for (int i = 0; i < consultants.size(); i++) {
            Consultant c = consultants.get(i);
            c.beginShift(); // IMPORTANT: reset flag for this shift
            Thread t = new Thread(c, shiftType + "-Consultant-" + c.getId());
            currentThreads[i] = t;
            t.start();
        }
    }

    private void switchShift() throws InterruptedException {
        endCurrentShift();

        isDayShift = !isDayShift;

        if (isDayShift) startShift(dayShiftConsultants, "DAY");
        else startShift(nightShiftConsultants, "NIGHT");
    }

    private void endCurrentShift() throws InterruptedException {
        System.out.println("Ending current shift...");

        List<Consultant> currentConsultants = isDayShift ? dayShiftConsultants : nightShiftConsultants;

        for (Consultant c : currentConsultants) {
            c.stopShift();
        }

        for (Thread t : currentThreads) {
            t.join(); // will not hang now
        }

        System.out.println("All consultants stopped.\n");
    }
}
