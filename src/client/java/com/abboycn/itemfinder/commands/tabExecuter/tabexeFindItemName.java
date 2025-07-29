package com.abboycn.itemfinder.commands.tabExecuter;

import com.abboycn.itemfinder.searcher.ItemNameMapLoader;
import com.abboycn.itemfinder.searcher.ItemNameMapLoader.*;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class tabexeFindItemName {
    public static CompletableFuture<Suggestions> fuzzySuggest(SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase(); // 获取当前输入（不区分大小写）
        List<String> options = ItemNameMapLoader.getItemNameTabList();

        for (String option : options) {
            if (option.toLowerCase().contains(input)) { // 如果选项包含输入内容
                builder.suggest(option); // 加入补全建议
            }
        }
        return builder.buildFuture();
    }
}
