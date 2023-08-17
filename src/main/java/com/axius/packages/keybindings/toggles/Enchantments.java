package com.axius.packages.keybindings.toggles;

import com.axius.api.Input;
import com.axius.api.ToolTip;
import com.axius.util.inventory.item.tool.Enchant;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class Enchantments {
    public Enchantments() { };

    public void silkTouchTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<Component> toolTip = event.getToolTip();

        if (itemStack.isEnchanted() && Enchant.contains(itemStack, net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH)) {
            int enchantmentLevel = Enchant.getEnchantmentLevel(itemStack, net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH);
            boolean enchantmentEnabled = enchantmentLevel > 0;

            ChatFormatting valueColor = enchantmentEnabled ? ChatFormatting.GOLD : ChatFormatting.GRAY;
            toolTip.add(Component.literal("Silk Touch").withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(" : ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(enchantmentEnabled ? "Enabled" : "Disabled").withStyle(valueColor)));
        }
    }

    Input.KeyboardEvent SilkTouchKey = new Input.KeyboardEvent("Toggle Silk touch", Input.Key.KEY_LEFT_SHIFT);
    ToolTip.IEvent silkTouchListener = this::silkTouchTooltip;
    ToolTip silkTouchToolTip = new ToolTip("SilkTouch", silkTouchListener);

}
