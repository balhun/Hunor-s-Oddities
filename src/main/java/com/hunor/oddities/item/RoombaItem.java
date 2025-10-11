package com.hunor.oddities.item;

import com.hunor.oddities.HunorsOddities;
import com.hunor.oddities.entity.RoombaEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RoombaItem extends Item {

    public RoombaItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }

        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockPos spawnPos = pos.offset(direction);

        // Spawn the Roomba
        RoombaEntity roomba = HunorsOddities.ROOMBA.create(world);
        if (roomba != null) {
            roomba.refreshPositionAndAngles(
                    spawnPos.getX() + 0.5,
                    spawnPos.getY(),
                    spawnPos.getZ() + 0.5,
                    0.0F,
                    0.0F
            );
            world.spawnEntity(roomba);
            stack.decrement(1);
        }

        return ActionResult.CONSUME;
    }

}
