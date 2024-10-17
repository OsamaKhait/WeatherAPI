package com.example.weatherapi;

import junit.framework.TestCase;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class WeatherTest extends TestCase {

    public void testGetTempShouldGet12() {
        double expectedTemp = 12;
        Weather weather = new Weather(12, 7,3,5);
        weather.setTemp(expectedTemp);

        double actualTemp = weather.getTemp();

        assertEquals(expectedTemp, actualTemp);
    }


}
