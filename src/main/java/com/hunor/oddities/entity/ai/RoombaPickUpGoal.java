package com.hunor.oddities.entity.ai;

import com.hunor.oddities.entity.RoombaEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class RoombaPickUpGoal extends Goal {

    private final RoombaEntity roomba;
    private ItemEntity targetItem;
    private int cooldown;

    public RoombaPickUpGoal(RoombaEntity roomba) {
        this.roomba = roomba;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {

        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        this.cooldown = 10;

        List<ItemEntity> items = this.roomba.getWorld().getEntitiesByClass(
                ItemEntity.class,
                this.roomba.getBoundingBox().expand(8.0),
                item -> !item.isRemoved() && item.isAlive()
        );

        if (!items.isEmpty()) {
            // Find the closest item that can actually fit in inventory
            this.targetItem = items.stream()
                    .filter(item -> this.canFitInInventory(item.getStack()))
                    .min((a, b) -> Double.compare(
                            this.roomba.squaredDistanceTo(a),
                            this.roomba.squaredDistanceTo(b)
                    ))
                    .orElse(null);
            return this.targetItem != null;
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.targetItem != null
                && !this.targetItem.isRemoved()
                && this.targetItem.isAlive()
                && this.canFitInInventory(this.targetItem.getStack());
    }

    @Override
    public void start() {
        this.roomba.getNavigation().startMovingTo(this.targetItem, 1);
    }

    @Override
    public void stop() {
        this.targetItem = null;
        this.roomba.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetItem == null || this.targetItem.isRemoved()) {
            return;
        }

        double distance = this.roomba.squaredDistanceTo(this.targetItem);

        // If close enough, pick up
        if (distance < 1.5) { // Within ~1.22 blocks
            this.pickupItem(this.targetItem);
            return;
        }

        // Calculate direction from roomba to item
        double dx = this.targetItem.getX() - this.roomba.getX();
        double dz = this.targetItem.getZ() - this.roomba.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);

        // Pathfind to a position 1 block past the item
        // This forces the roomba to go through the item
        if (length > 0) {
            double targetX = this.targetItem.getX() + (dx / length);
            double targetZ = this.targetItem.getZ() + (dz / length);

            this.roomba.getNavigation().startMovingTo(
                    targetX,
                    this.targetItem.getY(),
                    targetZ,
                    1
            );
        }
    }

    private void pickupItem(ItemEntity item) {
        ItemStack stack = item.getStack();
        ItemStack remaining = this.roomba.getInventory().addStack(stack.copy());

        if (remaining.isEmpty()) {
            item.discard();
            this.targetItem = null;
        } else {
            stack.setCount(remaining.getCount());
        }
    }

    private boolean canFitInInventory(ItemStack itemStack) {
        // Check if this specific item can fit in the inventory
        // Either there's an empty slot, or there's a matching stack with room
        for (int i = 0; i < this.roomba.getInventory().size(); i++) {
            ItemStack slot = this.roomba.getInventory().getStack(i);

            // Empty slot - can always fit
            if (slot.isEmpty()) {
                return true;
            }

            // Matching item with room to stack
            if (ItemStack.areItemsAndComponentsEqual(slot, itemStack) && slot.getCount() < slot.getMaxCount()) {
                return true;
            }
        }

        return false;
    }
}
