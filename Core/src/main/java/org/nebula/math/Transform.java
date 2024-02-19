package org.nebula.math;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * <br>
 * <h2>Transform</h2>
 * <br>
 * The Transform class represents a 2D transformation in graphics, including translation,
 * scaling, and rotation. It utilizes the Vector2f class from JOML for handling
 * 2D vector operations.
 *
 * @author Anton Schoenfeld
 */
public class Transform {
    private final Vector3f translation;
    private final Vector3f scale;
    private final Vector2f rotation;

    /**
     * Constructs a Transform with default values (no translation, unit scale, and no rotation).
     */
    public Transform() {
        this(new Vector3f(), new Vector3f(1f), new Vector2f());
    }

    /**
     * Constructs a Transform with a specified translation and default scale and rotation.
     *
     * @param translation The translation vector.
     */
    public Transform(Vector3f translation) {
        this(translation, new Vector3f(1f), new Vector2f());
    }

    /**
     * Constructs a Transform with a specified translation, scale, and rotation.
     *
     * @param translation The translation vector.
     * @param rotation    The rotation angle in degrees.
     */
    public Transform(Vector3f translation, Vector2f rotation) {
        this(translation, new Vector3f(1f), rotation);
    }

    /**
     * Constructs a Transform with specified translation, scale, and rotation.
     *
     * @param translation The translation vector.
     * @param scale       The scaling vector.
     * @param rotation    The rotation angle in degrees.
     */
    public Transform(Vector3f translation, Vector3f scale, Vector2f rotation) {
        this.translation = translation;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Transform(Transform that) {
        this();
        set(that);
    }

    public Transform withTranslation(Vector3f translation) {
        Transform withTranslation = new Transform(this);
        withTranslation.setTranslation(translation);
        return withTranslation;
    }

    public Transform withScale(Vector3f scale) {
        Transform withScale = new Transform(this);
        withScale.setScale(scale);
        return withScale;
    }

    public Transform withRotation(Vector2f rotation) {
        Transform withRotation = new Transform(this);
        withRotation.setRotation(rotation);
        return withRotation;
    }

    public void set(Transform that) {
        this.translation.set(that.translation);
        this.scale.set(that.scale);
        this.rotation.set(that.rotation);
    }

    /**
     * Gets the translation vector.
     *
     * @return The translation vector.
     */
    public Vector3f getTranslation() {
        return translation;
    }

    /**
     * Sets the translation vector.
     *
     * @param translation The new translation vector.
     */
    public void setTranslation(Vector3f translation) {
        this.translation.set(translation);
    }

    /**
     * Gets the scaling vector.
     *
     * @return The scaling vector.
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * Sets the scaling vector.
     *
     * @param scale The new scaling vector.
     */
    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }

    /**
     * Gets the rotation angle in degrees.
     *
     * @return The rotation angle.
     */
    public Vector2f getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation angle in degrees.
     *
     * @param rotation The new rotation angle.
     */
    public void setRotation(Vector2f rotation) {
        this.rotation.set(rotation);
    }

    /**
     * Checks if this Transform is equal to another object.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transform transform = (Transform) o;

        if (!rotation.equals(transform.rotation)) return false;
        if (!translation.equals(transform.translation)) return false;
        return scale.equals(transform.scale);
    }

    /**
     * Generates a hash code for this Transform.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int result = translation.hashCode();
        result = 31 * result + scale.hashCode();
        result = 31 * result + rotation.hashCode();
        return result;
    }

    /**
     * Generates a string representation of this Transform.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "Transform{" +
                "translation=" + translation +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}
