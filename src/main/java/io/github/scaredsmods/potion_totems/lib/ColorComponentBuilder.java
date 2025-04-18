package io.github.scaredsmods.potion_totems.lib;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ColorComponentBuilder(int color) {



    public static final Codec<ColorComponentBuilder> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("color").forGetter(ColorComponentBuilder::color)
            ).apply(instance, ColorComponentBuilder::new)
    );
    public static final StreamCodec<ByteBuf, ColorComponentBuilder> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ColorComponentBuilder::color,
            ColorComponentBuilder::new
    );
}
