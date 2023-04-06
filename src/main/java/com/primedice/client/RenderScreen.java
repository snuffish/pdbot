package com.primedice.client;

import com.bethecoder.ascii_table.ASCIITable;
import org.apache.commons.math3.util.Precision;

public class RenderScreen extends Client {
    private static int ROLLS_TO_SHOW = 10;

    public RenderScreen() {
        ROLLS_TO_SHOW = Integer.parseInt(config.getValue(config.ROLLS_TO_SHOW_ON_PANEL));
    }

    public void refresh(Player player) {
        // Clear panel screen
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Player stats
        System.out.println("[" + VERSION + "] " + player.getUsername().toUpperCase());
        String[] userdataHeader = { "BALANCE", "BETS", "WINS", "LOSSES", "PROFIT", "WAGERED", "CLIENT SEED / NONCE"};
        String[][] userdata = {
                { Double.toString(Precision.round(player.getUserBalance(), 2)), Integer.toString(player.getUserBets()), Integer.toString(player.getUserWins()), Integer.toString(player.getUserLosses()), Double.toString(Precision.round(player.getUserProfit(), 2)), Double.toString(Precision.round(player.getUserWagered(), 2)), player.getClientSeed() + " / " + player.getNonceSeed() }
        };

        ASCIITable.getInstance().printTable(userdataHeader, userdata);

        // Player rolls
        System.out.printf("LATEST " + ROLLS_TO_SHOW + " ROLLS YOU'VE MADE\n");
        String[] rollsHeader = { "BET", "TARGET", "MULTIPLIER", "ROLL", "WIN?", "PROFIT" };
        String[][] rollsData = new String[ROLLS_TO_SHOW][0];

        Bet bet;
        for (int i = 1; i <= ROLLS_TO_SHOW; i++) {
            try {
                bet = betHistory.get(betHistory.size() - i);
                String win = "NO";
                if (bet.getWin()) {
                    win = "YES";
                }

                String[] row = new String[]{Double.toString(Precision.round(bet.getAmount(), 2)), bet.getCondition() + " " + Double.toString(Precision.round(bet.getTarget(), 2)), Double.toString(Precision.round(bet.getMultiplier(), 2)), Double.toString(Precision.round(bet.getRoll(), 2)), win, Double.toString(Precision.round(bet.getProfit(), 2))};
                rollsData[i - 1] = row;
            } catch (ArrayIndexOutOfBoundsException e) { }
        }

        ASCIITable.getInstance().printTable(rollsHeader, rollsData);

        // Info about this session
        System.out.println("SESSION OVERVIEW");
        String[] sessionHeader = { "Bets", "Wins", "Losses", "Profit", "Wagered", "Winning streak", "Loosing streak" };

        String[][] sessionData = {
                { Integer.toString(player.getRolls()), Integer.toString(player.getWins()), Integer.toString(player.getLosses()), Double.toString(Precision.round(player.getProfit(), 2)), Double.toString(Precision.round(player.getWagered(), 2)), Integer.toString(player.getWinningStreak()), Integer.toString(player.getLoosingStreak()) }
        };

        ASCIITable.getInstance().printTable(sessionHeader, sessionData);
    }
}