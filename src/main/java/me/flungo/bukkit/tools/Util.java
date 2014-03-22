/*
 * Copyright (C) 2014 Fabrizio Lungo <fab@lungo.co.uk> - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fabrizio Lungo <fab@lungo.co.uk>, March 2014
 */
package me.flungo.bukkit.tools;

/**
 *
 * @author Fabrizio Lungo <fab@lungo.co.uk>
 */
public class Util {

    public static final long TIME_SECOND = 1000;
    public static final long TIME_MINUTE = TIME_SECOND * 60;
    public static final long TIME_HOUR = TIME_MINUTE * 60;
    public static final long TIME_DAY = TIME_HOUR * 24;
    public static final long TIME_YEAR = TIME_DAY * 365;

    public static String formatTime(long time) {
        if (time <= 0) {
            return "now";
        } else if (time < TIME_SECOND) {
            return time + "ms";
        } else if (time < TIME_MINUTE) {
            return (time / TIME_SECOND) + "s";
        } else if (time < TIME_HOUR) {
            return (time / TIME_MINUTE) + "m " + formatTime(time % TIME_MINUTE);
        } else if (time < TIME_DAY) {
            return (time / TIME_HOUR) + "h " + formatTime(time % TIME_HOUR);
        } else if (time < TIME_YEAR) {
            return (time / TIME_DAY) + "d " + formatTime(time % TIME_DAY);
        } else {
            return (time / TIME_YEAR) + "y " + formatTime(time % TIME_YEAR);
        }
    }
}
