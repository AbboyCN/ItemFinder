package com.abboycn.itemfinder.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.abboycn.itemfinder.searcher.ItemNameMapLoader;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.Item;


import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFindItemName {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("finditemname")
                .then(argument("itemName", StringArgumentType.string())
                        .executes(c -> cmdFindItemNameExecuter(c.getSource().getPlayer(),StringArgumentType.getString(c,"itemName")))));
    }

    public static int cmdFindItemNameExecuter(ClientPlayerEntity player, String targetItemName){
        return cmdFindItem.cmdFindItemExecuter(player,convertItem(targetItemName));
    }

    public static Item convertItem(String NameToConvert){
        return ItemNameMapLoader.getItemNameMap().get(NameToConvert);
    }
}
