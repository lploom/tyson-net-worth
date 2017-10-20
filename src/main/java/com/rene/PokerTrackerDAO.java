package com.rene;

import java.sql.*;

public class PokerTrackerDAO {

    private Connection connection;

    private long playerId;
    private Timestamp sessionStartTime;
    private String currency;

    public void init(Conf conf) {
        try {
            connection = DriverManager.getConnection(conf.getDbUrl(), conf.getDbUser(), conf.getDbPass());
        } catch (SQLException e) {
            throw new RuntimeException("Error creating database connection", e);
        }

        try {
            PreparedStatement stmnt = connection.prepareStatement("SELECT id_player FROM player where player_name_search = ?");
            stmnt.setString(1, conf.getPlayerName().toLowerCase());
            ResultSet resultSet = stmnt.executeQuery();
            if (resultSet.next()) {
                playerId = resultSet.getLong(1);
            } else {
                throw new IllegalArgumentException("Player not found in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying player id", e);
        }

        this.sessionStartTime = Timestamp.valueOf(conf.getSessionStartTime());
        this.currency = conf.getCurrency();
    }

    public double querySessionNetWon() {
        String sql = "select SUM (tr.amt_won - ts.amt_buyin - ts.amt_fee) as net_won_total from tourney_results tr inner join tourney_summary ts on ts.id_tourney = tr.id_tourney where tr.id_player = ? AND ts.currency = ? AND tr.date_end IS NOT NULL AND tr.date_start IS NOT NULL AND tr.date_start > ?";
        try {
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setLong(1, playerId);
            stmnt.setString(2, currency);
            stmnt.setTimestamp(3, sessionStartTime);

            ResultSet resultSet = stmnt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying session net won", e);
        }
        return 0;
    }

}
