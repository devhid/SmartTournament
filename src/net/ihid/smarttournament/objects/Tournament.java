package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.config.Lang;
import net.ihid.smarttournament.hooks.VanishNoPacketHook;
import net.ihid.smarttournament.managers.MainManager;
import net.ihid.smarttournament.objects.player.SavedPlayerState;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

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
        this.stage = TournamentStage.INACTIVE;
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
        setStage(TournamentStage.INACTIVE);
    }

    private void reset(boolean end) {
        final VanishNoPacketHook hook = TournamentPlugin.getHookHandler().getVanishNoPacketHook();

        if (hook.isEnabled()) {
            if(!mainManager.getTournamentManager().getOriginalParticipants().isEmpty()) {
                mainManager.getTournamentManager().getOriginalParticipants().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null && hook.isVanished(Bukkit.getPlayer(uuid)))
                .forEach(uuid -> hook.unvanish(Bukkit.getPlayer(uuid)));
            }
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
