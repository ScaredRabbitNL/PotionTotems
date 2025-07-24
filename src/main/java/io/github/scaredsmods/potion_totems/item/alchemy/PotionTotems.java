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







}
