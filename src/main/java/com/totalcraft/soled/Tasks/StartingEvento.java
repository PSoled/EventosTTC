package com.totalcraft.soled.Tasks;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Command.*;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Utils.EventoUtils.stopEvento;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class StartingEvento {
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledStarting, scheduledCountdown;

    public static void startEvento(EventoBase evento) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerEventoData playerData = getPlayerData(player.getName());
            if (!playerData.announceOption) {
                player.sendMessage(getPmTTC("&fO Evento &b" + evento.getName() + " &ffoi iniciado. Mas você está com anúncios de eventos &cdesativado"));
            }
        }
        eventoStart = true;
        eventoCurrent = evento;
        int math = evento.getIntervalCalls() * evento.getNumberCalls();
        final int[] time = {math};
        scheduledStarting = scheduler.scheduleAtFixedRate(() -> {
            if (time[0] <= 0) {
                scheduledStarting.cancel(true);
                if (playersEvento.size() < evento.getMinPlayers()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        PlayerEventoData playerData = getPlayerData(player.getName());
                        if (playerData.announceOption) {
                            player.sendMessage("");
                            player.sendMessage(getPmTTC("&cNão houve players suficiente para iniciar o evento"));
                            player.sendMessage("");
                        }
                    }
                    stopEvento();
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerEventoData playerData = getPlayerData(player.getName());
                    if (playerData.announceOption) {
                        player.sendMessage("");
                        player.sendMessage(getPmTTC("&fEvento &b" + evento.getName() + " &fIniciando !!!"));
                        player.sendMessage("");
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                    }
                }
                countdownEvent();
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerEventoData playerData = getPlayerData(player.getName());
                if (playerData.announceOption) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    player.sendMessage("");
                    player.sendMessage(getPmTTC("&fEvento &b" + evento.getName() + " &ffoi iniciado !!"));
                    if (!evento.getRewardMsg().equalsIgnoreCase("null")) {
                        player.sendMessage(getPmTTC("&fO prêmio é &b" + evento.getRewardMsg()));
                    }
                    player.sendMessage(getPmTTC("&fPara entrar utilize o comando &d/evento entrar"));
                    if (evento.saveItems) {
                        player.sendMessage(getPmTTC("&aEste evento salva os itens do seu inventário"));
                    } else {
                        player.sendMessage(getPmTTC("&aEste evento &cnão &asalva os itens do seu inventário"));
                    }
                    if (evento.entryItems) {
                        player.sendMessage(getPmTTC("&aÉ permitido a entrada de itens neste evento"));
                    } else {
                        player.sendMessage(getPmTTC("&cNão &aé permitido a entrada de itens neste evento"));
                    }
                    player.sendMessage(getPmTTC("&fPara saber mais, utilize &e/evento comandos"));
                    player.sendMessage(getPmTTC("&fEvento iniciando em &b" + time[0] + " &fSegundos"));
                    player.sendMessage("");
                }
            }
            time[0] -= evento.getIntervalCalls();
        }, 0, evento.getIntervalCalls(), TimeUnit.SECONDS);
    }
    public static boolean movePlayers;
    public static void countdownEvent() {
        eventoStarted = true;
        movePlayers = false;
        int num = 0;
        for (String nick : playersEvento.keySet()) {
            Player player = Bukkit.getPlayer(nick);
            if (num > eventoCurrent.getLocation().size() - 1) num = 0;
            player.teleport(eventoCurrent.getLocation().get(num));
            num++;
        }
        final int[] time = {5};
        scheduledCountdown = scheduler.scheduleAtFixedRate(() -> {
            if (time[0] <= 0) {
                scheduledCountdown.cancel(true);
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(getPmTTC("&aEvento Iniciado !!!"));
                Bukkit.broadcastMessage("");
                for (String nick : playersEvento.keySet()) {
                    Player player = Bukkit.getPlayer(nick);
                    player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
                }
                movePlayers = true;
                startingEvento();
                return;
            }
            for (String nick : playersEvento.keySet()) {
                Player player = Bukkit.getPlayer(nick);
                player.sendMessage(getPmTTC("&aEvento iniciando em " + time[0]));
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
            }
            time[0]--;
        }, 0, 1, TimeUnit.SECONDS);
    }
    private static void startingEvento() {
        if (eventoCurrent.getType() instanceof Running) {
            ((Running) eventoCurrent.getType()).startRunning();
        }
        if (eventoCurrent.getType() instanceof Spleef) {
            ((Spleef) eventoCurrent.getType()).startSpleef();
        }
        if (eventoCurrent.getType() instanceof SkyWars) {
            ((SkyWars) eventoCurrent.getType()).startSkyWars();
        }
    }
}
