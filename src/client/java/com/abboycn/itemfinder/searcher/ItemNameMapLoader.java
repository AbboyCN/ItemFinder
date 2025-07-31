package com.abboycn.itemfinder.searcher;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemNameMapLoader {
    public static final Map<String, Item> itemMap = new HashMap<>();
    public static final List<String> itemNameTabList = new ArrayList<>();
    public static final Multimap<String, String> itemPinyinMap = ArrayListMultimap.create();

    public static void load(){
        itemMap.clear();
        for(Item item: Registries.ITEM){
            String itemName = item.getName().getString();
            itemMap.put(itemName,item);
            itemNameTabList.add(itemName);
            itemPinyinMap.put(Pinyin4jHelper.getPinyinInitials(itemName),itemName);
        }
    }

    public static Map<String,Item> getItemNameMap(){
        return itemMap;
    }
    public static List<String> getItemNameTabList(){
        return itemNameTabList;
    }
    public static Multimap<String,String> getItemPinyinMap(){
        return itemPinyinMap;
    }
}
