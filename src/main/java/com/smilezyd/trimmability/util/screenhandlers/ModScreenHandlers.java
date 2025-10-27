package com.smilezyd.trimmability.util.screenhandlers;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.util.screenhandlers.custom.EngravingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static ExtendedScreenHandlerType<EngravingTableScreenHandler, BlockPos> ENGRAVING_TABLE_SCREEN_HANDLER;

    public static void registerScreenHandlers() {
        Trimmability.LOGGER.info("Registering screen handlers for " + Trimmability.MOD_ID + "...");

        ENGRAVING_TABLE_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                Identifier.of(Trimmability.MOD_ID, "engraving_table"),
                new ExtendedScreenHandlerType<>(EngravingTableScreenHandler::new,
                        PacketCodecs.codec(BlockPos.CODEC))

        );

        Trimmability.LOGGER.info("Registered screen handlers for " + Trimmability.MOD_ID);

    }
}
