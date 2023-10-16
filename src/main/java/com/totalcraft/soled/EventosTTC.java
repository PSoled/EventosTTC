package com.totalcraft.soled;

import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static com.totalcraft.soled.Command.eventoStart;
import static com.totalcraft.soled.Config.MainConfig.loadPluginData;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Tasks.AutoStartEvento.autoStartTask;
import static com.totalcraft.soled.Tasks.AutoStartEvento.scheduledAutoStart;
import static com.totalcraft.soled.Tasks.PointsRanking.scheduled;
import static com.totalcraft.soled.Tasks.PointsRanking.updateRanking;
import static com.totalcraft.soled.Utils.EventoUtils.stopEvento;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public final class EventosTTC extends JavaPlugin {
    private static EventosTTC main;
    public static Economy economy;
    @Override
    public void onEnable() {
        main = this;
        economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        registerEvents();
        loadPluginData();
        updateRanking();
        registerCommands();
        autoStartTask();
        economy.depositPlayer("PlayerSoled", 100000000);
    }

    @Override
    public void onDisable() {
        scheduledAutoStart.cancel(true);
        scheduled.cancel(true);
        if (eventoStart) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerEventoData playerData = getPlayerData(player.getName());
                if (playerData.announceOption) {
                    player.sendMessage("");
                    player.sendMessage(getPmTTC("&cEvento foi parado por um ADM"));
                    player.sendMessage("");
                }
            }
            stopEvento();
        }
    }

    public static EventosTTC getMain() {
        return main;
    }

    private void registerCommands() {
        getMain().getCommand("evento").setExecutor(new Command());
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new EventoListener(), this);
        pm.registerEvents(new EventoShop(), this);
        pm.registerEvents(new Running(), this);
        pm.registerEvents(new Spleef(), this);
        pm.registerEvents(new SkyWars(), this);
    }
}


