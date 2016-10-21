package net.ihid.smarttournament;

import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.Match;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CommandTournament implements CommandExecutor {
    private TournamentPlugin plugin;
    private MainManager mainManager;

    private List<UUID> partCopy;

    CommandTournament(TournamentPlugin instance) {
        this.plugin = instance;
        this.mainManager = TournamentPlugin.getMainManager();
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
        Player player;

        switch(subCommand) {
            case "join":
                checkPlayer(sender);
                player = (Player) sender;

                if(!mainManager.isTournamentRunning()) {
                    player.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if(mainManager.getTournament().getStage() == TournamentStage.ACTIVE) {
                    player.sendMessage(Lang.TOURNAMENT_ALREADY_STARTED.toString());
                    return true;
                }

                if(mainManager.isInTournament(player)) {
                    player.sendMessage(Lang.ALREADY_IN_TOURNAMENT.toString());
                    return true;
                }

                int allowed = plugin.getConfig().getInt("configuration.maximum-players-allowed");

                if(mainManager.getParticipants().size() == allowed && allowed != -1 ) {
                    player.sendMessage(Lang.MAXIMUM_PLAYERS_REACHED.toString());
                    return true;
                }

                if(plugin.getConfig().getBoolean("configuration.force-player-clear-inventory")) {
                    if (!hasEmptyInventory(player)) {
                        player.sendMessage(Lang.REQUIRE_EMPTY_INVENTORY.toString());
                        return true;
                    }
                }

                if(TournamentPlugin.getHookHandler().getVanishNoPacketHook().isEnabled()) {
                    if(plugin.getConfig().getBoolean("configuration.hide-spectators-mode-enabled")) {
                        TournamentPlugin.getHookHandler().getVanishNoPacketHook().vanish(player);
                    } else {
                        TournamentPlugin.getHookHandler().getVanishNoPacketHook().unvanish(player);
                    }
                }

                mainManager.addParticipant(player);
                mainManager.removeTag(player);

                Bukkit.broadcastMessage(Lang.TOURNAMENT_JOINED_BROADCAST.toString().replace("{username}", player.getName()));
                player.sendMessage(Lang.TOURNAMENT_JOINED_SUCCESS.toString());
                player.teleport(mainManager.getSpectatorArea());

                break;

            case "leave":
                checkPlayer(sender);
                player = (Player) sender;

                if(!mainManager.isTournamentRunning()) {
                    player.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                if(!mainManager.isInTournament(player)) {
                    player.sendMessage(Lang.NOT_IN_TOURNAMENT.toString());
                    return true;
                }

                player.sendMessage(Lang.TOURNAMENT_LEFT_SUCCESS.toString());
                Bukkit.broadcastMessage(Lang.TOURNAMENT_LEFT_BROADCAST.toString().replace("{username}", player.getName()));

                if(mainManager.isInMatch(player)) {
                    final Match match = mainManager.getMatch(player);
                    match.setWinner( player.getName().equalsIgnoreCase(match.getInitiator().getName()) ? match.getOpponent() : match.getInitiator() );

                    mainManager.addMatchWinner(match.getWinner());
                    mainManager.endMatch(mainManager.getMatch(player));
                }

                if(plugin.getConfig().getBoolean("configuration.hide-spectators-mode-enabled")) {
                    if(TournamentPlugin.getHookHandler().getVanishNoPacketHook().isEnabled()) {
                        TournamentPlugin.getHookHandler().getVanishNoPacketHook().unvanish(player);
                    }
                }

                mainManager.removeFromTournament(player);
                player.teleport(mainManager.getWorldSpawn());
                break;

            case "start":
                checkPerm(sender, "smarttournament.start");

                if(mainManager.isTournamentRunning()) {
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
                
                mainManager.startTournament();
                break;

            case "end":
                checkPerm(sender, "smarttournament.end");

                if(!mainManager.isTournamentRunning()) {
                    sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                Bukkit.broadcastMessage(Lang.TOURNAMENT_END_BROADCAST.toString());
                sender.sendMessage(Lang.TOURNAMENT_END_SUCCESS.toString());
                
                mainManager.endTournament();
                break;

            case "setspawn":
                checkPlayer(sender);
                player = (Player) sender;

                checkPerm(sender, "smarttournament.setspawn");
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("-spectator")) {
                        sender.sendMessage(Lang.SPECTATOR_AREA_SET.toString());
                        mainManager.setSpectatorArea(player);
                        return true;
                    }

                    if(args[1].equalsIgnoreCase("-world")) {
                        sender.sendMessage(Lang.WORLD_SPAWN_SET.toString());
                        mainManager.setWorldSpawn(player);
                    }
                }

                if(args.length == 3) {
                    String arenaName = args[1];

                    checkNum(args[2]);
                    Integer num = Integer.parseInt(args[2]);

                    if(num < 1 || num > 2) {
                        player.sendMessage(Lang.ARENA_INVALID_POSITION.toString());
                        return true;
                    }

                    sender.sendMessage(Lang.ARENA_SET_SUCCESS.toString().replace("{arena}", arenaName).replace("{position}", num.toString()));
                    mainManager.setLocation(arenaName, player, num);
                    return true;
                }
                break;
            case "end-match":
                checkPerm(sender, "smarttournament.end-match");
                if(args.length == 1) {
                    if(mainManager.getMatches().size() == 1) {
                        Bukkit.broadcastMessage("This match has been forcefully ended."); // switch to Lang.
                        sender.sendMessage("You have successfully ended the current match"); // switch to Lang.
                        mainManager.getMatchManager().endMatch(mainManager.getMatches().get(0));
                    } else  {
                        String matches = mainManager.getMatches()
                                .stream()
                                .map(match -> match.getMatchTask().getTaskId() + " - " + match.getInitiator().getName() + " vs " + match.getOpponent().getName())
                                .collect(Collectors.joining(", "));
                        sender.sendMessage("Please select which match to end: " + matches);
                    }
                }

                checkArgs(args, 2);
                Match match = mainManager.getMatchManager().getMatchById(Integer.parseInt(args[1]));
                mainManager.getMatchManager().endMatch(match);
                Bukkit.broadcastMessage("You have successfully ended the match between " + match.getInitiator().getName() + " and " + match.getOpponent().getName());
                break;
            /*case "list":
                checkPerm(sender, "smarttournament.list");

                if(!mainManager.isTournamentRunning()) {
                    sender.sendMessage(Lang.NO_TOURNAMENTS_RUNNING.toString());
                    return true;
                }

                partCopy = new ArrayList<>(mainManager.getParticipants());
                mainManager.getParticipants().
            case "forcestart":
                checkPerm(sender, "smarttournament.forcestart");

                if(!mainManager.isTournamentRunning()) {
                    sender.sendMessage(Lang.REQUIRE_START_BEFORE_FORCESTART.toString());
                    return true;
                }*/


            default:
                throw new CommandException(Lang.IMPROPER_USAGE.toString());
        }
        return false;
    }

    private boolean hasEmptyInventory(Player player) {
        final Stream<ItemStack> contents = Stream.concat(Arrays.stream(player.getInventory().getContents()), Arrays.stream(player.getInventory().getArmorContents()));
        return !contents.filter(itemStack -> itemStack != null).anyMatch(itemStack -> itemStack.getType() != Material.AIR);
    }

    private void checkNum(String number) {
        try {
            int num = Integer.parseInt(number);
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
