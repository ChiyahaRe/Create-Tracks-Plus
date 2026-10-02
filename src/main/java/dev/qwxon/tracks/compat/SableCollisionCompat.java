package dev.qwxon.tracks.compat;

public final class SableCollisionCompat {
    private static final String RAPIER_VOXEL_BAKERY = "dev.ryanhcode.sable.physics.impl.rapier.collider.RapierVoxelColliderBakery";

    private SableCollisionCompat() {
    }

    // Sable bakes a sub-level's blocks into Rapier physics colliders separately from vanilla collision
    // shape queries, and CollisionContext gives no way to tell them apart. Without this check, a hidden
    // wheel mount still gets baked as a solid Rapier collider even though it correctly reports no shape
    // everywhere else, so anything resting on it (e.g. Bits n Tracks blocks) collides with the invisible mount.
    public static boolean isSableColliderBake() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (RAPIER_VOXEL_BAKERY.equals(element.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
