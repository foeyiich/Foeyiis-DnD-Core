package me.foeyii.fdndcore.utility;

public class DnDUtils {

    private DnDUtils() {
    }

    public static int calculateModifier(int value, final int MAX_VALUE) {
        return Math.floorDiv((value - (MAX_VALUE / 2)), 2);
    }

}
