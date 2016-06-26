package com.intuit.autumn.utils;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VarArgsTest {

    @Test
    public void varArgWithSingleNullIsConsideredEmpty() {
        assertVarArgsConsideredEmpty(null);
    }

    @Test
    public void varArgWithMultipleNullsIsConsideredEmpty() {
        assertVarArgsConsideredEmpty(null, null);
        assertVarArgsConsideredEmpty(null, null, null);
    }

    @Test
    public void varArgWithNoArgumentIsConsideredEmpty() {
        assertVarArgsConsideredEmpty();
    }

    @Test
    public void varArgWithAtLeastOneNonNullElementIsConsideredNotEmpty() {
        assertVarArgsConsideredNotEmpty("present");
        assertVarArgsConsideredNotEmpty(null, "present");
        assertVarArgsConsideredNotEmpty("present", null);
    }

    //    @Ignore("fixme: not failing as expected, see PropertyFactoryTest")
    @Test(expected = InvocationTargetException.class)
    public void privateConstructor() throws Exception {
        Constructor<VarArgs> varArgsConstructor = VarArgs.class.getDeclaredConstructor();

        varArgsConstructor.setAccessible(true);

        varArgsConstructor.newInstance();
    }

    private void assertVarArgsConsideredEmpty(String... arg) {
        assertThat(VarArgs.isEmpty(arg), is(true));
    }

    private void assertVarArgsConsideredNotEmpty(String... arg) {
        assertThat(VarArgs.isEmpty(arg), is(false));
    }
}