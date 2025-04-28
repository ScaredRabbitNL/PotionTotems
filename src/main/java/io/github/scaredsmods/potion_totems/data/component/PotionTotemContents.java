package io.github.scaredsmods.potion_totems.data.component;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotemItem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record PotionTotemContents(Optional<Holder<PotionTotem>> potionTotem, List<MobEffectInstance> effects) {

    public static final PotionTotemContents EMPTY = new PotionTotemContents(Optional.empty(), List.of());
    private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);
    private static final Codec<PotionTotemContents> FULL_CODEC = RecordCodecBuilder.create(
            p_348387_ -> p_348387_.group(
                            PotionTotem.CODEC.optionalFieldOf("potion_totem").forGetter(PotionTotemContents::potionTotem),
                            MobEffectInstance.CODEC.listOf().optionalFieldOf("custom_effects", List.of()).forGetter(PotionTotemContents::effects)
                    )
                    .apply(p_348387_, PotionTotemContents::new)
    );
    public static final Codec<PotionTotemContents> CODEC = Codec.withAlternative(FULL_CODEC, PotionTotem.CODEC, PotionTotemContents::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionTotemContents> STREAM_CODEC = StreamCodec.composite(
            PotionTotem.STREAM_CODEC.apply(ByteBufCodecs::optional), PotionTotemContents::potionTotem,
            MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()), PotionTotemContents::effects,
            PotionTotemContents::new
    );

    public PotionTotemContents(Holder<PotionTotem> p_331208_) {
        this(Optional.of(p_331208_), List.of());
    }


    public static ItemStack createItemStack(Item item, Holder<Potion> potion) {
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return itemstack;
    }

    public boolean is(Holder<PotionTotem> potion) {
        return this.potionTotem.isPresent() && this.potionTotem.get().is(potion) && this.effects.isEmpty();
    }


    public Iterable<MobEffectInstance> getAllEffects() {
        if (this.potionTotem.isEmpty()) {
            return this.effects;
        } else {
            return (Iterable<MobEffectInstance>)(this.effects.isEmpty()
                    ? this.potionTotem.get().value().getEffects()
                    : Iterables.concat(this.potionTotem.get().value().getEffects(), this.effects));
        }
    }

    public void forEachEffect(Consumer<MobEffectInstance> action) {
        if (this.potionTotem.isPresent()) {
            for (MobEffectInstance mobeffectinstance : this.potionTotem.get().value().getEffects()) {
                action.accept(new MobEffectInstance(mobeffectinstance));
            }
        }

        for (MobEffectInstance mobeffectinstance1 : this.effects) {
            action.accept(new MobEffectInstance(mobeffectinstance1));
        }
    }

    public boolean hasEffects() {
        return !this.effects.isEmpty() || this.potionTotem.isPresent() && !this.potionTotem.get().value().getEffects().isEmpty();
    }

    public List<MobEffectInstance> customEffects() {
        return Lists.transform(this.effects, MobEffectInstance::new);
    }

    public void addPotionTooltip(Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond) {
        addPotionTooltip(this.getAllEffects(), tooltipAdder, durationFactor, ticksPerSecond);
    }

    public static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        boolean flag = true;

        for (MobEffectInstance mobeffectinstance : effects) {
            flag = false;
            MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
            Holder<MobEffect> holder = mobeffectinstance.getEffect();
            holder.value().createModifiers(mobeffectinstance.getAmplifier(), (p_331556_, p_330860_) -> list.add(new Pair<>(p_331556_, p_330860_)));
            if (mobeffectinstance.getAmplifier() > 0) {
                mutablecomponent = Component.translatable(
                        "potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier())
                );
            }

            if (!mobeffectinstance.endsWithin(20)) {
                mutablecomponent = Component.translatable(
                        "potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, durationFactor, ticksPerSecond)
                );
            }

            tooltipAdder.accept(mutablecomponent.withStyle(holder.value().getCategory().getTooltipFormatting()));
        }

        if (flag) {
            tooltipAdder.accept(NO_EFFECT);
        }

        if (!list.isEmpty()) {
            tooltipAdder.accept(CommonComponents.EMPTY);
            tooltipAdder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            // Neo: Override handling of potion attribute tooltips to support IAttributeExtension
            net.neoforged.neoforge.common.util.AttributeUtil.addPotionTooltip(list, tooltipAdder);
            if (true) return;

            for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                AttributeModifier attributemodifier = pair.getSecond();
                double d1 = attributemodifier.amount();
                double d0;
                if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    d0 = attributemodifier.amount();
                } else {
                    d0 = attributemodifier.amount() * 100.0;
                }

                if (d1 > 0.0) {
                    tooltipAdder.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d1 < 0.0) {
                    d0 *= -1.0;
                    tooltipAdder.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }
        }
    }
}
