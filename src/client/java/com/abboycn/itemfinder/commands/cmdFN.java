package com.abboycn.itemfinder.commands;

import com.abboycn.itemfinder.commands.tabExecuter.TabexeFindItemPinyin;
import com.abboycn.itemfinder.commands.tabExecuter.UnsortedSuggestionsBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static com.abboycn.itemfinder.commands.cmdFindItemName.cmdFindItemNameExecuter;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFN {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("fn")
                .then(argument("itemName", StringArgumentType.greedyString())
                        .suggests((context,builder)-> TabexeFindItemPinyin.fuzzySuggestPinyin(new UnsortedSuggestionsBuilder(builder)))
                        .executes(c -> cmdFindItemNameExecuter(c.getSource().getPlayer(),StringArgumentType.getString(c,"itemName")))));
    }
}
