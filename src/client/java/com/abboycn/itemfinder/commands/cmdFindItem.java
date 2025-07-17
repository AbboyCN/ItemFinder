package com.abboycn.itemfinder.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;


public class cmdFindItem {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("finditem").executes(c -> cmdFindItemExecuter(c.getSource().getPlayer())));
    }

    public static int cmdFindItemExecuter(ClientPlayerEntity player){
        player.sendMessage(Text.literal("物品xxx在 x y z 位置."),false);
        return Command.SINGLE_SUCCESS;
        //return 0;
    }
}
