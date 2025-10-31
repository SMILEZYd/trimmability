package com.smilezyd.trimmability.util.screenhandlers.custom;

import com.smilezyd.trimmability.item.ModItems;
import com.smilezyd.trimmability.util.ModComponents;
import com.smilezyd.trimmability.util.screenhandlers.ModScreenHandlers;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.screen.CraftingScreenHandler;


import java.util.*;

import static net.minecraft.item.Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE;

public class EngravingTableScreenHandler extends ScreenHandler {
    private final SimpleInventory inputInventory = new SimpleInventory(2);
    private final SimpleInventory outputInventory = new SimpleInventory(1);
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private boolean editorScreen;



    public EngravingTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(ModScreenHandlers.ENGRAVING_TABLE_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        World world = player.getWorld();
        this.context = ScreenHandlerContext.create(world, pos);

        this.addSlot(new Slot(inputInventory, 0, -28, 12));         //Slot 0 (Blank trim)
        this.addSlot(new Slot(inputInventory, 1, -28, 46));         //Slot 1 (Chisel)
        this.addSlot(new Slot(outputInventory, 0, -28, 80) {        //Slot 2 (Output)
            @Override
            public boolean canInsert(ItemStack stack) {return false;}

            @Override
            public void onQuickTransfer(ItemStack newItem, ItemStack original) {
               super.onQuickTransfer(newItem, original);
                ItemStack trim = inputInventory.getStack(0);
                if (!trim.isEmpty()) {
                    trim.decrement(1);
                }
                onContentChanged(inputInventory);
                // Damage or remove the chisel
                damageSlot(1, player);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);

                // Consume the blank trim
                ItemStack trim = inputInventory.getStack(0);
                if (!trim.isEmpty()) {
                    trim.decrement(1);
                    updateResult(EngravingTableScreenHandler.this, player, inputInventory, outputInventory);
                }

                // Damage or remove the chisel
                damageSlot(1, player);

            }
        });

        this.addPlayerSlots(playerInventory, 8, 84);
        this.inputInventory.addListener(this::onContentChanged);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> {
            if (world instanceof ServerWorld serverWorld) {
                updateResult(this, this.player, this.inputInventory, this.outputInventory);
            }
        });
    }

    public void updateEditorScreen(boolean editorScreen) {
        this.editorScreen = editorScreen;
    }

    public void damageSlot(int slotId, PlayerEntity player1) {

        Slot inventorySlot = getSlotFromID(slotId, player1);

        Inventory inventory = inventorySlot.inventory;
        int slotIndex = inventorySlot.getIndex();

        ItemStack chiselStack = inventory.getStack(slotIndex);
        if (!chiselStack.isEmpty()) {
            if (player1.getWorld() != null && !player1.getWorld().isClient()) {
                // Damage 1 point, handle breakage with a callback
                chiselStack.damage(1, (ServerWorld) player1.getWorld(), (ServerPlayerEntity) player1, brokenItem -> {
                    // This is called when the item breaks
                    player1.sendEquipmentBreakStatus(chiselStack.getItem(), EquipmentSlot.MAINHAND);
                    // Remove broken chisel
                    inventory.setStack(2, ItemStack.EMPTY);
                });
            }
        }
    }

    protected static void updateResult(ScreenHandler handler, PlayerEntity player, Inventory input, Inventory output) {
        ItemStack trim = input.getStack(0);
        ItemStack chisel = input.getStack(1);
        ItemStack result = ItemStack.EMPTY;
        String mirroredTrimData = "820043100"; // sample demo data, mirrored on load

        if (!trim.isEmpty() && trim.getItem() == SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE && chisel.getItem() == ModItems.CHISEL) {
            result = trim.copy();

            result.setCount(1);
            result.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
            result.set(ModComponents.CUSTOM_HELMET_TRIM, mirroredTrimData);


        }

        output.setStack(0, result);

        output.markDirty();
        handler.setPreviousTrackedSlot(2, result);
        handler.sendContentUpdates();

        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.networkHandler.sendPacket(
                    new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 2, result)
            );
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stackInSlot = slot.getStack();
            itemStack = stackInSlot.copy();

            if (index < 2) {
                // move from editor slots to player inventory
                if (!this.insertItem(stackInSlot, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 2) {
                if (!this.insertItem(stackInSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(stackInSlot, itemStack);
            } else {
                    // move from player inventory to editor slots
                    if (!this.insertItem(stackInSlot, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }

            if (stackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inputInventory));

    }

    public static Slot getSlotFromID(int id, PlayerEntity player) {
        ScreenHandler handler = player.currentScreenHandler;
        Slot slot = handler.getSlot(id);
        return slot;
    }

}
