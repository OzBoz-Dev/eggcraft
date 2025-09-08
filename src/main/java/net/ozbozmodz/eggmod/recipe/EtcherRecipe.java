package net.ozbozmodz.eggmod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

public record EtcherRecipe(Ingredient template, Ingredient egg, ItemStack output) implements Recipe<EtcherRecipeInput> {

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        System.out.println("Constructing!");
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.template);
        return list;
    }

    // Read Recipe JSON -> new EtcherRecipe

    @Override
    public boolean matches(EtcherRecipeInput input, World world) {
        System.out.println("TEST0");
        if (world.isClient()) return false;
        System.out.println("TEST");
        return template.test(input.getStackInSlot(0)) && egg.test(input.getStackInSlot(1));
    }

    @Override
    public ItemStack craft(EtcherRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RegisterAll.ETCHER_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
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
