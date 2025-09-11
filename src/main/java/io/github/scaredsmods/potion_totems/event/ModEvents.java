package io.github.scaredsmods.potion_totems.event;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import io.github.scaredsmods.potion_totems.item.alchemy.PotionTotemItem;
import io.github.scaredsmods.potion_totems.util.PotionType;
import io.github.scaredsmods.potion_totems.init.PTItems;
import io.github.scaredsmods.potion_totems.init.PTPotions;
import io.github.scaredsmods.potion_totems.init.PTVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.scaredsmods.potion_totems.util.VillagerUtils.getOffer;

@EventBusSubscriber(modid = PotionTotemsMain.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {


        List<Holder<Potion>> regularPotionsTier1 = List.of(Potions.INFESTED, Potions.OOZING, Potions.LUCK, Potions.LEAPING, Potions.HEALING);
        List<Holder<Potion>> regularPotionsTier2 = List.of(Potions.NIGHT_VISION, Potions.STRONG_REGENERATION, Potions.POISON);
        List<Holder<Potion>> regularPotionsTier3 = List.of(PTPotions.AGGRESSION.holder(), Potions.STRONG_STRENGTH, Potions.STRONG_POISON);
        List<Holder<Potion>> potionTotemsTier1 = List.of(Potions.INVISIBILITY, Potions.LEAPING, Potions.FIRE_RESISTANCE);
        List<Holder<Potion>> potionTotemsTier2 = List.of(Potions.HEALING, Potions.REGENERATION, Potions.STRENGTH);
        List<Holder<Potion>> potionTotemsTier3 = List.of(PTPotions.AGGRESSION.holder());

        if(event.getType() == PTVillagers.TOTEM_MASTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((trader, random) -> getOffer(PotionType.POTION, new PotionContents(regularPotionsTier1.get(random.nextInt(regularPotionsTier1.size()))), 1, 0.8F, 15, random.nextIntBetweenInclusive(10, 30)));
            trades.get(1).add((trader, random) -> getOffer(PotionType.POTION_TOTEM, new PotionContents(potionTotemsTier1.get(random.nextInt(potionTotemsTier1.size()))), 1, 0.8F, 15,  random.nextIntBetweenInclusive(10, 30)));

            trades.get(2).add((trader, random) -> getOffer(PotionType.POTION_TOTEM, new PotionContents(potionTotemsTier2.get(random.nextInt(potionTotemsTier2.size()))), 3, 0.4F, 10, random.nextIntBetweenInclusive(27, 45)));
            trades.get(2).add((trader, random) -> getOffer(PotionType.POTION, new PotionContents(regularPotionsTier2.get(random.nextInt(regularPotionsTier2.size()))), 3, 0.4F, 10, random.nextIntBetweenInclusive(27, 45)));

            trades.get(3).add((trader, random) -> getOffer(PotionType.POTION_TOTEM, new PotionContents(potionTotemsTier3.get(random.nextInt(potionTotemsTier3.size()))), 5, 0.2F, 5, random.nextIntBetweenInclusive(40, 64)));
            trades.get(3).add((trader, random) -> getOffer(PotionType.POTION, new PotionContents(regularPotionsTier3.get(random.nextInt(regularPotionsTier3.size()))), 5, 0.2F, 5, random.nextIntBetweenInclusive(40, 64)));
        }
    }
}
