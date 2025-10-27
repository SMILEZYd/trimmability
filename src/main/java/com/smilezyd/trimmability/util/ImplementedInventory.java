package com.smilezyd.trimmability.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;


// Simple convenience mixin for inventories in block entities
public interface ImplementedInventory extends Inventory {
    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        ItemStack result = ItemStack.EMPTY;
        if (slot >= 0 && slot < getItems().size() && !getItems().get(slot).isEmpty()) {
            ItemStack stack = getItems().get(slot);
            result = stack.split(amount);
            if (stack.isEmpty()) {
                getItems().set(slot, ItemStack.EMPTY);
            }
            markDirty();
        }
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        if (slot >= 0 && slot < getItems().size()) {
            ItemStack stack = getItems().get(slot);
            getItems().set(slot, ItemStack.EMPTY);
            markDirty();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < getItems().size()) {
            getItems().set(slot, stack);
            if (stack.getCount() > getMaxCountPerStack()) {
                stack.setCount(getMaxCountPerStack());
            }
            markDirty();
        }
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    default void markDirty() {
        // Called when inventory changes — optional override
    }
}
