package com.hunor.oddities.renderer;

import com.hunor.oddities.entity.RoombaEntity;
import com.hunor.oddities.model.RoombaGeoModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RoombaGeoRenderer extends GeoEntityRenderer<RoombaEntity> {
    public RoombaGeoRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new RoombaGeoModel());
        this.shadowRadius = 0.5f;
    }
}