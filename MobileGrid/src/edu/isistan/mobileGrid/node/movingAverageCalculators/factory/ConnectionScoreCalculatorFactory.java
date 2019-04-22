package edu.isistan.mobileGrid.node.movingAverageCalculators.factory;

import edu.isistan.mobileGrid.node.ConnectionScoreCalculator;

import java.lang.reflect.InvocationTargetException;

public class ConnectionScoreCalculatorFactory {
    public static <T extends ConnectionScoreCalculator> T createConnectionScoreCalculator(Class<T> clazz, String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (args != null) ? clazz.getConstructor(String.class).newInstance((Object[])args) : clazz.getConstructor().newInstance();
    }

}