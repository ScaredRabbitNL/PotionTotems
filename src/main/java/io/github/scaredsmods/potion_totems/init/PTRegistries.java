package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class PTRegistries {

    public static final Registry<PotionTotem> TOTEM_REGISTRY = new RegistryBuilder<>(PTResourceKeys.TOTEM_KEY)
            .sync(true)
            .defaultKey(PotionTotems.id("empty"))
            .create();
}
