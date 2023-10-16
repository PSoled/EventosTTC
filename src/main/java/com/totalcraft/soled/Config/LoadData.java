package com.totalcraft.soled.Config;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Config.MainConfig.dataEventos;
import static com.totalcraft.soled.Config.MainConfig.dataPlayers;
import static com.totalcraft.soled.EventoBase.events;
import static com.totalcraft.soled.EventoBase.getObjectString;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.playersData;
import static com.totalcraft.soled.Utils.ConfigUtils.*;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class LoadData {
    public static void loadEventoData() {
        if (dataEventos.contains("Eventos")) {
            ConfigurationSection eventos = dataEventos.getConfigurationSection("Eventos");

            for (String nomeEvento : eventos.getKeys(false)) {
                ConfigurationSection evento = eventos.getConfigurationSection(nomeEvento);
                String type = evento.getString("Tipo");
                Map<String, Object> mapLoc = null;
                Object locationsObj = evento.get("Localizações");
                if (!(locationsObj instanceof String) || !((String) locationsObj).equalsIgnoreCase("null")) {
                    mapLoc = evento.getConfigurationSection("Localizações").getValues(false);
                }
                List<Location> locations = new ArrayList<>();
                if (mapLoc != null) {
                    for (Map.Entry<String, Object> entry : mapLoc.entrySet()) {
                        String s = (String) entry.getValue();
                        String[] part = s.split(" ");
                        String world = part[0];
                        double x = Double.parseDouble(part[1]);
                        double y = Double.parseDouble(part[2]);
                        double z = Double.parseDouble(part[3]);
                        float yaw = Float.parseFloat(part[4]);
                        float pitch = Float.parseFloat(part[5]);
                        Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                        locations.add(loc);
                    }
                }
                String hubString = evento.getString("Hub");
                Location hub = getLocationString(hubString);
                String camaroteString = evento.getString("Camarote");
                Location camarote = getLocationString(camaroteString);
                int numberCalls = evento.getInt("Chamadas");
                int intervalCalls = evento.getInt("Intervalo");
                int maxPlayers = evento.getInt("MáximoDePlayers");
                int minPlayers = evento.getInt("MinimoDePlayers");
                String rewardMsg = evento.getString("MensagemDePremiação");
                List<String> rewardConsole = new ArrayList<>();
                Object rewardConsoleObj = evento.get("ComandoDeConsole");
                Map<String, Object> mapItem = null;
                if (!(rewardConsoleObj instanceof String) || !((String) rewardConsoleObj).equalsIgnoreCase("null")) {
                    mapItem = evento.getConfigurationSection("ComandoDeConsole").getValues(false);
                }
                if (mapItem != null) {
                    for (Map.Entry<String, Object> entry : mapItem.entrySet()) {
                        String s = (String) entry.getValue();
                        rewardConsole.add(s);
                    }
                }
                mapItem = null;
                Object rewardItemObj = evento.get("ItensPremeiados");
                if (!(rewardItemObj instanceof String) || !((String) rewardItemObj).equalsIgnoreCase("null")) {
                    mapItem = evento.getConfigurationSection("ItensPremeiados").getValues(false);
                }
                List<ItemStack> rewardItem = new ArrayList<>();
                if (mapItem != null) {
                    for (Map.Entry<String, Object> entry : mapItem.entrySet()) {
                        String num = entry.getKey();
                        if (!num.contains("Enchants")) {
                            String s = (String) entry.getValue();
                            String[] part = s.split("-");
                            String id = part[0];
                            short meta = Short.parseShort(part[1]);
                            int amount = Integer.parseInt(part[2]);
                            ItemStack item = new ItemStack(Material.getMaterial(id), amount, meta);
                            if (mapItem.containsKey(num + " Enchants")) {
                                ItemMeta itemMeta = item.getItemMeta();
                                String objEnchant = (String) mapItem.get(num + " Enchants");
                                String[] enchants = objEnchant.split(" ");
                                for (String enchant : enchants) {
                                    String enchantment = enchant.split("-")[0];
                                    int level = Integer.parseInt(enchant.split("-")[1]);
                                    itemMeta.addEnchant(Enchantment.getByName(enchantment), level, true);
                                }
                                item.setItemMeta(itemMeta);
                            }
                            rewardItem.add(item);
                        }
                    }
                }
                mapItem = null;
                Object itemsGiveObj = evento.get("ItensGivado");
                if (!(itemsGiveObj instanceof String) || !((String) itemsGiveObj).equalsIgnoreCase("null")) {
                    mapItem = evento.getConfigurationSection("ItensGivado").getValues(false);
                }
                List<ItemStack> itemsGive = new ArrayList<>();
                if (mapItem != null) {
                    for (Map.Entry<String, Object> entry : mapItem.entrySet()) {
                        String num = entry.getKey();
                        if (!num.contains("Enchants")) {
                            String s = (String) entry.getValue();
                            String[] part = s.split("-");
                            String id = part[0];
                            short meta = Short.parseShort(part[1]);
                            int amount = Integer.parseInt(part[2]);
                            ItemStack item = new ItemStack(Material.getMaterial(id), amount, meta);
                            if (mapItem.containsKey(num + " Enchants")) {
                                ItemMeta itemMeta = item.getItemMeta();
                                String objEnchant = (String) mapItem.get(num + " Enchants");
                                String[] enchants = objEnchant.split(" ");
                                for (String enchant : enchants) {
                                    String enchantment = enchant.split("-")[0];
                                    int level = Integer.parseInt(enchant.split("-")[1]);
                                    itemMeta.addEnchant(Enchantment.getByName(enchantment), level, true);
                                }
                                item.setItemMeta(itemMeta);
                            }
                            itemsGive.add(item);
                        }
                    }
                }
                int rewardMoney = evento.getInt("MoneyPremiado");
                Object typeEvento = getObjectString(type);

                if (typeEvento instanceof Running) {
                    String checkpointString = evento.getString("Checkpoint");
                    String respawnString = evento.getString("Respawn");
                    String winnerString = evento.getString("Winner");
                    ItemStack checkpoint = getItemStackString(checkpointString);
                    ItemStack respawn = getItemStackString(respawnString);
                    ItemStack winner = getItemStackString(winnerString);
                    typeEvento = new Running(checkpoint, respawn, winner);
                }

                if (typeEvento instanceof Spleef) {
                    boolean breakAutoSnow = evento.getBoolean("AutoQuebrarNeve");
                    mapLoc = null;
                    locationsObj = evento.get("RecolocarNeve");
                    if (!(locationsObj instanceof String) || !((String) locationsObj).equalsIgnoreCase("null")) {
                        mapLoc = evento.getConfigurationSection("RecolocarNeve").getValues(false);
                    }
                    List<Location> replaceSnow = new ArrayList<>();
                    if (mapLoc != null) {
                        for (Map.Entry<String, Object> entry : mapLoc.entrySet()) {
                            String s = (String) entry.getValue();
                            String[] part = s.split(" ");
                            String world = part[0];
                            double x = Double.parseDouble(part[1]);
                            double y = Double.parseDouble(part[2]);
                            double z = Double.parseDouble(part[3]);
                            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
                            replaceSnow.add(loc);
                        }
                    }
                    typeEvento = new Spleef(breakAutoSnow, replaceSnow);
                }

                if (typeEvento instanceof SkyWars) {
                    String region1String = evento.getString("Região1");
                    Location region1 = getLocationString(region1String);
                    String region2String = evento.getString("Região2");
                    Location region2 = getLocationString(region2String);
                    Map<Location, String> mapBlocks = new HashMap<>();
                    Map<Location, Inventory> mapBlocksInventory = new HashMap<>();
                    LoadSaveRegions(evento.getName(), mapBlocks, mapBlocksInventory);
                    typeEvento = new SkyWars(region1, region2, mapBlocks, mapBlocksInventory);
                }
                boolean saveItems = evento.getBoolean("SalvarItens");
                boolean entryItems = evento.getBoolean("EntradaDeItens");
                EventoBase eventoBase = new EventoBase(nomeEvento, typeEvento, locations, hub, camarote, numberCalls, intervalCalls, maxPlayers, minPlayers, itemsGive, rewardMsg, rewardConsole, rewardItem, rewardMoney, saveItems, entryItems);
                events.put(nomeEvento, eventoBase);
                Bukkit.getConsoleSender().sendMessage(getPmTTC("&fEvento &b" + nomeEvento + " &fCarregado com Sucesso!"));
            }
        }
    }

    public static void loadPlayerData() {
        if (dataPlayers.contains("Players")) {
            ConfigurationSection players = dataPlayers.getConfigurationSection("Players");
            for (String nickname : players.getKeys(false)) {
                ConfigurationSection player = players.getConfigurationSection(nickname);
                boolean hidePlayer = player.getBoolean("HidePlayer");
                int pointsShop = player.getInt("PointsShop");
                Map<String, Integer> pointsEvents = new HashMap<>();
                pointsEvents.put("TotalPoints", 0);
                Object obj = player.get("PointsEvents");
                if (obj instanceof ConfigurationSection) {
                    ConfigurationSection points = (ConfigurationSection) obj;
                    for (String event : points.getKeys(false)) {
                        pointsEvents.put(event, points.getInt(event));
                    }
                }
                String locString = player.getString("ChestItems");
                Location chestItems = getChestString(locString);
                boolean announceOption = player.getBoolean("AnnounceOption");
                PlayerEventoData playerData = new PlayerEventoData(nickname, hidePlayer, pointsShop, pointsEvents, chestItems, announceOption);
                playersData.put(nickname, playerData);
            }
        }
    }

}
