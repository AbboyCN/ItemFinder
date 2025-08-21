package com.abboycn.itemfinder.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.*;

public class shulkerRender {
    private static final Map<BlockPos, ShulkerMarker> activeMarkers = new HashMap<>();
    private static final int DURATION_MS = 30000; // 30秒

    // 潜影贝标记数据类
    private static class ShulkerMarker {
        public final long spawnTime;
        public final ShulkerEntity shulkerEntity;

        public ShulkerMarker(long spawnTime, ShulkerEntity shulkerEntity) {
            this.spawnTime = spawnTime;
            this.shulkerEntity = shulkerEntity;
        }
    }

    public static void clearAll() {
        // 移除所有潜影贝实体
        for (ShulkerMarker marker : activeMarkers.values()) {
            marker.shulkerEntity.discard();
        }
        activeMarkers.clear();
    }

    public static void addMarkers(Collection<BlockPos> positions) {
        clearAll();
        long time = System.currentTimeMillis();
        ClientWorld world = getClientWorld();

        if (world == null) return;

        for (BlockPos pos : positions) {
            BlockPos immutablePos = pos.toImmutable();

            // 创建潜影贝实体
            ShulkerEntity shulker = createShulkerMarker(world, immutablePos);

            // 添加到世界和管理器
            world.addEntity(shulker);
            activeMarkers.put(immutablePos, new ShulkerMarker(time, shulker));
        }
    }

    public static void update(ClientWorld world) {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<BlockPos, ShulkerMarker>> it = activeMarkers.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<BlockPos, ShulkerMarker> entry = it.next();
            ShulkerMarker marker = entry.getValue();

            // 移除过期标记
            if (now - marker.spawnTime > DURATION_MS) {
                marker.shulkerEntity.discard();
                it.remove();
                continue;
            }
//
//            // 更新潜影贝效果（例如脉动效果）
//            updateShulkerEffects(marker.shulkerEntity, marker.spawnTime, now);
        }
    }

    private static ShulkerEntity createShulkerMarker(ClientWorld world, BlockPos pos) {
        ShulkerEntity shulker = new ShulkerEntity(EntityType.SHULKER, world);

        // 设置位置
        shulker.setPosition(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5
        );

        // 设置外观和属性
        shulker.setInvulnerable(true);
        shulker.setSilent(true);
        shulker.setAiDisabled(true);
        shulker.setNoGravity(true);

        // 设置效果
        shulker.addStatusEffect(new StatusEffectInstance(//发光
                StatusEffects.GLOWING,
                Integer.MAX_VALUE,
                1,
                false,
                false
        ));
        shulker.addStatusEffect(new StatusEffectInstance(//隐身
                StatusEffects.INVISIBILITY,
                Integer.MAX_VALUE,
                1,
                false,
                false
        ));

        return shulker;
    }

//    private static void updateShulkerEffects(ShulkerEntity shulker, long spawnTime, long currentTime) {
//        float progress = (currentTime - spawnTime) / 1000f; // 转换为秒
//        double yOffset = Math.sin(progress * 2) * 0.05; // 缓慢上下浮动
//
//        shulker.setPosition(shulker.getX(), shulker.getY() + yOffset, shulker.getZ());
//    }

    private static ClientWorld getClientWorld() {
        return net.minecraft.client.MinecraftClient.getInstance().world;
    }

    // 获取当前活跃的标记数量（用于调试）
    public static int getActiveMarkerCount() {
        return activeMarkers.size();
    }

    // 获取特定位置的标记（如果需要交互）
    public static ShulkerEntity getMarkerAt(BlockPos pos) {
        ShulkerMarker marker = activeMarkers.get(pos.toImmutable());
        return marker != null ? marker.shulkerEntity : null;
    }
}