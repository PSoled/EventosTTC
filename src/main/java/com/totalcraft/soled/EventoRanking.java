package com.totalcraft.soled;

import com.totalcraft.soled.PlayerData.PlayerEventoData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Tasks.PointsRanking.TopRanking;
import static com.totalcraft.soled.Tasks.PointsRanking.TopRankingNumber;
import static com.totalcraft.soled.Utils.PrefixMsg.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class EventoRanking {

    public static void setItemsRanking(Inventory eventoRanking) {
        ItemStack glassInfo = new ItemStack(Material.THIN_GLASS);
        ItemMeta glassMeta = glassInfo.getItemMeta();
        glassMeta.setDisplayName(getFormatColor(getPmTTC("&bShop")));
        glassInfo.setItemMeta(glassMeta);
        eventoRanking.setItem(0, glassInfo);
        eventoRanking.setItem(1, glassInfo);
        eventoRanking.setItem(2, glassInfo);
        eventoRanking.setItem(3, glassInfo);
        eventoRanking.setItem(4, glassInfo);
        eventoRanking.setItem(5, glassInfo);
        eventoRanking.setItem(6, glassInfo);
        eventoRanking.setItem(7, glassInfo);
        eventoRanking.setItem(8, glassInfo);
        eventoRanking.setItem(9, glassInfo);
        eventoRanking.setItem(17, glassInfo);
        eventoRanking.setItem(18, glassInfo);
        eventoRanking.setItem(26, glassInfo);
        eventoRanking.setItem(27, glassInfo);
        eventoRanking.setItem(28, glassInfo);
        eventoRanking.setItem(29, glassInfo);
        eventoRanking.setItem(30, glassInfo);
        eventoRanking.setItem(31, glassInfo);
        eventoRanking.setItem(32, glassInfo);
        eventoRanking.setItem(33, glassInfo);
        eventoRanking.setItem(34, glassInfo);
        eventoRanking.setItem(35, glassInfo);
    }

    public static Inventory openEventosRanking(Player player) {
        Inventory eventoRanking = Bukkit.createInventory(null, 36, getPmTTC("&0Ranking"));
        setItemsRanking(eventoRanking);
        PlayerEventoData pData = getPlayerData(player.getName());
        ItemStack head = new ItemStack(397, 1, (short) 3);
        ItemMeta metaHead = head.getItemMeta();
        metaHead.setDisplayName(getFormatColor("&b" + "º" + TopRanking.get(player.getName()) + " &f" + player.getName()));
        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add("");
        for (Map.Entry<String, Integer> evento : pData.getPointsEvents().entrySet()) {
            String loreEvento = evento.getKey();
            if (loreEvento.equalsIgnoreCase("TotalPoints")) {
                loreEvento = "Total Ganhos";
                lorePlayer.add("");
                lorePlayer.add(getFormatColor("&e" + loreEvento + " &9--&6 " + evento.getValue()));
            } else {
                lorePlayer.add(getFormatColor("&e" + loreEvento + " &9--&6 " + evento.getValue()));
            }
        }
        metaHead.setLore(lorePlayer);
        head.setItemMeta(metaHead);
        eventoRanking.setItem(31, head);
        int i = 10;
        for (int a = 1; a < 15; a++) {
            String name = TopRankingNumber.get(a);
            if (i == 26) break;
            PlayerEventoData playerData = getPlayerData(name);
            ItemMeta meta = head.getItemMeta();
            meta.setDisplayName(getFormatColor("&b" + "º" + a + " &f" + name));
            List<String> lore = new ArrayList<>();
            lore.add("");
            for (Map.Entry<String, Integer> evento : playerData.getPointsEvents().entrySet()) {
                String loreEvento = evento.getKey();
                if (loreEvento.equalsIgnoreCase("TotalPoints")) {
                    loreEvento = "Total Ganhos";
                    lore.add("");
                    lore.add(getFormatColor("&e" + loreEvento + " &9--&6 " + evento.getValue()));
                } else {
                    lore.add(getFormatColor("&e" + loreEvento + " &9--&6 " + evento.getValue()));
                }
            }
            meta.setLore(lore);
            head.setItemMeta(meta);
            if (i == 17) {
                i = 19;
            }
            eventoRanking.setItem(i, head);
            i++;
        }
        return eventoRanking;
    }
}
