package com.abboycn.itemfinder.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

import static com.abboycn.itemfinder.commands.cmdFindItemName.cmdFindItemNameExecuter;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFN {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("fn")
                .then(argument("itemName", StringArgumentType.string())
                        .executes(c -> cmdFindItemNameExecuter(c.getSource().getPlayer(),StringArgumentType.getString(c,"itemName")))));
    }
}
