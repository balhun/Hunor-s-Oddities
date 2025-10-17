package com.hunor.oddities;

import com.hunor.oddities.block.ModBlocks;
import com.hunor.oddities.entity.RoombaEntity;
import com.hunor.oddities.item.ModItems;
import com.hunor.oddities.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class HunorsOddities implements ModInitializer {
	public static final String MOD_ID = "hunors_oddities";

	public static final EntityType<RoombaEntity> ROOMBA = Registry.register(Registries.ENTITY_TYPE,
			Identifier.of(MOD_ID, "roomba"),
			EntityType.Builder.create(RoombaEntity::new, SpawnGroup.CREATURE)
					.dimensions(0.9f, 0.3f).build());

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModBlocks.registerModBlocks();
		ModSounds.register();

		FabricDefaultAttributeRegistry.register(ROOMBA, RoombaEntity.createAttributes());
	}
}