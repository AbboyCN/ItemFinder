package com.abboycn.itemfinder.commands.tabExecuter;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.abboycn.itemfinder.searcher.ItemNameMapLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TabexeFindItemPinyin {
    public static CompletableFuture<Suggestions> fuzzySuggestPinyin(SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        Multimap<String, String> itemPinyinMap = ItemNameMapLoader.getItemPinyinMap();
        List<String> itemNames = ItemNameMapLoader.getItemNameTabList();

        // 创建三个独立列表保持优先级顺序
        List<String> exactMatches = new ArrayList<>(itemPinyinMap.get(input));
        List<String> startsWithMatches = new ArrayList<>();

        itemPinyinMap.asMap().forEach((pinyin, items) -> {
            if (!pinyin.equals(input) && pinyin.startsWith(input)) {
                startsWithMatches.addAll(items);
            }
        });

        for(String name : itemNames){
            if(name.contains(input)){
                builder.suggest(name);
            }
        }

        exactMatches.forEach(builder::suggest);
        startsWithMatches.forEach(builder::suggest);

        return builder.buildFuture();
    }
}
