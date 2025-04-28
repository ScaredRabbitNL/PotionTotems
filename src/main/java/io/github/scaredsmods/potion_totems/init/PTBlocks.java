package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.block.BaseInfuserBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PTBlocks {

    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, PotionTotems.MOD_ID);

    public static final RegistryEntry<Block> INFUSER = BLOCKS.register("infuser", () -> new BaseInfuserBlock(BlockBehaviour.Properties.of()));
}
