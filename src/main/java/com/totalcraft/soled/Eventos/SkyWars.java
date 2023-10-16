package com.totalcraft.soled.Eventos;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.EventosTTC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

import static com.totalcraft.soled.Command.*;
import static com.totalcraft.soled.Config.MainConfig.dataEventos;
import static com.totalcraft.soled.Config.MainConfig.saveEventosData;
import static com.totalcraft.soled.EventosTTC.getMain;
import static com.totalcraft.soled.Utils.EventoUtils.*;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class SkyWars implements Listener {
    public static List<String> methodsSkyWars = Arrays.asList("Region1", "Region2", "SaveBlocks", "LoadBlocks", "ExpandVert", "ClearItems");
    private Location region1, region2;
    private Map<Location, String> saveBlocks;
    private Map<Location, Inventory> saveItemsBlock;

    public SkyWars(Location region1, Location region2, Map<Location, String> saveBlocks, Map<Location, Inventory> saveItemsBlock) {
        this.region1 = region1;
        this.region2 = region2;
        this.saveBlocks = saveBlocks;
        this.saveItemsBlock = saveItemsBlock;
    }

    public SkyWars() {
        region1 = null;
        region2 = null;
        saveBlocks = new HashMap<>();
        saveItemsBlock = new HashMap<>();
    }

    public Location getRegion1() {
        return region1;
    }

    public Location getRegion2() {
        return region2;
    }

    public Map<Location, String> getSaveBlocks() {
        return saveBlocks;
    }

    public Map<Location, Inventory> getSaveItemsBlock() {
        return saveItemsBlock;
    }

    public void setRegion1(EventoBase evento, Location region1) {
        this.region1 = region1;
        String loc = region1.getWorld().getName() + " " + region1.getX() + " " + region1.getY() + " " + region1.getZ();
        dataEventos.set("Eventos." + evento.getName() + ".Região1", loc);
        saveEventosData();
    }

    public void setRegion2(EventoBase evento, Location region2) {
        this.region2 = region2;
        String loc = region2.getWorld().getName() + " " + region2.getX() + " " + region2.getY() + " " + region2.getZ();
        dataEventos.set("Eventos." + evento.getName() + ".Região2", loc);
        saveEventosData();
    }

    public void setSaveBlocks(Map<Location, String> saveBlocks) {
        this.saveBlocks = saveBlocks;
    }

    public void setSaveItemsBlock(Map<Location, Inventory> saveItemsBlock) {
        this.saveItemsBlock = saveItemsBlock;
    }

    public void removeRegion1(EventoBase evento) {
        this.region1 = null;
        dataEventos.set("Eventos." + evento.getName() + ".Região1", "null");
        saveEventosData();
    }

    public void removeRegion2(EventoBase evento) {
        this.region2 = null;
        dataEventos.set("Eventos." + evento.getName() + ".Região2", "null");
        saveEventosData();
    }

    public boolean removeSaveBlocks(EventoBase evento) {
        this.saveBlocks = new HashMap<>();
        String child = "SaveRegions/" + evento.getName() + ".yml";
        File fileSavesRegions = new File(EventosTTC.getMain().getDataFolder(), child);
        return fileSavesRegions.delete();
    }
    public static List<String> noFallDmg = new ArrayList<>();
    BukkitTask taskSkyWars;
    public void startSkyWars() {
        for (String name : playersEvento.keySet()) {
            noFallDmg.add(name);
            Player player = Bukkit.getPlayer(name);
            player.getLocation().subtract(0, 1, 0).getBlock().setType(Material.AIR);
        }
        SkyWars skyWars = (SkyWars) eventoCurrent.getType();
        taskSkyWars = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
                if (eventoStarted && playersEvento.size() == 1) {
                    Player player = Bukkit.getPlayer((String) playersEvento.keySet().toArray()[0]);
                    String msg = getPmTTC("&b" + player.getName() + " &fGanhou o Evento &b" + eventoCurrent.getName() + " !!!");
                    winnerPlayer(player, "Skywars", msg);
                    if (skyWars.saveBlocks.size() > 0) {
                        LoadBlocksSave(skyWars.getRegion1(), skyWars.getRegion2(), skyWars.saveBlocks, skyWars.saveItemsBlock);
                    }
                    clearItemsLoc(skyWars.region1, skyWars.region2);
                    taskSkyWars.cancel();
                }
        }, 0, 20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                String name = ((Player) event.getEntity()).getName();
                if (noFallDmg.contains(name)) {
                    event.setCancelled(true);
                    noFallDmg.remove(name);
                }
            }
        }
    }
}
