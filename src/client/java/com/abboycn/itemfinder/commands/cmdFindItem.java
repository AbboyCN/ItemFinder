package com.abboycn.itemfinder.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFindItem {

    private static final Map<Identifier, ItemPosition> ITEM_POSITIONS = new HashMap<>();

    private static class ItemPosition {
        int x, y, z;
    }

    // 加载物品坐标数据
    public static void loadItemPositions() {
        try (InputStream inputStream = cmdFindItem.class.getClassLoader()
                .getResourceAsStream("assets/itemfinder/item_locations/item_positions.json")) {

            if (inputStream == null) throw new RuntimeException("物品坐标文件未找到");

            JsonObject json = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            for (String itemId : json.keySet()) {
                JsonObject posData = json.getAsJsonObject(itemId);
                ItemPosition pos = new ItemPosition();
                pos.x = posData.get("x").getAsInt();
                pos.y = posData.get("y").getAsInt();
                pos.z = posData.get("z").getAsInt();

                ITEM_POSITIONS.put(Identifier.of(itemId), pos);
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
        ItemPosition position=ITEM_POSITIONS.get(itemID);
        if(position==null) {
            player.sendMessage(Text.literal("无法找到物品\"" + item.getName().getString() + "\"的位置信息"), false);
            return 0;
        }
        player.sendMessage(Text.literal(String.format("§a查询到 §e%s §a位于: §b%d, %d, %d",
                item.getName().getString(),
                position.x,
                position.y,
                position.z
        )),false);
        return 1;
        //return 0;
    }
}
