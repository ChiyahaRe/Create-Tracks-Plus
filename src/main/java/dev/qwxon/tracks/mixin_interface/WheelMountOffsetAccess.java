/*
 * Decompiled with CFR 0.152.
 */
package dev.qwxon.tracks.mixin_interface;

public interface WheelMountOffsetAccess {
    public double tracks$adjustLateralOffset(int var1);

    public double tracks$adjustLongitudinalOffset(int var1);

    public double tracks$adjustHeightOffset(int var1, boolean sideInteraction);

    public double tracks$adjustTuning(String var1, int var2);

    public void tracks$resetTuning();

    public double tracks$getTuning(String var1);

    public double tracks$getLerpedLateralOffset(float var1);

    public double tracks$getLerpedLongitudinalOffset(float var1);

    public double tracks$getLerpedHeightOffset(float var1);

    public double tracks$getLerpedYaw(float var1);

    public boolean tracks$isVisualSuspensionHidden();

    public void tracks$toggleVisualSuspensionHidden();

    public int tracks$getClientSteeringSignalLeft();

    public int tracks$getClientSteeringSignalRight();
}

