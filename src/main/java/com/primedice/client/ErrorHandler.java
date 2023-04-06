package com.primedice.client;

import java.io.*;

public class ErrorHandler {
    public ErrorHandler(String message, Exception exception) {
        throwError(message, exception);
    }

    public ErrorHandler(Exception exception) {
        throwError("", exception);
    }

    public ErrorHandler(String message) {
        throwError(message, null);
    }

    private void throwError(String message, Exception exception) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File("error.log"),true));
            if (!message.equals("")) {
                pw.println(message);
            }
            if (exception != null) {
                exception.printStackTrace(pw);
            }
            pw.close();

            System.out.println(message);
        } catch (FileNotFoundException e) { }
    }
}