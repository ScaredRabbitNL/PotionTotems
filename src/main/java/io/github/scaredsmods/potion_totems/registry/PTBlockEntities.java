package io.github.scaredsmods.potion_totems.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.block.entity.BaseInfuserBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PTBlockEntities {

    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PotionTotems.MOD_ID);
    public static final RegistryEntry<BlockEntityType<BaseInfuserBlockEntity>> INFUSER_BE = BLOCK_ENTITIES.register("infuser_be", () -> BlockEntityType.Builder.of(
            BaseInfuserBlockEntity::new, PTBlocks.INFUSER_BLOCK.get()).build(null));

}
