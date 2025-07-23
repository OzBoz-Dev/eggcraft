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
    // All the parameters for eggshell armor's values
    public static RegistryEntry<ArmorMaterial> EGGSHELL_ARMOR_MATERIAL = registerArmorMaterial("eggshell",
            () -> new ArmorMaterial(
                    Map.of(
                    ArmorItem.Type.HELMET, 1,
                    ArmorItem.Type.CHESTPLATE, 1,
                    ArmorItem.Type.LEGGINGS, 1,
                    ArmorItem.Type.BOOTS, 1,
                    ArmorItem.Type.BODY, 1
                    ),
                    0,
                    SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                    () -> Ingredient.ofItems(RegisterAll.EGGSHELL_ITEM),
                    List.of(new ArmorMaterial.Layer(Identifier.of("eggmod", "eggshell"))),
                    0,
                    0
                    ));

    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> materialSupplier){
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of("eggmod", name), materialSupplier.get());
    }
}
