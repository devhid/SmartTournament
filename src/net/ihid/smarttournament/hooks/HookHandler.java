package net.ihid.smarttournament.hooks;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;

/**
 * Created by Mikey on 7/23/2016.
 */
public class HookHandler {
    @Getter
    private CombatTagPlusHook combatTagPlusHook;

    public HookHandler() {
        this.combatTagPlusHook = new CombatTagPlusHook();
    }
}
