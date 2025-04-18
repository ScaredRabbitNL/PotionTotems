package io.github.scaredsmods.potion_totems.items;

import io.github.scaredsmods.potion_totems.lib.item.TotemItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PotionTotemItem extends Item implements TotemItem {


    private  final Holder<MobEffect> extraPotionEffect;
    private final int duration;
    private final int amplifier;
    private final int color;



    public PotionTotemItem(int duration, int amplifier, int color) {
        this(null, duration, amplifier, color);
    }

    public PotionTotemItem(Holder<MobEffect>  effect, int duration, int amplifier, int color) {
        super(new Properties());
        this.extraPotionEffect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.color = color;
    }


    @Override
    public boolean canActivate(LivingEntity entity) {
        return true;
    }

    @Override
    public void activateTotem(LivingEntity entity, ItemStack stack) {


        if (stack != null ) {

            if (entity instanceof ServerPlayer) {
                ((ServerPlayer) entity).awardStat(Stats.ITEM_USED.get(this));
                CriteriaTriggers.USED_TOTEM.trigger(((ServerPlayer) entity), stack);
            }


                entity.setHealth(entity.getMaxHealth() / 2F);
                entity.removeAllEffects();

                if (this.extraPotionEffect.isBound()) {
                    entity.addEffect(new MobEffectInstance(this.extraPotionEffect, this.duration, this.amplifier));
                }

                entity.level().broadcastEntityEvent(entity, (byte)35);
                stack.shrink(1);




        }
    }

    public  Holder<MobEffect> getEffectHolder() {
        return this.extraPotionEffect;
    }



}
