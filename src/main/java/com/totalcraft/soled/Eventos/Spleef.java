package com.totalcraft.soled.Eventos;

import com.totalcraft.soled.EventoBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Command.*;
import static com.totalcraft.soled.Config.MainConfig.dataEventos;
import static com.totalcraft.soled.Config.MainConfig.saveEventosData;
import static com.totalcraft.soled.EventosTTC.getMain;
import static com.totalcraft.soled.Tasks.StartingEvento.movePlayers;
import static com.totalcraft.soled.Utils.EventoUtils.winnerPlayer;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class Spleef implements Listener {
    public static List<String> methodsSpleef = Arrays.asList("BreakAutoSnow", "AutoReplaceSnow", "ReplaceSnow");
    boolean breakAutoSnow;
    private List<Location> autoReplaceSnow;

    public Spleef(boolean breakAutoSnow, List<Location> autoReplaceSnow) {
        this.breakAutoSnow = breakAutoSnow;
        this.autoReplaceSnow = autoReplaceSnow;
    }

    public Spleef() {
        this.breakAutoSnow = false;
        autoReplaceSnow = new ArrayList<>();
    }

    public void setBreakAutoSnow(EventoBase evento, boolean breakAutoSnow) {
        this.breakAutoSnow = breakAutoSnow;
        dataEventos.set("Eventos." + evento.getName() + ".AutoQuebrarNeve", breakAutoSnow);
        saveEventosData();
    }

    public boolean getBreakAutoSnow() {
        return this.breakAutoSnow;
    }

    public List<Location> getAutoReplaceSnow() {
        return autoReplaceSnow;
    }

    public void addAutoReplaceSnow(EventoBase evento, Location autoReplaceSnow) {
        this.autoReplaceSnow.add(autoReplaceSnow);
        String loc = autoReplaceSnow.getWorld().getName() + " " + autoReplaceSnow.getBlockX() + " " + autoReplaceSnow.getBlockY() + " " + autoReplaceSnow.getBlockZ();
        dataEventos.set("Eventos." + evento.getName() + ".RecolocarNeve." + this.autoReplaceSnow.size(), loc);
        saveEventosData();
    }

    public void removeAutoReplaceSnow(EventoBase evento, int index) {
        this.autoReplaceSnow.remove(index);
        dataEventos.set("Eventos." + evento.getName() + ".RecolocarNeve", "null");
        if (this.autoReplaceSnow.size() > 0) {
            int num = 1;
            for (Location location : this.autoReplaceSnow) {
                String loc = location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
                dataEventos.set("Eventos." + evento.getName() + ".RecolocarNeve." + num, loc);
                num++;
            }
        }
        saveEventosData();
    }

    public void removeAutoReplaceSnow(EventoBase evento) {
        this.autoReplaceSnow = new ArrayList<>();
        dataEventos.set("Eventos." + evento.getName() + ".RecolocarNeve", "null");
        saveEventosData();
    }

    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledSpleefAfk;
    BukkitTask taskSpleef;

    public void startSpleef() {
        for (String p : playersEvento.keySet()) {
            playerAfk.put(p, 0);
        }
        afkPlayer();
        Spleef spleef = (Spleef) eventoCurrent.getType();
        taskSpleef = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
            if (eventoStarted) {
                for (Block block : removeBlocks) {
                    block.setType(Material.AIR);
                }
                removeBlocks = new ArrayList<>();
            }
            if (eventoStarted && playersEvento.size() == 1) {
                for (Location loc : spleef.getAutoReplaceSnow()) {
                    replaceSnow(loc);
                }
                Player player = Bukkit.getPlayer((String) playersEvento.keySet().toArray()[0]);
                String msg = getPmTTC("&b" + player.getName() + " &fGanhou o Evento &b" + eventoCurrent.getName() + " !!!");
                winnerPlayer(player, "Spleef", msg);
                taskSpleef.cancel();
            }
        }, 0, 10);
    }

    public static void replaceSnow(Location loc) {
        World w = loc.getWorld();
        boolean stop1 = true;
        for (int cx = 0; stop1; cx++) {
            Block block1 = w.getBlockAt(loc.getBlockX() + cx, loc.getBlockY(), loc.getBlockZ());
            if (block1.getType() == Material.SNOW_BLOCK || block1.getType() == Material.AIR) {
                block1.setType(Material.SNOW_BLOCK);
            } else {
                stop1 = false;
            }
            boolean stop2 = true;
            for (int cz = 0; stop2; cz++) {
                Block block2 = w.getBlockAt(loc.getBlockX() - cx, loc.getBlockY(), loc.getBlockZ() + cz);
                if (block2.getType() == Material.SNOW_BLOCK || block2.getType() == Material.AIR) {
                    block2.setType(Material.SNOW_BLOCK);
                } else {
                    stop2 = false;
                }
            }
            boolean stop3 = true;
            for (int cz = 0; stop3; cz++) {
                Block block3 = w.getBlockAt(loc.getBlockX() + cx, loc.getBlockY(), loc.getBlockZ() - cz);
                if (block3.getType() == Material.SNOW_BLOCK || block3.getType() == Material.AIR) {
                    block3.setType(Material.SNOW_BLOCK);
                } else {
                    stop3 = false;
                }
            }
            boolean stop4 = true;
            for (int cz = 0; stop4; cz++) {
                Block block4 = w.getBlockAt(loc.getBlockX() - cx, loc.getBlockY(), loc.getBlockZ() - cz);
                if (block4.getType() == Material.SNOW_BLOCK || block4.getType() == Material.AIR) {
                    block4.setType(Material.SNOW_BLOCK);
                } else {
                    stop4 = false;
                }
            }
            boolean stop5 = true;
            for (int cz = 0; stop5; cz++) {
                Block block5 = w.getBlockAt(loc.getBlockX() + cx, loc.getBlockY(), loc.getBlockZ() + cz);
                if (block5.getType() == Material.SNOW_BLOCK || block5.getType() == Material.AIR) {
                    block5.setType(Material.SNOW_BLOCK);
                } else {
                    stop5 = false;
                }
            }
        }
    }

    static Map<String, Integer> playerAfk = new HashMap<>();

    static List<Block> removeBlocks = new ArrayList<>();

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        if (eventoStarted && (eventoCurrent.getType() instanceof Spleef)) {
            Player player = event.getPlayer();
            if (playersEvento.containsKey(player.getName())) {
                Location from = event.getFrom();
                Location to = event.getTo();
                Spleef spleef = (Spleef) eventoCurrent.getType();
                Location loc = player.getLocation();
                if (movePlayers) {
                    if (to.getBlockZ() != from.getBlockZ() || to.getBlockX() != from.getBlockX()) {
                        playerAfk.put(player.getName(), 0);
                    }
                    if (spleef.getBreakAutoSnow()) {
                        Bukkit.getScheduler().runTaskLater(getMain(), () -> {
                            if (loc.subtract(0, 1, 0).getBlock().getType() == Material.SNOW_BLOCK) {
                                removeBlocks.add(loc.getBlock());
                            }
                        }, 20);
                    }
                }
            }
        }
    }

    private static void afkPlayer() {
        scheduledSpleefAfk = scheduler.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, Integer> entry : playerAfk.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                int time = entry.getValue();
                Location loc = player.getLocation();
                if (time > 7) {
                    playerAfk.put(player.getName(), 0);
                    player.sendMessage(getPmTTC("&eOpa amigo, parece que ficar parado não é uma boa opção"));
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            Block block = loc.getWorld().getBlockAt(loc.getBlockX() + i, loc.getBlockY() - 1, loc.getBlockZ() + j);
                            Block block1 = loc.getWorld().getBlockAt(loc.getBlockX() + i, loc.getBlockY() - 2, loc.getBlockZ() + j);
                            if (block.getType() == Material.SNOW_BLOCK) {
                                block.setType(Material.AIR);
                            }
                            if (block1.getType() == Material.SNOW_BLOCK) {
                                block1.setType(Material.AIR);
                            }
                        }
                    }
                } else {
                    time++;
                    entry.setValue(time);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (eventoStarted && (eventoCurrent.getType() instanceof Spleef)) {
                Player player = (Player) event.getEntity();
                if (playersEvento.containsKey(player.getName())) {
                    event.setDamage(0);
                }
            }
        }
    }
}
