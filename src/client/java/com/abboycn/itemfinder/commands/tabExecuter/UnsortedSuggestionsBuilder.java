package com.abboycn.itemfinder.commands.tabExecuter;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UnsortedSuggestionsBuilder extends SuggestionsBuilder {
    private final List<Suggestion> unsortedResult;
    public UnsortedSuggestionsBuilder(SuggestionsBuilder builder){
        super(builder.getInput(), builder.getStart());
        unsortedResult = new ArrayList<>();
    }

    public CompletableFuture<Suggestions> buildFuture() {
        return CompletableFuture.completedFuture(this.build());
    }

    public Suggestions build() {
        return UnsortedSuggestions.create(this.getInput(), this.unsortedResult);
    }

    public SuggestionsBuilder suggest(String text) {
        if (!text.equals(this.getRemaining())) {
            this.unsortedResult.add(new Suggestion(StringRange.between(this.getStart(), this.getInput().length()), text));
        }
        return this;
    }
}
