package org.nebula.math;

import org.joml.Vector2f;

public class Maths {

    /**
     * Applies the transformations specified in the given Transform object to the provided position.
     * The order of transformations is Rotation, Scaling, and then Translation.
     *
     * @param position The original position vector.
     * @param transform The Transform object containing rotation, scaling, and translation information.
     * @return The transformed position vector.
     */
    public static Vector2f transform(Vector2f position, Transform transform) {
        // Apply rotation
        float cos = (float) Math.cos(Math.toRadians(transform.getRotation()));
        float sin = (float) Math.sin(Math.toRadians(transform.getRotation()));

        float newX = position.x * cos - position.y * sin;
        float newY = position.x * sin + position.y * cos;

        position.set(newX, newY);

        // Apply scaling
        position.mul(transform.getScale());

        // Apply translation
        position.add(transform.getTranslation());

        return position;
    }
}
