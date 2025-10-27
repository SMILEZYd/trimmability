package com.smilezyd.trimmability.screen;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.screen.custom.EngravingTableScreen;
import com.smilezyd.trimmability.util.screenhandlers.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreens {
    public static void registerModScreens() {
        Trimmability.LOGGER.info("Registering screens for " + Trimmability.MOD_ID + "...");

        HandledScreens.register(ModScreenHandlers.ENGRAVING_TABLE_SCREEN_HANDLER, EngravingTableScreen::new);

        Trimmability.LOGGER.info("Registered screens for " + Trimmability.MOD_ID);

    }
}
