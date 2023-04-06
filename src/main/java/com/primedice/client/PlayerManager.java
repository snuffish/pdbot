package com.primedice.client;

import java.io.*;
import java.net.*;
import java.util.Random;

public class PlayerManager extends Client {
    private Player player;

    public PlayerManager(Player player) {
        this.player = player;
    }

    public void newSeed() {
        try {
            Random random = new Random();
            String randNr = "";
            for (int i = 0; i < 13; i++) {
                randNr += Integer.toString(random.nextInt(9));
            }
            long seed = Long.parseLong(randNr);

            String payload = "seed=" + seed;

            URL url = new URL(PRIMEDICE_API_URL + "seed?api_key=" + player.getAPI_KEY());
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            OutputStream os = connection.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
            pw.write(payload);
            pw.close();

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
        } catch (MalformedURLException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player updatePlayerObject(Bet bet) {
        // Add to betHistory
        betHistory.add(bet);

        // Player rolled the dice
        player.rolledDice();

        // Add to players session wagered
        player.setWagered(player.getWagered() + bet.getAmount());

        // Shift condition after rolls
        if (Integer.parseInt(config.getValue(config.SHIFT_CONDITION_AFTER_ROLLS)) != 0) {
            player.increaseShiftConditionCounter();

            if (player.getShiftConditionCount() == Integer.parseInt(config.getValue(config.SHIFT_CONDITION_AFTER_ROLLS))) {
                if (player.getCondition().equals("<")) {
                    player.setCondition(">");
                } else if (player.getCondition().equals(">")) {
                    player.setCondition("<");
                }

                player.resetShiftConditionCounter();
            }
        }

        // Manage Win/losses for the player
        if (bet.getWin() != null) {
            if (bet.getWin()) {
                player.increaseWins();
                player.increaseWinCount();
                player.increaseWinningStreakCount();
                player.setBet(player.getBet() * (Double.parseDouble(config.getValue(config.INCREASE_BASEBET_ON_WIN)) == 0 ? 1 : Double.parseDouble(config.getValue(config.INCREASE_BASEBET_ON_WIN))));

                if (player.getLoosingStreak() < player.getLoosingStreakCount()) {
                    player.setLoosingStreak(player.getLoosingStreakCount());
                }

                if (config.getValue(config.RESET_TO_BASEBET_ON_WIN).equals("1")) {
                    player.setBet(Double.parseDouble(config.getValue(config.BASEBET)));
                    player.resetLossCount();
                }

                player.resetLoosingStreakCount();
            } else {
                player.increaseLosses();
                player.increaseLossCount();
                player.increaseLoosingStreakCount();
                player.setBet(player.getBet() * (Double.parseDouble(config.getValue(config.INCREASE_BASEBET_ON_LOSS)) == 0 ? 1 : Double.parseDouble(config.getValue(config.INCREASE_BASEBET_ON_LOSS))));

                if (player.getWinningStreak() < player.getWinningStreakCount()) {
                    player.setWinningStreak(player.getWinningStreakCount());
                }

                if (config.getValue(config.RESET_TO_BASEBET_ON_LOSS).equals("1")) {
                    player.setBet(Double.parseDouble(config.getValue(config.BASEBET)));
                    player.resetWinCount();
                }

                player.resetWinningStreakCount();
            }

            player.setProfit(player.getProfit() + bet.getProfit());
        }

        // Stop betting after a certain Win/loss count
        if (Integer.parseInt(config.getValue(config.STOP_AFTER_WIN_COUNT)) != 0 && player.getWinCount() == Integer.parseInt(config.getValue(config.STOP_AFTER_WIN_COUNT))) {
            player.stopBetting();
            new ErrorHandler("You just reached your WINNING STREAK limit!");
        }

        if (Integer.parseInt(config.getValue(config.STOP_AFTER_LOSS_COUNT)) != 0 && player.getLossCount() == Integer.parseInt(config.getValue(config.STOP_AFTER_LOSS_COUNT))) {
            player.stopBetting();
            new ErrorHandler("You just reached your LOSSING STREAK limit!");
        }

        // Stop betting after a certain Win/loss profit
        if (Double.parseDouble(config.getValue(config.STOP_AFTER_TOTAL_PROFIT)) != 0 && player.getProfit() >= Double.parseDouble(config.getValue(config.STOP_AFTER_TOTAL_PROFIT))) {
            player.stopBetting();
            new ErrorHandler("You just reached your TOTAL PROFIT limit!");
        }

        if (Double.parseDouble(config.getValue(config.STOP_AFTER_TOTAL_LOSS)) != 0 && player.getProfit() <= Double.parseDouble(config.getValue(config.STOP_AFTER_TOTAL_LOSS)) * -1) {
            player.stopBetting();
            new ErrorHandler("You just reached your TOTAL LOSS limit!");
        }

        // Reset to basebet after Win/loss count
        if (Integer.parseInt(config.getValue(config.RESET_TO_BASEBET_AFTER_WIN_COUNT)) != 0 && player.getWinCount() == Integer.parseInt(config.getValue(config.RESET_TO_BASEBET_AFTER_WIN_COUNT))) {
            player.setBet(Double.parseDouble(config.getValue(config.BASEBET)));
            player.resetWinCount();
            new ErrorHandler("Reset to basebet after reached the WIN COUNT limit!");
        }

        if (Integer.parseInt(config.getValue(config.RESET_TO_BASEBET_AFTER_LOSS_COUNT)) != 0 && player.getLossCount() == Integer.parseInt(config.getValue(config.RESET_TO_BASEBET_AFTER_LOSS_COUNT))) {
            player.setBet(Double.parseDouble(config.getValue(config.BASEBET)));
            player.resetLossCount();
            new ErrorHandler("Reset to basebet after reached the LOSS COUNT limit!");
        }

        // Check if MIN BET has been reached
        if (Integer.parseInt(config.getValue(config.MIN_BET_RESET)) != 0 && player.getBet() <= Double.parseDouble(config.getValue(config.MIN_BET_RESET))) {
            player.setBet(Double.parseDouble(config.getValue(config.BASEBET)));
            new ErrorHandler("Your MIN BET has been reached!");
        }

        // Check if MAX BET has been reached
        if (Double.parseDouble(config.getValue(config.MAX_BET_RESET)) != 0 && player.getBet() >= Double.parseDouble(config.getValue(config.MAX_BET_RESET))) {
            player.setBet(Double.parseDouble(config.getValue(config.BASEBET)));
            new ErrorHandler("Your MAX BET has been reached!");
        }

        // Check if MIN BALANCE has been reached
        if ((Double.parseDouble(config.getValue(config.MIN_BALANCE)) != 0 && player.getUserBalance() != 0) && player.getUserBalance() <= Double.parseDouble(config.getValue(config.MIN_BALANCE))) {
            player.stopBetting();
            new ErrorHandler("Your MIN BALANCE has been reached!");
        }

        // Check if MAX BALANCE has been reached
        if ((Double.parseDouble(config.getValue(config.MAX_BALANCE)) != 0 && player.getUserBalance() != 0) && player.getUserBalance() >= Double.parseDouble(config.getValue(config.MAX_BALANCE))) {
            player.stopBetting();
            new ErrorHandler("Your MAX BALANCE has been reached!");
        }

        // Stop betting if your BALANCE is empty
        if (player.getUserBalance() != 0 && player.getUserBalance() < player.getBet()) {
            player.stopBetting();
            new ErrorHandler("Your balance is EMPTY!");
        }

        // Stop betting after a certian amount of rolls
        if (Integer.parseInt(config.getValue(config.STOP_AFTER_ROLLS)) != 0 && player.getRolls() == Integer.parseInt(config.getValue(config.STOP_AFTER_ROLLS))) {
            player.stopBetting();
            new ErrorHandler("Total amount of ROLLS has been reached!");
        }

        // Create new seed
        if (Integer.parseInt(config.getValue(config.NEW_SEED_AFTER_NONCES)) != 0) {
            player.increaseNewSeedCount();

            if (player.getNonceSeed() >= Integer.parseInt(config.getValue(config.NEW_SEED_AFTER_NONCES))) {
                newSeed();
                player.resetNewSeedCount();
            }
        }

        /* Update user data */
        player.setUsername(bet.getUsername());
        player.setUserBalance(bet.getUserBalance());
        player.setUserBets(bet.getUserBets());
        player.setUserWins(bet.getUserWins());
        player.setUserLosses(bet.getUserLosses());
        player.setUserProfit(bet.getUserProfit());
        player.setUserWagered(bet.getUserWagered());
        player.setNonceSeed(bet.getNonceSeed());
        player.setClientSeed(bet.getClientSeed());
        player.setServerSeed(bet.getServerSeed());

        return player;
    }
}