package com.iridiumposting.arsastrologique.setup.registry;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.iridiumposting.arsastrologique.common.glyphs.*;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void init() {
        registerGlyphs();
    }

    public static void registerGlyphs(){
        register(GlyphAlacrity.INSTANCE);
        register(GlyphStalwart.INSTANCE);
        register(GlyphEmpower.INSTANCE);
        register(GlyphBless.INSTANCE);
        register(GlyphInvigorate.INSTANCE);
        register(GlyphFalseLife.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart){
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

}
