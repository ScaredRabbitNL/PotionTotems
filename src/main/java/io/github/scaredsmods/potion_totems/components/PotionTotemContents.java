package io.github.scaredsmods.potion_totems.components;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.scaredsmods.potion_totems.init.PTDataComponents;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotem;
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
import net.minecraft.util.FastColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;

public record PotionTotemContents(Optional<Holder<PotionTotem>> potionTotem, Optional<Integer> customColor, List<MobEffectInstance> customEffects) {
    public static final PotionTotemContents EMPTY = new PotionTotemContents(Optional.empty(), Optional.empty(), List.of());
    private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);
    private static final int BASE_POTION_COLOR = -13083194;
    private static final Codec<PotionTotemContents> FULL_CODEC = RecordCodecBuilder.create(
            p_348387_ -> p_348387_.group(
                            PotionTotem.CODEC.optionalFieldOf("potion_totem").forGetter(PotionTotemContents::potionTotem),
                            Codec.INT.optionalFieldOf("custom_color").forGetter(PotionTotemContents::customColor),
                            MobEffectInstance.CODEC.listOf().optionalFieldOf("custom_effects", List.of()).forGetter(PotionTotemContents::customEffects)
                    )
                    .apply(p_348387_, PotionTotemContents::new)
    );
    public static final Codec<PotionTotemContents> CODEC = Codec.withAlternative(FULL_CODEC, PotionTotem.CODEC, PotionTotemContents::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionTotemContents> STREAM_CODEC = StreamCodec.composite(
            PotionTotem.STREAM_CODEC.apply(ByteBufCodecs::optional),
            PotionTotemContents::potionTotem,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional),
            PotionTotemContents::customColor,
            MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            PotionTotemContents::customEffects,
            PotionTotemContents::new
    );

    public PotionTotemContents(Holder<PotionTotem> potionTotem) {
        this(Optional.of(potionTotem), Optional.empty(), List.of());
    }

    public static ItemStack createItemStack(Item item, Holder<PotionTotem> potionTotem) {
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(PTDataComponents.POTION_TOTEM_CONTENTS, new PotionTotemContents(potionTotem));
        return itemstack;
    }

    public boolean is(Holder<PotionTotem> potionTotem) {
        return this.potionTotem.isPresent() && this.potionTotem.get().is(potionTotem) && this.customEffects.isEmpty();
    }

    public Iterable<MobEffectInstance> getAllEffects() {
        if (this.potionTotem.isEmpty()) {
            return this.customEffects;
        } else {
            return (Iterable<MobEffectInstance>)(this.customEffects.isEmpty()
                    ? this.potionTotem.get().value().getEffects()
                    : Iterables.concat(this.potionTotem.get().value().getEffects(), this.customEffects));
        }
    }

    public void forEachEffect(Consumer<MobEffectInstance> action) {
        if (this.potionTotem.isPresent()) {
            for (MobEffectInstance mobeffectinstance : this.potionTotem.get().value().getEffects()) {
                action.accept(new MobEffectInstance(mobeffectinstance));
            }
        }

        for (MobEffectInstance mobeffectinstance1 : this.customEffects) {
            action.accept(new MobEffectInstance(mobeffectinstance1));
        }
    }

    public PotionTotemContents withPotion(Holder<PotionTotem> potion) {
        return new PotionTotemContents(Optional.of(potion), this.customColor, this.customEffects);
    }

    public PotionTotemContents withEffectAdded(MobEffectInstance effect) {
        return new PotionTotemContents(this.potionTotem, this.customColor, Util.copyAndAdd(this.customEffects, effect));
    }

    public int getColor() {
        return this.customColor.isPresent() ? this.customColor.get() : getColor(this.getAllEffects());
    }

    public static int getColor(Holder<PotionTotem> potion) {
        return getColor(potion.value().getEffects());
    }

    public static int getColor(Iterable<MobEffectInstance> effects) {
        return getColorOptional(effects).orElse(-13083194);
    }

    public static OptionalInt getColorOptional(Iterable<MobEffectInstance> effects) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;

        for (MobEffectInstance mobeffectinstance : effects) {
            if (mobeffectinstance.isVisible()) {
                int i1 = mobeffectinstance.getEffect().value().getColor();
                int j1 = mobeffectinstance.getAmplifier() + 1;
                i += j1 * FastColor.ARGB32.red(i1);
                j += j1 * FastColor.ARGB32.green(i1);
                k += j1 * FastColor.ARGB32.blue(i1);
                l += j1;
            }
        }

        return l == 0 ? OptionalInt.empty() : OptionalInt.of(FastColor.ARGB32.color(i / l, j / l, k / l));
    }

    public boolean hasEffects() {
        return !this.customEffects.isEmpty() ? true : this.potionTotem.isPresent() && !this.potionTotem.get().value().getEffects().isEmpty();
    }

    public List<MobEffectInstance> customEffects() {
        return Lists.transform(this.customEffects, MobEffectInstance::new);
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
                        "potion.withAmplifier", mutablecomponent, Component.translatable("potion_totem.potency." + mobeffectinstance.getAmplifier())
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
