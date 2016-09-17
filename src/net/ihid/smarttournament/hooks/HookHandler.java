package net.ihid.smarttournament.hooks;

import lombok.Getter;

public class HookHandler {
    @Getter
    private final CombatTagPlusHook combatTagPlusHook;

    @Getter
    private final VanishNoPacketHook vanishNoPacketHook;

    @Getter
    private final CrackShotHook crackShotHook;

    public HookHandler() {
        this.combatTagPlusHook = new CombatTagPlusHook();
        this.vanishNoPacketHook = new VanishNoPacketHook();
        this.crackShotHook = new CrackShotHook();
    }
}
