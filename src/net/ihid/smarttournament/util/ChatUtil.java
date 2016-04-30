package net.ihid.smarttournament.util;

import org.bukkit.ChatColor;

/**
 * Created by Mikey on 4/25/2016.
 */
public class ChatUtil {

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
