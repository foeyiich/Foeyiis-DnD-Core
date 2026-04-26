package me.foeyii.fdndcore.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DnDLogger {
    private DnDLogger() {
        /* This utility class should not be instantiated */
    }

    private static final String PREFIX = "DnD Core";

    private static final Map<Class<?>, Logger> LOGGER_CACHE = new ConcurrentHashMap<>();

    public static Logger getLogger(Class<?> caller) {
        return LOGGER_CACHE.computeIfAbsent(caller, c ->
                LogManager.getLogger(PREFIX + "/" + c.getSimpleName())
        );
    }

    public static Logger getLogger() {
        return LogManager.getLogger(PREFIX);
    }

}
