package io.github.scaredsmods.potion_totems.config;


import io.github.scaredsmods.potion_totems.PotionTotems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = PotionTotems.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PTConfig {


    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue DURATION = BUILDER
            .comment("The duration of the potion effect. Notated in ticks.")
            .defineInRange("duration", 400, 20, 5000);

    private static final ModConfigSpec.IntValue AMPLIFIER = BUILDER
            .comment("Determines the level of the potion effect. Ex: Strength II where I is the amplifier. (1 + amplifier)")
            .defineInRange("amplifier", 1, 0, 5);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int duration;
    public static int amplifier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        duration = DURATION.get();
        amplifier = AMPLIFIER.get();

    }
}
