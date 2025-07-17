package com.abboycn.itemfinder.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFindItem {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher,CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("finditem")
                        .then(argument("itemID", ItemStackArgumentType.itemStack(registryAccess))
                        .executes(c -> cmdFindItemExecuter(c.getSource().getPlayer(),ItemStackArgumentType.getItemStackArgument(c,"itemID").getItem()))));
    }

    public static int cmdFindItemExecuter(ClientPlayerEntity player, Item item){
        player.sendMessage(Text.literal(item.getName().getString()),false);
        return Command.SINGLE_SUCCESS;
        //return 0;
    }
}
