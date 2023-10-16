package com.totalcraft.soled;

import com.totalcraft.soled.Eventos.Running;
import com.totalcraft.soled.Eventos.SkyWars;
import com.totalcraft.soled.Eventos.Spleef;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.totalcraft.soled.Config.MainConfig.dataEventos;
import static com.totalcraft.soled.Config.MainConfig.saveEventosData;

public class EventoBase {
    public static List<String> TiposEventos = Arrays.asList("Running", "Spleef", "Skywars");
    public static List<String> methodsBase = Arrays.asList("Hub", "Camarote", "Location", "NumberCalls", "IntervalCalls", "MaxPlayers", "MinPlayers", "ItemsGive", "RewardMsg", "RewardConsole", "RewardItem", "RewardMoney", "SaveItems", "EntryItems");
    private final String name;
    private Object type;
    private List<Location> location;
    private Location hub, camarote;
    private String rewardMsg;
    private List<String> rewardConsole;
    private int rewardMoney, numberCalls, intervalCalls, maxPlayers, minPlayers;
    private List<ItemStack> rewardItem, itemsGive;
    public boolean saveItems, entryItems;

    public EventoBase(String name, Object type, List<Location> location, Location hub, Location camarote, int numberCalls, int intervalCalls, int maxPlayers, int minPlayers, List<ItemStack> itemsGive, String rewardMsg, List<String> rewardConsole, List<ItemStack> rewardItem, int rewardMoney, boolean saveItems, boolean entryItems) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.hub = hub;
        this.camarote = camarote;
        this.numberCalls = numberCalls;
        this.intervalCalls = intervalCalls;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.itemsGive = itemsGive;
        this.rewardMsg = rewardMsg;
        this.rewardConsole = rewardConsole;
        this.rewardItem = rewardItem;
        this.rewardMoney = rewardMoney;
        this.saveItems = saveItems;
        this.entryItems = entryItems;
    }


    public EventoBase(String event) {
        this.name = event;
        this.type = null;
        this.location = new ArrayList<>();
        this.hub = null;
        this.camarote = null;
        this.numberCalls = 4;
        this.intervalCalls = 15;
        this.maxPlayers = 0;
        this.minPlayers = 2;
        this.itemsGive = new ArrayList<>();
        this.rewardMsg = null;
        this.rewardConsole = new ArrayList<>();
        this.rewardItem = new ArrayList<>();
        this.rewardMoney = 0;
        this.saveItems = true;
        this.entryItems = true;
    }

    public static final Map<String, EventoBase> events = new HashMap<>();

    public static void newEvent(String nome, String type) {
        Object typeEvent = getObjectString(type);
        if (typeEvent != null) {
            EventoBase evento = new EventoBase(nome);
            evento.setType(typeEvent);
            events.put(nome, evento);
            dataEventos.set("Eventos." + nome + ".Tipo", typeEvent.getClass().getSimpleName());
            dataEventos.set("Eventos." + nome + ".Localizações", "null");
            dataEventos.set("Eventos." + nome + ".Hub", "null");
            dataEventos.set("Eventos." + nome + ".Camarote", "null");
            dataEventos.set("Eventos." + nome + ".Chamadas", 4);
            dataEventos.set("Eventos." + nome + ".Intervalo", 15);
            dataEventos.set("Eventos." + nome + ".MáximoDePlayers", 0);
            dataEventos.set("Eventos." + nome + ".MinimoDePlayers", 2);
            dataEventos.set("Eventos." + nome + ".ItensGivado", "null");
            dataEventos.set("Eventos." + nome + ".MensagemDePremiação", "null");
            dataEventos.set("Eventos." + nome + ".ComandoDeConsole", "null");
            dataEventos.set("Eventos." + nome + ".ItensPremeiados", "null");
            dataEventos.set("Eventos." + nome + ".MoneyPremiado", 0);
            dataEventos.set("Eventos." + nome + ".SalvarItens", true);
            dataEventos.set("Eventos." + nome + ".EntradaDeItens", true);
            if (typeEvent instanceof Running) {
                dataEventos.set("Eventos." + nome + ".Checkpoint", "null");
                dataEventos.set("Eventos." + nome + ".Respawn", "null");
                dataEventos.set("Eventos." + nome + ".Winner", "null");
            }
            if (typeEvent instanceof Spleef) {
                dataEventos.set("Eventos." + nome + ".AutoQuebrarNeve", false);
                dataEventos.set("Eventos." + nome + ".RecolocarNeve", "null");
            }
            if (typeEvent instanceof SkyWars) {
                dataEventos.set("Eventos." + nome + ".Região1", "null");
                dataEventos.set("Eventos." + nome + ".Região2", "null");
            }
            saveEventosData();
        }
    }

    public static Object getObjectString(String name) {
        Object type = null;
        if (name.equalsIgnoreCase("running")) {
            type = new Running();
        }
        if (name.equalsIgnoreCase("spleef")) {
            type = new Spleef();
        }
        if (name.equalsIgnoreCase("skywars")) {
            type = new SkyWars();
        }
        return type;
    }

    public static EventoBase getEvent(String nome) {
        return events.get(nome);
    }

    public static void deleteEvent(String nome) {
        events.remove(nome);
        dataEventos.set("Eventos." + nome, null);
        saveEventosData();
    }

    public String getName() {
        return name;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void addLocation(Location location) {
        this.location.add(location);
        String loc = location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
        dataEventos.set("Eventos." + this.name + ".Localizações." + this.location.size(), loc);
        saveEventosData();
    }

    public void removeLocation(int index) {
        this.location.remove(index);
        dataEventos.set("Eventos." + this.name + ".Localizações", "null");
        if (location.size() > 0) {
            int num = 1;
            for (Location location : location) {
                String loc = location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
                dataEventos.set("Eventos." + this.name + ".Localizações." + num, loc);
                num++;
            }
        }
        saveEventosData();
    }

    public void removeLocation() {
        this.location = new ArrayList<>();
        dataEventos.set("Eventos." + this.name + ".Localizações", "null");
        saveEventosData();
    }

    public Location getHub() {
        return hub;
    }

    public void setHub(Location hub) {
        this.hub = hub;
        String loc = hub.getWorld().getName() + " " + hub.getX() + " " + hub.getY() + " " + hub.getZ() + " " + hub.getYaw() + " " + hub.getPitch();
        dataEventos.set("Eventos." + this.name + ".Hub", loc);
        saveEventosData();
    }

    public void removeHub() {
        this.hub = null;
        dataEventos.set("Eventos." + this.name + ".Hub", "null");
        saveEventosData();
    }

    public Location getCamarote() {
        return camarote;
    }

    public void setCamarote(Location camarote) {
        this.camarote = camarote;
        String loc = camarote.getWorld().getName() + " " + camarote.getX() + " " + camarote.getY() + " " + camarote.getZ() + " " + camarote.getYaw() + " " + camarote.getPitch();
        dataEventos.set("Eventos." + this.name + ".Camarote", loc);
        saveEventosData();
    }

    public void removeCamarote() {
        this.camarote = null;
        dataEventos.set("Eventos." + this.name + ".Camarote", "null");
        saveEventosData();
    }

    public int getNumberCalls() {
        return numberCalls;
    }

    public void setNumberCalls(int numberCalls) {
        this.numberCalls = numberCalls;
        dataEventos.set("Eventos." + this.name + ".Chamadas", numberCalls);
        saveEventosData();
    }

    public int getIntervalCalls() {
        return intervalCalls;
    }

    public void setIntervalCalls(int intervalCalls) {
        this.intervalCalls = intervalCalls;
        dataEventos.set("Eventos." + this.name + ".Intervalo", intervalCalls);
        saveEventosData();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        dataEventos.set("Eventos." + this.name + ".MáximoDePlayers", maxPlayers);
        saveEventosData();
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        dataEventos.set("Eventos." + this.name + ".MinimoDePlayers", minPlayers);
        saveEventosData();
    }

    public List<ItemStack> getItemsGive() {
        return itemsGive;
    }

    public void addItemsGive(ItemStack itemsGive) {
        this.itemsGive.add(itemsGive);
        String item = itemsGive.getType().name() + "-" + itemsGive.getDurability() + "-" + itemsGive.getAmount();
        dataEventos.set("Eventos." + this.name + ".ItensGivado." + this.itemsGive.size(), item);
        ItemMeta meta = itemsGive.getItemMeta();
        StringBuilder sb = new StringBuilder();
        if (meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                sb.append(entry.getKey().getName()).append("-").append(entry.getValue()).append(" ");
            }
            dataEventos.set("Eventos." + this.name + ".ItensGivado." + this.itemsGive.size() + " Enchants", sb.toString());
        }
        saveEventosData();
    }

    public void removeItemsGive(int index) {
        this.itemsGive.remove(index);
        dataEventos.set("Eventos." + this.name + ".ItensGivado", "null");
        if (itemsGive.size() > 0) {
            int num = 1;
            for (ItemStack rewardItem : itemsGive) {
                String item = rewardItem.getType().name() + "-" + rewardItem.getDurability() + "-" + rewardItem.getAmount();
                dataEventos.set("Eventos." + this.name + ".ItensGivado." + num, item);
                num++;
            }
        }
        saveEventosData();
    }

    public void removeItemsGive() {
        this.itemsGive = new ArrayList<>();
        dataEventos.set("Eventos." + this.name + ".ItensGivado", "null");
        saveEventosData();
    }

    public String getRewardMsg() {
        return rewardMsg;
    }

    public void setRewardMsg(String rewardMsg) {
        this.rewardMsg = rewardMsg;
        dataEventos.set("Eventos." + this.name + ".MensagemDePremiação", rewardMsg);
        saveEventosData();
    }

    public void removeRewardMsg() {
        this.rewardMsg = null;
        dataEventos.set("Eventos." + this.name + ".MensagemDePremiação", "null");
        saveEventosData();
    }

    public List<String> getRewardConsole() {
        return rewardConsole;
    }

    public void addRewardConsole(String rewardConsole) {
        this.rewardConsole.add(rewardConsole);
        dataEventos.set("Eventos." + this.name + ".ComandoDeConsole." + this.rewardConsole.size(), rewardConsole);
        saveEventosData();
    }

    public void removeRewardConsole(int index) {
        this.rewardConsole.remove(index);
        dataEventos.set("Eventos." + this.name + ".ComandoDeConsole", "null");
        if (this.rewardConsole.size() > 0) {
            int num = 1;
            for (String list : this.rewardConsole) {
                dataEventos.set("Eventos." + this.name + ".ComandoDeConsole." + num, list);
                num++;
            }
        }
        saveEventosData();
    }

    public void removeRewardConsole() {
        this.rewardConsole = new ArrayList<>();
        dataEventos.set("Eventos." + this.name + ".ComandoDeConsole", "null");
        saveEventosData();
    }

    public List<ItemStack> getRewardItem() {
        return rewardItem;
    }

    public void addRewardItem(ItemStack rewardItem) {
        this.rewardItem.add(rewardItem);
        String item = rewardItem.getType().name() + "-" + rewardItem.getDurability() + "-" + rewardItem.getAmount();
        dataEventos.set("Eventos." + this.name + ".ItensPremeiados." + this.rewardItem.size(), item);
        ItemMeta meta = rewardItem.getItemMeta();
        StringBuilder sb = new StringBuilder();
        if (meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                sb.append(entry.getKey().getName()).append("-").append(entry.getValue()).append(" ");
            }
            dataEventos.set("Eventos." + this.name + ".ItensPremeiados." + this.rewardItem.size() + " Enchants", sb.toString());
        }
        saveEventosData();
    }

    public void removeRewardItem(int index) {
        this.rewardItem.remove(index);
        dataEventos.set("Eventos." + this.name + ".ItensPremeiados", "null");
        if (this.rewardItem.size() > 0) {
            int num = 1;
            for (ItemStack rewardItem : this.rewardItem) {
                String item = rewardItem.getType().name() + "-" + rewardItem.getDurability() + "-" + rewardItem.getAmount();
                dataEventos.set("Eventos." + this.name + ".ItensPremeiados." + num, item);
                num++;
            }
        }
        saveEventosData();
    }

    public void removeRewardItem() {
        this.rewardItem = new ArrayList<>();
        dataEventos.set("Eventos." + this.name + ".ItensPremeiados", "null");
        saveEventosData();
    }

    public int getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(int rewardMoney) {
        this.rewardMoney = rewardMoney;
        dataEventos.set("Eventos." + this.name + ".MoneyPremiado", rewardMoney);
        saveEventosData();
    }

    public void setEntryItems(boolean entryItems) {
        this.entryItems = entryItems;
        dataEventos.set("Eventos." + this.name + ".EntradaDeItens", entryItems);
        saveEventosData();
    }

    public void setSaveItems(boolean saveItems) {
        this.saveItems = saveItems;
        dataEventos.set("Eventos." + this.name + ".SalvarItens", saveItems);
        saveEventosData();
    }

    public boolean containBasicEvento() {
        if (this.location.size() == 0 || this.minPlayers == 0) {
            return true;
        }
        if (this.type instanceof Running) {
            Running running = (Running) this.type;
            if (running.getWinner() == null || running.getRespawn() == null || running.getCheckpoint() == null) {
                return true;
            }
        }
        return false;
    }
}
