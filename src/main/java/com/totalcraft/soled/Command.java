package com.totalcraft.soled;

import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.PlayerData.PlayerEventoData;
import mods.tinker.tconstruct.TConstruct;
import mods.tinker.tconstruct.util.player.TPlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.EventoBase.*;
import static com.totalcraft.soled.EventoConfig.eventoSetRemove;
import static com.totalcraft.soled.EventoRanking.openEventosRanking;
import static com.totalcraft.soled.EventoShop.openEventoShop;
import static com.totalcraft.soled.Eventos.Spleef.replaceSnow;
import static com.totalcraft.soled.PlayerData.PlayerEventoData.getPlayerData;
import static com.totalcraft.soled.Tasks.AutoStartEvento.tz;
import static com.totalcraft.soled.Tasks.StartingEvento.*;
import static com.totalcraft.soled.Utils.EventoUtils.*;
import static com.totalcraft.soled.Utils.PrefixMsg.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class Command implements CommandExecutor {
    public static EventoBase eventoCurrent;
    public static boolean eventoStart, eventoStarted, eventoStop;
    public static Map<String, Location> playersEvento = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        switch (args.length > 0 ? args[0].toLowerCase() : "") {
            case "entrar":
            case "sair":
            case "iniciar":
            case "parar":
            case "criar":
            case "deletar":
            case "info":
            case "config":
            case "criados":
            case "ver":
            case "ranking":
            case "shop":
            case "pontos":
            case "anuncio":
            case "camarote":
            case "forçarinicio":
            case "reload":
            case "teste":
                break;
            default:
                sender.sendMessage(getCommandEvento(sender));
                return true;
        }
        if (args[0].equalsIgnoreCase("entrar")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            if (!eventoStart) {
                player.sendMessage(getPmTTC("&cNão há nenhum evento iniciado"));
                return true;
            }
            if (playersEvento.containsKey(player.getName())) {
                player.sendMessage(getPmTTC("&cOpa, parece que você já está no evento"));
                return true;
            }
            if (eventoStarted) {
                player.sendMessage(getPmTTC("&cEste evento já iniciou"));
                return true;
            }
            if (eventoCurrent.getMaxPlayers() > 0) {
                if (playersEvento.size() > eventoCurrent.getMaxPlayers()) {
                    player.sendMessage(getPmTTC("&cEste evento já está na capacidade máxima de players"));
                    return true;
                }
            }
            if (!eventoCurrent.entryItems) {
                if (getAdm(player)) {
                    boolean contains = false;
                    for (ItemStack item : player.getInventory()) {
                        if (item != null) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        player.sendMessage(getPmTTC("&cPara entrar neste evento você precisa esvaziar seu inventário inteiro"));
                        return true;
                    }
                }
            }

//            TPlayerStats stats = TConstruct.playerTracker.getPlayerStats(player.getName());
//            net.minecraft.item.ItemStack[] inv = stats.armor.inventory;
//            boolean contain = false;
//            for (net.minecraft.item.ItemStack item : inv) {
//                if (item != null) {
//                    contain = true;
//                    break;
//                }
//            }
//            if (contain) {
//                player.sendMessage(getPmTTC("&cPara entrar neste evento você precisar limpar o Inventário que você abre ao apertar a letra O"));
//                player.sendMessage(getPmTTC("&fOBS: &cAté mesmo os corações adicionais"));
//                return true;
//            }

            PlayerEventoData playerData = getPlayerData(player.getName());
            if (eventoCurrent.saveItems) {
                playerData.setChestItems(saveLocationChest(player));
            }
            playersEvento.put(player.getName(), player.getLocation());
            if (eventoCurrent.getHub() != null) {
                eventoCurrent.getHub().getChunk().load();
                player.teleport(eventoCurrent.getHub());
            }
            for (ItemStack item : eventoCurrent.getItemsGive()) {
                player.getInventory().addItem(item);
            }
            player.sendMessage(getPmTTC("&bVocê entrou no evento"));
            hidePlayers(player, playerData.hidePlayers);
            if (eventoCurrent.getType() instanceof Running) {
                player.sendMessage("");
                if (playerData.hidePlayers) {
                    player.sendMessage(getPmTTC("&aTodos os players ficaram escondidos durante o evento"));
                    player.sendMessage(getPmTTC("&bCaso queira ver eles denovo use /evento ver"));
                } else {
                    player.sendMessage(getPmTTC("&aOs players estão aparecendo para você"));
                    player.sendMessage(getPmTTC("&bCaso queira esconder eles use /evento ver"));
                }
            }

            if (playersEvento.size() > 0) {
                for (String nick : playersEvento.keySet()) {
                    Bukkit.getPlayer(nick).sendMessage(getPmTTC(player.getName() + " &aEntrou no Evento"));
                }
            }


            return true;
        }
        if (args[0].equalsIgnoreCase("sair")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            if (!playersEvento.containsKey(player.getName())) {
                player.sendMessage(getPmTTC("&cMas você nem está no evento, bobinho"));
                return true;
            }
            PlayerLeaveEvento(player);
            player.sendMessage(getPmTTC("&bVocê saiu do evento"));
            for (String nick : playersEvento.keySet()) {
                Player eventoPlayer = Bukkit.getPlayer(nick);
                eventoPlayer.sendMessage(getPmTTC("&b" + player.getName() + " &fSaiu do evento"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("camarote")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            if (!eventoStart) {
                player.sendMessage(getPmTTC("&cNão há nenhum evento iniciado"));
                return true;
            }
            if (eventoCurrent.getCamarote() == null) {
                player.sendMessage(getPmTTC("&cEste evento não contém um camarote"));
                return true;
            }
            player.teleport(eventoCurrent.getCamarote());
            player.sendMessage(getPmTTC("&aTeleportado para o Camarote do Evento com Sucesso"));
            return true;
        }



        if (args[0].equalsIgnoreCase("ver")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            PlayerEventoData playerData = getPlayerData(player.getName());
            if (playerData.hidePlayers) {
                playerData.setHidePlayers(false);
                player.sendMessage(getPmTTC("&aVocê fez todos os Players aparecerem"));
            } else {
                playerData.setHidePlayers(true);
                player.sendMessage(getPmTTC("&aVocê escondeu os Players"));
            }
            if ((eventoStart) && (eventoCurrent.getType() instanceof Running))
                hidePlayers(player, playerData.hidePlayers);
            else
                player.sendMessage(getPmTTC("&cAVISO: &fIsto funciona apenas quando você esta em eventos especificos do Server"));
            return true;
        }

        if (args[0].equalsIgnoreCase("pontos")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            PlayerEventoData playerData = getPlayerData(player.getName());
            player.sendMessage(getPmTTC("&fSeus pontos de eventos é &a" + playerData.getPointsShop()));
            return true;
        }

        if (args[0].equalsIgnoreCase("ranking")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            player.openInventory(openEventosRanking(player));
            return true;
        }
        if (args[0].equalsIgnoreCase("shop")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            player.openInventory(openEventoShop(player));
            return true;
        }

        if (args[0].equalsIgnoreCase("anuncio")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            PlayerEventoData playerData = getPlayerData(player.getName());
            if (playerData.announceOption) {
                player.sendMessage(getPmTTC("&aVocê desativou o anúncio de eventos para você"));
                playerData.setAnnounceOption(false);
            } else {
                player.sendMessage(getPmTTC("&aVocê ativou o anúncio de eventos para você"));
                playerData.setAnnounceOption(true);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            if (eventoStart) {
                if (sender instanceof Player && getAdm(sender)) {
                    sender.sendMessage(eventoInfo(eventoCurrent));
                    sender.sendMessage("\n§9>> §ePlayers no evento: §f" + playersEvento.size());
                } else {
                    sender.sendMessage(eventoInfoAdm(eventoCurrent));
                    sender.sendMessage("\n§9>> §ePlayers no evento: §f" + playersEvento.size());
                }
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage(getPmTTC("&cNenhum evento ativo, Use: /evento info (evento)"));
                return true;
            }
            if (!events.containsKey(args[1])) {
                sender.sendMessage(getPmTTC("&cNão existe esse evento"));
                return true;
            }
            EventoBase evento = getEvent(args[1]);
            if (sender instanceof Player && getAdm(sender)) {
                sender.sendMessage(eventoInfo(evento));
            } else {
                sender.sendMessage(eventoInfoAdm(evento));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("criados")) {
            sender.sendMessage(getPmTTC("&bEventos criados:" +
                    "\n&9>> &eNome &0-- &fTipo"));
            sender.sendMessage("");
            StringBuilder sb = new StringBuilder();
            for (String event : events.keySet()) {
                EventoBase evento = getEvent(event);
                sb.append("§9>> §e").append(event).append(" §0-- §f ").append(evento.getType().getClass().getSimpleName()).append("\n");
            }
            sender.sendMessage(sb.toString());
            return true;
        }

        if (sender instanceof Player) {
            if (getAdm(sender)) {
                sender.sendMessage(getPmNotPerm());
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("iniciar")) {
            if (args.length != 2) {
                sender.sendMessage(getPmTTC("&cUse: /evento iniciar (evento)"));
                sender.sendMessage(getPmTTC("&bEventos criados:"));
                StringBuilder sb = new StringBuilder();
                for (String event : events.keySet()) {
                    EventoBase evento = getEvent(event);
                    sb.append("§9>> §e").append(event).append(" §0-- §f ").append(evento.getType().getClass().getSimpleName()).append("\n");
                }
                sender.sendMessage(sb.toString());
                return true;
            }
            if (eventoStart) {
                sender.sendMessage(getPmTTC("&cJá tem um evento acontecendo"));
                return true;
            }
            if (!events.containsKey(args[1])) {
                sender.sendMessage(getPmTTC("&cEvento não existe"));
                return true;
            }
            EventoBase evento = getEvent(args[1]);
            if (evento.containBasicEvento()) {
                sender.sendMessage(getPmTTC("&cEste evento não contém dados necessários para ser iniciado"));
                return true;
            }
            startEvento(evento);
            sender.sendMessage(getPmTTC("&fVocê iniciou o evento &b" + evento.getName()));
        }

        if (args[0].equalsIgnoreCase("parar")) {
            if (!eventoStart) {
                sender.sendMessage(getPmTTC("&cNão há nenhum evento iniciado"));
                return true;
            }
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

        if (args[0].equalsIgnoreCase("forçarinicio")) {
            if (!eventoStart) {
                sender.sendMessage(getPmTTC("&cNão há um evento sendo iniciado"));
                return true;
            }
            if (eventoStarted) {
                sender.sendMessage(getPmTTC("&cEvento já foi iniciado"));
                return true;
            }
            cancelTask(scheduledStarting);
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerEventoData playerData = getPlayerData(player.getName());
                if (playerData.announceOption) {
                    player.sendMessage("");
                    player.sendMessage(getPmTTC("&fEvento &b" + eventoCurrent.getName() + " &fIniciando !!!"));
                    player.sendMessage("");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
            }
            countdownEvent();
        }

        if (args[0].equalsIgnoreCase("criar")) {
            if (args.length < 3) {
                sender.sendMessage(getPmTTC("&cUse: /evento criar (nome) (tipo)"));
                sender.sendMessage(getPmTTC("&bTipos de eventos que podem ser criado:" +
                        "\n&e" + TiposEventos));
                return true;
            }
            boolean contain = false;
            for (String evento : TiposEventos) {
                if (evento.equalsIgnoreCase(args[2])) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                sender.sendMessage(getPmTTC("&cEste tipo de evento não existe"));
                return true;
            }
            if (events.containsKey(args[1])) {
                sender.sendMessage(getPmTTC("&cJá existe um evento com este nome"));
                return true;
            }
            newEvent(args[1], args[2]);
            String typeName = getEvent(args[1]).getType().getClass().getSimpleName();
            sender.sendMessage(getPmTTC("&fEvento &b" + args[1] + " &fdo tipo &b" + typeName + " &fcriado com Sucesso!"));
        }

        if (args[0].equalsIgnoreCase("deletar")) {
            if (args.length != 2) {
                sender.sendMessage(getPmTTC("&cUse: /evento deletar (evento)"));
                return true;
            }
            if (!events.containsKey(args[1])) {
                sender.sendMessage(getPmTTC("&cEste evento não existe"));
                return true;
            }
            deleteEvent(args[1]);
            sender.sendMessage(getPmTTC("&aVocê deletou o evento com sucesso"));
        }

        if (args[0].equalsIgnoreCase("config")) {
            if (args.length > 1) {
                if (!events.containsKey(args[1])) {
                    sender.sendMessage(getPmTTC("&cNão existe esse evento"));
                    return true;
                }
            }
            if (args.length == 1) {
                sender.sendMessage(getPmTTC("&bEventos criados:" +
                        "\n&9>> &eNome &0-- &fTipo"));
                sender.sendMessage("");
                StringBuilder sb = new StringBuilder();
                for (String event : events.keySet()) {
                    EventoBase evento = getEvent(event);
                    sb.append("§9>> §e").append(event).append(" §0-- §f ").append(evento.getType().getClass().getSimpleName()).append("\n");
                }
                sender.sendMessage(sb.toString());
                sender.sendMessage(getPmTTC("&cUse: /evento config (evento)"));
                return true;
            }
            if (args.length == 2) {
                EventoBase evento = getEvent(args[1]);
                sender.sendMessage(getConfigEvento(evento));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                sender.sendMessage(getPmTTC("&bConfigure os eventos pela config e use /evento reloadconfig"));
                return true;
            }
            if (args.length < 4) {
                sender.sendMessage(getPmTTC("&cUse: /evento config &b" + args[1] + " &c(set/remove) (método) (opcional)"));
                return true;
            }
            EventoBase evento = getEvent(args[1]);
            Player player = (Player) sender;
            String optional = null;
            StringBuilder sb;
            if (args.length > 4) {
                sb = new StringBuilder();
                for (int i = 4; i < args.length; i++) {
                    if (i == 4) {
                        sb.append(args[i]);
                    } else if (i == 5) {
                        sb.append(" ").append(args[i]).append(" ");
                    } else {
                        sb.append(args[i]).append(" ");
                    }
                }
                optional = sb.toString();
            }
            eventoSetRemove(player, evento, args[2], args[3], optional);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Calendar cal = Calendar.getInstance(tz);
            Date date = cal.getTime();
            sender.sendMessage(date.getHours() + " " + date.getMinutes());
        }

        if (args[0].equalsIgnoreCase("teste")) {
            if (args[1].equalsIgnoreCase("points")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                PlayerEventoData playerData = getPlayerData(player.getName());
                playerData.addPointsShop(1000);
            }

            if (args[1].equalsIgnoreCase("savechest")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                saveLocationChest(player);
            }
            if (args[1].equalsIgnoreCase("loadchest")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                Location start = new Location(player.getWorld(), 108, 72, 214);
                loadLocationChest(player, start);
            }
            if (args[1].equalsIgnoreCase("replacesnow")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                replaceSnow(player.getLocation().subtract(0, 1, 0));
                player.sendMessage(getPmTTC("Colocado neve no seu pé :V"));
            }

        }
        return true;
    }
}
