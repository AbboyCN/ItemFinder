package com.abboycn.itemfinder.searcher;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class ItemSearcher {
    public static List<BlockPos> search(ClientWorld world, Item target) {
        List<BlockPos> results = new ArrayList<>();

        // 检查方块实体（花盆）
        for (ZoneLoader.Zone zone : ZoneLoader.getZones()) {
            for (BlockPos pos : BlockPos.iterate(
                    (int)zone.box.minX, (int)zone.box.minY, (int)zone.box.minZ,
                    (int)zone.box.maxX, (int)zone.box.maxY, (int)zone.box.maxZ
            )) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof FlowerPotBlock){
                    if(((FlowerPotBlock)state.getBlock()).getContent().asItem()==target)
                        results.add(pos.toImmutable());
                }
            }
        }

        // 检查实体（物品展示框）
        for (Entity entity : world.getEntities()) {
            for (ZoneLoader.Zone zone : ZoneLoader.getZones()) {
                for (BlockPos pos : BlockPos.iterate(
                        (int)zone.box.minX, (int)zone.box.minY, (int)zone.box.minZ,
                        (int)zone.box.maxX, (int)zone.box.maxY, (int)zone.box.maxZ
                )) {
                    if (entity instanceof ItemFrameEntity frame &&
                            frame.getHeldItemStack().getItem() == target &&
                            frame.getBlockPos().equals(pos)) {
                        results.add(pos.toImmutable());
                    }
                }
            }
        }

        // 检查普通方块
        for (ZoneLoader.Zone zone : ZoneLoader.getZones()) {
            for (BlockPos pos : BlockPos.iterate(
                    (int)zone.box.minX, (int)zone.box.minY, (int)zone.box.minZ,
                    (int)zone.box.maxX, (int)zone.box.maxY, (int)zone.box.maxZ
            )) {
                if (world.getBlockState(pos).getBlock().asItem() == target) {
                    results.add(pos.toImmutable());
                }
            }
        }

        return results;
    }

    // 获取已加载区块的方法
    private static List<WorldChunk> getLoadedChunks(ClientWorld world) {
        List<WorldChunk> chunks = new ArrayList<>();
        int viewDist = MinecraftClient.getInstance().options.getViewDistance().getValue();
        BlockPos center = MinecraftClient.getInstance().player.getBlockPos();

        for (int x = -viewDist; x <= viewDist; x++) {
            for (int z = -viewDist; z <= viewDist; z++) {
                WorldChunk chunk = world.getChunk(
                        (center.getX() >> 4) + x,
                        (center.getZ() >> 4) + z
                );
                if (chunk != null) {
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }
}
