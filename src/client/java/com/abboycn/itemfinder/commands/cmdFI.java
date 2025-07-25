package com.abboycn.itemfinder.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;

import static com.abboycn.itemfinder.commands.cmdFindItem.cmdFindItemExecuter;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFI {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("fi")
                .then(argument("itemID", ItemStackArgumentType.itemStack(registryAccess))
                        .executes(c -> cmdFindItemExecuter(c.getSource().getPlayer(),ItemStackArgumentType.getItemStackArgument(c,"itemID").getItem()))));
    }
}
