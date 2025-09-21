package net.ozbozmodz.eggmod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.ozbozmodz.eggmod.util.RegisterAll;

public record EtcherRecipe(Ingredient template, Ingredient egg, ItemStack output) implements Recipe<SimpleInventory> {

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(0, this.template);
        list.add(1, this.egg);
        return list;
    }

    // Read Recipe JSON -> new EtcherRecipe

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;
        return template.test(inventory.getStack(0)) && egg.test(inventory.getStack(2));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<EtcherRecipe> getSerializer() {
        return RegisterAll.ETCHER_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<EtcherRecipe> getType() {
        return RegisterAll.ETCHER_RECIPE_TYPE;
    }


    public static class Serializer implements RecipeSerializer<EtcherRecipe> {
        // Credit: Modding by Kaupenjoe

        // Looks for field template and field result, then gets them from the JSON where they should be
        public static final MapCodec<EtcherRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("template").forGetter(EtcherRecipe::template),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("egg").forGetter(EtcherRecipe::egg),
                ItemStack.CODEC.fieldOf("result").forGetter(EtcherRecipe::output)
        ).apply(inst, EtcherRecipe::new));

        // Client-Server Synchronization
        public static final PacketCodec<RegistryByteBuf, EtcherRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, EtcherRecipe::template,
                        Ingredient.PACKET_CODEC, EtcherRecipe::egg,
                        ItemStack.PACKET_CODEC, EtcherRecipe::output,
                        EtcherRecipe::new);

        @Override
        public MapCodec<EtcherRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, EtcherRecipe> packetCodec() {
            return STREAM_CODEC;
        }

    }
}
