package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class PTPotions {

    public static final ResourcefulRegistry<Potion> POTIONS = ResourcefulRegistries.create(BuiltInRegistries.POTION, PotionTotemsMain.MOD_ID);

    public static final HolderRegistryEntry<Potion> AGGRESSION = POTIONS.registerHolder("aggression", () -> new Potion(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1),
            new MobEffectInstance(MobEffects.REGENERATION, 6000, 1), new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 1),
            new MobEffectInstance(MobEffects.INFESTED, 6000)
    ));
}
