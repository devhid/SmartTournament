package net.ihid.smarttournament;

import net.ihid.smarttournament.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * Created by Mikey on 4/25/2016.
 */
public class CommandTournament implements CommandExecutor {
    private TournamentPlugin plugin;
    private TournamentAPI tournamentAPI;

    public CommandTournament(TournamentPlugin instance) {
        plugin = instance;
        tournamentAPI = TournamentPlugin.getTournamentAPI();
    }

    private class CommandException extends RuntimeException {
        CommandException(String msg) {
            super(msg);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            checkPerm(sender, "smarttournament.default");
            checkArgs(args, 1);
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }

        try {
            run(sender, cmd, label, args[0], args);
        } catch (CommandException e) {
            sender.sendMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Lang.SEVERE_ERROR.toString());
        }

        return true;
    }

    private boolean run(CommandSender sender, Command cmd, String label, String subCommand, String[] args) {
        Player ps;

        switch(subCommand) {
            case "join":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!tournamentAPI.isTournamentRunning()) {
                    ps.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if(tournamentAPI.getTournament().getStage() == TournamentStage.ACTIVE) {
                    ps.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if(tournamentAPI.isInTournament(ps)) {
                    ps.sendMessage(Lang.ALREADY_IN_TOURNAMENT.toString());
                    return true;
                }

                if(plugin.getConfig().getBoolean("configuration.force-player-clear-inventory")) {
                    if (!hasEmptyInventory(ps)) {
                        ps.sendMessage(Lang.REQUIRE_EMPTY_INVENTORY.toString());
                        return true;
                    }
                }

                tournamentAPI.addToTournament(ps);
                tournamentAPI.removeTag(ps);

                Bukkit.broadcastMessage(Lang.TOURNAMENT_JOINED_BROADCAST.toString().replace("{username}", ps.getName()));
                ps.sendMessage(Lang.TOURNAMENT_JOINED_SUCCESS.toString());
                ps.teleport(tournamentAPI.getSpectatorArea());

                break;

            case "leave":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!tournamentAPI.isTournamentRunning()) {
                    ps.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if(!tournamentAPI.isInTournament(ps)) {
                    ps.sendMessage(Lang.NOT_IN_TOURNAMENT.toString());
                    return true;
                }

                ps.sendMessage(Lang.TOURNAMENT_LEFT_SUCCESS.toString());
                tournamentAPI.removeFromTournament(ps);
                break;

            case "start":
                checkPerm(sender, "smarttournament.start");

                if(tournamentAPI.isTournamentRunning()) {
                    sender.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if(plugin.getConfig().get("arenas") == null ||
                        plugin.getConfig().get("spectator") == null ||
                        plugin.getConfig().get("world-spawn") == null) {
                    sender.sendMessage(Lang.TOURNAMENT_AREAS_NOT_SET.toString());
                    return true;
                }

                Bukkit.broadcastMessage(Lang.TOURNAMENT_PRE_START_BROADCAST.toString());
                sender.sendMessage(Lang.TOURNAMENT_START_SUCCESS.toString());
                
                tournamentAPI.startTournament();
                break;

            case "end":
                checkPerm(sender, "smarttournament.end");

                if(!tournamentAPI.isTournamentRunning()) {
                    sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                Bukkit.broadcastMessage(Lang.TOURNAMENT_END_BROADCAST.toString());
                sender.sendMessage(Lang.TOURNAMENT_END_SUCCESS.toString());
                
                tournamentAPI.endTournament();
                break;

            case "setspawn":
                checkPlayer(sender);
                ps = (Player) sender;

                checkPerm(sender, "smarttournament.setspawn");
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("-spectator")) {
                        sender.sendMessage(Lang.SPECTATOR_AREA_SET.toString());
                        tournamentAPI.setSpectatorArea(ps);
                        return true;
                    }

                    if(args[1].equalsIgnoreCase("-world")) {
                        sender.sendMessage(Lang.WORLD_SPAWN_SET.toString());
                        tournamentAPI.setWorldSpawn(ps);
                    }
                }

                if(args.length == 3) {
                    String arenaName = args[1];

                    checkNum(args[2]);
                    Integer num = Integer.parseInt(args[2]);

                    if(num < 1 || num > 2) {
                        ps.sendMessage(Lang.ARENA_INVALID_POSITION.toString());
                        return true;
                    }

                    sender.sendMessage(Lang.ARENA_SET_SUCCESS.toString().replace("{arena}", arenaName).replace("{position}", num.toString()));
                    tournamentAPI.setLocation(arenaName, ps, num);
                    return true;
                }
                break;

            default:
                throw new CommandException(Lang.IMPROPER_USAGE.toString());
        }
        return false;
    }

    private boolean hasEmptyInventory(Player player) {
        for(ItemStack item: player.getInventory().getContents()) {
            if(item != null) {
                if(item.getType() != Material.AIR) {
                    return false;
                }
            }
        }

        for(ItemStack item: player.getInventory().getArmorContents()) {
            if(item != null) {
                if(item.getType() != Material.AIR) {
                    return false;
                }
            }
        }

        return true;
    }

    private void checkNum(String number) {
        int num;
        try {
            num = Integer.parseInt(number);
        } catch(NumberFormatException ex) {
            throw new CommandException(Lang.INVALID_NUMBER.toString());
        }
    }

    private void checkPerm(CommandSender cs, String perm) {
        if (!cs.hasPermission(perm))
            throw new CommandException(Lang.NO_PERMISSION.toString());
    }

    private void checkArgs(String[] args, int min) {
        if (args.length < min)
            throw new CommandException(Lang.IMPROPER_USAGE.toString());
    }

    private void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException(Lang.INVALID_SENDER.toString());
    }
}
