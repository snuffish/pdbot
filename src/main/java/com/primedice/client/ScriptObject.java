package com.primedice.client;

public class ScriptObject extends Client {
    private Bet bet;

    public void Bet(Double amount, Double target, String condition) {
        try {
            bet = new Bet(amount, target, condition);
            bet = betManager.createBet(bet);

            if (bet != null) {
                player = playerManager.updatePlayerObject(bet);

                renderScreen.refresh(player);
            }

            Thread.sleep(Long.parseLong(config.getValue(config.ROLL_DELAY)));
        } catch (InterruptedException e) {

        }
    }

    public String Config(String name) { return config.getValue(name); }

    public Double Balance() {
        return player.getUserBalance();
    }

    public Boolean Win() {
        return bet.getWin();
    }

    public Double Roll() {
        return bet.getRoll();
    }

    public Double Profit() {
        return bet.getProfit();
    }

    public Double TotalProfit() {
        return player.getUserProfit();
    }
}