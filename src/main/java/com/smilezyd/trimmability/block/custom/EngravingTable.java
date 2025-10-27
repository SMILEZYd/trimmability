package com.smilezyd.trimmability.block.custom;

import com.mojang.serialization.MapCodec;
import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.util.screenhandlers.custom.EngravingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EngravingTable extends Block {

    private static final Text TITLE = Text.translatable("container.trimmability.engraving_table");

    public EngravingTable(AbstractBlock.Settings settings) {super(settings);}

    public MapCodec<? extends EngravingTable> getCodec() {
        return createCodec(EngravingTable::new);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Trimmability.LOGGER.info("Block right clicked");
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected ExtendedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public Object getScreenOpeningData(ServerPlayerEntity player) {
                return pos;
            }

            @Override
            public Text getDisplayName() {
                return TITLE;
            }

            @Override
            public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new EngravingTableScreenHandler(syncId, playerInventory, pos);
            }
        };
    }


}
