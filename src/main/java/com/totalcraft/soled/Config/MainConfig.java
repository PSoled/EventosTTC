package com.totalcraft.soled.Config;

import com.totalcraft.soled.EventosTTC;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static com.totalcraft.soled.Config.LoadData.loadEventoData;
import static com.totalcraft.soled.Config.LoadData.loadPlayerData;
import static com.totalcraft.soled.EventoListener.cmdAllowed;
import static com.totalcraft.soled.Tasks.AutoStartEvento.autoStart;
import static com.totalcraft.soled.Tasks.AutoStartEvento.enableAutoStart;

public class MainConfig {
    public static File fileMain = new File(EventosTTC.getMain().getDataFolder(), "config.yml");
    public static File fileEventoData = new File(EventosTTC.getMain().getDataFolder(), "eventos.yml");
    public static File filePlayerData = new File(EventosTTC.getMain().getDataFolder(), "playerdata.yml");
    public static YamlConfiguration configMain, dataEventos, dataPlayers;

    public static void loadPluginData() {
        configMain = YamlConfiguration.loadConfiguration(fileMain);
        dataEventos = YamlConfiguration.loadConfiguration(fileEventoData);
        dataPlayers = YamlConfiguration.loadConfiguration(filePlayerData);

        configMain.options().header("-- Config EventosTTC --\n\n");
        configMain.options().copyDefaults(true);

        dataEventos.options().header("-- Eventos Criados --\n\n");
        dataEventos.options().copyDefaults(true);

        dataPlayers.options().header("-- Dados dos Players --\n\n");
        dataPlayers.options().copyDefaults(true);

        loadMainConfig();
        loadEventoData();
        loadPlayerData();

        try {
            configMain.save(fileMain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMainConfig() {
        cmdAllowed = configMain.getStringList("Comandos habilitados durante evento");
        enableAutoStart = configMain.getBoolean("Auto Iniciar.Habilitar");
        autoStart = configMain.getStringList("Auto Iniciar.Eventos");
        configMain.addDefault("Comandos habilitados durante evento", cmdAllowed);
        configMain.addDefault("Auto Iniciar.Habilitar", enableAutoStart);
        configMain.addDefault("Auto Iniciar.Eventos", autoStart);
    }



    public static void saveEventosData() {
        try {
            dataEventos.save(fileEventoData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerData() {
        try {
            dataPlayers.save(filePlayerData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
