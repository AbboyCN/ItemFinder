package com.abboycn.itemfinder.commands.tabExecuter;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TabexeItemFinder {
    public static CompletableFuture<Suggestions> suggestItemFinderReload(SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        List<String> targets = new ArrayList<>();
        targets.add("pinyin");
        targets.add("unstackable_items");
        targets.add("zones");

        for(String target : targets){
            if (target.startsWith(input))
                builder.suggest(target);
        }

        return builder.buildFuture();
    }
}
