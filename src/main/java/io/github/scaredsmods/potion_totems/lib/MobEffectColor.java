package io.github.scaredsmods.potion_totems.lib;

import net.minecraft.resources.ResourceLocation;

public enum MobEffectColor {
    SPEED("speed", 0xFF7CAFC6),
    POISON("poison", 0xFF4E9331),
    REGENERATION("regeneration", 0xFFE45AAF),
    STRENGTH("strength", 0xFF932423),
    DEFAULT("default", 0xFFFFFFFF);

    private final String effectId;
    private final int color;


    MobEffectColor(String effectId, int color) {
        this.effectId = effectId;
        this.color = color;

    }


    public static int getColor(ResourceLocation id) {
        for (MobEffectColor value : values()) {
            if (value.effectId.equals(id.getPath())) {
                return value.color;
            }
        }
        return DEFAULT.color;
    }


}