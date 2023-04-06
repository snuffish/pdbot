package com.primedice.client;

public class Player extends Client {
    private String API_KEY;
    private Double bet;
    private int rolls = 0;
    private Double target;
    private String condition;
    private int winCount = 0;
    private int winningStreakCount = 0;
    private int winningStreak = 0;
    private int lossCount = 0;
    private int loosingStreakCount = 0;
    private int loosingStreak = 0;
    private Double profit = 0D;
    private Boolean isBetting = true;
    private int shiftConditionCount = 0;
    private Double wagered = 0D;

    private int wins = 0;
    private int losses = 0;

    private int nonceSeed = 0;
    private String clientSeed = "";
    private String serverSeed = "";
    private int newSeedCount = 0;
    private String username = "";
    private Double userBalance = 0D;
    private int userBets = 0;
    private int userWins = 0;
    private int userLosses = 0;
    private Double userProfit = 0D;
    private Double userWagered = 0D;

    public Player(String API_KEY, Double bet, Double target, String condition) {
        this.API_KEY = API_KEY;
        this.bet = bet;
        this.target = target;
        this.condition = condition;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public Double getBet() {
        // Get PREROLL value if is set
        if (Integer.parseInt(config.getValue(config.PREROLLS)) != 0 && rolls <= Integer.parseInt(config.getValue(config.PREROLLS))) {
            return 0D;
        }

        return bet;
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

    public int getNewSeedCount() { return newSeedCount; }

    public void increaseNewSeedCount() { newSeedCount++; }

    public void resetNewSeedCount() { newSeedCount = 0; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public int getWins() { return wins; }

    public void increaseWins() { wins++; }

    public int getLosses() { return losses; }

    public void increaseLosses() { losses++; }

    public int getShiftConditionCount() { return shiftConditionCount; }

    public void increaseShiftConditionCounter() { shiftConditionCount++; }

    public void resetShiftConditionCounter() { shiftConditionCount = 0; }

    public Double getWagered() { return wagered; }

    public void setWagered(Double wagered) { this.wagered = wagered; }

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

    public void setBet(Double bet) {
        this.bet = bet;
    }

    public void rolledDice() {
        rolls++;
    }

    public int getRolls() {
        return rolls;
    }

    public Double getTarget() {
        return target;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) { this.condition = condition; }

    public void resetWinCount() {
        winCount = 0;
    }

    public void resetLossCount() {
        lossCount = 0;
    }

    public void increaseWinCount() {
        winCount++;
    }

    public void increaseLossCount() {
        lossCount++;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public int getLoosingStreakCount() { return loosingStreakCount; }

    public void increaseLoosingStreakCount() { loosingStreakCount++; }

    public void resetLoosingStreakCount() { loosingStreakCount = 0; }

    public int getLoosingStreak() { return loosingStreak; }

    public void setLoosingStreak(int loosingStreak) { this.loosingStreak = loosingStreak; }

    public int getWinningStreakCount() { return winningStreakCount; }

    public void increaseWinningStreakCount() { winningStreakCount++; }

    public void resetWinningStreakCount() { winningStreakCount = 0; }

    public int getWinningStreak() { return winningStreak; }

    public void setWinningStreak(int winningStreak) { this.winningStreak = winningStreak; }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getProfit() {
        return profit;
    }

    public Boolean isBetting() {
        return isBetting;
    }

    public void stopBetting() {
        isBetting = false;
    }
}