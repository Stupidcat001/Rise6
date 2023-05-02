package net.minecraft.client.renderer;

import com.alan.clients.Client;
import com.alan.clients.module.impl.other.SecurityFeatures;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public abstract class InventoryEffectRenderer extends GuiContainer {
    /**
     * True if there is some potion effect to display
     */
    private boolean hasActivePotionEffects;

    public InventoryEffectRenderer(final Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        super.initGui();
        this.updateActivePotionEffects();
    }

    protected void updateActivePotionEffects() {
        if (!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
            this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
            this.hasActivePotionEffects = true;
        } else {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }

    /**
     * Display the potion effects list
     */
    private void drawActivePotionEffects() {
        final int i = this.guiLeft - 124;
        int j = this.guiTop;
        final int k = 166;
        final Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5) {
                l = 132 / (collection.size() - 1);
            }

            for (final PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects()) {
                try {
                    final Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.mc.getTextureManager().bindTexture(inventoryBackground);
                    this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

                    if (potion.hasStatusIcon()) {
                        final int i1 = potion.getStatusIconIndex();
                        this.drawTexturedModalRect(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                    }

                    String s1 = I18n.format(potion.getName());

                    if (potioneffect.getAmplifier() == 1) {
                        s1 = s1 + " " + I18n.format("enchantment.level.2");
                    } else if (potioneffect.getAmplifier() == 2) {
                        s1 = s1 + " " + I18n.format("enchantment.level.3");
                    } else if (potioneffect.getAmplifier() == 3) {
                        s1 = s1 + " " + I18n.format("enchantment.level.4");
                    }

                    this.fontRendererObj.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 6), 16777215);
                    final String s = Potion.getDurationString(potioneffect);
                    this.fontRendererObj.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 6 + 10), 8355711);
                    j += l;
                } catch (final Exception exception) {
                    exception.printStackTrace();

                    if (!Client.INSTANCE.getModuleManager().get(SecurityFeatures.class).isEnabled()) {
                        System.out.println("Potion crasher detected, exiting to prevent detection.");
                        mc.shutdown();
                    }
                }
            }
        }
    }
}
