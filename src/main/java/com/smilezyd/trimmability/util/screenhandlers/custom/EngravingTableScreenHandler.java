package com.smilezyd.trimmability.util.screenhandlers.custom;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.util.screenhandlers.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import java.util.*;

public class EngravingTableScreenHandler extends ScreenHandler {
    private final SimpleInventory inputInventory = new SimpleInventory(2);
    private final SimpleInventory outputInventory = new SimpleInventory(1);
    private final ScreenHandlerContext context;
    private boolean editorScreen;


    public EngravingTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(ModScreenHandlers.ENGRAVING_TABLE_SCREEN_HANDLER, syncId);
        PlayerEntity player = playerInventory.player;
        World world = player.getWorld();

        this.context = ScreenHandlerContext.create(world, pos);

        //TBD
        int universalX = -5;

        //Slot 0 (Blank trim)
        this.addSlot(new Slot(inputInventory, 0, -28, 12));

        //Slot 1 (Chisel)
        this.addSlot(new Slot(inputInventory, 1, -28, 46));

        //Slot 2 (Output)
        this.addSlot(new Slot(outputInventory, 0, -28, 80) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false; // player cannot insert manually
            }
        });


        this.addPlayerSlots(playerInventory, 8, 84);
    }

    public void updateEditorScreen(boolean editorScreen) {
        this.editorScreen = editorScreen;
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

            // Example logic split
            if (this.editorScreen) {
                // 🧩 special editor-mode handling
                if (index < 3) {
                    // move from editor slots to player inventory
                    if (!this.insertItem(stackInSlot, 3, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // move from player inventory to editor slots
                    if (!this.insertItem(stackInSlot, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // 🧱 normal mode logic
                if (index < 3) {
                    if (!this.insertItem(stackInSlot, 3, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.insertItem(stackInSlot, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
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

}
