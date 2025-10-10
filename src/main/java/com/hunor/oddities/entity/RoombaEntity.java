package com.hunor.oddities.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RoombaEntity extends AnimalEntity {

    private final SimpleInventory inventory = new SimpleInventory(9);

    public RoombaEntity(EntityType<? extends AnimalEntity> entityType, World world) { super(entityType, world); }

    public SimpleInventory getInventory() { return inventory; }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new TemptGoal(this, 1, (stack) -> { return stack.isOf(Items.REDSTONE); }, false));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1, 50));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }

    @Override
    public void tick() {
        super.tick();

        List<ItemEntity> items = this.getWorld().getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.3), i -> true);
        for (ItemEntity item : items) {
            if (!item.isRemoved()) {
                ItemStack stack = item.getStack();

                ItemStack remaining = inventory.addStack(stack);
                if (remaining.isEmpty()) item.remove(RemovalReason.DISCARDED); else item.setStack(remaining);


            }
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                ItemScatterer.spawn(this.getWorld(), this.getX(), this.getY(), this.getZ(), stack);
            }
            inventory.clear();
        }
        super.onDeath(damageSource);
    }


    @Override
    public boolean isBreedingItem(ItemStack stack) { return false; }

    @Override
    public boolean shouldDropXp() { return false; }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }
}
