package com.hunor.oddities.renderer;

import com.hunor.oddities.HunorsOddities;
import com.hunor.oddities.entity.RoombaEntity;
import com.hunor.oddities.model.RoombaModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class RoombaRenderer extends MobEntityRenderer<RoombaEntity, RoombaModel<RoombaEntity>> {

    public RoombaRenderer(EntityRendererFactory.Context context) {
        super(context, new RoombaModel<>(context.getPart(RoombaModel.ROOMBA)), 0.5f);
    }

    @Override
    public Identifier getTexture(RoombaEntity entity) {
        return Identifier.of(HunorsOddities.MOD_ID, "textures/entity/roomba.png");
    }
}
