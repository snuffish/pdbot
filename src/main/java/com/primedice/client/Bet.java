package com.primedice.client;

public class Bet {
    // Bet data
    private Double amount;
    private Double target;
    private String condition;
    private Boolean win;
    private Double roll;
    private Double multiplier;
    private Double profit;
    private String player;
    private Boolean jackpot;

    // User data
    private String username;
    private Double userBalance;
    private int userBets;
    private int userWins;
    private int userLosses;
    private Double userProfit;
    private Double userWagered;
    private int nonceSeed;
    private String clientSeed;
    private String serverSeed;

    public Bet() { }

    public Bet(Double amount, Double target, String condition) {
        this.amount = amount;
        this.target = target;
        this.condition = condition;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public int getUserBets() {
        return userBets;
    }

    public void setUserBets(int userBets) {
        this.userBets = userBets;
    }

    public int getUserWins() {
        return userWins;
    }

    public void setUserWins(int userWins) {
        this.userWins = userWins;
    }

    public int getUserLosses() {
        return userLosses;
    }

    public void setUserLosses(int userLosses) {
        this.userLosses = userLosses;
    }

    public Double getUserProfit() { return userProfit; }

    public void setUserProfit(Double userProfit) { this.userProfit = userProfit; }

    public Double getUserWagered() { return userWagered; }

    public void setUserWagered(Double userWagered) { this.userWagered = userWagered; }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }

    public Double getRoll() {
        return roll;
    }

    public void setRoll(Double roll) {
        this.roll = roll;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Boolean getJackpot() {
        return jackpot;
    }

    public void setJackpot(Boolean jackpot) {
        this.jackpot = jackpot;
    }

    public int getNonceSeed() {
        return nonceSeed;
    }

    public void setNonceSeed(int nonceSeed) {
        this.nonceSeed = nonceSeed;
    }

    public String getClientSeed() {
        return clientSeed;
    }

    public void setClientSeed(String clientSeed) {
        this.clientSeed = clientSeed;
    }

    public String getServerSeed() {
        return serverSeed;
    }

    public void setServerSeed(String serverSeed) {
        this.serverSeed = serverSeed;
    }
}