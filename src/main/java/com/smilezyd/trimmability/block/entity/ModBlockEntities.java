package com.smilezyd.trimmability.block.entity;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.block.ModBlocks;
import com.smilezyd.trimmability.block.entity.custom.EngravingTableEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.logging.Logger;

public class ModBlockEntities {
    public static BlockEntityType<EngravingTableEntity> ENGRAVING_TABLE_ENTITY;

    public static void registerBlockEntities() {
        Trimmability.LOGGER.info("Registering BlockEntities for " + Trimmability.MOD_ID + "...");

//        ENGRAVING_TABLE_ENTITY = register("engraving_table", EngravingTableEntity::new, ModBlocks.ENGRAVING_TABLE);

        Trimmability.LOGGER.info("Registered BlockEntities for " + Trimmability.MOD_ID);

    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(Trimmability.MOD_ID, name);

        Trimmability.LOGGER.info("Registering BlockEntity with ID: " + id);

        try {
            return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
        } catch (Exception e) {
            Trimmability.LOGGER.error("Warning, failed to register block item for block:" + id + " because " + e.getMessage());
            throw new RuntimeException(e);
        }


    }
}
