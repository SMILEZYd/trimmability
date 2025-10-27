package com.smilezyd.trimmability.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.block.custom.EngravingTable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;



public class ModBlocks{
    
    public static Block ENGRAVING_TABLE;
    
    public static void registerModBlocks() {
        
        Trimmability.LOGGER.info("Registering Blocks for " + Trimmability.MOD_ID + "...");

        ENGRAVING_TABLE = register(
                "engraving_table",
                EngravingTable::new,
                Block.Settings.create().strength(2.5f, 1200f).sounds(BlockSoundGroup.WOOD).nonOpaque(), true
        );


        
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.addAfter(Items.LOOM, ModBlocks.ENGRAVING_TABLE);
        });
        
        Trimmability.LOGGER.info("Registered Blocks for " + Trimmability.MOD_ID);
        
    }
    
    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
            // Create a registry key for the block
            RegistryKey<Block> blockKey = keyOfBlock(name);
            // Create the block instance
            Block block = blockFactory.apply(settings.registryKey(blockKey));

            // Sometimes, you may not want to register an item for the block.
            // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
            if (shouldRegisterItem) {
                

                // Items need to be registered with a different type of registry key, but the ID
                // can be the same.
                RegistryKey<Item> itemKey = keyOfItem(name);
                                
                try {
                    
                    BlockItem blockItem = new BlockItem(block, new Item
                        .Settings()
                        .registryKey(itemKey)
                    );
                
                    Registry.register(Registries.ITEM, itemKey, blockItem);
                
                    Trimmability.LOGGER.info("Registered block item for " + itemKey+ " with ID: "+ Registries.ITEM.getId(blockItem));
                } catch (Exception e) {
                    // The 'e' variable now holds the ArithmeticException object.
                    // You can access its properties, such as the error message.
                    Trimmability.LOGGER.error("Warning, failed to register block item for block:" + block + " because " + e.getMessage() + (". See next line for details"));
                    Trimmability.LOGGER.error("", e);
                }

            }

            return Registry.register(Registries.BLOCK, blockKey, block);
    }
    
    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Trimmability.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Trimmability.MOD_ID, name));
    }
    
    
    
}
