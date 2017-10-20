package com.rene;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Conf {

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    private String playerName;
    private LocalDateTime sessionStartTime;
    private String currency;

    public Conf() {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            dbUrl = prop.getProperty("dburl");
            dbUser = prop.getProperty("dbuser");
            dbPass = prop.getProperty("dbpass");
            playerName = prop.getProperty("player");
            sessionStartTime = LocalDateTime.parse(prop.getProperty("session_start_time"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            currency = prop.getProperty("currency");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getPlayerName() {
        return playerName;
    }

    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public String getCurrency() {
        return currency;
    }
}
