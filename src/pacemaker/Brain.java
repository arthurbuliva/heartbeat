/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacemaker;

/**
 * @author bulivaa <arthur.buliva@unon.org>
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class Brain
 Logs messages to file
 */
public class Brain
{

    public final String SEPARATOR = "@";

    /**
     * Initializes the class
     */
    public Brain()
    {
        //Start by creating the logApplicationMessage folder if it does not exist
        File logFolder = new File("log");
        if (!logFolder.exists())
        {
            logFolder.mkdir();
        }
    }

    /**
     * Logs system events to a file
     * @param message The message to be logged
     */
    public static void logApplicationMessage(String message)
    {
        FileWriter fstream;
        BufferedWriter writer = null;

        try
        {
            fstream = new FileWriter(getLogFileName(), true);
            writer = new BufferedWriter(fstream);
            writer.write(getTimeStamp() + " " + message);
            writer.newLine();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Brain.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Brain.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    /**
     * Logs the stack trace of an exception
     * @param exception The exception to log the stack trace of
     */
    public static void logExceptionStackTrace(Exception exception)
    {
        String exceptionStacktrace = "";

        StackTraceElement[] ex = exception.getStackTrace();

        for (StackTraceElement e : ex)
        {
            exceptionStacktrace += ("at " + e.getClassName() + ".");
            exceptionStacktrace += (e.getMethodName());
            exceptionStacktrace += ("(");
            exceptionStacktrace += (e.getFileName());
            exceptionStacktrace += (":");
            exceptionStacktrace += (e.getLineNumber());
            exceptionStacktrace += (")");
            exceptionStacktrace += ("\n");
        }

        logApplicationMessage("[ System Error ]" + "\r\n"
                + exception.toString() + "\r\n" + exceptionStacktrace);
    }

    /**
     * Gets the current system time
     * @return The time in the form yyyy-MM-dd HH:mm:ss
     */
    private static String getTimeStamp()
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date());
    }

    /**
     * Gets the name of the logApplicationMessage file
     * @return The logApplicationMessage file in the form yyyy-MM-dd.logApplicationMessage
     */
    public static String getLogFileName()
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String date = f.format(new Date());
        String fileName = "log/" + date + ".log";

        File logFile = new File("log");
        if(!logFile.exists())
        {
            logFile.mkdirs();
        }

        return fileName;
    }
}

