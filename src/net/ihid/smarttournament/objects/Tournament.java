package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Tournament {

    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private TournamentTask tournamentTask;

    @Getter
    private PreTournamentTask preTournamentTask;

    public Tournament() {
        this.stage = TournamentStage.NON_ACTIVE;
    }

    public void start() {
        reset(false);
        TournamentPlugin.getTournamentAPI().loadArenas();

        preTournamentTask = new PreTournamentTask(this);
        preTournamentTask.runTaskTimer(TournamentPlugin.getInstance(), 0L, 20L);
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

    public void reset(boolean end) {
        TournamentAPI api = TournamentPlugin.getTournamentAPI();
        Collection<? extends Player> online = Bukkit.getOnlinePlayers();

        if(end) {
            online.stream().filter(api::isInTournament).forEach(player -> {
                if(api.getPlayerStates().containsKey(player)) {
                    api.getPlayerStates().get(player).revert();
                    api.getPlayerStates().remove(player);
                }
                player.teleport(api.getWorldSpawn());
            });

            api.getPlayers().clear();
        }

        api.getWinners().clear();
        api.getMatches().clear();
        api.getArenas().clear();
    }
}
