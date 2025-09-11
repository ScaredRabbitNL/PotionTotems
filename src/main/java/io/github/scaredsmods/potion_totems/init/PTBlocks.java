package io.github.scaredsmods.potion_totems.init;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.block.AdvancedInfuserBlock;
import io.github.scaredsmods.potion_totems.block.InfuserBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PTBlocks {

    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, PotionTotemsMain.MOD_ID);

    public static final RegistryEntry<Block> INFUSER = BLOCKS.register("infuser", () -> new InfuserBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final RegistryEntry<Block> ADVANCED_INFUSER = BLOCKS.register("advanced_infuser", () -> new AdvancedInfuserBlock(BlockBehaviour.Properties.of().noOcclusion()));
}
