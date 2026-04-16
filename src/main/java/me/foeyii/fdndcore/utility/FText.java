package me.foeyii.fdndcore.utility;

public class FText {

    private static final String PREFIX = format("&8[&6FDnD&8] ");

    public static String format(String msg) {
        return msg.replace('&', '§');
    }

    public static String formatPrefixed(String msg) {
        return PREFIX + format(msg);
    }

}
