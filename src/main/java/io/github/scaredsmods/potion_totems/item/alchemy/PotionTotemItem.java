package io.github.scaredsmods.potion_totems.item.alchemy;


import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.item.TotemItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;

public class PotionTotemItem extends PotionItem implements TotemItem {




    public PotionTotemItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));

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
            entity.level().broadcastEntityEvent(entity, (byte)35);
            stack.shrink(1);

        }
    }



    private void addEffects(LivingEntity entity, ItemStack stack) {
        Iterable<MobEffectInstance> customPEffects = stack.get(DataComponents.POTION_CONTENTS).getAllEffects();
        getEffects(entity, customPEffects);
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);

        getTooltipTexts(context, tooltipComponents, potionContents);


    }

    public void getEffects(LivingEntity entity, Iterable<MobEffectInstance> effects) {
        for (MobEffectInstance instance : effects) {
            Holder<MobEffect> effect = instance.getEffect();
            entity.addEffect(new MobEffectInstance(effect, PTConfig.duration, PTConfig.amplifier));
        }
    }

    private void getTooltipTexts(Item.TooltipContext context, List<Component> tooltipComponents, PotionContents contents) {
        if (contents != null) {
            contents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }

}
