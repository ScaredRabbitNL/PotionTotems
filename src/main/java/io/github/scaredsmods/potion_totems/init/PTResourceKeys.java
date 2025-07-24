package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PTResourceKeys {

    public static final ResourceKey<Registry<PotionTotem>> RK_R_POTION_TOTEM = ResourceKey.createRegistryKey(PotionTotemsMain.id("potion_totem"));
}
