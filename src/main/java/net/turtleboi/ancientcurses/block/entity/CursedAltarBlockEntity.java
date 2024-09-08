package net.turtleboi.ancientcurses.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CursedAltarBlockEntity extends BlockEntity {
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final RandomSource RANDOM = RandomSource.create();

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot){
            setChanged();
            if(!level.isClientSide){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.getCount() > 1) {
                stack.setCount(1);
            }
            return super.insertItem(slot, stack, simulate);
        }
    };

    public CursedAltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CURSED_ALTAR_BE.get(), pPos, pBlockState);
    }

    public static void bookAnimationTick(Level pLevel, BlockPos pPos, BlockState pState, CursedAltarBlockEntity pBlockEntity) {
        pBlockEntity.oOpen = pBlockEntity.open;
        pBlockEntity.oRot = pBlockEntity.rot;
        Player $$4 = pLevel.getNearestPlayer((double)pPos.getX() + 0.5, (double)pPos.getY() + 0.5, (double)pPos.getZ() + 0.5, 3.0, false);
        if ($$4 != null) {
            double $$5 = $$4.getX() - ((double)pPos.getX() + 0.5);
            double $$6 = $$4.getZ() - ((double)pPos.getZ() + 0.5);
            pBlockEntity.tRot = (float) Mth.atan2($$6, $$5);
            pBlockEntity.open += 0.1F;
            if (pBlockEntity.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float $$7 = pBlockEntity.flipT;

                do {
                    pBlockEntity.flipT += (float)(RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while($$7 == pBlockEntity.flipT);
            }
        } else {
            pBlockEntity.tRot += 0.02F;
            pBlockEntity.open -= 0.1F;
        }

        while(pBlockEntity.rot >= 3.1415927F) {
            pBlockEntity.rot -= 6.2831855F;
        }

        while(pBlockEntity.rot < -3.1415927F) {
            pBlockEntity.rot += 6.2831855F;
        }

        while(pBlockEntity.tRot >= 3.1415927F) {
            pBlockEntity.tRot -= 6.2831855F;
        }

        while(pBlockEntity.tRot < -3.1415927F) {
            pBlockEntity.tRot += 6.2831855F;
        }

        float $$8;
        for($$8 = pBlockEntity.tRot - pBlockEntity.rot; $$8 >= 3.1415927F; $$8 -= 6.2831855F) {
        }

        while($$8 < -3.1415927F) {
            $$8 += 6.2831855F;
        }

        pBlockEntity.rot += $$8 * 0.4F;
        pBlockEntity.open = Mth.clamp(pBlockEntity.open, 0.0F, 1.0F);
        ++pBlockEntity.time;
        pBlockEntity.oFlip = pBlockEntity.flip;
        float $$9 = (pBlockEntity.flipT - pBlockEntity.flip) * 0.4F;
        float $$10 = 0.2F;
        $$9 = Mth.clamp($$9, -0.2F, 0.2F);
        pBlockEntity.flipA += ($$9 - pBlockEntity.flipA) * 0.9F;
        pBlockEntity.flip += pBlockEntity.flipA;
    }

    public ItemStack getGemInSlot(int slot) {
        return itemStackHandler.getStackInSlot(slot);
    }

    public void setGemInSlot(int slot, ItemStack stack) {
        itemStackHandler.setStackInSlot(slot, stack);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("gems", itemStackHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("gems")) {
            itemStackHandler.deserializeNBT(tag.getCompound("gems"));
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
