package com.hunor.oddities.model;

import com.hunor.oddities.entity.RoombaEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RoombaGeoModel extends GeoModel<RoombaEntity> {
    private static final Identifier MODEL = Identifier.of("hunors_oddities", "geo/roomba.geo.json");
    private static final Identifier TEXTURE = Identifier.of("hunors_oddities", "textures/entity/roomba.png");
    private static final Identifier ANIMATION = Identifier.of("hunors_oddities", "animations/roomba.animation.json");

    @Override
    public Identifier getModelResource(RoombaEntity animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(RoombaEntity animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(RoombaEntity animatable) {
        return ANIMATION;
    }
}