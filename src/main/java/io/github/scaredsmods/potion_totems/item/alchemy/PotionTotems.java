package io.github.scaredsmods.potion_totems.item.alchemy;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.init.PTRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

public class PotionTotems {


    public static final ResourcefulRegistry<PotionTotem> POTION_TOTEMS = ResourcefulRegistries.create(PTRegistries.R_POTION_TOTEM, PotionTotemsMain.MOD_ID);


    public static final HolderRegistryEntry<PotionTotem> INVISIBILITY = POTION_TOTEMS.registerHolder("invisibility" , () -> new PotionTotem(new MobEffectInstance(MobEffects.INVISIBILITY)));
    public static final HolderRegistryEntry<PotionTotem> NIGHT_VISION = POTION_TOTEMS.registerHolder("night_vision", () -> new PotionTotem(new MobEffectInstance(MobEffects.NIGHT_VISION)));
    public static final HolderRegistryEntry<PotionTotem> HEALING = POTION_TOTEMS.registerHolder("healing", () -> new PotionTotem(new MobEffectInstance(MobEffects.HEAL)));
    public static final HolderRegistryEntry<PotionTotem> HEALTH_BOOST = POTION_TOTEMS.registerHolder("health_boost", () -> new PotionTotem(new MobEffectInstance(MobEffects.HEALTH_BOOST)));
    public static final HolderRegistryEntry<PotionTotem> FIRE_RESISTANCE = POTION_TOTEMS.registerHolder("fire_resistance", () -> new PotionTotem(new MobEffectInstance(MobEffects.FIRE_RESISTANCE)));





}
