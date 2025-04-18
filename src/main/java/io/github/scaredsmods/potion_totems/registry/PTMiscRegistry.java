package io.github.scaredsmods.potion_totems.registry;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.lib.ColorComponentBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PTMiscRegistry {

    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PotionTotems.MOD_ID);


    public static final Supplier<DataComponentType<ColorComponentBuilder>> COLOR_COMPONENT = REGISTRAR.registerComponentType(
            "color",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(ColorComponentBuilder.CODEC)
                    .networkSynchronized(ColorComponentBuilder.STREAM_CODEC)

    );
}
