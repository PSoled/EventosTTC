package com.totalcraft.soled.Eventos;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Command.*;
import static com.totalcraft.soled.Config.MainConfig.dataEventos;
import static com.totalcraft.soled.Config.MainConfig.saveEventosData;
import static com.totalcraft.soled.EventosTTC.getMain;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Utils.EventoUtils.stopEvento;
import static com.totalcraft.soled.Utils.EventoUtils.winnerPlayer;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class Running implements Listener {
    public static List<String> methodsRunning = Arrays.asList("Checkpoint", "Respawn", "Winner");
    private ItemStack checkpoint, respawn, winner;

    public Running(ItemStack checkpoint, ItemStack respawn, ItemStack winner) {
        this.checkpoint = checkpoint;
        this.respawn = respawn;
        this.winner = winner;
    }

    public Running() {
        this.checkpoint = null;
        this.respawn = null;
        this.winner = null;
    }

    public void setCheckpoint(EventoBase evento, ItemStack checkpoint) {
        this.checkpoint = checkpoint;
        String item = checkpoint.getType().name() + "-" + checkpoint.getDurability();
        dataEventos.set("Eventos." + evento.getName() + ".Checkpoint", item);
        saveEventosData();
    }

    public void setRespawn(EventoBase evento, ItemStack respawn) {
        this.respawn = respawn;
        String item = respawn.getType().name() + "-" + respawn.getDurability();
        dataEventos.set("Eventos." + evento.getName() + ".Respawn", item);
        saveEventosData();
    }

    public void setWinner(EventoBase evento, ItemStack winner) {
        this.winner = winner;
        String item = winner.getType().name() + "-" + winner.getDurability();
        dataEventos.set("Eventos." + evento.getName() + ".Winner", item);
        saveEventosData();
    }

    public ItemStack getCheckpoint() {
        return checkpoint;
    }

    public ItemStack getRespawn() {
        return respawn;
    }

    public ItemStack getWinner() {
        return winner;
    }

    public void removeCheckpoint(EventoBase evento) {
        checkpoint = null;
        dataEventos.set("Eventos." + evento.getName() + ".Checkpoint", "null");
        saveEventosData();
    }

    public void removeRespawn(EventoBase evento) {
        respawn = null;
        dataEventos.set("Eventos." + evento.getName() + ".Respawn", "null");
        saveEventosData();
    }

    public void removeWinner(EventoBase evento) {
        winner = null;
        dataEventos.set("Eventos." + evento.getName() + ".Winner", "null");
        saveEventosData();
    }

    public String toStringCheckpoint() {
        String string = null;
        if (this.checkpoint != null) {
            string = "§fID: §6" + this.checkpoint.getTypeId() + " §fMeta: §6" + this.checkpoint.getDurability();
        }
        return string;
    }

    public String toStringRespawn() {
        String string = null;
        if (this.respawn != null) {
            string = "§fID: §6" + this.respawn.getTypeId() + " §fMeta: §6" + this.respawn.getDurability();
        }
        return string;
    }

    public String toStringWinner() {
        String string = null;
        if (this.winner != null) {
            string = "§fID: §6" + this.winner.getTypeId() + " §fMeta: §6" + this.winner.getDurability();
        }
        return string;
    }

    BukkitTask taskRunning;

    public void startRunning() {
        for (String nick : playersEvento.keySet()) {
            locRunning.put(nick, eventoCurrent.getLocation().get(0));
        }
        taskRunning = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
            if (eventoStarted && playersEvento.size() < 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerEventoData playerData = getPlayerData(player.getName());
                    if (playerData.announceOption) {
                        player.sendMessage("");
                        player.sendMessage(getPmTTC("&cNão houve vencedores no evento &f" + eventoCurrent.getName()));
                        player.sendMessage("");
                    }
                }
                stopEvento();
                taskRunning.cancel();
            }
        }, 0, 20);
    }


    public static Map<String, Location> locRunning = new HashMap<>();
    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        if (eventoStarted && (eventoCurrent.getType() instanceof Running)) {
            Player player = event.getPlayer();
            if (playersEvento.containsKey(player.getName())) {
                Running running = (Running) eventoCurrent.getType();
                Location loc = player.getLocation();
                Location newLoc = new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5);
                ItemStack checkpoint = running.getCheckpoint();
                ItemStack respawn = running.getRespawn();
                ItemStack winner = running.getWinner();
                Block block = player.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ());
                if (block.getType() == winner.getType() && block.getData() == winner.getDurability()) {
                    String msg = getPmTTC("&b" + player.getName() + " &fGanhou o Evento &b" + eventoCurrent.getName() + " !!!");
                    winnerPlayer(player, "Running", msg);
                }
                boolean saveCheck = true;
                if (locRunning.containsKey(player.getName())) {
                    Location check = locRunning.get(player.getName());
                    if (loc.getY() < 2) {
                        player.teleport(new Location(check.getWorld(), check.getX(), check.getY(), check.getZ(), loc.getYaw(), loc.getPitch()));
                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                        return;
                    }
                    if (block.getType() == respawn.getType() && block.getData() == respawn.getDurability()) {
                        player.teleport(new Location(check.getWorld(), check.getX(), check.getY(), check.getZ(), loc.getYaw(), loc.getPitch()));
                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    }
                    if (check.getBlockZ() == newLoc.getBlockZ() && check.getBlockY() == newLoc.getBlockY() && check.getBlockX() == newLoc.getBlockX()) {
                        saveCheck = false;
                    }
                }
                if (loc.getY() < 0) {
                    Location evento = eventoCurrent.getLocation().get(0);
                    player.teleport(new Location(evento.getWorld(), evento.getX(), evento.getY(), evento.getZ(), loc.getYaw(), loc.getPitch()));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    return;
                }
                if (saveCheck && block.getType() == checkpoint.getType() && block.getData() == checkpoint.getDurability()) {
                    locRunning.put(player.getName(), newLoc);
                    player.sendMessage(getPmTTC("&aVocê pegou checkpoint no evento!"));
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                }
            }
        }
    }
}
