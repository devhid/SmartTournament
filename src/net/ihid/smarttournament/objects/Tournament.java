package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.hooks.VanishNoPacketHook;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.player.SavedPlayerState;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Tournament {
    private final MainManager mainManager;

    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private TournamentTask tournamentTask;

    @Getter
    private PreTournamentTask preTournamentTask;

    public Tournament() {
        this.mainManager = TournamentPlugin.getMainManager();
        if(mainManager == null) {
            Bukkit.broadcastMessage("mainManager is null in Tournament class.");
        }
        this.stage = TournamentStage.NON_ACTIVE;
    }

    public void start(boolean forced) {
        reset(false);
        mainManager.loadArenas();

        if(forced) {
            Bukkit.broadcastMessage(Lang.TOURNAMENT_POST_START_BROADCAST.toString());
            tournamentTask = new TournamentTask(this);
            setTournamentTask(tournamentTask);

            tournamentTask.runTaskTimer(TournamentPlugin.getInstance(), 0L, 40L);
        } else {
            preTournamentTask = new PreTournamentTask(this);
            preTournamentTask.runTaskTimer(TournamentPlugin.getInstance(), 0L, 20L);
        }
    }

    public void end() {
        if(preTournamentTask != null) {
            preTournamentTask.cancel();
        }

        if(tournamentTask != null) {
            tournamentTask.cancel();
        }

        reset(true);
        setStage(TournamentStage.NON_ACTIVE);
    }

    private void reset(boolean end) {
        if (TournamentPlugin.getHookHandler().getVanishNoPacketHook().isEnabled()) {
            mainManager.getTournamentManager().getOriginalParticipants().stream()
                    .filter(uuid -> TournamentPlugin.getHookHandler().getVanishNoPacketHook().isVanished(Bukkit.getPlayer(uuid)))
                    .forEach(uuid -> TournamentPlugin.getHookHandler().getVanishNoPacketHook().unvanish(Bukkit.getPlayer(uuid)));
        }

        Collection<? extends Player> online = Bukkit.getOnlinePlayers();

        if(end) {
            online.stream().filter(mainManager::isInTournament).forEach(player -> {
                HashMap<String, SavedPlayerState> playerStates = mainManager.getPlayerStates();

                if(playerStates.containsKey(player.getName())) {
                    playerStates.get(player.getName()).revert();
                    playerStates.remove(player.getName());
                }

                player.teleport(mainManager.getWorldSpawn());
            });
            mainManager.clearParticipants();
            mainManager.getMatches().forEach(match -> match.getMatchTask().cancel());
        }

        mainManager.clearMatchWinners();
        mainManager.clearMatches();
        mainManager.clearArenas();
    }
}
