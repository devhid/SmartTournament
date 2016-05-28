package net.ihid.smarttournament;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Created by Mikey on 4/25/2016.
 */
public class CommandTournament implements CommandExecutor {
    private TournamentPlugin plugin;

    private TournamentAPI api = TournamentPlugin.getTournamentAPI();

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
            sender.sendMessage(ChatUtil.color(e.getMessage()));
            return true;
        }

        try {
            run(sender, cmd, label, args[0], args);
        } catch (CommandException e) {
            sender.sendMessage(ChatUtil.color(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatUtil.color("&4Tournament &8// &cSevere error. Please contact a server administrator for assistance."));
            //TournamentPlugin.i.disable();
        }

        return true;
    }

    private boolean run(CommandSender sender, Command cmd, String label, String subCommand, String[] args) {
        Player ps;

        switch(subCommand) {
            case "join":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!api.isTournamentRunning()) {
                    ps.sendMessage(ChatUtil.color("&4Tournament &8// &cThere are currently no tournaments running."));
                    return true;
                }

                if(api.getTournament().getStage() == TournamentStage.ACTIVE) {
                    ps.sendMessage(ChatUtil.color("&4Tournament &8// &cThis tournament has already started."));
                    return true;
                }

                if(api.getPlayers().contains(ps)) {
                    ps.sendMessage(ChatUtil.color("&4Tournament &8// &cYou have already joined the tournament."));
                    return true;
                }

                api.getPlayers().add(ps);
                api.removeTag(ps);

                ps.sendMessage(ChatUtil.color("&4Tournament &8// &aYou have successfully joined the tournament."));
                ps.teleport(api.getSpectatorArea());

                break;

            case "leave":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!api.isTournamentRunning()) {
                    ps.sendMessage(ChatUtil.color("&4Tournament &8// &cThere are currently no tournaments running."));
                    return true;
                }
                
                if(!api.getPlayers().contains(ps)) {
                    ps.sendMessage(ChatUtil.color("&4Tournament &8// &cYou have not joined a tournament."));
                    return true;
                }

                ps.sendMessage(ChatUtil.color("&4Tournament &8// &aYou have successfully left the tournament."));
                api.getPlayers().remove(ps);
                api.getWinners().remove(ps);
                break;

            case "start":
                checkPerm(sender, "smarttournament.start");

                if(api.isTournamentRunning()) {
                    sender.sendMessage(ChatUtil.color("&4Tournament &8// &cA tournament is already running."));
                    return true;
                }

                if(TournamentPlugin.i.getConfig().get("arenas") == null ||
                        TournamentPlugin.i.getConfig().get("spectator") == null) {
                    sender.sendMessage(ChatUtil.color("&4Tournament &8// &cYou must set a spectator area and at least one arena first."));
                    return true;
                }

                sender.sendMessage(ChatUtil.color("&4Tournament &8// &aYou have successfully started the tournament."));
                api.startTournament();
                break;

            case "end":
                checkPerm(sender, "smarttournament.end");

                if(!api.isTournamentRunning()) {
                    sender.sendMessage(ChatUtil.color("&4Tournament &8// &cThere is no tournament currently running."));
                    return true;
                }
                api.endTournament();
                break;

            case "setspawn":
                checkPlayer(sender);
                ps = (Player) sender;

                checkPerm(sender, "smarttournament.setspawn");
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("spectator")) {
                        api.setSpectatorArea(ps);
                        return true;
                    }
                }

                if(args.length == 3) {//  /st setspawn <arena name> <num>
                    String arenaName = args[1];

                    checkNum(args[2]);
                    int num = Integer.parseInt(args[2]);

                    if(num < 1 || num > 2) {
                        ps.sendMessage(ChatUtil.color("&4Tournament &8// &cYou must enter either '1' or '2' for the position."));
                        return true;
                    }

                    api.setLocation(arenaName, ps, num);
                    return true;
                }
                break;

            default:
                sender.sendMessage(ChatUtil.color("&4Tournament &8// &cImproper usage. Type /tournament start | end | join | leave | setspawn <arena> <num>."));
                break;
        }
        return false;
    }

    private void checkNum(String number) {
        int num;
        try {
            num = Integer.parseInt(number);
        } catch(NumberFormatException ex) {
            throw new CommandException(ChatUtil.color("&4Tournament &8// &cYou must enter a positive integer for the third argument."));
        }
    }

    private void checkPerm(CommandSender cs, String perm) {
        if (!cs.hasPermission(perm))
            throw new CommandException("&4Tournament &8// &cYou do not have permission to execute this command.");
    }

    private void checkArgs(String[] args, int min) {
        if (args.length < min)
            throw new CommandException("&4Tournament &8// &cYou did not specify enough arguments for this command.");
    }

    private void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException("&4Tournament &8// &cThis command is not usable from the console.");
    }
}
