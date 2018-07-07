package sk.stuba.fiit.vava.util;

import org.apache.log4j.Logger;

import javax.validation.constraints.NotNull;

/**
 * Application utility functions
 */
public class Utilities {

    public static void logPostRequest(@NotNull String className, @NotNull String route, String message) {
        Logger.getLogger(className).debug(
                "POST " + route + " --> " + message);
    }

    public static void logPostResponse(@NotNull String className, @NotNull String route, String message) {
        Logger.getLogger(className).debug(
                "POST " + route + " <-- " + message);
    }
}
