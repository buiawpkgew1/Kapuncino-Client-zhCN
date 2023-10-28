package coffee.client.feature.module.impl.combat;

import coffee.client.feature.config.EnumSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.feature.utils.InvUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class MCE extends Module {
    int pealcount;

    final EnumSetting<MCE.bypass> mode = this.config.create(new EnumSetting.Builder<>(bypass.FULLINV).name("bypass").description("Mode to use").get());

    public MCE() {
        super("MCE", "Throw enderpearl on middle-click", ModuleType.COMBAT);
    }

    @Override
    public void onFastTick() {
        if (Module.client.options.pickItemKey.isPressed()){
            if(Module.client.currentScreen instanceof GenericContainerScreen) return;
            pealcount = InvUtils.getamount(Items.ENDER_PEARL);

            if (pealcount == 0) return;

            switch (mode.getValue()){
                case HOTBAR -> {
                    int index = InvUtils.findItemInHotbar(Items.ENDER_PEARL);
                    if(index == -1) return;

                    int priorslot = Module.client.player.getInventory().selectedSlot;
                    Module.client.player.getInventory().selectedSlot = index;
                    Module.client.interactionManager.interactItem(Module.client.player, Hand.MAIN_HAND);
                    Module.client.player.getInventory().selectedSlot = priorslot;
                }

                case FULLINV -> {
                    int index = InvUtils.finditem(Items.ENDER_PEARL);
                    if(index == -1) return;

                    putinmain(index);
                    Module.client.interactionManager.interactItem(Module.client.player, Hand.MAIN_HAND);
                    putback(index);
                }
            }

        }
    }

    private void putinmain(int slot){
        Module.client.interactionManager.clickSlot(0, InvUtils.getslot(slot), 0, SlotActionType.PICKUP, Module.client.player);
        Module.client.interactionManager.clickSlot(0, 36, 0, SlotActionType.PICKUP, Module.client.player);
    }

    private void putback(int slot){
        Module.client.interactionManager.clickSlot(0, 36, 0, SlotActionType.PICKUP, Module.client.player);
        Module.client.interactionManager.clickSlot(0, InvUtils.getslot(slot), 0, SlotActionType.PICKUP, Module.client.player);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        pealcount = 0;
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }

    public enum bypass{
        FULLINV,
        HOTBAR
    }
}
