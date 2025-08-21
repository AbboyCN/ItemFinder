package com.abboycn.itemfinder.render;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class beamRender {
    private static final Map<BlockPos, Long> activeBeams = new HashMap<>();
    private static final int DURATION_MS = 30000; // 30秒

    public static void clearAll() {
        activeBeams.clear();
    }

    public static void addBeams(Collection<BlockPos> positions) {
        clearAll();
        long time = System.currentTimeMillis();
        positions.forEach(pos -> activeBeams.put(pos.toImmutable(), time));
    }

    public static void update(ClientWorld world) {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<BlockPos, Long>> it = activeBeams.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<BlockPos, Long> entry = it.next();
            BlockPos pos = entry.getKey();

            // 移除过期光柱
            if (now - entry.getValue() > DURATION_MS) {
                it.remove();
                continue;
            }

            // 生成光柱
            spawnBeam(world, pos, calculateAlpha(entry.getValue(), now));
        }
    }

    private static float calculateAlpha(long startTime, long currentTime) {
        float progress = (currentTime - startTime) / (float)DURATION_MS;
        return progress > 0.9f ? 1 - (progress - 0.9f) * 10 : 1.0f;
    }

    private static void spawnBeam(ClientWorld world, BlockPos pos, float alpha) {
        int height = 320 - pos.getY();
        Random random = world.random;

        for (double y = -10; y < height; y += 0.25) {
            world.addParticle(
                    new DustParticleEffect(
                            1693951, // 青蓝色
                            alpha
                    ),
                    true,
                    false,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2,
                    pos.getY() + y,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2,
                    0, 0.05, 0
            );
        }
    }
}
