package com.totalcraft.soled;

import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.totalcraft.soled.EventoBase.methodsBase;
import static com.totalcraft.soled.Eventos.Running.methodsRunning;
import static com.totalcraft.soled.Eventos.SkyWars.methodsSkyWars;
import static com.totalcraft.soled.Eventos.Spleef.methodsSpleef;
import static com.totalcraft.soled.Eventos.Spleef.replaceSnow;
import static com.totalcraft.soled.Utils.EventoUtils.*;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class EventoConfig {

    public static void eventoSetRemove(Player player, EventoBase evento, String config, String method, String optional) {
        if (!config.equalsIgnoreCase("set") && !config.equalsIgnoreCase("remove")) {
            player.sendMessage(getPmTTC("&cUse Set ou Remove na Config"));
            return;
        }
        for (String methods : methodsBase) {
            if (methods.equalsIgnoreCase(method)) {
                ConfigBase(player, evento, config, method, optional);
                return;
            }
        }
        if (evento.getType() instanceof Running) {
            for (String methods : methodsRunning) {
                if (methods.equalsIgnoreCase(method)) {
                    configRunning(player, evento, config, method);
                    return;
                }
            }
        }
        if (evento.getType() instanceof Spleef) {
            for (String methods : methodsSpleef) {
                if (methods.equalsIgnoreCase(method)) {
                    configSpleef(player, evento, config, method, optional);
                    return;
                }
            }
        }
        if (evento.getType() instanceof SkyWars) {
            for (String methods : methodsSkyWars) {
                if (methods.equalsIgnoreCase(method)) {
                    configSkyWars(player, evento, config, method);
                    return;
                }
            }
        }
        player.sendMessage(getPmTTC("&cEste método não existe para este evento"));
    }

    private static void ConfigBase(Player player, EventoBase evento, String config, String method, String optional) {
        if (config.equalsIgnoreCase("set")) {
            if (method.equalsIgnoreCase("type")) {
                player.sendMessage(getPmTTC("&cSe você quiser outro tipo de evento, deve criar outro"));
            }

            if (method.equalsIgnoreCase("location")) {
                evento.addLocation(player.getLocation());
                player.sendMessage(getPmTTC("&aVocê adicionou uma nova localização neste Evento com Sucesso!"));
            }

            if (method.equalsIgnoreCase("hub")) {
                evento.setHub(player.getLocation());
                player.sendMessage(getPmTTC("&aVocê setou a localização do Hub neste Evento com Sucesso!"));
            }

            if (method.equalsIgnoreCase("camarote")) {
                evento.setCamarote(player.getLocation());
                player.sendMessage(getPmTTC("&aVocê setou a localização do Camarote neste Evento com Sucesso!"));
            }

            if (method.equalsIgnoreCase("rewardmsg")) {
                if (optional != null) {
                    evento.setRewardMsg(optional);
                    player.sendMessage(getPmTTC("&aVocê setou a mensagem de premiação com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar a mensagem de premiação no argumento Opcional"));
                }
            }
            if (method.equalsIgnoreCase("rewardconsole")) {
                if (optional != null) {
                    evento.addRewardConsole(optional);
                    player.sendMessage(getPmTTC("&aVocê setou a premiação por comando de console com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar o comando de console no argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("rewarditem")) {
                ItemStack item = player.getItemInHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(getPmTTC("&cPodexá que irei dar 3 Packs de Ar para o players :)"));
                    return;
                }
                evento.addRewardItem(item);
                player.sendMessage(getPmTTC("&aAdicionado item da sua mão na premiação com Sucesso!"));
            }

            if (method.equalsIgnoreCase("rewardmoney")) {
                if (optional != null) {
                    int number;
                    try {
                        number = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cVocê deve usar um número inteiro para o money"));
                        return;
                    }
                    if (number < 0) {
                        player.sendMessage(getPmTTC("&cSacanagem retirar money do player né meu parceiro"));
                        return;
                    }
                    evento.setRewardMoney(number);
                    player.sendMessage(getPmTTC("&aVocê colocou a premiação deste evento por R$ " + number));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar o valor do money no argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("numbercalls")) {
                if (optional != null) {
                    int number;
                    try {
                        number = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cVocê deve usar um número inteiro para as chamadas"));
                        return;
                    }
                    if (number < 1) {
                        player.sendMessage(getPmTTC("&cEvento sendo chamado negativamente né"));
                        return;
                    }
                    evento.setNumberCalls(number);
                    player.sendMessage(getPmTTC("&aVocê colocou o numero de chamados para " + number + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar número de chamados no argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("intervalcalls")) {
                if (optional != null) {
                    int number;
                    try {
                        number = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cVocê deve usar um número inteiro para os intervalos"));
                        return;
                    }
                    if (number < 5) {
                        player.sendMessage(getPmTTC("&cEita porra, olha o evento iniciando mais rápido que o Flash"));
                        return;
                    }
                    evento.setIntervalCalls(number);
                    player.sendMessage(getPmTTC("&aVocê colocou o numero de intervalos para " + number + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar número de intervalos no argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("maxplayers")) {
                if (optional != null) {
                    int number;
                    try {
                        number = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cVocê deve usar um número inteiro para a quantidade máxima de players"));
                        return;
                    }
                    if (number < 0) {
                        player.sendMessage(getPmTTC("&cAi meu amigo, querem que entrem no evento como?"));
                        return;
                    }
                    evento.setMaxPlayers(number);
                    player.sendMessage(getPmTTC("&aVocê colocou o numero máximo de players para " + number + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar número máximo de players no argumento Opcional"));
                }
            }
            if (method.equalsIgnoreCase("minplayers")) {
                if (optional != null) {
                    int number;
                    try {
                        number = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cVocê deve usar um número inteiro para a quantidade minima de players"));
                        return;
                    }
                    if (number < 1) {
                        player.sendMessage(getPmTTC("&cAi meu amigo, querem que entrem no evento como?"));
                        return;
                    }
                    evento.setMinPlayers(number);
                    player.sendMessage(getPmTTC("&aVocê colocou o numero minimo de players para " + number + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar número minimo de players no argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("itemsgive")) {
                ItemStack item = player.getItemInHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(getPmTTC("&cPodexá que irei dar 3 Packs de Ar para o players :)"));
                    return;
                }
                evento.addItemsGive(item);
                player.sendMessage(getPmTTC("&aAdicionado item da sua mão para inicio do evento!"));
            }

            if (method.equalsIgnoreCase("saveitems")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("true")) {
                        evento.setSaveItems(true);
                        player.sendMessage(getPmTTC("&aVocê setou para este evento salvar itens"));
                        return;
                    }
                    if (optional.equalsIgnoreCase("false")) {
                        player.sendMessage(getPmTTC("&aVocê setou para este evento não salvar itens"));
                        evento.setSaveItems(false);
                        return;
                    }
                    player.sendMessage(getPmTTC("&cVocê só pode usar true ou false"));
                } else {
                    player.sendMessage(getPmTTC("&cUse true ou false no Opcional"));
                }
            }

            if (method.equalsIgnoreCase("entryitems")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("true")) {
                        player.sendMessage(getPmTTC("&aVocê setou para poderem entrar na evento com itens"));
                        evento.setEntryItems(true);
                        return;
                    }
                    if (optional.equalsIgnoreCase("false")) {
                        player.sendMessage(getPmTTC("&aVocê setou para não poderem entrar na evento com itens"));
                        evento.setEntryItems(false);
                        return;
                    }
                    player.sendMessage(getPmTTC("&cVocê só pode usar true ou false"));
                } else {
                    player.sendMessage(getPmTTC("&cUse true ou false no Opcional"));
                }
            }
        }


        if (config.equalsIgnoreCase("remove")) {
            if (method.equalsIgnoreCase("type")) {
                player.sendMessage(getPmTTC("&cVocê não pode remover um tipo do Evento"));
            }
            if (method.equalsIgnoreCase("location")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("all")) {
                        evento.removeLocation();
                        player.sendMessage(getPmTTC("&aRemovido todas as localizações deste evento com Sucesso"));
                        return;
                    }
                    int index;
                    try {
                        index = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cO número do Index deve ser um número inteiro"));
                        return;
                    }
                    if (index < 1) {
                        player.sendMessage(getPmTTC("&cVamos aceessar aquela localização negativa né meu parceiro"));
                        return;
                    }
                    if (evento.getLocation().size() < index) {
                        player.sendMessage(getPmTTC("&cEste index não contém nas localizações"));
                        return;
                    }
                    evento.removeLocation(index - 1);
                    player.sendMessage(getPmTTC("&aRemovido a localização de número " + index + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve remover uma localização pelo numero dele ou usando 'all' para remover tudo. No argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("hub")) {
                if (evento.getHub() == null) {
                    player.sendMessage("&cNão tem uma localização de hub setada");
                    return;
                }
                evento.removeHub();
                player.sendMessage(getPmTTC("&cRemovido localização do Hub com Sucesso!"));
            }

            if (method.equalsIgnoreCase("camarote")) {
                if (evento.getCamarote() == null) {
                    player.sendMessage("&cNão tem uma localização de camarote setada");
                    return;
                }
                evento.removeCamarote();
                player.sendMessage(getPmTTC("&cRemovido localização do Camarote com Sucesso!"));
            }

            if (method.equalsIgnoreCase("rewardmsg")) {
                if (evento.getRewardMsg() == null) {
                    player.sendMessage("&cNão tem uma mensagem de premiação setada");
                    return;
                }
                evento.removeRewardMsg();
                player.sendMessage(getPmTTC("&cRemovido mensagem de premiação com Sucesso!"));
            }
            if (method.equalsIgnoreCase("rewardconsole")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("all")) {
                        evento.removeRewardConsole();
                        player.sendMessage(getPmTTC("&cRemovido todas premiação por comando de console com Sucesso!"));
                        return;
                    }
                    int index;
                    try {
                        index = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cO número do Index deve ser um número inteiro"));
                        return;
                    }
                    if (index < 1) {
                        player.sendMessage(getPmTTC("&cVamos acessar aquele item negativo né meu parceiro"));
                        return;
                    }
                    if (evento.getRewardConsole().size() < index) {
                        player.sendMessage(getPmTTC("&cEste index não contém nos itens"));
                        return;
                    }
                    evento.removeRewardConsole(index - 1);
                    player.sendMessage(getPmTTC("&aRemovido o mensagem de console de número " + index + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve remover um comando pelo numero dele ou usando 'all' para remover tudo. No Argumento Opcional"));
                }
            }

            if (method.equalsIgnoreCase("rewarditem")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("all")) {
                        evento.removeRewardItem();
                        player.sendMessage(getPmTTC("&aRemovido todos itens premiados deste evento com Sucesso"));
                        return;
                    }
                    int index;
                    try {
                        index = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cO número do Index deve ser um número inteiro"));
                        return;
                    }
                    if (index < 1) {
                        player.sendMessage(getPmTTC("&cVamos acessar aquele item negativo né meu parceiro"));
                        return;
                    }
                    if (evento.getRewardItem().size() < index) {
                        player.sendMessage(getPmTTC("&cEste index não contém nos itens"));
                        return;
                    }
                    evento.removeRewardItem(index - 1);
                    player.sendMessage(getPmTTC("&aRemovido o item premiado de número " + index + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve remover uma item pelo numero dele ou usando 'all' para remover tudo. No Argumento Opcional"));
                }
            }
            if (method.equalsIgnoreCase("rewardmoney")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set 0 no money???"));
            }
            if (method.equalsIgnoreCase("numbercalls")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set 0 no chamado???"));
            }
            if (method.equalsIgnoreCase("intervalcalls")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set 0 no intervalo???"));
            }
            if (method.equalsIgnoreCase("maxplayers")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set 0 no máximo de players???"));
            }
            if (method.equalsIgnoreCase("minplayers")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set 2 no minimo de players???"));
            }
            if (method.equalsIgnoreCase("saveitems")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set true ou false?"));
            }
            if (method.equalsIgnoreCase("entryitems")) {
                player.sendMessage(getPmTTC("&cAi meu parceiro, não é mais fácil dar set true ou false?"));
            }

            if (method.equalsIgnoreCase("itemsgive")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("all")) {
                        evento.removeItemsGive();
                        player.sendMessage(getPmTTC("&aRemovido todos itens givado deste evento com Sucesso"));
                        return;
                    }
                    int index;
                    try {
                        index = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cO número do Index deve ser um número inteiro"));
                        return;
                    }
                    if (index < 1) {
                        player.sendMessage(getPmTTC("&cVamos aceessar aquele item negativo né meu parceiro"));
                        return;
                    }
                    if (evento.getItemsGive().size() < index) {
                        player.sendMessage(getPmTTC("&cEste index não contém nos itens"));
                        return;
                    }
                    evento.removeItemsGive(index - 1);
                    player.sendMessage(getPmTTC("&aRemovido o item givado de número " + index + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve remover uma item pelo numero dele ou usando 'all' para remover tudo. No Argumento Opcional"));
                }
            }
        }
    }

    private static void configRunning(Player player, EventoBase evento, String config, String method) {
        Running running = (Running) evento.getType();
        if (config.equalsIgnoreCase("set")) {
            if (method.equalsIgnoreCase("checkpoint")) {
                ItemStack item = player.getItemInHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(getPmTTC("&cVocê deve colocar bloco de checkpoint pelo item na mão"));
                    return;
                }
                running.setCheckpoint(evento, item);
                player.sendMessage(getPmTTC("&aVocê setou o checkpoint com Sucesso!"));

            }
            if (method.equalsIgnoreCase("respawn")) {
                ItemStack item = player.getItemInHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(getPmTTC("&cVocê deve colocar bloco de respawn pelo item na mão"));
                    return;
                }
                running.setRespawn(evento, item);
                player.sendMessage(getPmTTC("&aVocê setou o respawn com Sucesso!"));
            }
            if (method.equalsIgnoreCase("winner")) {
                ItemStack item = player.getItemInHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(getPmTTC("&cVocê deve colocar bloco de winner pelo item na mão"));
                    return;
                }
                running.setWinner(evento, item);
                player.sendMessage(getPmTTC("&aVocê setou o winner com Sucesso!"));
            }
        }
        if (config.equalsIgnoreCase("remove")) {
            if (method.equalsIgnoreCase("checkpoint")) {
                if (running.getCheckpoint() == null) {
                    player.sendMessage(getPmTTC("&cEste item não está setado"));
                    return;
                }
                running.removeCheckpoint(evento);
                player.sendMessage(getPmTTC("&aRemovido checkpoint deste evento com Sucesso!"));
            }
            if (method.equalsIgnoreCase("respawn")) {
                if (running.getRespawn() == null) {
                    player.sendMessage(getPmTTC("&cEste item não está setado"));
                    return;
                }
                running.removeRespawn(evento);
                player.sendMessage(getPmTTC("&aRemovido respawn deste evento com Sucesso!"));
            }
            if (method.equalsIgnoreCase("winner")) {
                if (running.getWinner() == null) {
                    player.sendMessage(getPmTTC("&cEste item não está setado"));
                    return;
                }
                running.removeWinner(evento);
                player.sendMessage(getPmTTC("&aRemovido winner deste evento com Sucesso!"));
            }
        }
    }

    private static void configSpleef(Player player, EventoBase evento, String config, String method, String optional) {
        Spleef spleef = (Spleef) evento.getType();
        if (config.equalsIgnoreCase("set")) {
            if (method.equalsIgnoreCase("breakautosnow")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("true")) {
                        spleef.setBreakAutoSnow(evento, true);
                        player.sendMessage(getPmTTC("&aVocê colocou opção de auto quebrar a neve como true"));
                        return;
                    }
                    if (optional.equalsIgnoreCase("false")) {
                        spleef.setBreakAutoSnow(evento, false);
                        player.sendMessage(getPmTTC("&aVocê colocou opção de auto quebrar a neve como false"));
                        return;
                    }
                    player.sendMessage(getPmTTC("&cVocê deve colocar apenas true ou false"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve colocar true ou false no argumento Opcional"));
                }
            }
            if (method.equalsIgnoreCase("autoreplacesnow")) {
                Location loc = player.getLocation();
                Location newLoc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
                player.sendMessage(getPmTTC("&aVocê setou o bloco abaixo de você como auto replace de snow para o evento"));
                spleef.addAutoReplaceSnow(evento, newLoc);
            }
            if (method.equalsIgnoreCase("replacesnow")) {
                if (spleef.getAutoReplaceSnow().size() < 1) {
                    player.sendMessage(getPmTTC("Este evento não contém localizações de Replace Snow setado"));
                    return;
                }
                for (Location loc : spleef.getAutoReplaceSnow()) {
                    replaceSnow(loc);
                }
            }
        }

        if (config.equalsIgnoreCase("remove")) {
            if (method.equalsIgnoreCase("breakautosnow")) {
                player.sendMessage(getPmTTC("&cVocê só pode usar a config set true ou false neste método"));
            }
            if (method.equalsIgnoreCase("replacesnow")) {
                player.sendMessage(getPmTTC("&cNão há uso de remove neste método."));
            }
            if (method.equalsIgnoreCase("autoreplacesnow")) {
                if (optional != null) {
                    if (optional.equalsIgnoreCase("all")) {
                        spleef.removeAutoReplaceSnow(evento);
                        player.sendMessage(getPmTTC("&aRemovido todas as localizações de auto replace de snow deste evento com Sucesso"));
                        return;
                    }
                    int index;
                    try {
                        index = Integer.parseInt(optional);
                    } catch (Exception ignored) {
                        player.sendMessage(getPmTTC("&cO número do Index deve ser um número inteiro"));
                        return;
                    }
                    if (index < 1) {
                        player.sendMessage(getPmTTC("&cVamos aceessar aquela localização negativa né meu parceiro"));
                        return;
                    }
                    if (spleef.getAutoReplaceSnow().size() < index) {
                        player.sendMessage(getPmTTC("&cEste index não contém nas localizações"));
                        return;
                    }
                    spleef.removeAutoReplaceSnow(evento, index - 1);
                    player.sendMessage(getPmTTC("&aRemovido a localização de auto replace de snow do número " + index + " com Sucesso!"));
                } else {
                    player.sendMessage(getPmTTC("&cVocê deve remover uma localização pelo numero dele ou usando 'all' para remover tudo. No argumento Opcional"));
                }
            }
        }
    }

    private static void configSkyWars(Player player, EventoBase evento, String config, String method) {
        SkyWars skyWars = (SkyWars) evento.getType();
        if (config.equalsIgnoreCase("set")) {
            if (method.equalsIgnoreCase("region1")) {
                Location loc = player.getLocation();
                if (skyWars.getRegion2() != null) {
                    if (!skyWars.getRegion2().getWorld().getName().equals(loc.getWorld().getName())) {
                        player.sendMessage(getPmTTC("&cO mundo da região 2 é diferente desta região"));
                        return;
                    }
                    if (skyWars.getRegion2().distance(loc) > 700) {
                        player.sendMessage(getPmTTC("&cA região 2 está numa distãncia muito grande desta região"));
                        return;
                    }
                }
                skyWars.setRegion1(evento, new Location(player.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                player.sendMessage(getPmTTC("&aVocê setou local da região 1 do Skywars com Sucesso!"));
            }

            if (method.equalsIgnoreCase("region2")) {
                Location loc = player.getLocation();
                if (skyWars.getRegion1() != null) {
                    if (!skyWars.getRegion1().getWorld().getName().equals(loc.getWorld().getName())) {
                        player.sendMessage(getPmTTC("&cO mundo da região 1 é diferente desta região"));
                        return;
                    }
                    if (skyWars.getRegion1().distance(loc) > 700) {
                        player.sendMessage(getPmTTC("&cA região 1 está numa distãncia muito grande desta região"));
                        return;
                    }
                }
                skyWars.setRegion2(evento, new Location(player.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                player.sendMessage(getPmTTC("&aVocê setou local da região 2 do Skywars com Sucesso!"));
            }

            if (method.equalsIgnoreCase("saveblocks")) {
                boolean cancel = false;
                if (skyWars.getRegion1() == null) {
                    player.sendMessage(getPmTTC("&cA região 1 não está setada para salvar blocos"));
                    cancel = true;
                }
                if (skyWars.getRegion2() == null) {
                    player.sendMessage(getPmTTC("&cA região 2 não está setada para salvar blocos"));
                    cancel = true;
                }
                if (cancel) {
                    return;
                }
                Map<Location, String> saveBlocks = new HashMap<>();
                Map<Location, Inventory> saveItemsBlock = new HashMap<>();
                SaveBlocks(evento, skyWars.getRegion1(), skyWars.getRegion2(), saveBlocks, saveItemsBlock);
                skyWars.setSaveBlocks(saveBlocks);
                skyWars.setSaveItemsBlock(saveItemsBlock);
                player.sendMessage(getPmTTC("&aVocê salvou os blocos do Skywars no estado atual como Sucesso!"));
            }

            if (method.equalsIgnoreCase("loadblocks")) {
                if (skyWars.getSaveBlocks().size() == 0) {
                    player.sendMessage(getPmTTC("&cEste Skywars não tem blocos salvos"));
                    return;
                }
                LoadBlocksSave(skyWars.getRegion1(), skyWars.getRegion2(), skyWars.getSaveBlocks(), skyWars.getSaveItemsBlock());
                player.sendMessage(getPmTTC("&aVocê deu load nos blocos salvos do Skywars com Sucesso"));
            }

            if (method.equalsIgnoreCase("expandvert")) {
                boolean cancel = false;
                if (skyWars.getRegion1() == null) {
                    player.sendMessage(getPmTTC("&cA região 1 não está setada para salvar blocos"));
                    cancel = true;
                }
                if (skyWars.getRegion2() == null) {
                    player.sendMessage(getPmTTC("&cA região 2 não está setada para salvar blocos"));
                    cancel = true;
                }
                if (cancel) {
                    return;
                }
                Location region1 = skyWars.getRegion1();
                Location region2 = skyWars.getRegion2();
                region1.setY(0);
                region2.setY(255);
                skyWars.setRegion1(evento, region1);
                skyWars.setRegion2(evento, region2);
                player.sendMessage(getPmTTC("&aVocê expandiu verticalmente a região do Skywars com Sucesso!"));
            }

            if (method.equalsIgnoreCase("clearitems")) {
                boolean cancel = false;
                if (skyWars.getRegion1() == null) {
                    player.sendMessage(getPmTTC("&cA região 1 não está setada para limpar os itens"));
                    cancel = true;
                }
                if (skyWars.getRegion2() == null) {
                    player.sendMessage(getPmTTC("&cA região 2 não está setada para limpar os itens"));
                    cancel = true;
                }
                if (cancel) {
                    return;
                }
                clearItemsLoc(skyWars.getRegion1(), skyWars.getRegion2());
                player.sendMessage(getPmTTC("&aForam limpos os itens dropado na região do skywars com Sucesso!"));
            }

        }
        if (config.equalsIgnoreCase("remove")) {
            if (method.equalsIgnoreCase("region1")) {
                if (skyWars.getRegion1() == null) {
                    player.sendMessage(getPmTTC("&cNão tem uma Região 1 setada para remover"));
                    return;
                }
                skyWars.removeRegion1(evento);
                player.sendMessage(getPmTTC("&aVocê removeu Região 1 com Sucesso"));
            }
            if (method.equalsIgnoreCase("region2")) {
                if (skyWars.getRegion2() == null) {
                    player.sendMessage(getPmTTC("&cNão tem uma Região 2 setada para remover"));
                    return;
                }
                skyWars.removeRegion2(evento);
                player.sendMessage(getPmTTC("&aVocê removeu Região 2 com Sucesso"));
            }

            if (method.equalsIgnoreCase("saveblocks")) {
                if (skyWars.getSaveBlocks().size() == 0) {
                    player.sendMessage(getPmTTC("&cNão tem blocos salvos neste Skywars"));
                    return;
                }
                if (skyWars.removeSaveBlocks(evento)) {
                    player.sendMessage(getPmTTC("&aVocê removou os blocos salvos deste Skywars com Sucesso"));
                } else {
                    player.sendMessage(getPmTTC("&cHouve Problemas para remover o arquivo de SaveRegions deste Skywars"));
                }
            }

            if (method.equalsIgnoreCase("loadblocks")) {
                player.sendMessage(getPmTTC("&cIsto aqui não pode ser removido meu amigo"));
            }
            if (method.equalsIgnoreCase("expandvert")) {
                player.sendMessage(getPmTTC("&cIsto aqui não pode ser removido meu amigo"));
            }
            if (method.equalsIgnoreCase("clearitems")) {
                player.sendMessage(getPmTTC("&cIsto aqui não pode ser removido meu amigo"));
            }
        }
    }
}
