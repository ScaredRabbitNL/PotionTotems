package io.github.scaredsmods.potion_totems.datagen.client;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

public class PTIItemModelGenerator extends ItemModelProvider {


    public PTIItemModelGenerator(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, PotionTotems.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        PTItems.ITEMS.stream().map(RegistryEntry::get).forEach(item -> {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            String itemName = id.getPath();
            withExistingParent(itemName, mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/base_totem"));
        });
    }
}
