package dev.waradu.vanquor.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void Chat(PlayerChatEvent event){
        String message = event.getMessage();
        Player player = event.getPlayer();

        char cara1 = 0x263A;
        char cara2 = 0x263B;
        char nota1 = 0x266A;
        char nota2 = 0x266B;
        char corazon = 0x2665;
        char rombo = 0x2666;
        char trebol = 0x2663;
        char picas = 0x2660;
        char hombre = 0x2642;
        char mujer = 0x2640;
        char ankh = 0x2625;

        char die1 = 0x2680;
        char die2 = 0x2681;
        char die3 = 0x2682;
        char die4 = 0x2683;
        char die5 = 0x2684;
        char die6 = 0x2685;

        if(player.isOp()){
            message = message.replace(":c1:", Character.toString(cara1));
            message = message.replace(":c2:", Character.toString(cara2));
            message = message.replace(":n1:", Character.toString(nota1));
            message = message.replace(":n2:", Character.toString(nota2));
            message = message.replace(":c:", Character.toString(corazon));
            message = message.replace(":r:", Character.toString(rombo));
            message = message.replace(":t:", Character.toString(trebol));
            message = message.replace(":p:", Character.toString(picas));
            message = message.replace(":h:", Character.toString(hombre));
            message = message.replace(":m:", Character.toString(mujer));
            message = message.replace(":ankh:", Character.toString(ankh));

            message = message.replace(":die1:", Character.toString(die1));
            message = message.replace(":die2:", Character.toString(die2));
            message = message.replace(":die3:", Character.toString(die3));
            message = message.replace(":die4:", Character.toString(die4));
            message = message.replace(":die5:", Character.toString(die5));
            message = message.replace(":die6:", Character.toString(die6));

            event.setMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
