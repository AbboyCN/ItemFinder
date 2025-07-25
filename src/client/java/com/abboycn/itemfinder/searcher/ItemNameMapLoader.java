package com.abboycn.itemfinder.searcher;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.HashMap;
import java.util.Map;

public class ItemNameMapLoader {
    public static final Map<String, Item> itemMap = new HashMap<>();

    public static void load(){
        for(Item item: Registries.ITEM){
            itemMap.put(item.getName().getString(),item);
            System.out.println(item.getName().getString());
        }
    }

    public static Map<String,Item> getItemNameMap(){
        return itemMap;
    }
}
