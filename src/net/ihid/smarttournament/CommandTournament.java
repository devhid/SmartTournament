package net.ihid.smarttournament;

import net.ihid.smarttournament.config.Lang;
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

                if(!api.isTournamentRunning()) {
                    ps.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if(api.getTournament().getStage() == TournamentStage.ACTIVE) {
                    ps.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if(api.getPlayers().contains(ps)) {
                    ps.sendMessage(Lang.ALREADY_IN_TOURNAMENT.toString());
                    return true;
                }

                api.getPlayers().add(ps);
                api.removeTag(ps);

                ps.sendMessage(Lang.JOINED_TOURNAMENT_SUCCESS.toString());
                ps.teleport(api.getSpectatorArea());

                break;

            case "leave":
                checkPlayer(sender);
                ps = (Player) sender;

                if(!api.isTournamentRunning()) {
                    ps.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }
                
                if(!api.getPlayers().contains(ps)) {
                    ps.sendMessage(Lang.NOT_IN_TOURNAMENT.toString());
                    return true;
                }

                ps.sendMessage(Lang.LEFT_TOURNAMENT_SUCCESS.toString());
                api.getPlayers().remove(ps);
                api.getWinners().remove(ps);
                break;

            case "start":
                checkPerm(sender, "smarttournament.start");

                if(api.isTournamentRunning()) {
                    sender.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if(TournamentPlugin.i.getConfig().get("arenas") == null ||
                        TournamentPlugin.i.getConfig().get("spectator") == null) {
                    sender.sendMessage(Lang.TOURNAMENT_AREAS_NOT_SET.toString());
                    return true;
                }

                sender.sendMessage(Lang.TOURNAMENT_START_SUCCESS.toString());
                api.startTournament();
                break;

            case "end":
                checkPerm(sender, "smarttournament.end");

                if(!api.isTournamentRunning()) {
                    sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }
                sender.sendMessage(ChatUtil.color(""));
                api.endTournament();
                break;

            case "setspawn":
                checkPlayer(sender);
                ps = (Player) sender;

                checkPerm(sender, "smarttournament.setspawn");
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("spectator")) {
                        sender.sendMessage(Lang.SPECTATOR_AREA_SET.toString());
                        api.setSpectatorArea(ps);
                        return true;
                    }
                }

                if(args.length == 3) {//  /st setspawn <arena name> <num>
                    String arenaName = args[1];

                    checkNum(args[2]);
                    int num = Integer.parseInt(args[2]);

                    if(num < 1 || num > 2) {
                        ps.sendMessage(Lang.ARENA_INVALID_POSITION.toString());
                        return true;
                    }

                    sender.sendMessage("&4You have successfully set the arena: " + arenaName + ".");
                    api.setLocation(arenaName, ps, num);
                    return true;
                }
                break;

            default:
                throw new CommandException(Lang.IMPROPER_USAGE.toString());
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
            throw new CommandException("&4Tournament &8// &cImproper usage. Type /tournament start | end | join | leave | setspawn <arena> <num>.");
    }

    private void checkPlayer(CommandSender cs) {
        if (!(cs instanceof Player))
            throw new CommandException("&4Tournament &8// &cThis command is not usable from the console.");
    }
}
