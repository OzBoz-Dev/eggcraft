package net.ozbozmodz.eggmod.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.ozbozmodz.eggmod.util.RegisterAll;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EggshellArmorMaterial {
    public static RegistryEntry<ArmorMaterial> EGGSHELL_ARMOR_MATERIAL = registerArmorMaterial("eggshell_armor",
            () -> new ArmorMaterial(
                    Map.of(
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.BOOTS, 0,
                    ArmorItem.Type.BODY, 0
                    ),
                    0,
                    SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
                    () -> Ingredient.ofItems(RegisterAll.EGGSHELL_ITEM),
                    List.of(new ArmorMaterial.Layer(Identifier.of("eggmod", "eggshell_armor"))),
                    0,
                    0
                    ));
    public static void initialize(){

    }

    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> materialSupplier){
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of("eggmod", name), materialSupplier.get());
    }
}
