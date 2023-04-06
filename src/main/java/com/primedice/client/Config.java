package com.primedice.client;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class Config extends Client {
    public static final String API_KEY = "API_KEY";
    public static final String ROLLS_TO_SHOW_ON_PANEL = "ROLLS_TO_SHOW_ON_PANEL";
    public static final String ROLL_DELAY = "ROLL_DELAY";
    public static final String NEW_SEED_AFTER_NONCES = "NEW_SEED_AFTER_NONCES";
    public static final String BASEBET = "BASEBET";
    public static final String TARGET = "TARGET";
    public static final String CONDITION = "CONDITION";
    public static final String SHIFT_CONDITION_AFTER_ROLLS = "SHIFT_CONDITION_AFTER_ROLLS";
    public static final String INCREASE_BASEBET_ON_WIN = "INCREASE_BASEBET_ON_WIN";
    public static final String INCREASE_BASEBET_ON_LOSS = "INCREASE_BASEBET_ON_LOSS";
    public static final String RESET_TO_BASEBET_ON_WIN = "RESET_TO_BASEBET_ON_WIN";
    public static final String RESET_TO_BASEBET_ON_LOSS = "RESET_TO_BASEBET_ON_LOSS";
    public static final String STOP_AFTER_WIN_COUNT = "STOP_AFTER_WIN_COUNT";
    public static final String STOP_AFTER_LOSS_COUNT = "STOP_AFTER_LOSS_COUNT";
    public static final String STOP_AFTER_TOTAL_PROFIT = "STOP_AFTER_TOTAL_PROFIT";
    public static final String STOP_AFTER_TOTAL_LOSS = "STOP_AFTER_TOTAL_LOSS";
    public static final String RESET_TO_BASEBET_AFTER_WIN_COUNT = "RESET_TO_BASEBET_AFTER_WIN_COUNT";
    public static final String RESET_TO_BASEBET_AFTER_LOSS_COUNT = "RESET_TO_BASEBET_AFTER_LOSS_COUNT";
    public static final String MIN_BET_RESET = "MIN_BET_RESET";
    public static final String MAX_BET_RESET = "MAX_BET_RESET";
    public static final String MIN_BALANCE = "MIN_BALANCE";
    public static final String MAX_BALANCE = "MAX_BALANCE";
    public static final String STOP_AFTER_ROLLS = "STOP_AFTER_ROLLS";
    public static final String PREROLLS = "PREROLLS";
    public static final String SCRIPT = "SCRIPT";

    public Config(String file) {
        // Create empty config object and set default vvalues
        for (Field f : Config.class.getDeclaredFields()) {
            String defaultValue = "0";
            switch (f.getName()) {
                case API_KEY: defaultValue = ""; break;
                case ROLLS_TO_SHOW_ON_PANEL: defaultValue = "10"; break;
                case ROLL_DELAY: defaultValue = "1000"; break;
                case NEW_SEED_AFTER_NONCES: defaultValue = "50"; break;
                case SCRIPT: defaultValue = ""; break;
                case BASEBET: defaultValue = "1"; break;
                case TARGET: defaultValue = "50"; break;
                case CONDITION: defaultValue = "<"; break;

            }

            configValues.add(new ConfigValue(f.getName(), defaultValue));
        }

        String errorMessage = "";
        int rowCount = 0;
        try {
            FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            // Set config values from config file
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (!strLine.equals("")) {
                    rowCount++;

                    String[] split = strLine.split("=");
                    String key = split[0];
                    String value = split[1];

                    setValue(key, value);
                }
            }
        } catch (FileNotFoundException e) {
            errorMessage = "CONFIGFILE: '" + file + "' DOESNT EXISTS! Type: java -jar PrimeDiceBOT.jar <ConfigFile>";
        } catch (IOException e) {
            errorMessage = "SOMETHING IS WRONG WITH CONFIGFILE: '" + file + "' (Row " + rowCount + ")";
        } catch (ArrayIndexOutOfBoundsException e) {
            errorMessage = "SOMETHING IS WRONG WITH CONFIGFILE: '" + file + "' (Row " + rowCount + ")";
        }

        if (!errorMessage.equals("")) {
            new ErrorHandler(errorMessage);
            quit();
        }
    }

    public static String getValue(String key) {
        for (ConfigValue configValue : configValues) {
            if (configValue.getKey().equals(key)) {
                return configValue.getValue();
            }
        }

        return "";
    }

    public static void setValue(String key, String value) {
        for (int i = 0; i < configValues.size(); i++) {
            if (configValues.get(i).getKey().equals(key)) {
                configValues.get(i).setValue(value);
            }
        }
    }

    public static List<ConfigValue> getAllConfigValues() {
        return configValues;
    }

    public static void writeNewConfigFile() {
        try {
            PrintWriter writer = new PrintWriter("settings.conf", "UTF-8");
            for (ConfigValue configValue : configValues) {
                writer.println(configValue.getKey() + "=" + configValue.getValue());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}