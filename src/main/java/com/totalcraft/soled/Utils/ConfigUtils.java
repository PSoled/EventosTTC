package com.totalcraft.soled.Utils;

import com.totalcraft.soled.EventosTTC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Map;

public class ConfigUtils {

    public static Location getLocationString(String s) {
        Location loc = null;
        if (!s.equalsIgnoreCase("null")) {
            String[] part = s.split(" ");
            String world = part[0];
            double x = Double.parseDouble(part[1]);
            double y = Double.parseDouble(part[2]);
            double z = Double.parseDouble(part[3]);
            if (part.length > 4) {
                float yaw = Float.parseFloat(part[4]);
                float pitch = Float.parseFloat(part[5]);
                loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            } else {
                loc = new Location(Bukkit.getWorld(world), x, y, z);
            }
        }
        return loc;
    }
    public static Location getLocationString(String s, World world) {
        Location loc = null;
        if (!s.equalsIgnoreCase("null")) {
            String[] part = s.split(" ");
            double x = Double.parseDouble(part[0]);
            double y = Double.parseDouble(part[1]);
            double z = Double.parseDouble(part[2]);
            loc = new Location(world , x, y, z);
        }
        return loc;
    }

    public static Location getChestString(String s) {
        Location loc = null;
        if (s != null && !s.equalsIgnoreCase("null")) {
            String[] part = s.split(" ");
            double x = Double.parseDouble(part[0]);
            double y = Double.parseDouble(part[1]);
            double z = Double.parseDouble(part[2]);
            loc = new Location(Bukkit.getWorld("world"), x, y, z);
        }
        return loc;
    }

    public static ItemStack getItemStackString(String s) {
        ItemStack item = null;
        if (!s.equalsIgnoreCase("null")) {
            String[] part = s.split("-");
            String id = part[0];
            short meta = Short.parseShort(part[1]);
            item = new ItemStack(Material.getMaterial(id), 1, meta);
        }
        return item;
    }

    public static void LoadSaveRegions(String eventoName, Map<Location, String> saveBlocks, Map<Location, Inventory> saveItemsBlock) {
        File fileSave = new File(EventosTTC.getMain().getDataFolder(), "SaveRegions");
        if (fileSave.exists() && fileSave.isDirectory()) {
            File[] files = fileSave.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    if (fileName.equals(eventoName)) {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(config.getString("World"));
                        ConfigurationSection blocks = config.getConfigurationSection("Blocks");
                        for (String locString : blocks.getKeys(false)) {
                            Location loc = getLocationString(locString, world);
                            String id = blocks.getString(locString);
                            saveBlocks.put(loc, id);
                        }
                        if (config.contains("InventoryBlocks")) {
                            ConfigurationSection inventoryBlocks = config.getConfigurationSection("InventoryBlocks");
                            for (String locString : inventoryBlocks.getKeys(false)) {
                                Location loc = getLocationString(locString, world);
                                Inventory inv = Bukkit.createInventory(null, 180, "");
                                ConfigurationSection setItem = inventoryBlocks.getConfigurationSection(locString);
                                for (String slotItem : setItem.getKeys(false)) {
                                    String id = setItem.getString(slotItem);
                                    int slot = Integer.parseInt(slotItem);
                                    String[] enchantsItem = id.split("-");
                                    String[] partId = enchantsItem[0].split(" ");
                                    ItemStack item = new ItemStack(Material.getMaterial(partId[0]), Integer.parseInt(partId[2]), Short.parseShort(partId[1]));
                                    ItemMeta meta = item.getItemMeta();
                                    for (int i = 1; i < enchantsItem.length; i++) {
                                        String[] enchant = enchantsItem[i].split(" ");
                                        meta.addEnchant(Enchantment.getByName(enchant[0]), Integer.parseInt(enchant[1]), true);
                                    }
                                    item.setItemMeta(meta);
                                    inv.setItem(slot, item);
                                    saveItemsBlock.put(loc, inv);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
