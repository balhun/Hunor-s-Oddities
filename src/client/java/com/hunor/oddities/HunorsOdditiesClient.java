package com.hunor.oddities;

import com.hunor.oddities.model.RoombaModel;
import com.hunor.oddities.renderer.RoombaRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class HunorsOdditiesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		EntityModelLayerRegistry.registerModelLayer(RoombaModel.ROOMBA, RoombaModel::getTexturedModelData);
		EntityRendererRegistry.register(HunorsOddities.ROOMBA, RoombaRenderer::new);
	}
}