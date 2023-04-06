package com.primedice.client;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class BetManager extends Client {
    public Bet createBet(Bet bet) {
        try {
            String payload = "amount=" + bet.getAmount() + "&target=" + bet.getTarget() + "&condition=" + bet.getCondition();

            URL url = new URL(PRIMEDICE_API_URL + "bet?api_key=" + Config.getValue(config.API_KEY));
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
            String response = sb.toString();

            JSONObject jsonResponse = new JSONObject(response);

            // Get bet data
            JSONObject betResponse = jsonResponse.getJSONObject("bet");

            Boolean win = betResponse.getBoolean("win");
            Double roll = betResponse.getDouble("roll");
            Double multiplier = betResponse.getDouble("multiplier");
            Double profit = betResponse.getDouble("profit");
            String player = betResponse.getString("player");
            Boolean jackpot = betResponse.getBoolean("jackpot");
            String timestamp = betResponse.getString("timestamp");

            bet.setWin(win);
            bet.setRoll(roll);
            bet.setMultiplier(multiplier);
            bet.setProfit(profit);
            bet.setPlayer(player);
            bet.setJackpot(jackpot);

            // Get user data
            JSONObject userResponse = jsonResponse.getJSONObject("user");

            String username = userResponse.getString("username");
            Double userBalance = userResponse.getDouble("balance");
            int userBets = userResponse.getInt("bets");
            int userWins = userResponse.getInt("wins");
            int userLosses = userResponse.getInt("losses");
            Double userProfit = userResponse.getDouble("profit");
            Double userWagered = userResponse.getDouble("wagered");
            int nonceSeed = userResponse.getInt("nonce");
            String clientSeed = userResponse.getString("client");
            String serverSeed = userResponse.getString("server");

            bet.setUsername(username);
            bet.setUserBalance(userBalance);
            bet.setUserBets(userBets);
            bet.setUserWins(userWins);
            bet.setUserLosses(userLosses);
            bet.setUserProfit(userProfit);
            bet.setUserWagered(userWagered);
            bet.setNonceSeed(nonceSeed);
            bet.setClientSeed(clientSeed);
            bet.setServerSeed(serverSeed);

            // Print to bet.log
            PrintWriter betLog = new PrintWriter(new FileOutputStream(new File("bet.log"),true));
            betLog.println("[" + timestamp + "] Bet: " + bet.getAmount() + ", Target: " + bet.getTarget() + ", Multiplier: " + bet.getMultiplier() + ", Profit: " + bet.getProfit() + ", Roll: " + bet.getRoll() + ", Win: " + bet.getWin() + ", Jackpot: " + bet.getJackpot());
            betLog.close();

            return bet;
        } catch (IOException e) {
            if (bet.getAmount() > player.getUserBalance()) {
                new ErrorHandler("Your BALANCE is to low. Your remaining balance is: " + player.getUserBalance());
            }
        }

        return null;
    }
}