package com.abboycn.itemfinder.commands;

import com.abboycn.itemfinder.render.beamRender;
import com.abboycn.itemfinder.searcher.ItemSearcher;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class cmdFindItem {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher,CommandRegistryAccess registryAccess){
        dispatcher.register(ClientCommandManager.literal("finditem")
                        .then(argument("itemID", ItemStackArgumentType.itemStack(registryAccess))
                        .executes(c -> cmdFindItemExecuter(c.getSource().getPlayer(),ItemStackArgumentType.getItemStackArgument(c,"itemID").getItem()))));
    }

    public static int cmdFindItemExecuter(ClientPlayerEntity player, Item target){
        if(player==null)
            return 0;
        if(!(player.getWorld().getRegistryKey() == World.OVERWORLD)){
            player.sendMessage(Text.literal("§c维度\"minecraft:overworld\"未在客户端加载"),false);
            return 0;
        }
        if(target== Items.AIR){
            player.sendMessage(Text.literal("§c你可以按下Alt+F4退出游戏,谁闲的没事通过崩端退游戏啊?!"),false);
            return 0;
        }
        List<BlockPos> found = ItemSearcher.search(player.clientWorld, target);
        if(found.isEmpty()) {
            player.sendMessage(Text.literal("§c无法找到物品\"" + target.getName().getString() + "\"的位置信息"), false);
            return 0;
        }
        // 更新光柱
        beamRender.addBeams(found);
        // 发送结果
        player.sendMessage(Text.literal("§a物品 §e" + target.getName().getString() + "§a 位于：§b("+ found.size() + ")"), false);
        found.forEach(pos ->
                player.sendMessage(Text.literal(
                        String.format("§7- §f[%d, %d, %d]",
                                pos.getX(), pos.getY(), pos.getZ())
                ), false)
        );
        return 1;
    }
}
