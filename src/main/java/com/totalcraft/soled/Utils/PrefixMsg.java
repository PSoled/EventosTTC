package com.totalcraft.soled.Utils;

import com.totalcraft.soled.EventoBase;
import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.EventoBase.methodsBase;
import static com.totalcraft.soled.Eventos.Running.methodsRunning;
import static com.totalcraft.soled.Eventos.SkyWars.methodsSkyWars;
import static com.totalcraft.soled.Eventos.Spleef.methodsSpleef;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class PrefixMsg {
    public static String getPmTTC(String message) {
        return ChatColor.translateAlternateColorCodes('&', "&7&l[&6&lTTC&7&l]&r ") + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getFormatColor(String message) {
        String msg = null;
        if (message != null) msg = ChatColor.translateAlternateColorCodes('&', message);
        return msg;
    }

    public static String getPmNotPerm() {
        return getPmTTC("&cVocê não tem permissão para executar este comando.");
    }

    public static String getPmConsole() {
        return getPmTTC("&cEste comando só pode ser executado por um jogador.");
    }

    public static String getCommandEvento(CommandSender sender) {
        String msg = getPmTTC("&bComandos do Evento" +
                "\n&a/evento entrar &fEntrar no Evento" +
                "\n&a/evento sair &fSair do Evento" +
                "\n&a/evento info &fInfomações dos Evento" +
                "\n&a/evento criados &fMostrar os Eventos Criados" +
                "\n&a/evento ver &fDesativar ou Ativar a Invisibilidade dos Players" +
                "\n&a/evento ranking &fRanking dos Tops Players" +
                "\n&a/evento shop &fShop do Evento" +
                "\n&a/evento pontos &fPontos de Evento Para Gastar no Shop" +
                "\n&a/evento anuncio &fDesativar ou Ativar o Anúncio de Eventos");
        String adm = getFormatColor("\n&c/evento porra &fLotado de porra" +
                "\n&c/evento realmente &fLOTADO");
        if (!getAdm(sender)) {
            return msg + adm;
        }
        return msg;
    }

    public static String eventoInfoAdm(EventoBase evento) {
        String msg = null;
        StringBuilder info = null;
        if (evento.getType() instanceof Running) {
            Running running = (Running) evento.getType();
            msg = "\n" + getPmTTC("&bSetters e Removes do Running\n&9>> &e" + methodsRunning);
            info = new StringBuilder();
            info.append("\n\n§9>> §eBloco de checkpoint: §f").append(running.toStringCheckpoint()).append("\n§9>> §eBloco de respawn: §f").append(running.toStringRespawn()).append("\n§9>> §eBloco de winner: §f").append(running.toStringWinner());
        }

        if (evento.getType() instanceof Spleef) {
            Spleef spleef = (Spleef) evento.getType();
            msg = "\n" + getPmTTC("&bSetters e Removes do Spleef\n&9>> &e" + methodsSpleef);
            info = new StringBuilder();
            info.append("\n\n§9>> §eAuto quebrar blocos de neve: §f").append(spleef.getBreakAutoSnow());

            if (spleef.getAutoReplaceSnow().size() > 0) {
                int num = 0;
                for (Location loc : spleef.getAutoReplaceSnow()) {
                    num++;
                    info.append("\n§9>> §eAuto Replace Snow ").append(num).append(" §6World: §f").append(loc.getWorld().getName()).append(" §6X: §f").append(loc.getBlockX()).append(" §6Y: §f").append(loc.getBlockY()).append(" §6X: §F").append(loc.getBlockZ());
                }
            } else {
                info.append("\n§9>> §eSem Localizações de Auto Replace Snow");
            }
        }

        if (evento.getType() instanceof SkyWars) {
            SkyWars skyWars = (SkyWars) evento.getType();
            msg = "\n" + getPmTTC("&bSetters e Removes do Skywars\n&9>> &e" + methodsSkyWars);
            info = new StringBuilder();
            if (skyWars.getRegion1() != null) {
                Location loc = skyWars.getRegion1();
                info.append("\n\n§9>> §eLocalização da Região 1:").append(" §6World: §f").append(loc.getWorld().getName()).append(" §6X: §f").append(loc.getBlockX()).append(" §6Y: §f").append(loc.getBlockY()).append(" §6X: §F").append(loc.getBlockZ());
            } else {
                info.append("\n\n§9>> §eSem localização da Região 1");
            }
            if (skyWars.getRegion2() != null) {
                Location loc = skyWars.getRegion2();
                info.append("\n§9>> §eLocalização da Região 2:").append(" §6World: §f").append(loc.getWorld().getName()).append(" §6X: §f").append(loc.getBlockX()).append(" §6Y: §f").append(loc.getBlockY()).append(" §6X: §F").append(loc.getBlockZ());
            } else {
                info.append("\n§9>> §eSem localização da Região 2");
            }
            if (skyWars.getSaveBlocks().size() > 0) {
                info.append("\n§9>> §eOs Blocos estão salvos");
            } else {
                info.append("\n§9>> §eOs Blocos §cnão §eestão salvos");
            }
        }


        StringBuilder sb = new StringBuilder("\n " + getPmTTC("&aInformações do Evento &b" + evento.getName()));
        sb.append("\n§9>> §eTipo: §f").append(evento.getType().getClass().getSimpleName());
        if (evento.getLocation().size() > 0) {
            int num = 0;
            for (Location loc : evento.getLocation()) {
                num++;
                sb.append("\n§9>> §eLocalização ").append(num).append(" §6World: §f").append(loc.getWorld().getName()).append(" §6X: §f").append(loc.getBlockX()).append(" §6Y: §f").append(loc.getBlockY()).append(" §6X: §F").append(loc.getBlockZ());
            }
        } else {
            sb.append("\n§9>> §eSem Localizações");
        }
        if (evento.getHub() != null) {
            Location loc = evento.getHub();
            sb.append("\n§9>> §eLocalização do Hub ").append(" §6World: §f").append(loc.getWorld().getName()).append(" §6X: §f").append(loc.getBlockX()).append(" §6Y: §f").append(loc.getBlockY()).append(" §6X: §F").append(loc.getBlockZ());
        } else {
            sb.append("\n§9>> §eSem Localização de Hub");
        }
        if (evento.getCamarote() != null) {
            Location loc = evento.getCamarote();
            sb.append("\n§9>> §eLocalização do Camarote ").append(" §6World: §f").append(loc.getWorld().getName()).append(" §6X: §f").append(loc.getBlockX()).append(" §6Y: §f").append(loc.getBlockY()).append(" §6X: §F").append(loc.getBlockZ());
        } else {
            sb.append("\n§9>> §eSem Localização de Camarote");
        }
        sb.append("\n§9>> §eMáximo de Players: §f").append(evento.getMaxPlayers() == 0 ? "Ilimitado" : evento.getMaxPlayers());
        sb.append("\n§9>> §eMinimo de Players: §f").append(evento.getMinPlayers());
        sb.append("\n§9>> §eQuantidade de Chamados: §f").append(evento.getNumberCalls());
        sb.append("\n§9>> §eIntervalo entre as Chamadas: §f").append(evento.getIntervalCalls());
        sb.append("\n§9>> §eMensagem de premiação: §f").append(getFormatColor(evento.getRewardMsg()));
        if (evento.getRewardConsole().size() > 0) {
            for (String cmd : evento.getRewardConsole()) {
                int num = 1;
                sb.append("\n§9>> §eComando de Console ").append(num).append(" §f").append(getFormatColor(cmd));
            }
        } else {
            sb.append("\n§9>> §eSem Comandos de Console");
        }
        if (evento.getItemsGive().size() > 0) {
            int num = 0;
            for (ItemStack itemStack : evento.getItemsGive()) {
                num++;
                sb.append("\n§9>> §eItem Givado ").append(num).append(" §6ID: §f").append(itemStack.getTypeId()).append(" §6Meta: §f").append(itemStack.getDurability()).append(" §6Quantidade: §f").append(itemStack.getAmount());
            }
        } else {
            sb.append("\n§9>> §eSem Itens para Givar");
        }
        if (evento.getRewardItem().size() > 0) {
            int num = 0;
            for (ItemStack itemStack : evento.getRewardItem()) {
                num++;
                sb.append("\n§9>> §eItem Premiado ").append(num).append(" §6ID: §f").append(itemStack.getTypeId()).append(" §6Meta: §f").append(itemStack.getDurability()).append(" §6Quantidade: §f").append(itemStack.getAmount());
            }
        } else {
            sb.append("\n§9>> §eSem Itens para Premiação");
        }
        sb.append("\n§9>> §ePremiação de Money: §aR$ ").append(evento.getRewardMoney());
        sb.append("\n§9>> §eSalvar itens: §f").append(evento.saveItems);
        sb.append("\n§9>> §ePermitir entrada de itens: §f").append(evento.entryItems);
        sb.append(info);
        sb.append("\n\n");
        sb.append(getPmTTC("&bGetters e Setters do EventoBase")).append("\n§e").append(methodsBase);
        sb.append("\n");
        sb.append(msg);
        return sb.toString();
    }

    public static String getConfigEvento(EventoBase evento) {
        StringBuilder sb = new StringBuilder(getPmTTC("&aConfigurações do Evento &b" + evento.getName()));
        for (String methods : methodsBase) {
            sb.append("\n§e/evento config §b").append(evento.getName()).append(" §e(set/remove) §f").append(methods).append(" §9(Opcional)");
        }

        if (evento.getType() instanceof Running) {
            for (String methods : methodsRunning) {
                sb.append("\n§e/evento config §b").append(evento.getName()).append(" §e(set/remove) §f").append(methods).append(" §9(Opcional)");
            }
        }
        if (evento.getType() instanceof Spleef) {
            for (String methods : methodsSpleef) {
                sb.append("\n§e/evento config §b").append(evento.getName()).append(" §e(set/remove) §f").append(methods).append(" §9(Opcional)");
            }
        }
        if (evento.getType() instanceof SkyWars) {
            for (String methods : methodsSkyWars) {
                sb.append("\n§e/evento config §b").append(evento.getName()).append(" §e(set/remove) §f").append(methods).append(" §9(Opcional)");
            }
        }
        return sb.toString();
    }

    public static String eventoInfo(EventoBase evento) {
        return getPmTTC("&aInformações do Evento &b" + evento.getName()) +
                "\n§9>> §eTipo do Evento: §f" + evento.getType().getClass().getSimpleName() +
                "\n§9>> §eMáximo de Players: §f" + evento.getMaxPlayers() +
                "\n§9>> §eMinimo de Players: §f" + evento.getMinPlayers() +
                "\n§9>> §ePrêmio: " + getFormatColor(evento.getRewardMsg()) +
                "\n§9>> §eSalvar os itens: §f" + evento.saveItems +
                "\n§9>> §ePermitir entrada de itens: §f" + evento.entryItems;
    }

}
