package io.github.scaredsmods.potion_totems.item;

import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.rabbilib.api.item.TotemItem;
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


    public PotionTotemItem() {
        this(null);
    }

    public PotionTotemItem(Holder<MobEffect>  effect) {
        super(new Properties());
        this.extraPotionEffect = effect;
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
                entity.addEffect(new MobEffectInstance(getEffectHolder(), PTConfig.duration, PTConfig.amplifier));
                entity.level().broadcastEntityEvent(entity, (byte)35);
                stack.shrink(1);


        }
    }

    public  Holder<MobEffect> getEffectHolder() {
        return this.extraPotionEffect;
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
            Holder<MobEffect> effect = item.getEffectHolder();
            return getEffectColor(effect);
        }
        return 0xFFFFFFFF;
    }
}
