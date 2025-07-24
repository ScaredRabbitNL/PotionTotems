package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class PTRegistries {

    public static final Registry<PotionTotem> R_POTION_TOTEM = new RegistryBuilder<>(PTResourceKeys.RK_R_POTION_TOTEM)
            .sync(false)
            .defaultKey(PotionTotemsMain.id("empty"))
            .maxId(256)
            .create();


}
