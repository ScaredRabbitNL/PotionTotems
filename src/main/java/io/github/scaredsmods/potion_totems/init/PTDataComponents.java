package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.components.PotionTotemContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class PTDataComponents {

    public static final ResourcefulRegistry<DataComponentType<?>> DATA_COMPONENT_TYPES = ResourcefulRegistries.create(BuiltInRegistries.DATA_COMPONENT_TYPE, PotionTotemsMain.MOD_ID);
    public static final RegistryEntry<DataComponentType<PotionTotemContents>> POTION_TOTEM_CONTENTS = buildComponent("potion_totem_dc_type",
            builder -> builder.persistent(PotionTotemContents.CODEC));

    private static <T> RegistryEntry<DataComponentType<T>> buildComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }



}
