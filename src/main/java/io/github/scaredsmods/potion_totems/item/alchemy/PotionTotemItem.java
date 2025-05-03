package io.github.scaredsmods.potion_totems.item.alchemy;


import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.item.TotemItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.stream.Stream;

public class PotionTotemItem extends PotionItem implements TotemItem {


    public PotionTotemItem() {
        super(new Properties().stacksTo(1));

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

            if (entity instanceof Player player) {

                entity.setHealth(entity.getMaxHealth() / 2F);
                entity.removeAllEffects();

                PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
                potioncontents.customEffects().stream().map(MobEffectInstance::getEffect).forEach(effectHolder -> {
                    entity.addEffect(new MobEffectInstance(effectHolder, PTConfig.duration,PTConfig.amplifier));
                });

            }
            entity.level().broadcastEntityEvent(entity, (byte) 35);
            stack.shrink(1);
        }
    }



    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
        return itemstack;
    }
    public static int getEffectColor(Holder<MobEffect> effectHolder) {
        MobEffect effect = effectHolder.value();
        int baseColor = effect.getColor();
        return baseColor | 0xFF000000;
    }




    public static int getColorForStack(ItemStack stack) {
        if (stack.getItem() instanceof PotionTotemItem) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            for (MobEffectInstance effect : contents.customEffects().stream().toList()) {
                return getEffectColor(effect.getEffect());
            }
        }
        return 0xFFFFFFFF;
    }





}
