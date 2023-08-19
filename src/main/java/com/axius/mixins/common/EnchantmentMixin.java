package com.axius.mixins.common;

/**
 * Contains classes related to configuration and setup of the mod.
 */
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Contains classes related to mixin transformations.
 */
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


/**
 * This mixin class provides modifications to the Enchantment class.
 */
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow public abstract boolean isCurse();
    @Shadow public abstract String getDescriptionId();

    /**
     * Retrieves the full name of the enchantment.
     * @author Diaxium
     * @reason Remove's the appending tag if an enchantments level is below 1.
     *
     * @param level The enchantment level.
     * @return A Component representing the enchantment name.
     * @see Enchantment#getFullname(int)
     */
    @Overwrite
    public Component getFullname(int level) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());

        if (this.isCurse()) {
            mutablecomponent.withStyle(ChatFormatting.RED);
        } else {
            mutablecomponent.withStyle(ChatFormatting.GRAY);
        }

        return mutablecomponent;
    }
}
