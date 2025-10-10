package com.hunor.oddities;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HunorsOddities implements ModInitializer {
	public static final String MOD_ID = "hunors-oddities";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your oddities id as the logger's name.
	// That way, it's clear which oddities wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
	}
}