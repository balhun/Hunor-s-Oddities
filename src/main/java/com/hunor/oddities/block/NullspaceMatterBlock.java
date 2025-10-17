package com.hunor.oddities.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class NullspaceMatterBlock extends Block {
    public NullspaceMatterBlock(Settings settings) {
        super(settings.sounds(BlockSoundGroup.STONE));
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        // Spawn particles
        if (!world.isClient()) {
            for (int i = 0; i < 3; i++) {
                double x = pos.getX() + 0.5 + (Math.random() - 0.5) * 0.5;
                double y = pos.getY() + 0.5 + (Math.random() - 0.5) * 0.5;
                double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.5;

                ((ServerWorld) world).spawnParticles(
                        ParticleTypes.SMOKE,
                        x, y, z,
                        1,
                        0, 0, 0,
                        0.05
                );
            }
        }

        super.onBroken(world, pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        // Spawn particles
        if (!world.isClient()) {
            for (int i = 0; i < 3; i++) {
                double x = pos.getX() + 0.5 + (Math.random() - 0.5) * 0.5;
                double y = pos.getY() + 0.5 + (Math.random() - 0.5) * 0.5;
                double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.5;

                ((ServerWorld) world).spawnParticles(
                        ParticleTypes.SMOKE,
                        x, y, z,
                        1,
                        0, 0, 0,
                        0.05
                );
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

}