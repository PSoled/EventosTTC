package com.totalcraft.soled;

import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Command.*;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Tasks.StartingEvento.movePlayers;
import static com.totalcraft.soled.Utils.EventoUtils.*;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class EventoListener implements Listener {
    List<String> playerQuit = new ArrayList<>();

    @EventHandler
    public void playerQuitEvento(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        showPlayers(player);
        if (playersEvento.containsKey(player.getName())) {
            Inventory inv = player.getInventory();
            for (ItemStack give : eventoCurrent.getItemsGive()) {
                for (int i = 0; i < inv.getSize() - 1; i++) {
                    ItemStack item = inv.getItem(i);
                    if (item != null && item.getTypeId() == give.getTypeId() && item.getDurability() == give.getDurability()) {
                        inv.removeItem(item);
                    }
                }
            }
            playersEvento.remove(player.getName());
            playerQuit.add(player.getName());
            for (String nick : playersEvento.keySet()) {
                Player eventoPlayer = Bukkit.getPlayer(nick);
                eventoPlayer.sendMessage(getPmTTC("&b" + player.getName() + " &fDeslogou durante o evento"));
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (playerQuit.contains(player.getName())) {
            player.sendMessage(getPmTTC("&cOpa amigo, parece que você kitou durante o evento."));
            player.sendMessage(getPmTTC("&bCaso queira sair também pode usar /evento sair"));
            playerQuit.remove(player.getName());
        }
        PlayerEventoData playerData = getPlayerData(player.getName());
        if (playerData.getChestItems() != null) {
            loadLocationChest(player, playerData.getChestItems());
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        if (eventoStarted) {
            Player player = event.getPlayer();
            if (playersEvento.containsKey(player.getName())) {
                if (!movePlayers) {
                    Location from = event.getFrom();
                    Location to = event.getTo();
                    if (to.getBlockZ() != from.getBlockZ() || to.getBlockX() != from.getBlockX()) {
                        player.teleport(new Location(from.getWorld(), from.getBlockX() + 0.5, from.getBlockY(), from.getBlockZ() + 0.5, to.getYaw(), to.getPitch()));
                    }
                } else {
                    Location loc = player.getLocation();
                    if (loc.getY() < 0) {
                        PlayerLeaveEvento(player);
                        player.sendMessage(getPmTTC("&cGAME OVER &fPara você meu amigo"));
                        for (String nick : playersEvento.keySet()) {
                            Player eventoPlayer = Bukkit.getPlayer(nick);
                            eventoPlayer.sendMessage(getPmTTC("&b" + player.getName() + " &fCaiu no void KKKK"));
                        }
                    }
                }
            }
        }
    }
    public static Map<String, Location> playerRespawn = new HashMap<>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (eventoStarted) {
            if (playersEvento.containsKey(player.getName())) {
                Player killer = player.getKiller();
                if (eventoCurrent.getType() instanceof SkyWars) {
                    for (ItemStack item : player.getInventory()) {
                        if (item != null) {
                            player.getWorld().dropItem(player.getLocation(), item);
                        }
                    }
                }
                playerRespawn.put(player.getName(), playersEvento.get(player.getName()));
                PlayerLeaveEvento(player);
                player.sendMessage(getPmTTC("&cGAME OVER &fPara você meu amigo"));
                if (killer != null) {
                    for (String nick : playersEvento.keySet()) {
                        Player eventoPlayer = Bukkit.getPlayer(nick);
                        eventoPlayer.sendMessage(getPmTTC("&b" + player.getName() + " &fMorreu para o &b" + killer.getName() + " &fno evento"));
                    }
                } else {
                    for (String nick : playersEvento.keySet()) {
                        Player eventoPlayer = Bukkit.getPlayer(nick);
                        eventoPlayer.sendMessage(getPmTTC("&b" + player.getName() + " &fMorreu no evento"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (playerRespawn.containsKey(player.getName())) {
            player.teleport(playerRespawn.get(player.getName()));
        }
    }


    @EventHandler
    public void cancelClickRanking(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (inv != null && inv.getName() != null) {
            String name = getPmTTC("&0Ranking");
            if (inv.getName().equals(name)) event.setCancelled(true);
        }
    }

    public static Map<String, Integer> kickPlayer = new HashMap<>();

    @EventHandler
    public void BlockPlace(BlockPlaceEvent event) {
        if (eventoStarted) {
            if (eventoCurrent.getType() instanceof Running || eventoCurrent.getType() instanceof Spleef) {
                Player player = event.getPlayer();
                if (playersEvento.containsKey(player.getName())) {
                    if (getAdm(player)) {
                        if (kickPlayer.containsKey(player.getName())) {
                            int kick = kickPlayer.get(player.getName());
                            if (kick > 9) {
                                player.sendMessage(getPmTTC("&cVocê foi expulso do evento"));
                                PlayerLeaveEvento(player);
                                for (String nick : playersEvento.keySet()) {
                                    Player eventoPlayer = Bukkit.getPlayer(nick);
                                    eventoPlayer.sendMessage(getPmTTC("&b" + player.getName() + " &fFoi expulso do evento"));
                                }
                            } else {
                                kick++;
                                player.sendMessage(getPmTTC("&cAviso " + kick + "/10: Por favor não tente colocar blocos durante o evento. Você será expulso"));
                                kickPlayer.put(player.getName(), kick);
                            }
                        } else {
                            kickPlayer.put(player.getName(), 1);
                            player.sendMessage(getPmTTC("&cAviso 1/10: Por favor não tente colocar blocos durante o evento. Você será expulso"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (eventoStart && playersEvento.containsKey(player.getName()) && getAdm(player)) {
            player.sendMessage(getPmTTC("&cVocê não pode dropar itens durante o evento"));
            event.setCancelled(true);
        }
    }

    public static List<String> cmdAllowed = new ArrayList<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        if (getAdm(player)) {
            if (eventoStart && playersEvento.containsKey(player.getName())) {
                if (msg.contains(" ")) {
                    msg = msg.split(" ")[0];
                }
                for (String cmd : cmdAllowed) {
                    if (cmd.equalsIgnoreCase(msg)) {
                        return;
                    }
                }
                player.sendMessage(getPmTTC("&cVocê não pode utilizar este comando durante o evento."));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!eventoStarted) return;
        if (event.isFlying()) {
            if (getAdm(player)) {
                if (playersEvento.containsKey(player.getName())) {
                    player.sendMessage(getPmTTC("&cSeu fly foi desativado no evento"));
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    event.setCancelled(true);
                }
            }
        }
    }

}
