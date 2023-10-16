package com.totalcraft.soled;

import com.totalcraft.soled.PlayerData.PlayerEventoData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Utils.PrefixMsg.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class EventoShop implements Listener {
    public static Inventory openEventoShop(Player player) {
        Inventory eventoShop = Bukkit.createInventory(null, 54, getPmTTC("&bShop"));
        ItemStack glassInfo = new ItemStack(Material.THIN_GLASS);
        ItemMeta glassMeta = glassInfo.getItemMeta();
        glassMeta.setDisplayName(getPmTTC("&bShop"));
        glassInfo.setItemMeta(glassMeta);
        eventoShop.setItem(0, glassInfo);
        eventoShop.setItem(1, glassInfo);
        eventoShop.setItem(2, glassInfo);
        eventoShop.setItem(3, glassInfo);
        eventoShop.setItem(4, glassInfo);
        eventoShop.setItem(5, glassInfo);
        eventoShop.setItem(6, glassInfo);
        eventoShop.setItem(7, glassInfo);
        eventoShop.setItem(8, glassInfo);
        eventoShop.setItem(9, glassInfo);
        eventoShop.setItem(17, glassInfo);
        eventoShop.setItem(18, glassInfo);
        eventoShop.setItem(26, glassInfo);
        eventoShop.setItem(27, glassInfo);
        eventoShop.setItem(35, glassInfo);
        eventoShop.setItem(36, glassInfo);
        eventoShop.setItem(44, glassInfo);
        eventoShop.setItem(45, glassInfo);
        eventoShop.setItem(46, glassInfo);
        eventoShop.setItem(47, glassInfo);
        eventoShop.setItem(48, glassInfo);
        eventoShop.setItem(50, glassInfo);
        eventoShop.setItem(51, glassInfo);
        eventoShop.setItem(52, glassInfo);
        eventoShop.setItem(53, glassInfo);

        PlayerEventoData playerData = getPlayerData(player.getName());
        int points = playerData.getPointsShop();
        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemMeta goldMeta = gold.getItemMeta();
        goldMeta.setDisplayName(getPmTTC("&bShop"));
        List<String> lore = new ArrayList<>();
        lore.add(getFormatColor("&bPontos: &f" + points));
        goldMeta.setLore(lore);
        gold.setItemMeta(goldMeta);
        eventoShop.setItem(49, gold);
        return eventoShop;
    }

    private static ItemStack createItemShop(Material material, int meta, int valor) {
        ItemStack shop = new ItemStack(material, 1, (short) meta);
        ItemMeta shopMeta = shop.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(getFormatColor("&eValor:&f " + valor));
        shopMeta.setLore(lore);
        shop.setItemMeta(shopMeta);
        return shop;
    }

    @EventHandler
    public void shop(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (inv == null || inv.getName() == null || player == null || item == null) {
            return;
        }
        String invName = inv.getName();
        if (!invName.equals(getPmTTC("&bShop"))) {
            return;
        }
        event.setCancelled(true);
        PlayerEventoData playerData = getPlayerData(player.getName());
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            String lore = item.getItemMeta().getLore().get(0);
            if (lore.contains("Valor:")) {
                int value = Integer.parseInt(lore.split(" ")[1]);
                player.closeInventory();
                if (!playerData.hasPointsShop(value)) {
                    player.sendMessage(getPmTTC("&cVocê não tem pontos suficientes para adquirir este item"));
                    return;
                }
                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(getPmTTC("&cVocê precisa de ao menos 1 slot para adquirir este item"));
                    return;
                }
                playerData.removePointsShop(value);
                player.getInventory().addItem(new ItemStack(item.getType(), item.getAmount(), item.getDurability()));
                player.sendMessage(getPmTTC("&aVocê adquiriu este item com Sucesso"));
            }
        }
    }

}
