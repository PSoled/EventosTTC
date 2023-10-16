package com.totalcraft.soled.Tasks;

import com.totalcraft.soled.PlayerData.PlayerEventoData;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.PlayerData.PlayerEventoData.playersData;

public class PointsRanking {

    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduled;
    public static Map<String, Integer> TopRanking = new HashMap<>();
    public static Map<Integer, String> TopRankingNumber = new HashMap<>();

    public static void updateRanking() {
        scheduled = scheduler.scheduleAtFixedRate(() -> {
            Map<String, Integer> mapPlayers = new HashMap<>();
            for (Map.Entry<String, PlayerEventoData> entry : playersData.entrySet()) {
                Map<String, Integer> pointsEvents = entry.getValue().getPointsEvents();
                if (pointsEvents.containsKey("TotalPoints")) {
                    int totalPoints = pointsEvents.get("TotalPoints");
                    if (totalPoints > 0) mapPlayers.put(entry.getValue().getNickname(), totalPoints);
                }
            }
            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(mapPlayers.entrySet());
            sorted.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            int i = 1;
            for (Map.Entry<String, Integer> entry : sorted) {
                TopRanking.put(entry.getKey(), i);
                TopRankingNumber.put(i, entry.getKey());
                i++;
            }
        }, 5, 60, TimeUnit.SECONDS);
    }

}
