package com.abboycn.itemfinder.searcher;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.HashMap;
import java.util.Map;

public class ItemNameMapLoader {
    public static final Map<String, Item> itemMap = new HashMap<>();

    public static void load(){
        itemMap.clear();
        for(Item item: Registries.ITEM){
            itemMap.put(item.getName().getString(),item);
        }
    }

    public static Map<String,Item> getItemNameMap(){
        return itemMap;
    }
}
