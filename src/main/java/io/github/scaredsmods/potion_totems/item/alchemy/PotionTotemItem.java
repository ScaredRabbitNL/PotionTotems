package io.github.scaredsmods.potion_totems.item.alchemy;


import io.github.scaredsmods.potion_totems.components.PotionTotemContents;
import io.github.scaredsmods.potion_totems.config.PTConfig;
import io.github.scaredsmods.potion_totems.init.PTDataComponents;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import javax.annotation.Nonnull;
import java.util.List;

public class PotionTotemItem extends Item implements TotemItem {




    public PotionTotemItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).component(PTDataComponents.POTION_TOTEM_CONTENTS, PotionTotemContents.EMPTY));

    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(PTDataComponents.POTION_TOTEM_CONTENTS, new PotionTotemContents(PotionTotems.INVISIBILITY.holder()));
        return itemstack;
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
        @Nonnull
        Iterable<MobEffectInstance> customEffects = stack.get(PTDataComponents.POTION_TOTEM_CONTENTS).getAllEffects();
        for (MobEffectInstance instance : customEffects) {
            Holder<MobEffect> effect = instance.getEffect();
            entity.addEffect(new MobEffectInstance(effect, PTConfig.duration, PTConfig.amplifier));
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PotionTotemContents potioncontents = stack.get(PTDataComponents.POTION_TOTEM_CONTENTS);
        if (potioncontents != null) {
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }

}
