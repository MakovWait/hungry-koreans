package com.mewmakovs.gameobjects;

import java.util.Comparator;

public class ObjectComparator implements Comparator<Scrollable> {

    @Override
    public int compare(Scrollable a, Scrollable b) {
        float A = 0;
        float B = 0;
        if(a.getID() == 12) {
            A = a.getY();
        }else {
            A = a.getBotPointY();
        }
        if (b.getID() == 12) {
            B = b.getY();
        } else {
            B = b.getBotPointY();
        }

        if (A > B)

            return 1;
        else if(A < B)
            return -1;
        else
            return 0;
    }
}