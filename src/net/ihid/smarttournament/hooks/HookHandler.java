package net.ihid.smarttournament.hooks;

import lombok.Getter;
import net.ihid.smarttournament.TournamentPlugin;

/**
 * Created by Mikey on 7/23/2016.
 */
public class HookHandler {
    @Getter
    private CombatTagPlusHook combatTagPlusHook;

    @Getter
    private VanishNoPacketHook vanishNoPacketHook;

    @Getter
    private CrackShotHook crackShotHook;

    public HookHandler() {
        this.combatTagPlusHook = new CombatTagPlusHook();
        this.vanishNoPacketHook = new VanishNoPacketHook();
        this.crackShotHook = new CrackShotHook();
    }
}
