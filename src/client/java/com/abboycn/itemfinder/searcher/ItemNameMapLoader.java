package com.abboycn.itemfinder.searcher;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemNameMapLoader {
    public static final Map<String, Item> itemMap = new HashMap<>();
    public static final List<String> itemNameTabList = new ArrayList<>();

    public static void load(){
        itemMap.clear();
        for(Item item: Registries.ITEM){
            itemMap.put(item.getName().getString(),item);
            itemNameTabList.add(item.getName().getString());
        }
    }

    public static Map<String,Item> getItemNameMap(){
        return itemMap;
    }
    public static List<String> getItemNameTabList(){
        return itemNameTabList;
    }
}
