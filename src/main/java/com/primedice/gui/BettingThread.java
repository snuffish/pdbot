package com.primedice.gui;

import com.primedice.client.Bet;
import com.primedice.client.BetManager;
import com.primedice.client.Client;
import com.primedice.client.Config;

import static com.primedice.client.Client.betManager;
import static com.primedice.client.Client.player;
import static com.primedice.client.Client.playerManager;

public class BettingThread extends Thread {

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Bet bet = betManager.createBet(new Bet(player.getBet(), player.getTarget(), player.getCondition()));

            if (bet != null) {
                player = playerManager.updatePlayerObject(bet);
            }

            App.textArea.append("Roll: " + bet.getAmount() + ", Profit: " + bet.getProfit() + "\n");

            try {
                Thread.sleep(Long.parseLong(Config.getValue(Config.ROLL_DELAY)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalThreadStateException e) {
                e.printStackTrace();
            }
        }
    }
}
