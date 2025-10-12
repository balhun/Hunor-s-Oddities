package com.hunor.oddities.entity;

import com.hunor.oddities.ModItems;
import com.hunor.oddities.entity.ai.RoombaPickUpGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RoombaEntity extends AnimalEntity {

    private final SimpleInventory inventory = new SimpleInventory(9);

    public int cooldown;

    public RoombaEntity(EntityType<? extends AnimalEntity> entityType, World world) { super(entityType, world); }

    public SimpleInventory getInventory() { return inventory; }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new RoombaPickUpGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1, (stack) -> stack.isOf(Items.REDSTONE), false));
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient) {
            if (!inventory.isEmpty()) playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1F, 1f);
            cooldown = 20;
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if (!stack.isEmpty()) {
                    if (!player.getInventory().insertStack(stack)) {
                        ItemScatterer.spawn(this.getWorld(), this.getX(), this.getY(), this.getZ(), stack);
                    }
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }

            if (player.isInSneakingPose()) {
                this.discard();
                ItemStack ROOMBA_ITEM = new ItemStack(ModItems.ROOMBA_ITEM);
                if (!player.getInventory().insertStack(ROOMBA_ITEM)) {
                    ItemScatterer.spawn(this.getWorld(), this.getX(), this.getY(), this.getZ(), ROOMBA_ITEM);
                }
            }
        }
        return ActionResult.success(this.getWorld().isClient);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_ANVIL_LAND;
    }

    @Override
    protected void playHurtSound(DamageSource damageSource) {
        this.playSound(this.getHurtSound(damageSource), 0.1F, (float)(1.5 + Math.random() * (1.8 - 1.5)));
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_METAL_HIT;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && this.age % 40 == 0) { // Every 2 seconds
            this.playSound(SoundEvents.BLOCK_BEACON_AMBIENT, 0.1F, 1.5F);
        }
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        if (!this.getWorld().isClient) {
            dropStack(new ItemStack(ModItems.ROOMBA_ITEM));
            for (int i = 0; i < inventory.size(); i++) dropStack(inventory.getStack(i));
            inventory.clear();
        }
        super.dropLoot(damageSource, causedByPlayer);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) { return false; }

    @Override
    public boolean shouldDropXp() { return false; }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) { return null; }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory.getHeldStacks(), this.getRegistryManager());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory.getHeldStacks(), this.getRegistryManager());
        super.readNbt(nbt);
    }
}
