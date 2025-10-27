package com.smilezyd.trimmability.screen.custom;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.mixin.client.SlotAccessor;
import com.smilezyd.trimmability.util.networking.RepositionSlots;
import com.smilezyd.trimmability.util.screenhandlers.custom.EngravingTableScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;

public class EngravingTableScreen extends HandledScreen<EngravingTableScreenHandler> {
    private boolean editorScreen = false;
    private static final Identifier STARING_BACKGROUND = Identifier.of("trimmability", "textures/gui/engraving_table2.png");
    private static final Identifier EDITOR_BACKGROUND = Identifier.of("trimmability", "textures/gui/engraving_table.png");
    private static final Identifier EDITOR_OVERLAY_TEXTURE = Identifier.of("trimmability", "textures/gui/engraving_table_sidebar.png");

    private int[] getSlotXPos() {

//        return editorScreen ? new int[]{-3, 61, 133} : new int[]{26, 62, 134};

        return editorScreen ? new int[]{-28, -28, -28} : new int[]{26, 62, 134};
    }

    private int[] getSlotYPos() {

//        return editorScreen ? new int[]{47, 47, 47} : new int[]{48, 48, 48};

      return editorScreen ? new int[]{12, 46, 80} : new int[]{48, 48, 48};
    }



    private Identifier DRAWN_BACKGROUND;

    public EngravingTableScreen(EngravingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        int xmod = !editorScreen ? 84 : -29;
        int ymod = !editorScreen ? 47 : 137;
        for (int i = 0; i < 3; i++) {
            this.shiftSlotPosition (i, this.getSlotXPos()[i], this.getSlotYPos()[i]);
            this.handler.updateEditorScreen(editorScreen);

        }
        super.init();

        int guiLeft = this.x;
        int guiTop = this.y;

        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(!editorScreen ? "Editor" : ""), (btn) -> {
            // Code to run when button is pressed.
            this.editorScreen = !this.editorScreen;
            this.clearAndInit();
            for (int i = 0; i < 3; i++) {
                this.shiftSlotPosition (i, this.getSlotXPos()[i], this.getSlotYPos()[i]);
                this.handler.updateEditorScreen(editorScreen);

            }

        }).dimensions(guiLeft + xmod, guiTop + ymod, !editorScreen ? 44 : 18, 18).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(buttonWidget);
    }

    private void shiftSlotPosition(int index, int newX, int newY) {
        if (index < 3) {
            Slot slot = this.handler.slots.get(index);
            ((SlotAccessor) slot).setX(newX);
            ((SlotAccessor) slot).setY(newY);
        }
    }
    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        // Default vanilla check:
        boolean vanillaOutside = mouseX < (double)left || mouseY < (double)top
                || mouseX >= (double)(left + this.backgroundWidth)
                || mouseY >= (double)(top + this.backgroundHeight);

        if (!this.editorScreen) {
            // behave exactly like vanilla when not in editor mode
            return vanillaOutside;
        }

        // editorScreen = true: allow clicks a bit outside the GUI rectangle
        // so slots that live slightly left/top of the main background still count as "inside".
        int paddingLeft = 40;   // tune this; must be >= absolute value of your most-negative slot.x
        int paddingRight = 10;  // optional (if you extend right side)
        int paddingTop = 10;
        int paddingBottom = 10;

        double minX = left - paddingLeft;
        double minY = top - paddingTop;
        double maxX = left + this.backgroundWidth + paddingRight;
        double maxY = top + this.backgroundHeight + paddingBottom;

        // If mouse is outside this expanded rectangle then it's definitely outside.
        return mouseX < minX || mouseY < minY || mouseX >= maxX || mouseY >= maxY;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//         this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Just fill a rectangle for now (you can replace this with a texture later)

        DRAWN_BACKGROUND = !editorScreen ? STARING_BACKGROUND : EDITOR_BACKGROUND;

        context.drawTexture(
                net.minecraft.client.render.RenderLayer::getGuiTextured, // required lambda for new signature
                DRAWN_BACKGROUND, // your GUI texture
                x, y,    // screen position
                0f, 0f,  // u, v (texture origin)
                backgroundWidth, backgroundHeight, // drawn size
                256, 256 // texture size
        );


        if (editorScreen) {
            context.drawTexture(
                    net.minecraft.client.render.RenderLayer::getGuiTextured,
                    EDITOR_OVERLAY_TEXTURE,
                    x-40, y,
                    0f, 0f,
                    backgroundWidth, backgroundHeight,
                    256, 256

            );
        }
    }
}
