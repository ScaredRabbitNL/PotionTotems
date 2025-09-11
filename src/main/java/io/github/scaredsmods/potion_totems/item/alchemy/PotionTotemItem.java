package io.github.scaredsmods.potion_totems.item.alchemy;


import io.github.scaredsmods.potion_totems.config.PTCommonConfig;
import io.github.scaredsmods.potion_totems.init.PTConfigs;
import io.github.scaredsmods.potion_totems.item.TotemItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

public class PotionTotemItem extends Item implements TotemItem {

    public PotionTotemItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        return stack;
    }

    @Override
    public boolean canActivate(LivingEntity entity) {
        return true;
    }

    @Override
    public void activateTotem(LivingEntity entity, ItemStack stack) {
        if (stack != null) {
            if (entity instanceof ServerPlayer) {
                ((ServerPlayer) entity).awardStat(Stats.ITEM_USED.get(this));
                CriteriaTriggers.USED_TOTEM.trigger(((ServerPlayer) entity), stack);
            }
            entity.setHealth(entity.getMaxHealth() / 2F);
            entity.removeAllEffects();
            addEffects(entity, stack);
            addVanillaEffects(entity);
            entity.level().broadcastEntityEvent(entity, (byte)35);
            stack.shrink(1);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null) {
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }
    @Override
    public String getDescriptionId(ItemStack stack) {
        return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
    }

    private void addVanillaEffects(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800));
    }

    private void addEffects(LivingEntity entity, ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        PTCommonConfig.InfusedTotemSection infusedTotemSection = PTConfigs.ptCommonConfig.infusedTotemSection;
        if (contents != null) {
            if (contents.hasEffects()) {
                contents.forEachEffect(instance -> entity.addEffect(new MobEffectInstance(instance.getEffect(),infusedTotemSection.duration.get(), infusedTotemSection.amplifier.get())));
            }
        }
    }

}
