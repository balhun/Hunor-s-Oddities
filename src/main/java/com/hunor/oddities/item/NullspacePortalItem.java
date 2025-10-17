package com.hunor.oddities.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class NullspacePortalItem extends Item {
    public NullspacePortalItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        if (user instanceof ServerPlayerEntity player) {
            RegistryKey<World> currentDimension = player.getWorld().getRegistryKey();
            RegistryKey<World> nullspaceDimension = RegistryKey.of(RegistryKeys.WORLD, Identifier.of("hunors_oddities", "nullspace"));

            // If in Nullspace, return to bed spawn
            if (currentDimension.equals(nullspaceDimension)) {
                ServerWorld overworld = player.getServer().getOverworld();
                BlockPos spawnPos = player.getSpawnPointPosition();

                if (spawnPos != null) {
                    player.teleport(overworld, spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5, 0, 0);
                } else {
                    // Fallback to world spawn if no bed set
                    int x = overworld.getSpawnPos().getX();
                    int y = overworld.getSpawnPos().getY();
                    int z = overworld.getSpawnPos().getZ();
                    player.teleport(overworld, x, y, z, 0, 0);
                }
            } else {
                // Teleport to Nullspace
                ServerWorld nullspaceWorld = player.getServer().getWorld(nullspaceDimension);
                if (nullspaceWorld != null) {
                    player.teleport(nullspaceWorld, 0, 65, 0, 0, 0);
                }
            }
        }

        return TypedActionResult.consume(user.getStackInHand(hand));
    }
}