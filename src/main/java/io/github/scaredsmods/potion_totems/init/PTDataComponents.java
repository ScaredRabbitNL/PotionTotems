package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.data.component.PotionTotemContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class PTDataComponents {

    public static final ResourcefulRegistry<DataComponentType<?>> DATA_COMPONENT_TYPES =
            ResourcefulRegistries.create(BuiltInRegistries.DATA_COMPONENT_TYPE, PotionTotems.MOD_ID);

    public static final RegistryEntry<DataComponentType<PotionTotemContents>> TOTEM_CONTENTS = register("potion_totem_contents", potionTotemBuilder -> potionTotemBuilder.persistent(PotionTotemContents.CODEC)
            .networkSynchronized(PotionTotemContents.STREAM_CODEC).cacheEncoding());

    private static <T> RegistryEntry<DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

}
