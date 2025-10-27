package com.smilezyd.trimmability;

import com.smilezyd.trimmability.screen.ModScreens;
import net.fabricmc.api.ClientModInitializer;

public class TrimmabilityClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        
        ModScreens.registerModScreens();

    }
}
