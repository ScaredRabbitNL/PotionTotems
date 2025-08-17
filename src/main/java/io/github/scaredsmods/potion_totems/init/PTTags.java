package io.github.scaredsmods.potion_totems.init;

import io.github.scaredsmods.potion_totems.PotionTotemsMain;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PTTags {

    public static class Blocks {



        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(PotionTotemsMain.id(name));
        }

    }

    public static class Items {

        public static final TagKey<Item> TOTEMS = createTag("totems");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(PotionTotemsMain.id(name));
        }


    }




}
