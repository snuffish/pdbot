package com.primedice.client;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static final String VERSION = "Alpha 0.1";
    public static final String PRIMEDICE_API_URL = "https://api.primedice.com/api/";

    public static String configFile = "settings.conf";

    private static Boolean configuredProperly = false;

    public static RenderScreen renderScreen;
    private static Scanner reader;

    private static Bet bet = new Bet();
    public static Player player;

    public static Config config;
    public static BetManager betManager;
    public static PlayerManager playerManager;
    public static List<ConfigValue> configValues;
    public static List<Bet> betHistory;

    public static void restart() {
        try {
            System.out.println("Press (1) to Retry, (2) to Continue, (3) to Quit");

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            String[] list = {configFile};
            switch (option) {
                case 1: main(list); break;
                case 2: break;
                case 3: quit(); break;
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }

    public static void quit() {
        try {
            System.out.println("Press any key to quit..");

            Scanner scanner = new Scanner(System.in);
            int feelKeyStroke = scanner.nextInt();
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }

    public static void init(String configFile) {
        betHistory = new ArrayList<Bet>();

        configValues = new ArrayList<ConfigValue>();
        config = new Config(configFile);
        betManager = new BetManager();

        try {

            String API_KEY = config.getValue(config.API_KEY);
            if (!API_KEY.equals("")) {
                Double basebet = Double.parseDouble(config.getValue(config.BASEBET));
                Double target = Double.parseDouble(config.getValue(config.TARGET));
                String condition = config.getValue(config.CONDITION);

                player = new Player(API_KEY, basebet, target, condition);
                playerManager = new PlayerManager(player);

                renderScreen = new RenderScreen();
                reader = new Scanner(System.in);

                configuredProperly = true;
            } else {
                new ErrorHandler("No API_KEY has been set!");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            configFile = args[0];
        } catch (ArrayIndexOutOfBoundsException e) { }

        init(configFile);

        if (configuredProperly) {
            if (!config.getValue(config.SCRIPT).equals("")) {
                ScriptEngineManager factory = new ScriptEngineManager();
                ScriptEngine engine = factory.getEngineByName("JavaScript");
                try {
                    ScriptObject scriptObject = new ScriptObject();
                    engine.put("ScriptObject", scriptObject);

                    String scriptFile = config.getValue(config.SCRIPT);
                    FileInputStream fstream = new FileInputStream(scriptFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

                    String scriptCode = "";
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        scriptCode += strLine;
                    }

                    engine.eval(scriptCode);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ScriptException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    init(configFile);
                }
            } else {
                while (player.isBetting()) {
                    try {
                        bet = new Bet(player.getBet(), player.getTarget(), player.getCondition());
                        bet = betManager.createBet(bet);

                        if (bet != null) {
                            player = playerManager.updatePlayerObject(bet);

                            renderScreen.refresh(player);
                        }

                        Thread.sleep(Long.parseLong(config.getValue(config.ROLL_DELAY)));
                    } catch (InterruptedException e) {
                        player.stopBetting();
                        new ErrorHandler(e);
                    } catch (NullPointerException e) {
                        player.stopBetting();
                        new ErrorHandler(e);
                    }
                }
            }
        } else {
            new ErrorHandler("Not configured properly!");
        }
    }
}