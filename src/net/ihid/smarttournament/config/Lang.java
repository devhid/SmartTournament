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
    NOT_ENOUGH_PLAYERS("messages.not-enough-players"),
    MAXIMUM_PLAYERS_REACHED("messages.maximum-players-reached"),
    ALREADY_IN_TOURNAMENT("messages.already-in-tournament"),
    NOT_IN_TOURNAMENT("messages.not-in-tournament"),
    MATCH_START_BROADCAST("messages.match-start-broadcast"),
    MATCH_WINNER_BROADCAST("messages.match-winner-broadcast"),
    MATCH_IDLE_BROADCAST("messages.match-idle-broadcast"),
    SPECTATOR_AREA_SET("messages.spectator-area-set"),
    ARENA_INVALID_POSITION("messages.arena-invalid-position"),
    ARENA_SET_SUCCESS("messages.arena-set-success"),
    REQUIRE_EMPTY_INVENTORY("messages.require-empty-inventory"),
    /*REQUIRE_START_BEFORE_FORCESTART("messages.require-start-before-forcestart"),*/
    WORLD_SPAWN_SET("messages.world-spawn-set"),
    COMMAND_USE_DENIED("messages.command-use-denied"),
    TOURNAMENT_ALREADY_STARTED("messages.tournament-already-started"),
    TOURNAMENT_AREAS_NOT_SET("messages.tournament-areas-not-set"),
    TOURNAMENT_COUNTDOWN_BROADCAST("messages.tournament-countdown-broadcast"),
    TOURNAMENT_JOINED_SUCCESS("messages.tournament-joined-success"),
    TOURNAMENT_JOINED_BROADCAST("messages.tournament-joined-broadcast"),
    TOURNAMENT_LIST_PARTICIPANTS("messages.tournament-list-participants"),
    TOURNAMENT_LEFT_SUCCESS("messages.tournament-left-success"),
    TOURNAMENT_LEFT_BROADCAST("messages.tournament-left-broadcast"),
    TOURNAMENT_START_SUCCESS("messages.tournament-start-success"),
    TOURNAMENT_PRE_START_BROADCAST("messages.tournament-pre-start-broadcast"),
    TOURNAMENT_POST_START_BROADCAST("messages.tournament-post-start-broadcast"),
    TOURNAMENT_END_SUCCESS("messages.tournament-end-success"),
    TOURNAMENT_END_BROADCAST("messages.tournament-end-broadcast"),
    TOURNAMENT_NO_WINNER_BROADCAST("messages.tournament-no-winner-broadcast"),
    TOURNAMENT_WINNER_BROADCAST("messages.tournament-winner-broadcast"),
    TOURNAMENT_WINNER_REWARD_MESSAGE("messages.tournament-winner-reward-message");

    private final String path;
    private YamlConfiguration config = TournamentPlugin.getInstance().getConfig();

    Lang(String path) {
        this.path = path;
    }

    public String toString() {
        return ChatUtil.color((config.getBoolean("configuration.prefix-enabled") ? config.getString("configuration.prefix") : "" ) + config.getString(path));
    }
}
