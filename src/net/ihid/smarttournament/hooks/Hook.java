package net.ihid.smarttournament.hooks;

import org.bukkit.plugin.Plugin;

interface Hook {
    boolean isEnabled();

    Plugin getPlugin();

    String getName();
}
