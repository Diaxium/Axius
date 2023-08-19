package com.axius.packages.keybindings.toggles;

/**
 * Contains classes related to external APIs and utilities.
 */
import com.axius.api.Input;
import com.axius.api.ToolTip;
import com.axius.util.GradientConstructor;
import com.axius.util.inventory.item.tool.Enchant;

/**
 * Contains classes related to the Minecraft game client.
 */
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Contains classes related to text components and item manipulation.
 */
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

/**
 * Contains Java standard library classes for collections.
 */
import java.util.List;

/**
 * The ToggleEnchant class manages the behavior of toggling an enchantment effect on and off.
 * It displays a gradient-based visual representation of the toggle state in the item tooltip.
 */
public class ToggleEnchant {
    private final Input.KeyboardEvent toggleKeybinding;
    String UUID;
    private final int delay;
    private final Enchantment enchantment;
    private boolean allowedToggle, isToggled;
    private int delayCounter, toggleCounter;

    /**
     * Constructs a ToggleEnchant object with the specified enchantment, delay, and keybinding.
     *
     * @param enchantment The enchantment to be toggled.
     * @param delay       The delay between consecutive toggles.
     * @param keybinding  The keybinding to activate the toggle.
     */
    public ToggleEnchant(Enchantment enchantment, int delay, int keybinding) {
        this.UUID = Enchant.getName(enchantment) + "_tooltip";

        ToolTip tooltip = new ToolTip(this.UUID, this::tooltipEvent);
        this.toggleKeybinding = new Input.KeyboardEvent(this.UUID, keybinding);

        this.delay = delay;
        this.isToggled = false;
        this.allowedToggle = true;
        this.enchantment = enchantment;
    }

    /**
     * Handles the tooltip event for the toggled enchantment.
     *
     * @param event The tooltip event triggered when hovering over an item.
     */
    private void tooltipEvent(ItemTooltipEvent event) {
        if ((!this.allowedToggle) || (!(Minecraft.getInstance().screen instanceof AbstractContainerScreen) || Minecraft.getInstance().level == null) || (!event.getItemStack().isEnchanted() || !Enchant.contains(event.getItemStack(), this.enchantment))) {
            return;
        }

        List<Component> toolTip = event.getToolTip();
        ItemStack itemStack = event.getItemStack();

        int enchantLevel = Enchant.getEnchantmentLevel(itemStack, this.enchantment);
        boolean isEnabled = enchantLevel > 0;

        if (toggleKeybinding.isHolding() && !this.isToggled) {
            if (this.delayCounter <= this.delay) {
                delayCounter++;

                this.isToggled = this.renderToggle(toolTip, itemStack, isEnabled);

                return;
            }

            delayCounter = 0; // Resets the delay
            toggleCounter++; // Increment the counter

            this.isToggled = this.renderToggle(toolTip, itemStack, isEnabled);
        } else {
            toggleCounter = 0; // Resets the toggle count
            isToggled = toggleKeybinding.isHolding();

            String status = isEnabled ? "Enabled" : "Disabled";
            ChatFormatting statusColor = isEnabled ? ChatFormatting.GREEN : ChatFormatting.DARK_RED;

            String translatable = String.format("axius.packages.keybindings.toggles.%s", this.UUID);

            Component tooltip = Component.translatable(translatable, Input.getKeyName(toggleKeybinding.getKey()))
                    .withStyle(ChatFormatting.GREEN)
                    .append(Component.literal(" : ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(status).withStyle(statusColor));

            toolTip.add(tooltip);
        }
    }

    /**
     * Renders the toggle gradient in the tooltip and performs the enchantment toggling.
     *
     * @param toolTip    The list of tooltip components.
     * @param itemStack  The item stack being hovered over.
     * @param isEnabled  Whether the enchantment is currently enabled.
     * @return True if the toggle has been executed, false otherwise.
     */
    private boolean renderToggle(List<Component> toolTip, ItemStack itemStack, boolean isEnabled) {
        int max = (int) ((toggleCounter / (double) delay) * 10);

        int[] greenColor = {0, 255, 0};
        int[] yellowColor = {255, 255, 0};
        int[] redColor = {255, 0, 0};

        int[] startColor = isEnabled ? greenColor : redColor;
        int[] endColor = isEnabled ? redColor : greenColor;

        String gradientText = GradientConstructor.createGradient("✮".repeat(Math.min(max, 10)),
                startColor, yellowColor, endColor);

        String darkGrayPart = ChatFormatting.DARK_GRAY.toString();
        String finalGradientText = gradientText + darkGrayPart +
                "✮".repeat(Math.max(0, 10 - max));

        toolTip.add(Component.literal(finalGradientText));

        if (this.toggleCounter >= this.delay) {
            int toggleLevel = isEnabled ? -1 : 1;

            Enchant.modify(itemStack, this.enchantment, toggleLevel);

            return true;
        }

        return false;
    }
}