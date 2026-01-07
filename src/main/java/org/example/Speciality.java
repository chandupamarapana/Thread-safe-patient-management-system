package org.example;

import java.util.Random;

public enum Speciality {
    PAEDIATRIC, SURGERY, CARDIOLOGY;

    public static Speciality random(Random r) {
        return values()[r.nextInt(values().length)];
    }
}
