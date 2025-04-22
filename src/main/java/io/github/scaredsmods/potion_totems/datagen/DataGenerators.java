package io.github.scaredsmods.potion_totems.datagen;


import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.datagen.client.PTEnLanguageGenerator;
import io.github.scaredsmods.potion_totems.datagen.client.PTIItemModelGenerator;
import io.github.scaredsmods.potion_totems.datagen.server.PTRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PotionTotems.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new PTIItemModelGenerator(packOutput, existingFileHelper));
        generator.addProvider(true, new PTEnLanguageGenerator(packOutput));
        generator.addProvider(event.includeServer(), new PTRecipeProvider(packOutput, lookupProvider));

    }
}
