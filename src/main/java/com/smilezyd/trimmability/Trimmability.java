package com.smilezyd.trimmability;

import com.smilezyd.trimmability.block.entity.ModBlockEntities;
import com.smilezyd.trimmability.item.ModItems;
import com.smilezyd.trimmability.util.networking.ModServerNetworking;
import com.smilezyd.trimmability.util.networking.RepositionSlots;
import com.smilezyd.trimmability.util.screenhandlers.ModScreenHandlers;
import com.smilezyd.trimmability.util.ModComponents;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smilezyd.trimmability.block.ModBlocks;

public class Trimmability implements ModInitializer {
	public static final String MOD_ID = "trimmability";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        //Register client and server communication
        PayloadTypeRegistry.playC2S().register(RepositionSlots.ID, RepositionSlots.CODEC);
        ModServerNetworking.registerHandlers();


        ModBlocks.registerModBlocks();
        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerScreenHandlers();
        ModComponents.registerModComponents();
        ModItems.registerModItems();

	}
}
