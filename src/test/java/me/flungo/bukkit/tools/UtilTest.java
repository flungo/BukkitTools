/*
 * Copyright (C) 2014 Fabrizio Lungo <fab@lungo.co.uk> - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fabrizio Lungo <fab@lungo.co.uk>, March 2014
 */
package me.flungo.bukkit.tools;

import junit.framework.TestCase;

/**
 *
 * @author Fabrizio Lungo <fab@lungo.co.uk>
 */
public class UtilTest extends TestCase {

    public UtilTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_0() {
        System.out.println("formatTime time=0");
        long time = 0L;
        String expResult = "now";
        String result = Util.formatTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_MS() {
        System.out.println("formatTime time=526");
        long time = 529L;
        String expResult = "529ms";
        String result = Util.formatTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_S() {
        System.out.println("formatTime time=1526");
        long time = 1529L;
        String expResult = "1s";
        String result = Util.formatTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_M() {
        System.out.println("formatTime time=61526");
        long time = 61529L;
        String expResult = "1m 1s";
        String result = Util.formatTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_H() {
        System.out.println("formatTime time=3661529");
        long time = 3661529L;
        String expResult = "1h 1m 1s";
        String result = Util.formatTime(time);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_D() {
        System.out.println("formatTime time=90061529");
        long time = 90061529L;
        String expResult = "1d 1h 1m 1s";
        String result = Util.formatTime(time);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of formatTime method, of class Util.
     */
    public void testFormatTime_Y() {
        System.out.println("formatTime time=31626061529");
        long time = 31626061529L;
        String expResult = "1y 1d 1h 1m 1s";
        String result = Util.formatTime(time);
        assertEquals(expResult, result);
    }

}
