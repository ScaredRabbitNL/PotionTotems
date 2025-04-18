package io.github.scaredsmods.potion_totems.items;

import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.lib.item.TotemItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

    public static int getColor(ItemStack stack, int index) {
        if (index != 0 ) return -1;
        LocalPlayer player = Minecraft.getInstance().player;
        int y = ((int) player.getY()) + 64;
        int color = y * 255 / 384;
        return (color << 8 | color) << 8 | color;
    }



}
