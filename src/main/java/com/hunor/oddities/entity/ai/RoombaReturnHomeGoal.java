package com.hunor.oddities.entity.ai;

import com.hunor.oddities.entity.RoombaEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class RoombaReturnHomeGoal extends Goal {
    private final RoombaEntity roomba;

    public RoombaReturnHomeGoal(RoombaEntity roomba) {
        this.roomba = roomba;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        // Only return home if:
        // 1. Has items in inventory
        // 2. Not on cooldown (use roomba's cooldown)
        // 3. Home position exists
        // 4. Not already at home
        if (!this.roomba.hasItemsInInventory() || this.roomba.cooldown > 0 || this.roomba.getHomePos() == null) {
            return false;
        }

        // special case: already home but has items → drop them now
        if (this.isAtHome()) {
            this.dropItems();
            this.roomba.cooldown = 60;
            return false;
        }

        return true;
    }

    @Override
    public boolean shouldContinue() {
        // Keep going until we reach home
        return !this.isAtHome() && this.roomba.hasItemsInInventory();
    }

    @Override
    public void start() {
        // Start pathfinding to home
        BlockPos home = this.roomba.getHomePos();
        this.roomba.getNavigation().startMovingTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5, 1.0);
    }

    @Override
    public void stop() {
        this.roomba.getNavigation().stop();

        // If we reached home, drop items
        if (this.isAtHome()) {
            this.dropItems();
            this.roomba.cooldown = 60;
        }
    }

    @Override
    public void tick() {
        BlockPos home = this.roomba.getHomePos();

        if (home != null) {
            double distance = this.roomba.squaredDistanceTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5);

            if (distance < 0.5) {
                this.dropItems();
                this.roomba.cooldown = 100;
                return;
            }

            double dx = (home.getX() + 0.5) - this.roomba.getX();
            double dz = (home.getZ() + 0.5) - this.roomba.getZ();
            double length = Math.sqrt(dx * dx + dz * dz);


            if (length > 0) {
                double targetX = (home.getX() + 0.5) + (dx / length);
                double targetZ = (home.getZ() + 0.5) + (dz / length);

                this.roomba.getNavigation().startMovingTo(
                        targetX,
                        home.getY(),
                        targetZ,
                        1.0
                );
            }
        }
    }

    private boolean isAtHome() {
        BlockPos home = this.roomba.getHomePos();
        if (home == null) return false;

        return this.roomba.squaredDistanceTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5) < 0.5;
    }

    private void dropItems() {

        // Drop all items from inventory
        for (int i = 0; i < this.roomba.getInventory().size(); i++) {
            if (!this.roomba.getInventory().getStack(i).isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(
                        this.roomba.getWorld(),
                        this.roomba.getHomePos().getX() + 0.5,
                        this.roomba.getY(),
                        this.roomba.getHomePos().getZ() + 0.5,
                        this.roomba.getInventory().getStack(i)
                );

                itemEntity.setVelocity(0, 0, 0);
                this.roomba.getWorld().spawnEntity(itemEntity);
                this.roomba.getInventory().setStack(i, net.minecraft.item.ItemStack.EMPTY);
            }
        }
    }
}