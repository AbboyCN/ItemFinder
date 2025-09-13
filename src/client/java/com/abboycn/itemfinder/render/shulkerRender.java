package com.abboycn.itemfinder.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class shulkerRender {
    public static final String MOD_ID = "itemfinder";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Map<BlockPos, ShulkerMarker> activeMarkers = new HashMap<>();
    private static final int DURATION_MS = 30000; // 30秒

    // 潜影贝标记数据类
        private record ShulkerMarker(long spawnTime, ShulkerEntity shulkerEntity) {}

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

    public static void update() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<BlockPos, ShulkerMarker>> it = activeMarkers.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<BlockPos, ShulkerMarker> entry = it.next();
            ShulkerMarker marker = entry.getValue();

            // 移除过期标记
            if (now - marker.spawnTime > DURATION_MS) {
                marker.shulkerEntity.discard();
                it.remove();
                //continue;
            }
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
        shulker.setGlowing(true);
        shulker.setInvisible(true);
        if(shulker.isGlowingLocal()) {
            LOGGER.info("The shulker is not glowing,retrying...");
            shulker.addStatusEffect(new StatusEffectInstance(//发光
                    StatusEffects.GLOWING,
                    Integer.MAX_VALUE,
                    1,
                    false,
                    false
            ));
        }
        if(shulker.isInvisible()) {
            LOGGER.info("The shulker is not invisible,retrying...");
            shulker.addStatusEffect(new StatusEffectInstance(//隐身
                    StatusEffects.INVISIBILITY,
                    Integer.MAX_VALUE,
                    1,
                    false,
                    false
            ));
        }
        if(!(shulker.isInvisible()||shulker.isGlowingLocal())) {
            LOGGER.warn("The shulker can not be effected,deleted!");
            shulker.setHealth(0);
        }

        return shulker;
    }

    private static ClientWorld getClientWorld() {
        return net.minecraft.client.MinecraftClient.getInstance().world;
    }
}