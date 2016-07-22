package net.ihid.smarttournament.hooks;

import lombok.Getter;
import net.minelink.ctplus.CombatTagPlus;

/**
 * Created by Mikey on 7/20/2016.
 */
public class HookManager {
    @Getter
    private CombatTagPlusHook combatTagPlusHook;

    public HookManager() {
        this.combatTagPlusHook = new CombatTagPlusHook();
    }
}
