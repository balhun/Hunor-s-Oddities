package com.hunor.oddities.item;

import com.hunor.oddities.HunorsOddities;
import com.hunor.oddities.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item HUNORS_COFFEE = new HunorsCoffee(new Item.Settings());
    public static final Item ROOMBA_ITEM = new RoombaItem(new Item.Settings());
    public static final Item CIRCUIT_BOARD = new Item(new Item.Settings().maxCount(16));

    public static void initialize() {
        Registry.register(Registries.ITEM, Identifier.of(HunorsOddities.MOD_ID, "hunors_coffee"), HUNORS_COFFEE);
        Registry.register(Registries.ITEM, Identifier.of(HunorsOddities.MOD_ID, "roomba"), ROOMBA_ITEM);
        Registry.register(Registries.ITEM, Identifier.of(HunorsOddities.MOD_ID, "circuit_board"), CIRCUIT_BOARD);

        Registry.register(Registries.ITEM_GROUP, HUNORS_ODDITIES_GROUPKEY, HUNORS_ODDITIES_GROUP);
        ItemGroupEvents.modifyEntriesEvent(HUNORS_ODDITIES_GROUPKEY).register(itemGroup -> {
            itemGroup.add(ModItems.HUNORS_COFFEE);
            itemGroup.add(ModItems.ROOMBA_ITEM);
            itemGroup.add(ModItems.CIRCUIT_BOARD);

            itemGroup.add(ModBlocks.NULLSPACE_MATTER);
        });
    }

    public static final RegistryKey<ItemGroup> HUNORS_ODDITIES_GROUPKEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(HunorsOddities.MOD_ID, "item_group"));
    public static final ItemGroup HUNORS_ODDITIES_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.HUNORS_COFFEE))
            .displayName(Text.translatable("itemGroup.hunors_oddities"))
            .build();
}
