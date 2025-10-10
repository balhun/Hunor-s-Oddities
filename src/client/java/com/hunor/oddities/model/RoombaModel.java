package com.hunor.oddities.model;

import com.hunor.oddities.HunorsOddities;
import com.hunor.oddities.animation.RoombaAnimation;
import com.hunor.oddities.entity.RoombaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class RoombaModel<R extends AnimalEntity> extends EntityModel<RoombaEntity> {

	public static final AnimationState sweepersAnimationState = new AnimationState();
	public static final AnimationState movingAnimationState  = new AnimationState();

	public static final EntityModelLayer ROOMBA = new EntityModelLayer(Identifier.of(HunorsOddities.MOD_ID, "roomba"), "main");

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart wheels;
	private final ModelPart sweepers;
	private final ModelPart left;
	private final ModelPart right;
	public RoombaModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.wheels = this.root.getChild("wheels");
		this.sweepers = this.root.getChild("sweepers");
		this.left = this.sweepers.getChild("left");
		this.right = this.sweepers.getChild("right");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -2.0F, -7.0F, 10.0F, 2.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 16).cuboid(5.0F, -2.0F, -6.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F))
		.uv(28, 16).cuboid(-7.0F, -2.0F, -6.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData wheels = root.addChild("wheels", ModelPartBuilder.create().uv(24, 35).cuboid(-6.0F, -1.0F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(34, 35).cuboid(5.0F, -1.0F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 40).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData sweepers = root.addChild("sweepers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, -2.0F));

		ModelPartData left = sweepers.addChild("left", ModelPartBuilder.create().uv(10, 33).cuboid(-0.5F, 0.0F, -3.5F, 1.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(4.5F, 0.1F, -4.5F));

		ModelPartData cube_r1 = left.addChild("cube_r1", ModelPartBuilder.create().uv(10, 33).cuboid(-1.0F, 0.1F, -2.0F, 1.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -0.1F, 0.5F, 0.0F, -1.5708F, 0.0F));

		ModelPartData right = sweepers.addChild("right", ModelPartBuilder.create().uv(10, 33).cuboid(-0.5F, 0.0F, -3.5F, 1.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.5F, 0.1F, -4.5F));

		ModelPartData cube_r2 = right.addChild("cube_r2", ModelPartBuilder.create().uv(10, 33).cuboid(-1.0F, 0.1F, -2.0F, 1.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -0.1F, 0.5F, 0.0F, -1.5708F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		root.render(matrices, vertices, light, overlay);
	}

	@Override
	public void setAngles(RoombaEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float sweepersRotation = (entity.age + animationProgress) * 0.2f;
		this.left.yaw = sweepersRotation;
		this.right.yaw = -sweepersRotation;
	}
}