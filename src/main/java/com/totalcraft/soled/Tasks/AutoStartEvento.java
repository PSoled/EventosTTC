package com.totalcraft.soled.Tasks;

import com.totalcraft.soled.EventoBase;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Command.eventoStart;
import static com.totalcraft.soled.EventoBase.events;
import static com.totalcraft.soled.EventoBase.getEvent;
import static com.totalcraft.soled.Tasks.StartingEvento.startEvento;
import static com.totalcraft.soled.Utils.PrefixMsg.getPmTTC;

public class AutoStartEvento {
    public static TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
    public static boolean enableAutoStart;
    public static List<String> autoStart = new ArrayList<>();
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledAutoStart;

    public static void autoStartTask() {
        scheduledAutoStart = scheduler.scheduleAtFixedRate(() -> {
            if (enableAutoStart) {
                Calendar cal = Calendar.getInstance(tz);
                Date date = cal.getTime();
                for (String evento : autoStart) {
                    int hour = Integer.parseInt(evento.split("-")[1].split(":")[0]);
                    int minutes = Integer.parseInt(evento.split("-")[1].split(":")[1]);
                    if (date.getHours() == hour && date.getMinutes() == minutes) {
                        String name = evento.split("-")[0];
                        System.out.println(name);
                        boolean start = true;
                        if (!events.containsKey(name)) {
                            System.out.println(getPmTTC("&cErro ao auto iniciar o Evento &f" + name + ". &cEle não existe nos eventos do servidor"));
                            start = false;
                        }
                        if (eventoStart) {
                            start = false;
                        }
                        EventoBase eventoBase = getEvent(name);
                        if (eventoBase != null && eventoBase.containBasicEvento()) {
                            System.out.println(getPmTTC("&cErro ao auto iniciar o Evento &f" + name + ". &cEle não contém dados básico para iniciar."));
                            start = false;
                        }
                        if (start) {
                            startEvento(eventoBase);
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }


}
