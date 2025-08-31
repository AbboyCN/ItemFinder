package com.abboycn.itemfinder.commands.tabExecuter;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;

import java.util.*;

public class UnsortedSuggestions extends Suggestions {
    public UnsortedSuggestions(Suggestions suggestions){
        super(suggestions.getRange(),suggestions.getList());
    }

    public static Suggestions create(String command, Collection<Suggestion> suggestions) {
        if (suggestions.isEmpty()) {
            return new Suggestions(StringRange.at(0), new ArrayList<>());
        } else {
            int start = Integer.MAX_VALUE;
            int end = Integer.MIN_VALUE;

            for(Suggestion suggestion : suggestions) {
                start = Math.min(suggestion.getRange().getStart(), start);
                end = Math.max(suggestion.getRange().getEnd(), end);
            }

            StringRange range = new StringRange(start, end);
            List<Suggestion> unsorted = new ArrayList<>();

            for(Suggestion suggestion : suggestions) {
                unsorted.add(suggestion.expand(command,range));
            }

            return new Suggestions(range, unsorted);
        }
    }
}
