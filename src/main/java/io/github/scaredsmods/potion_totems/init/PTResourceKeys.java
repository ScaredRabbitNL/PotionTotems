package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PTResourceKeys {

    public static final ResourceKey<Registry<PotionTotem>> TOTEM_KEY = ResourceKey.createRegistryKey(PotionTotems.id("totems"));

}
