package com.abboycn.itemfinder.commands.tabExecuter;

import com.google.common.collect.Multimap;
import com.mojang.brigadier.suggestion.Suggestions;
import com.abboycn.itemfinder.searcher.ItemNameMapLoader;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TabexeFindItemPinyin {
    public static CompletableFuture<Suggestions> fuzzySuggestPinyin(UnsortedSuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        Multimap<String, String> itemPinyinMap = ItemNameMapLoader.getItemPinyinMap();
        List<String> itemNames = ItemNameMapLoader.getItemNameTabList();

        List<String> exactMatches = new ArrayList<>();
        List<String> startsWithMatches = new ArrayList<>();
        List<String> containsMatches = new ArrayList<>();

        itemPinyinMap.asMap().forEach((pinyin, items) -> {
            if(pinyin == null)
                return;
            if (pinyin.equals(input))
                exactMatches.addAll(items);
            else if (pinyin.startsWith(input))
                startsWithMatches.addAll(items);
            else if (pinyin.contains(input))
                containsMatches.addAll(items);
        });

        for(String name : itemNames){
            if(name.contains(input)){
                builder.suggest(name);
            }
        }

        exactMatches.forEach(builder::suggest);
        startsWithMatches.forEach(builder::suggest);
        containsMatches.forEach(builder::suggest);

        return builder.buildFuture();
    }
}
