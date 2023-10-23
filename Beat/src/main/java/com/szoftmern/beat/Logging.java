package com.szoftmern.beat;

import java.io.IOException;
import java.util.logging.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Logging {

    //Logging Implementation
    private static final Logger javafxLogger = Logger.getLogger("");

    //Function to start logging
    public static void startLogging() throws IOException {

        //Init time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        //Writeout to console
        System.out.println("Logging Started!");

        //Set the level of logging
        javafxLogger.setLevel(Level.INFO);

        FileHandler fh;

        //Set up critical and normal logging file
        try {
            fh = new FileHandler("Log/log_"+dtf.format(now)+".log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Set the formatting
        fh.setFormatter(new SimpleFormatter());

        //Add logging handler to javafx
        javafxLogger.addHandler(fh);
    }
}
