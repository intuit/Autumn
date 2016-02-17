package com.intuit.data.autumn.utils;

/**
 * A utility class to test whether or not the provided argument list is comprised of only null values
 */

public class VarArgs {

    private VarArgs() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if a given vararg is empty, in the sense of containing only null values.
     *
     * @param args vararg to check
     * @return true if empty, else false
     */

    public static boolean isEmpty(final Object... args) {
        if (args == null) {
            return true;
        }

        for (Object arg : args) {
            if (arg != null) {
                return false;
            }
        }

        return true;
    }
}