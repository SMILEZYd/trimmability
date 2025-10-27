package com.smilezyd.trimmability.util.networking;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.util.screenhandlers.custom.EngravingTableScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModServerNetworking {
    public static void registerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(RepositionSlots.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            boolean receivedData = payload.editorScreen();

            // All server-side logic must be run on the server thread
            context.server().execute(() -> {
                // Here is where you implement the logic to reposition slots.
                // You'll need to get the player's current ScreenHandler and modify it.
                if (player.currentScreenHandler instanceof EngravingTableScreenHandler screenHandler) {
                    Trimmability.LOGGER.info("Received reposition request with data: {}", receivedData);

                    // Call a method on your ScreenHandler to update the slot positions
                    screenHandler.updateEditorScreen(receivedData);
                    // Make sure the changes are synced back to the client
                    screenHandler.sendContentUpdates();
                }
            });
        });
    }
}
