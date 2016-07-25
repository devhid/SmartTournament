package net.ihid.smarttournament.tasks;

import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.Arena;
import net.ihid.smarttournament.objects.Match;
import net.ihid.smarttournament.objects.Tournament;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Mikey on 4/25/2016.
 */
public class TournamentTask extends BukkitRunnable {
    private MainManager mainManager;
    private Tournament tournament;
    private List<UUID> matchWinners, participants;

    public TournamentTask(Tournament tournament) {
        this.mainManager = TournamentPlugin.getMainManager();
        this.tournament = tournament;
        this.matchWinners = mainManager.getMatchWinners();
        this.participants = mainManager.getParticipants();
        tournament.setStage(TournamentStage.ACTIVE);
    }

    public void run() {
        if(mainManager.getAvailableArena() == null) {
            return;
        }

        Arena arena = mainManager.getAvailableArena();

        if(participants.size() > 1) {
            Match match = new Match(Bukkit.getPlayer(participants.remove(0)), Bukkit.getPlayer(participants.remove(0)));
            match.setArena(arena);

            arena.setOccupied(true);
            mainManager.startMatch(match);

        }

        else if(participants.size() == 1) {
            matchWinners.add(participants.remove(0));

        }

        else if(matchWinners.size() > 1 && participants.size() < 1) {
            participants.addAll(matchWinners);
            matchWinners.clear();
        }

        else if(matchWinners.size() == 1 && participants.size() < 1) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_WINNER_BROADCAST.toString().replace("{winner}", Bukkit.getPlayer(matchWinners.get(0)).getName()));

            final YamlConfiguration config = TournamentPlugin.getInstance().getConfig();
            final Player winner = Bukkit.getPlayer(matchWinners.get(0));

            if(config.getBoolean("configuration.winner-rewards.message-enabled")) {
                winner.sendMessage(Lang.TOURNAMENT_WINNER_REWARD_MESSAGE.toString());
            }

            config.getStringList("configuration.winner-rewards.reward-commands")
                    .forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("{username}", winner.getName())));
            tournament.end();
        }
    }
}
