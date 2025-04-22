package io.github.scaredsmods.potion_totems.datagen.client;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;

import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.registry.PTItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PTIItemModelGenerator extends ItemModelProvider {


    public PTIItemModelGenerator(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, PotionTotems.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {


        PTItems.ITEMS.stream().map(RegistryEntry::get).forEach(item -> {

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            String itemName = id.getPath();


            withExistingParent(itemName, ResourceLocation.fromNamespaceAndPath(PotionTotems.MOD_ID,"item/totem_base"));
        });
    }
}
