package net.ihid.smarttournament.config;

import net.ihid.smarttournament.ChatUtil;
import net.ihid.smarttournament.TournamentPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by Mikey on 6/11/2016.
 */
public enum Lang {
    NO_PERMISSION("messages.no-permission"),
    IMPROPER_USAGE("messages.improper-usage"),
    INVALID_SENDER("messages.invalid-sender"),
    INVALID_NUMBER("messages.invalid-number"),
    SEVERE_ERROR("messages.severe-error"),
    NO_TOURNAMENTS_RUNNING("messages.no-tournaments-running"),
    TOURNAMENT_ALREADY_STARTED("messages.tournament-already-started"),
    ALREADY_IN_TOURNAMENT("messages.already-in-tournament"),
    NOT_ENOUGH_PLAYERS("messages.not-enough-players"),
    JOINED_TOURNAMENT_SUCCESS("messages.joined-tournament-success"),
    NOT_IN_TOURNAMENT("messages.not-in-tournament"),
    LEFT_TOURNAMENT_SUCCESS("messages.left-tournament-success"),
    TOURNAMENT_AREAS_NOT_SET("messages.tournament-areas-not-set"),
    SPECTATOR_AREA_SET("messages.spectator-area-set"),
    TOURNAMENT_START_SUCCESS("messages.tournament-start-success"),
    TOURNAMENT_STARTING_BROADCAST("messages.tournament-starting-broadcast"),
    TOURNAMENT_HAS_STARTED_BROADCAST("messages.tournament-has-started-broadcast"),
    TOURNAMENT_END_SUCCESS("messages.tournament-end-success"),
    TOURNAMENT_END_BROADCAST("messages.tournament-end-broadcast"),
    TOURNAMENT_END_ERROR("messages.tournament-end-error"),
    MATCH_IDLE_BROADCAST("messages.match-idle-broadcast"),
    ARENA_INVALID_POSITION("messages.arena-invalid-position"),
    ARENA_SET_SUCCESS("messages.arena-set-success");

    private final String path;
    private YamlConfiguration config = TournamentPlugin.i.getConfig();

    Lang(String path) {
        this.path = path;
    }

    public String toString() {
        return ChatUtil.color(config.getString("messages.prefix") + config.getString(path));
    }
}