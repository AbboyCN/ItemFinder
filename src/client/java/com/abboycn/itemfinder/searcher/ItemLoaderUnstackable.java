package com.abboycn.itemfinder.searcher;

import com.google.gson.*;
import net.minecraft.util.math.BlockPos;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemLoaderUnstackable {
    private static final List<ItemCategory> LOADED_Categories = new ArrayList<>();

    public record ItemCategory(List<String> itemIds, BlockPos position) {}

    public static void load() {
        LOADED_Categories.clear();
        try (InputStream in = ItemLoaderUnstackable.class.getResourceAsStream("/assets/itemfinder/items/unstackableItems.json")) {
            if (in == null) {
                throw new RuntimeException("Could not find unstackableItems.json");
            }

            JsonObject json = JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject();
            JsonArray unstackableItems = json.getAsJsonArray("unstackableItems");

            for (JsonElement elem : unstackableItems) {
                JsonObject category = elem.getAsJsonObject();
                JsonArray contains = category.getAsJsonArray("contains");
                JsonArray pos = category.getAsJsonArray("position");

                // 转换物品ID列表
                List<String> itemIds = new ArrayList<>();
                for (JsonElement item : contains) {
                    itemIds.add(item.getAsString());
                }

                // 创建BlockPos
                BlockPos position = new BlockPos(
                        pos.get(0).getAsInt(),
                        pos.get(1).getAsInt(),
                        pos.get(2).getAsInt()
                );

                LOADED_Categories.add(new ItemCategory(itemIds, position));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load unstackable items data", e);
        }
    }

    public static BlockPos findPositionForUnstackableItem(String itemId) {
        for (ItemCategory category : LOADED_Categories) {
            if (category.itemIds.contains(itemId)) {
                return category.position;
            }
        }
        return null;
    }
}
