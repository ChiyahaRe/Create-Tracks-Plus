/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package dev.qwxon.tracks.content.items;

import dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlock;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlockEntity;
import dev.qwxon.tracks.mixin_interface.WheelMountOffsetAccess;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SuspensionKeyItem
extends Item {
    private static final String MODE_TAG = "TracksSuspensionKeyMode";
    public static final SoundEvent CRAFTER_CLICK = SoundEvent.createVariableRangeEvent((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"create", (String)"crafter_click"));

    public SuspensionKeyItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        TuningMode mode = SuspensionKeyItem.cycleMode(stack, player.isShiftKeyDown() ? -1 : 1);
        if (!level.isClientSide) {
            level.playSound(null, player.blockPosition(), CRAFTER_CLICK, SoundSource.PLAYERS, 0.45f, 1.0f);
            player.displayClientMessage((Component)Component.translatable((String)"item.tracks.suspension_key.mode", (Object[])new Object[]{mode.title()}).withStyle(ChatFormatting.GOLD), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    public InteractionResult useOn(UseOnContext context) {
        TuningMode mode = SuspensionKeyItem.getMode(context.getItemInHand());
        if (mode == TuningMode.RESET) {
            return this.resetOn(context);
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        Player player = context.getPlayer();
        int direction = player != null && player.isShiftKeyDown() ? -1 : 1;
        Direction clickedFace = context.getClickedFace();
        if (mode != TuningMode.POSITION) {
            if (blockEntity instanceof WheelMountOffsetAccess) {
                WheelMountOffsetAccess wheelMount = (WheelMountOffsetAccess)blockEntity;
                if (!mode.supportsWheel()) {
                    if (!level.isClientSide) {
                        this.feedback(level, pos, player, mode, 0.0, false);
                    }
                    return InteractionResult.sidedSuccess((boolean)level.isClientSide);
                }
                if (!level.isClientSide) {
                    double value = wheelMount.tracks$adjustTuning(mode.key, direction);
                    this.feedback(level, pos, player, mode, value, true);
                }
                return InteractionResult.sidedSuccess((boolean)level.isClientSide);
            }
            if (blockEntity instanceof SableTrackBlockEntity) {
                SableTrackBlockEntity track = (SableTrackBlockEntity)blockEntity;
                if (!level.isClientSide) {
                    double value = track.adjustTuning(mode.key, direction);
                    this.feedback(level, pos, player, mode, value, true);
                }
                return InteractionResult.sidedSuccess((boolean)level.isClientSide);
            }
            return InteractionResult.PASS;
        }
        if (blockEntity instanceof WheelMountOffsetAccess) {
            double value;
            String axisName;
            WheelMountOffsetAccess wheelMount = (WheelMountOffsetAccess)blockEntity;
            Direction facing = (Direction)level.getBlockState(pos).getValue(WheelMountBlock.HORIZONTAL_FACING);
            if (clickedFace == facing) {
                axisName = "height";
                value = wheelMount.tracks$adjustHeightOffset(direction, false);
            } else if (clickedFace.getAxis() == Direction.Axis.Y) {
                axisName = "forward/back";
                value = wheelMount.tracks$adjustLongitudinalOffset(direction);
            } else {
                axisName = "left/right";
                value = wheelMount.tracks$adjustLateralOffset(direction);
            }
            this.positionFeedback(level, pos, player, axisName, value);
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        if (blockEntity instanceof SableTrackBlockEntity) {
            double value;
            String axisName;
            SableTrackBlockEntity track = (SableTrackBlockEntity)blockEntity;
            Direction facing = (Direction)track.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
            if (clickedFace.getAxis() == Direction.Axis.Y) {
                axisName = "forward/back";
                value = track.adjustLongitudinalOffset(direction);
            } else if (clickedFace.getAxis() == facing.getClockWise().getAxis()) {
                axisName = "left/right";
                value = track.adjustLateralOffset(direction);
            } else {
                axisName = "height";
                value = track.adjustHeightOffset(direction, true);
            }
            this.positionFeedback(level, pos, player, axisName, value);
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult resetOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        Player player = context.getPlayer();
        if (blockEntity instanceof WheelMountOffsetAccess) {
            WheelMountOffsetAccess wheelMount = (WheelMountOffsetAccess)blockEntity;
            if (!level.isClientSide) {
                wheelMount.tracks$resetTuning();
                this.feedback(level, pos, player, TuningMode.RESET, 0.0, true);
            }
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        if (blockEntity instanceof SableTrackBlockEntity) {
            SableTrackBlockEntity track = (SableTrackBlockEntity)blockEntity;
            if (!level.isClientSide) {
                track.resetTuningToConnectedTrack();
                this.feedback(level, pos, player, TuningMode.RESET, 0.0, true);
            }
            return InteractionResult.sidedSuccess((boolean)level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private void feedback(Level level, BlockPos pos, Player player, TuningMode mode, double value, boolean applied) {
        if (level.isClientSide) {
            return;
        }
        level.playSound(null, pos, CRAFTER_CLICK, SoundSource.BLOCKS, 0.45f, 0.9f + level.random.nextFloat() * 0.2f);
        if (player != null) {
            String formattedValue = mode == TuningMode.RESET ? "-" : String.format(Locale.ROOT, mode == TuningMode.STRENGTH ? "%.0f" : "%.2fx", value);
            MutableComponent message = applied ? Component.translatable((String)"item.tracks.suspension_key.tuned", (Object[])new Object[]{mode.title(), formattedValue}) : Component.translatable((String)"item.tracks.suspension_key.unsupported", (Object[])new Object[]{mode.title()});
            player.displayClientMessage((Component)message.copy().withStyle(ChatFormatting.GRAY), true);
        }
    }

    public static TuningMode cycleMode(ItemStack stack, int direction) {
        TuningMode[] values = TuningMode.values();
        int next = Math.floorMod(SuspensionKeyItem.getMode(stack).ordinal() + direction, values.length);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt(MODE_TAG, next);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return values[next];
    }

    private void positionFeedback(Level level, BlockPos pos, Player player, String axisName, double value) {
        if (level.isClientSide) {
            return;
        }
        level.playSound(null, pos, CRAFTER_CLICK, SoundSource.BLOCKS, 0.45f, 0.9f + level.random.nextFloat() * 0.2f);
        if (player != null) {
            player.displayClientMessage((Component)Component.literal((String)("Position " + axisName + ": " + String.format(Locale.ROOT, "%.3f", value))).withStyle(ChatFormatting.GRAY), true);
        }
    }

    public static TuningMode getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int index = tag.getInt(MODE_TAG);
        TuningMode[] values = TuningMode.values();
        return values[Math.floorMod(index, values.length)];
    }

    public static enum TuningMode {
        STRENGTH("strength", "item.tracks.suspension_key.mode.strength", false),
        SPRING("spring", "item.tracks.suspension_key.mode.spring", true),
        DAMPING("damping", "item.tracks.suspension_key.mode.damping", true),
        BUMP_CLEARANCE("bump_clearance", "item.tracks.suspension_key.mode.bump_clearance", false),
        BUMP_FORCE("bump_force", "item.tracks.suspension_key.mode.bump_force", false),
        MAX_IMPULSE("max_impulse", "item.tracks.suspension_key.mode.max_impulse", false),
        DRIVE("drive", "item.tracks.suspension_key.mode.drive", true),
        GRIP("grip", "item.tracks.suspension_key.mode.grip", true),
        RESET("reset", "item.tracks.suspension_key.mode.reset", true),
        POSITION("position", "item.tracks.suspension_key.mode.position", true),
        ALL_POSITION("all_position", "item.tracks.suspension_key.mode.all_position", true);

        public final String key;
        private final String translationKey;
        private final boolean supportsWheel;

        private TuningMode(String key, String translationKey, boolean supportsWheel) {
            this.key = key;
            this.translationKey = translationKey;
            this.supportsWheel = supportsWheel;
        }

        public Component title() {
            return Component.translatable((String)this.translationKey);
        }

        public boolean supportsWheel() {
            return this.supportsWheel;
        }
    }
}

