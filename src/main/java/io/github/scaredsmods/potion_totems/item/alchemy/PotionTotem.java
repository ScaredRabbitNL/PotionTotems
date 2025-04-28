package io.github.scaredsmods.potion_totems.item.alchemy;

import com.mojang.serialization.Codec;
import io.github.scaredsmods.potion_totems.init.PTRegistries;
import io.github.scaredsmods.potion_totems.init.PTResourceKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.alchemy.Potion;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PotionTotem implements FeatureElement {

    public static final Codec<Holder<PotionTotem>> CODEC = PTRegistries.TOTEM_REGISTRY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PotionTotem>> STREAM_CODEC = ByteBufCodecs.holderRegistry(PTResourceKeys.TOTEM_KEY);
    /**
     * The base name for the potion type.
     */
    @Nullable
    private final String name;
    private final List<MobEffectInstance> effects;
    private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    public PotionTotem(MobEffectInstance... effects) {
        this(null, effects);
    }

    public PotionTotem(@Nullable String name, MobEffectInstance... effects) {
        this.name = name;
        this.effects = List.of(effects);
    }

    public PotionTotem requiredFeatures(FeatureFlag... requiredFeatures) {
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(requiredFeatures);
        return this;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    public static String getName(Optional<Holder<PotionTotem>> potion, String descriptionId) {
        if (potion.isPresent()) {
            String s = potion.get().value().name;
            if (s != null) {
                return descriptionId + s;
            }
        }

        String s1 = potion.flatMap(Holder::unwrapKey).map(p_331494_ -> p_331494_.location().getPath()).orElse("empty");
        return descriptionId + s1;
    }

    public List<MobEffectInstance> getEffects() {
        return this.effects;
    }

    public boolean hasInstantEffects() {
        if (!this.effects.isEmpty()) {
            for (MobEffectInstance mobeffectinstance : this.effects) {
                if (mobeffectinstance.getEffect().value().isInstantenous()) {
                    return true;
                }
            }
        }

        return false;
    }

}
