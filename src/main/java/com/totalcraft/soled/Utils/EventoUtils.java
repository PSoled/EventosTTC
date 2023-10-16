package com.totalcraft.soled.Utils;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.EventosTTC;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import mods.tinker.tconstruct.TConstruct;
import mods.tinker.tconstruct.util.player.TPlayerStats;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.totalcraft.soled.Command.*;
import static com.totalcraft.soled.EventoListener.kickPlayer;
import static com.totalcraft.soled.EventoListener.playerRespawn;
import static com.totalcraft.soled.Eventos.Running.locRunning;
import static com.totalcraft.soled.Eventos.SkyWars.noFallDmg;
import static com.totalcraft.soled.Eventos.Spleef.scheduledSpleefAfk;
import static com.totalcraft.soled.EventosTTC.economy;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Tasks.StartingEvento.scheduledCountdown;
import static com.totalcraft.soled.Tasks.StartingEvento.scheduledStarting;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class EventoUtils {

    public static void winnerPlayer(Player winner, String evento, String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerEventoData playerData = getPlayerData(player.getName());
            if (playerData.announceOption) {
                player.sendMessage("");
                player.sendMessage(msg);
                player.sendMessage("");
            }
        }
        winner.playSound(winner.getLocation(), Sound.LEVEL_UP, 1, 1);
        if (eventoCurrent.getRewardMoney() > 0) {
            economy.depositPlayer(winner.getName(), eventoCurrent.getRewardMoney());
        }
        for (String nick : playersEvento.keySet()) {
            Player player = Bukkit.getPlayer(nick);
            PlayerEventoData playerData = getPlayerData(player.getName());
            if (playerData.hidePlayers) {
                showPlayers(player);
            }
            player.getInventory().clear();
            player.getInventory().setBoots(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setHelmet(null);
            TPlayerStats stats = TConstruct.playerTracker.getPlayerStats(player.getName());
            stats.armor.inventory = new net.minecraft.item.ItemStack[7];
            if (playerData.getChestItems() != null) {
                loadLocationChest(player, playerData.getChestItems());
            }
            Location loc = playersEvento.get(player.getName());
            player.teleport(loc);
        }

        if (eventoCurrent.getRewardItem().size() > 0) {
            if (winner.getInventory().firstEmpty() == -1) {
                winner.sendMessage("");
                winner.sendMessage(getPmTTC("&cVocê está com invetário cheio, a sua premiação foi dropado no seu pé"));
                winner.sendMessage("");
                winner.sendMessage(getPmTTC("&bVá com inventário menos cheio no próximo evento, meu parceiro"));
            }
            for (ItemStack item : eventoCurrent.getRewardItem()) {
                if (winner.getInventory().firstEmpty() == -1) {
                    winner.getWorld().dropItem(winner.getLocation(), item);
                }
                winner.getInventory().addItem(item);
            }
        }

        if (eventoCurrent.getRewardConsole() != null) {
            if (eventoCurrent.getRewardConsole().size() > 0) {
                for (String cmd : eventoCurrent.getRewardConsole()) {
                    if (cmd.contains("%player%")) {
                        cmd = cmd.replace("%player%", winner.getName());
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
        }

        PlayerEventoData playerData = getPlayerData(winner.getName());
        playerData.addPointsShop(1);
        Map<String, Integer> points = playerData.getPointsEvents();
        if (points.containsKey(evento)) {
            points.put(evento, points.get(evento) + 1);
        } else {
            points.put(evento, 1);
        }
        playerData.setPointsEvents(points);
        clearData();
    }

    public static void stopEvento() {
        if (eventoStart) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerEventoData playerData = getPlayerData(player.getName());
                if (playerData.announceOption) {
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
                }
            }
            eventoStop = true;
            for (String nick : playersEvento.keySet()) {
                Player player = Bukkit.getPlayer(nick);
                PlayerEventoData playerData = getPlayerData(player.getName());
                if (playerData.hidePlayers) {
                    showPlayers(player);
                }
                player.getInventory().clear();
                player.getInventory().setBoots(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setHelmet(null);
//                TPlayerStats stats = TConstruct.playerTracker.getPlayerStats(player.getName());
//                stats.armor.inventory = new net.minecraft.item.ItemStack[7];
                if (playerData.getChestItems() != null) {
                    loadLocationChest(player, playerData.getChestItems());
                }
                Location loc = playersEvento.get(player.getName());
                player.teleport(loc);
            }
            clearData();
        }
    }

    private static void clearData() {
        playerRespawn = new HashMap<>();
        locRunning = new HashMap<>();
        kickPlayer = new HashMap<>();
        noFallDmg = new ArrayList<>();
        playersEvento = new HashMap<>();
        eventoStarted = false;
        eventoStart = false;
        eventoCurrent = null;
        cancelTask(scheduledStarting);
        cancelTask(scheduledCountdown);
        cancelTask(scheduledSpleefAfk);
    }

    public static void cancelTask(ScheduledFuture<?> scheduledFuture) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    public static void PlayerLeaveEvento(Player player) {
        player.getInventory().clear();
        player.getInventory().setBoots(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setHelmet(null);
        TPlayerStats stats = TConstruct.playerTracker.getPlayerStats(player.getName());
        stats.armor.inventory = new net.minecraft.item.ItemStack[7];
        showPlayers(player);
        PlayerEventoData playerData = getPlayerData(player.getName());
        if (playerData.getChestItems() != null) {
            loadLocationChest(player, playerData.getChestItems());
        }
        Location loc = playersEvento.get(player.getName());
        player.teleport(loc);
        playersEvento.remove(player.getName());
    }

    public static Location saveLocationChest(Player player) {
        World world = Bukkit.getWorld("spawn");
        Location start = new Location(world, 50000, 200, -50000);
        Block block = world.getBlockAt(start);
        while (block.getType() != Material.AIR) {
            start.add(2, 0, 0);
            block = world.getBlockAt(start);
        }
        block.setType(Material.CHEST);
        new Location(start.getWorld(), start.getBlockX(), start.getBlockY(), start.getBlockZ() + 1).getBlock().setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getInventory();
        PlayerInventory playerInv = player.getInventory();
        inv.setItem(45, playerInv.getHelmet());
        inv.setItem(46, playerInv.getChestplate());
        inv.setItem(47, playerInv.getLeggings());
        inv.setItem(48, playerInv.getBoots());
        playerInv.setArmorContents(null);
        for (int i = 0; i < playerInv.getSize(); i++) {
            ItemStack item = playerInv.getItem(i);
            if (item != null) {
                inv.setItem(i, item);
                playerInv.setItem(i, null);
            }
        }
        Location locSign = new Location(start.getWorld(), start.getBlockX(), start.getBlockY() + 1, start.getBlockZ());
        Block blockSign = locSign.getBlock();
        blockSign.setType(Material.SIGN_POST);
        Sign sign = (Sign) blockSign.getState();
        sign.setLine(1, player.getName());
        sign.update();
        world.save();
        return new Location(start.getWorld(), start.getBlockX(), start.getBlockY(), start.getBlockZ());
    }

    public static void loadLocationChest(Player player, Location loc) {
        Chest chest = (Chest) loc.getBlock().getState();
        Inventory inv = chest.getInventory();
        PlayerInventory playerInv = player.getInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                playerInv.setItem(i, item);
            }
        }
        ItemStack helmet = inv.getItem(45);
        ItemStack chestplate = inv.getItem(46);
        ItemStack leggings = inv.getItem(47);
        ItemStack boots = inv.getItem(48);

        if (helmet != null) {
            playerInv.setHelmet(helmet);
        }
        if (chestplate != null) {
            playerInv.setChestplate(chestplate);
        }
        if (leggings != null) {
            playerInv.setLeggings(leggings);
        }
        if (boots != null) {
            playerInv.setBoots(boots);
        }
        inv.clear();
        loc.add(0, 1, 0).getBlock().setType(Material.AIR);
        loc.subtract(0, 1, 0).getBlock().setType(Material.AIR);
        loc.add(0, 0, 1).getBlock().setType(Material.AIR);
        PlayerEventoData playerData = getPlayerData(player.getName());
        playerData.removeChestItems();
    }

    public static void hidePlayers(Player player, boolean hide) {
        if (hide) {
            if (eventoCurrent.getType() instanceof Running) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(online);
                }
            }
        } else {
            showPlayers(player);
        }
    }

    public static void showPlayers(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (getAdm(player)) {
                if (!online.hasPermission("teste.vanish")) {
                    player.showPlayer(online);
                }
            } else {
                player.showPlayer(online);
            }
        }
    }

    public static void SaveBlocks(EventoBase evento, Location loc1, Location loc2, Map<Location, String> saveBlocks, Map<Location, Inventory> saveItemsBlock) {
        String child = "SaveRegions/" + evento.getName() + ".yml";
        File fileSavesRegions = new File(EventosTTC.getMain().getDataFolder(), child);
        YamlConfiguration SaveRegions = YamlConfiguration.loadConfiguration(fileSavesRegions);
        SaveRegions.set("InventoryBlocks", null);
        SaveRegions.set("Blocks", null);
        SaveRegions.set("World", loc1.getWorld().getName());
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(loc1.getWorld(), x, y, z);
                    Block block = loc.getBlock();
                    if (block.getType() != Material.AIR) {
                        String id = block.getType().name() + " " + block.getData();
                        saveBlocks.put(loc, id);
                        SaveRegions.set("Blocks." + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ(), id);
                        if (block.getState() instanceof InventoryHolder) {
                            InventoryHolder invHolder = (InventoryHolder) block.getState();
                            Inventory inv = Bukkit.createInventory(null, 180, "null");
                            for (int i = 0; i < invHolder.getInventory().getSize(); i++) {
                                ItemStack item = invHolder.getInventory().getItem(i);
                                if (item != null) {
                                    inv.setItem(i, item);
                                    StringBuilder nameItem = new StringBuilder(item.getType().name() + " " + item.getDurability() + " " + item.getAmount());
                                    if (item.getItemMeta().hasEnchants()) {
                                        for (Map.Entry<Enchantment, Integer> entry : item.getItemMeta().getEnchants().entrySet()) {
                                            nameItem.append("-").append(entry.getKey().getName()).append(" ").append(entry.getValue());
                                        }
                                    }
                                    SaveRegions.set("InventoryBlocks." + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "." + i, nameItem.toString());
                                }
                            }
                            saveItemsBlock.put(loc, inv);
                        }
                    }
                }
            }
        }
        try {
            SaveRegions.save(fileSavesRegions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadBlocksSave(Location loc1, Location loc2, Map<Location, String> saveBlocks, Map<Location, Inventory> saveItemsBlock) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location currentLoc = new Location(loc1.getWorld(), x, y, z);
                    currentLoc.getBlock().setType(Material.AIR);
                }
            }
        }
        for (Map.Entry<Location, String> entry : saveBlocks.entrySet()) {
            String[] part = entry.getValue().split(" ");
            Material material = Material.getMaterial(part[0]);
            byte data = Byte.parseByte(part[1]);
            Block block = entry.getKey().getBlock();
            block.setType(material);
            block.setData(data);
        }

        for (Map.Entry<Location, Inventory> entry : saveItemsBlock.entrySet()) {
            InventoryHolder invHolder = (InventoryHolder) entry.getKey().getBlock().getState();
            Inventory inv = invHolder.getInventory();
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStack item = entry.getValue().getItem(i);
                if (item != null) {
                    System.out.println(item.getType().name() + " " + item.getDurability() + " " + item.getAmount());
                    inv.setItem(i, item);
                }
            }
        }
    }

    public static void clearItemsLoc(Location loc1, Location loc2) {
        World world = loc1.getWorld();
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item) {
                Location entityLocation = entity.getLocation();
                int entityX = entityLocation.getBlockX();
                int entityY = entityLocation.getBlockY();
                int entityZ = entityLocation.getBlockZ();

                if (entityX >= minX && entityX <= maxX &&
                        entityY >= minY && entityY <= maxY &&
                        entityZ >= minZ && entityZ <= maxZ) {
                    new Location(loc1.getWorld(), entityX, entityY, entityZ).getChunk().load();
                    entity.remove();
                }
            }
        }
    }
}
