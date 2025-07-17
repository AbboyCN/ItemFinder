package com.abboycn.itemfinder.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFindItem {

    private static final Map<Identifier, BlockPos> ITEM_POSITIONS = new HashMap<>();
    private static BlockPos lastFoundPos = null;
    private static long lastActivationTime = 0;
    private static final long DURATION_MS = 30_000;

    // 生成彩色光柱
    private static void spawnBeam(ClientWorld world, BlockPos pos) {
        Random random = world.random;

        // 粒子参数配置
        ParticleEffect particle = ParticleTypes.GLOW; // 基础发光粒子
        boolean force = true;      // 强制渲染（无视距离限制）
        boolean canSpawnOnMinimal = false; // 不在简化渲染模式下生成

        // 每0.3格一层粒子（原版信标间距为1）
        for (double y = pos.getY()-10; y <= 160; y += 0.3) {
            // 每层生成6个粒子（形成圆形横截面）
            for (int i = 0; i < 6; i++) {
                double angle = i * Math.PI * 2 / 6; // 6等分圆
                double radius = 0.2; // 光柱半径

                // 计算粒子位置（圆形分布）
                double xOffset = Math.cos(angle) * radius;
                double zOffset = Math.sin(angle) * radius;

                // 随机速度（向中心轻微聚拢）
                double velX = -xOffset * 0.01;
                double velY = 0.05 + random.nextDouble() * 0.03; // 基础上升+随机
                double velZ = -zOffset * 0.01;

                world.addParticle(
                        particle,
                        force,
                        canSpawnOnMinimal,
                        pos.getX() + 0.5 + xOffset,
                        y + 0.5,
                        pos.getZ() + 0.5 + zOffset,
                        velX, velY, velZ
                );
            }

            // 中心补充1个粒子（增强密度）
            world.addParticle(
                    ParticleTypes.ELECTRIC_SPARK, // 中心使用电火花粒子
                    force,
                    canSpawnOnMinimal,
                    pos.getX() + 0.5,
                    y + 0.5,
                    pos.getZ() + 0.5,
                    0, 0.1, 0
            );
        }
    }

    // 超时管理
    private static void updateBeamPosition(BlockPos pos) {
        lastFoundPos = pos;
        lastActivationTime = System.currentTimeMillis();
    }

    private static boolean isBeamExpired() {
        return System.currentTimeMillis() - lastActivationTime > DURATION_MS;
    }

    private static void spawnBeamWithTimeout(ClientWorld world) {
        if (lastFoundPos == null || isBeamExpired()) {
            lastFoundPos = null;
            return;
        }
        spawnBeam(world, lastFoundPos);
    }

    // 加载物品坐标数据
    public static void loadItemPositions() {
        try (InputStream inputStream = cmdFindItem.class.getClassLoader()
                .getResourceAsStream("assets/itemfinder/item_locations/item_positions.json")) {

            if (inputStream == null) throw new RuntimeException("物品坐标文件未找到");

            JsonObject json = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            for (String itemId : json.keySet()) {
                JsonObject posData = json.getAsJsonObject(itemId);
                ITEM_POSITIONS.put(
                        Identifier.of(itemId),
                        new BlockPos(
                                posData.get("x").getAsInt(),
                                posData.get("y").getAsInt(),
                                posData.get("z").getAsInt()
                        )
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("加载物品坐标失败: " + e.getMessage(), e);
        }
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher,CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("finditem")
                        .then(argument("itemID", ItemStackArgumentType.itemStack(registryAccess))
                        .executes(c -> cmdFindItemExecuter(c.getSource().getPlayer(),ItemStackArgumentType.getItemStackArgument(c,"itemID").getItem()))));
    }

    public static int cmdFindItemExecuter(ClientPlayerEntity player, Item item){
        if(player==null)
            return 0;
        Identifier itemID= Registries.ITEM.getId(item);
        BlockPos position=ITEM_POSITIONS.get(itemID);
        if(position==null) {
            player.sendMessage(Text.literal("无法找到物品\"" + item.getName().getString() + "\"的位置信息"), false);
            return 0;
        }
        player.sendMessage(Text.literal(String.format("§a查询到 §e%s §a位于: §b%d, %d, %d",
                item.getName().getString(),
                position.getX(),
                position.getY(),
                position.getZ()
        )),false);
        // 生成光柱
        updateBeamPosition(position);
        spawnBeam(player.clientWorld, position);
        return 1;
        //return 0;
    }

    // 注册光柱渲染器
    public static void registerBeamRenderer() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world != null) {
                spawnBeamWithTimeout(world);
            }
        });
    }
}
