package com.totalcraft.soled.Utils;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.EventosTTC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Utils {

    public static boolean getAdm(CommandSender sender) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        return !user.has("eventosttc.admin") && !sender.isOp();
    }

    public static boolean getPerm(CommandSender sender, String perm) {
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        return user.has(perm) || sender.isOp();
    }


}
