package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsUnitTest {

    @Test
    public void convertPriceWithSuccess() {
        int dollarPrice = 1000;
        int euroPrice = 850;
        assertEquals(euroPrice, Utils.convertDollarToEuro(dollarPrice));
        assertEquals(dollarPrice, Utils.convertEuroToDollar(euroPrice));
    }

    @Test
    public void getTodayDateWithSuccess() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(new Date());
        assertEquals(today, Utils.getTodayDate());
    }
}