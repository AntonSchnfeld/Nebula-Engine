package org.nebula.math;

import org.joml.Vector3f;

public class Maths {

    /**
     * Applies the transformations specified in the given Transform object to the provided position.
     * The order of transformations is Rotation, Scaling, and then Translation.
     *
     * @param position The original position vector.
     * @param transform The Transform object containing rotation, scaling, and translation information.
     * @return The transformed position vector.
     */
    public static Vector3f transform(Vector3f position, Transform transform) {
        // Apply rotation around the Z-axis
        float cosZ = (float) Math.cos(Math.toRadians(transform.getRotation().x));
        float sinZ = (float) Math.sin(Math.toRadians(transform.getRotation().x));

        float newX = position.x * cosZ - position.y * sinZ;
        float newY = position.x * sinZ + position.y * cosZ;

        // Apply rotation around the X-axis
        float cosX = (float) Math.cos(Math.toRadians(transform.getRotation().y));
        float sinX = (float) Math.sin(Math.toRadians(transform.getRotation().y));

        float tempY = newY * cosX - position.z * sinX;
        float newZ = position.z * cosX + newY * sinX;

        newY = tempY;

        position.set(newX, newY, newZ);

        // Apply scaling
        position.mul(transform.getScale());

        // Apply translation
        position.add(transform.getTranslation());

        return position;
    }
}
