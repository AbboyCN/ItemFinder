package com.abboycn.itemfinder.searcher;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSearcher {
    public static List<BlockPos> search(ClientWorld world, Item target) {
        Set<BlockPos> results = new HashSet<>();

        //空气：第二重保险
        if(target==Items.AIR)
            return new ArrayList<>(results);

        //方解石
        if(target== Items.CALCITE){
            results.add(new BlockPos(50,70,-1));
            return new ArrayList<>(results);
        }

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
                            frame.getBlockPos().equals(pos)){
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

        BlockPos pos = UnstackableItemSearcher.findPositionForUnstackableItem(target);
        if(pos!=null)
            results.add(pos.toImmutable());

        return new ArrayList<>(results);
    }

    public static class UnstackableItemSearcher {
        public static BlockPos findPositionForUnstackableItem(Item target) {
            // 获取物品的ID字符串
            Identifier targetItemId = Registries.ITEM.getId(target);
            String targetIdString = targetItemId.toString();

            // 查找对应的位置
            return ItemLoaderUnstackable.findPositionForUnstackableItem(targetIdString);
        }
    }
}
