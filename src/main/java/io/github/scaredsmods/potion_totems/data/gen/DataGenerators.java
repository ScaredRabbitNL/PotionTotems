package io.github.scaredsmods.potion_totems.data.gen;


import io.github.scaredsmods.potion_totems.PotionTotems;
import io.github.scaredsmods.potion_totems.data.gen.provider.PTBlockTagProvider;
import io.github.scaredsmods.potion_totems.data.gen.provider.PTEnLanguageGenerator;
import io.github.scaredsmods.potion_totems.data.gen.provider.PTIItemModelGenerator;
import io.github.scaredsmods.potion_totems.data.gen.provider.PTItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
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

        //BlockTagsProvider blockTagsProvider = new PTBlockTagProvider(packOutput, lookupProvider, existingFileHelper);

        generator.addProvider(event.includeClient(), new PTIItemModelGenerator(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new PTEnLanguageGenerator(packOutput));
        //generator.addProvider(event.includeServer(), new PTItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));


    }

}
