package io.github.scaredsmods.potion_totems.item.alchemy;


import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.data.component.PotionTotemContents;
import io.github.scaredsmods.potion_totems.init.PTDataComponents;
import io.github.scaredsmods.potion_totems.item.TotemItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;

public class PotionTotemItem extends PotionItem implements TotemItem {

    private final Holder<MobEffect> infusedEffect;

    public PotionTotemItem(Holder<MobEffect> infusedEffect) {
        super(new Properties().stacksTo(1));
        this.infusedEffect = infusedEffect;
    }


    @Override
    public boolean canActivate(LivingEntity entity) {
        return true;
    }
    @Override
    public void activateTotem(LivingEntity entity, ItemStack stack) {


        if (!entity.level().isClientSide) {
            if (stack != null ) {

                if (entity instanceof ServerPlayer) {
                    ((ServerPlayer) entity).awardStat(Stats.ITEM_USED.get(this));
                    CriteriaTriggers.USED_TOTEM.trigger(((ServerPlayer) entity), stack);
                }


                entity.setHealth(entity.getMaxHealth() / 2F);
                entity.removeAllEffects();
                PotionTotemContents potionTotemContents = stack.getOrDefault(PTDataComponents.TOTEM_CONTENTS, PotionTotemContents.EMPTY);
                potionTotemContents.forEachEffect(p_330883_ -> {
                        entity.addEffect(new MobEffectInstance(p_330883_.getEffect(), PTConfig.duration, PTConfig.amplifier));
                });

                entity.level().broadcastEntityEvent(entity, (byte)35);
                stack.shrink(1);


            }
        }

    }


    public static int getEffectColor(Holder<MobEffect> effectHolder) {
        MobEffect effect = effectHolder.value();
        int baseColor = effect.getColor();
        return baseColor | 0xFF000000;
    }


    public int getColor(ItemStack stack, int layer) {
        if (layer == 1) {
            return getColorForStack(stack);
        }
        return 0xFFFFFFFF;
    }

    public static int getColorForStack(ItemStack stack) {
        if (stack.getItem() instanceof PotionTotemItem item) {
            Holder<MobEffect> effect = item.getInfusedEffect();
            return getEffectColor(effect);
        }
        return 0xFFFFFFFF;
    }

    public Holder<MobEffect> getInfusedEffect() {
        return infusedEffect;
    }


}
