package net.ihid.smarttournament.commands;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.enums.TournamentStage;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Created by Mikey on 4/25/2016.
 */
public class CommandTournament implements CommandExecutor {
    private TournamentPlugin plugin;

    private static final MainManager mainManager = TournamentPlugin.getMainManager();

    public CommandTournament(TournamentPlugin instance) {
        plugin = instance;
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
            sender.sendMessage(ChatUtil.color(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage("&cSevere error. Please contact a server administrator for assistance.");
            TournamentPlugin.i.disable();
        }

        return true;
    }

    private boolean run(CommandSender sender, Command cmd, String label, String subCommand, String[] args) {
        Player ps;

        switch(subCommand) {
            case "join":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!mainManager.getTournamentManager().isTournamentRunning()) {
                    ps.sendMessage("&cThere are currently no tournaments running.");
                    return true;
                }

                if(mainManager.getTournamentManager().getTournament().getStage() != TournamentStage.WAITING) {
                    ps.sendMessage("&cThis tournament has already started.");
                    return true;
                }

                if(mainManager.getTournamentManager().getPlayers().contains(ps)) {
                    ps.sendMessage("&cYou have already joined the tournament.");
                    return true;
                }
                mainManager.getTournamentManager().getPlayers().add(ps);
                ps.teleport(mainManager.getTournamentManager().getSpectatorArea());

                break;

            case "leave":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!mainManager.getTournamentManager().isTournamentRunning()) {
                    ps.sendMessage("&cYou are currently not in a tournament.");
                    return true;
                }

                mainManager.getTournamentManager().getPlayers().remove(ps);
                mainManager.getMatchManager().getWinners().remove(ps);
                break;

            case "start":
                checkPerm(sender, "smarttournament.start");

                if(mainManager.getTournamentManager().isTournamentRunning()) {
                    sender.sendMessage("&cA tournament is already running.");
                    return true;
                }
                mainManager.getTournamentManager().startTournament();
                break;

            case "end":
                checkPerm(sender, "smarttournament.end");

                if(!mainManager.getTournamentManager().isTournamentRunning()) {
                    sender.sendMessage("&cThere is no tournament currently running.");
                    return true;
                }
                mainManager.getTournamentManager().endTournament();
                break;

            case "setspawn":
                checkPlayer(sender);
                ps = (Player) sender;

                checkPerm(sender, "smarttournament.setspawn");
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("spectator")) {
                        Location loc = ps.getLocation();

                    }
                }

                if(args.length == 3) {//  /st setspawn <arena name> <num>
                    String arenaName = args[1];

                    int num;
                    try {
                        num = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage("&cYou must enter a number for the third argument.");
                        return true;
                    }
                    num = Integer.parseInt(args[2]);

                    if(TournamentPlugin.i.getConfig().contains("arenas." + arenaName)) {
                       sender.sendMessage("&cYou have already created an arena with this name.");
                        return true;
                    }

                    // arena stuff here

                }
                break;

            default:
                sender.sendMessage("Wrong usage randy ass.");
        }
        return false;
    }
    private void checkPerm(CommandSender cs, String perm) {
        if (!cs.hasPermission(perm))
            throw new CommandException("&cYou do not have permission to execute this command.");
    }

    private void checkArgs(String[] args, int min) {
        if (args.length < min)
            throw new CommandException("&cYou did not specify enough arguments for this command.");
    }

    private void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException("&cThis command is not usable from the console.");
    }
}
