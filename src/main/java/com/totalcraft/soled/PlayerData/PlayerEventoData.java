package com.totalcraft.soled.PlayerData;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.Config.MainConfig.dataPlayers;
import static com.totalcraft.soled.Config.MainConfig.savePlayerData;

public class PlayerEventoData {

    private final String nickname;
    public boolean hidePlayers, announceOption;
    private int pointsShop;
    private Map<String, Integer> pointsEvents;
    private Location chestItems;

    public PlayerEventoData(String nick, boolean hidePlayers, int pointsShop, Map<String, Integer> pointsEvents, Location chestItems, boolean announceOption) {
        this.nickname = nick;
        this.hidePlayers = hidePlayers;
        this.pointsShop = pointsShop;
        this.pointsEvents = pointsEvents;
        this.chestItems = chestItems;
        this.announceOption = announceOption;
    }

    public PlayerEventoData(String nick) {
        this.nickname = nick;
        this.hidePlayers = true;
        this.pointsShop = 0;
        this.pointsEvents = new HashMap<>();
        this.pointsEvents.put("TotalPoints", 0);
        this.chestItems = null;
        this.announceOption = true;
    }

    public static final Map<String, PlayerEventoData> playersData = new HashMap<>();

    public static void newPlayerData(String nick) {
        PlayerEventoData playerData = new PlayerEventoData(nick);
        playersData.put(nick, playerData);
        dataPlayers.set("Players." + nick + ".HidePlayer", true);
        dataPlayers.set("Players." + nick + ".PointsShop", 0);
        dataPlayers.set("Players." + nick + ".PointsEvents", "null");
        dataPlayers.set("Players." + nick + ".ChestItems", "null");
        dataPlayers.set("Players." + nick + ".AnnounceOption", true);
        savePlayerData();
    }

    public static PlayerEventoData getPlayerData(String nick) {
        if (!playersData.containsKey(nick)) {
            newPlayerData(nick);
        }
        return playersData.get(nick);
    }

    public String getNickname() {
        return nickname;
    }

    public void setHidePlayers(boolean hidePlayers) {
        this.hidePlayers = hidePlayers;
        dataPlayers.set("Players." + this.nickname + ".HidePlayer", hidePlayers);
        savePlayerData();
    }

    public int getPointsShop() {
        return pointsShop;
    }

    public boolean hasPointsShop(int pointsShop) {
        return this.pointsShop >= pointsShop;
    }

    public void addPointsShop(int pointsShop) {
        this.pointsShop += pointsShop;
        dataPlayers.set("Players." + this.nickname + ".PointsShop", this.pointsShop);
        savePlayerData();
    }

    public void removePointsShop(int pointsShop) {
        this.pointsShop -= pointsShop;
        dataPlayers.set("Players." + this.nickname + ".PointsShop", this.pointsShop);
        savePlayerData();
    }

    public Map<String, Integer> getPointsEvents() {
        return pointsEvents;
    }

    public void setPointsEvents(Map<String, Integer> pointsEvents) {
        this.pointsEvents = pointsEvents;
        int totalPoints = 0;
        dataPlayers.set("Players." + this.nickname + ".PointsEvents", "null");
        for (Map.Entry<String, Integer> entry : this.pointsEvents.entrySet()) {
            dataPlayers.set("Players." + this.nickname + ".PointsEvents." + entry.getKey(), entry.getValue());
            if (!entry.getKey().equalsIgnoreCase("TotalPoints")) {
                totalPoints += entry.getValue();
            }
        }
        this.pointsEvents.put("TotalPoints", totalPoints);
        dataPlayers.set("Players." + this.nickname + ".PointsEvents.TotalPoints", totalPoints);
        savePlayerData();
    }

    public Location getChestItems() {
        return chestItems;
    }

    public void setChestItems(Location chestItems) {
        this.chestItems = chestItems;
        String loc = chestItems.getBlockX() + " " + chestItems.getBlockY() + " " + chestItems.getBlockZ();
        dataPlayers.set("Players." + this.nickname + ".ChestItems", loc);
        savePlayerData();
    }

    public void removeChestItems() {
        this.chestItems = null;
        dataPlayers.set("Players." + this.nickname + ".ChestItems", "null");
        savePlayerData();
    }

    public void setAnnounceOption(boolean announceOption) {
        this.announceOption = announceOption;
        dataPlayers.set("Players." + this.nickname + ".AnnounceOption", announceOption);
        savePlayerData();
    }
}
