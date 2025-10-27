package com.smilezyd.trimmability.block.entity.custom;

import com.smilezyd.trimmability.util.screenhandlers.custom.EngravingTableScreenHandler;
import com.smilezyd.trimmability.block.entity.ModBlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.RegistryWrapper;

public class EngravingTableEntity extends BlockEntity implements ExtendedScreenHandlerFactory, Inventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public EngravingTableEntity(BlockPos pos, net.minecraft.block.BlockState state) {
        super(ModBlockEntities.ENGRAVING_TABLE_ENTITY, pos, state);
    }

    // --- Inventory methods ---

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(items, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return player.getPos().squaredDistanceTo(this.pos.toCenterPos()) < 64.0D;
    }

    @Override
    public void clear() {
        items.clear();
    }

    // --- NBT saving/loading ---
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, items, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, items, registryLookup);
    }

    // --- GUI factory ---
    @Override
    public Text getDisplayName() {
        return Text.translatable("container.trimmability.engraving_table");
    }

    @Override
    public Object getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }
}
