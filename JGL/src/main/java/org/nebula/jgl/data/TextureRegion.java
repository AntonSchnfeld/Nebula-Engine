package org.nebula.jgl.data;

/**
 * <br>
 * <h2>TextureRegion</h2>
 * <br>
 * Represents a region within a texture defined by UV coordinates.
 * The UV coordinates are specified in the range [0, 1], where (0, 0) is the lower-left corner
 * and (1, 1) is the upper-right corner of the texture.
 * @author Anton Schoenfeld
 */
public class TextureRegion {

    /** The texture containing the region. */
    private final Texture texture;

    /**
     * The UV coordinates that define the Texture Region.
     * <pre>
     * (0,0) --------- (1,0)
     *  |               |
     *  |               |
     *  |               |
     * (0,1) --------- (1,1)
     * </pre>
     * The UVs are specified as an array of floats in the order:
     * [lower-left x, lower-left y, lower-right x, lower-right y,
     *  upper-left x, upper-left y, upper-right x, upper-right y]
     */
    private final float[] uvs;

    /**
     * Constructs a TextureRegion with specified dimensions.
     *
     * @param texture The texture containing the region.
     * @param startX  The starting X-coordinate of the region.
     * @param startY  The starting Y-coordinate of the region.
     * @param width   The width of the region.
     * @param height  The height of the region.
     */
    public TextureRegion(Texture texture, float startX, float startY, float width, float height) {
        this.texture = texture;
        this.uvs = new float[]{
                startX, startY - height,     // Lower left
                startX + width, startY - height, // Lower right
                startX, startY,              // Upper left
                startX + width, startY       // Upper right
        };
    }

    /**
     * Constructs a TextureRegion with custom UV coordinates.
     *
     * @param texture The texture containing the region.
     * @param uvs     The custom UV coordinates defining the region.
     *                The UVs are specified as an array of floats in the order:
     *                [lower-left x, lower-left y, lower-right x, lower-right y,
     *                 upper-left x, upper-left y, upper-right x, upper-right y]
     *                Values are automatically clamped to the range [0, 1].
     */
    public TextureRegion(Texture texture, float[] uvs) {
        this.texture = texture;

        // Clamp UV values to the range [0, 1]
        for (int i = 0; i < uvs.length; i++) {
            uvs[i] = Math.clamp(uvs[i], 0, 1);
        }

        this.uvs = uvs.clone(); // Store a copy to maintain immutability
    }

    /**
     * Constructs a TextureRegion with dimensions specified as integer pixel values.
     *
     * @param texture The texture containing the region.
     * @param startX  The starting X-coordinate of the region in pixels.
     * @param startY  The starting Y-coordinate of the region in pixels.
     * @param width   The width of the region in pixels.
     * @param height  The height of the region in pixels.
     */
    public TextureRegion(Texture texture, int startX, int startY, int width, int height) {
        this(texture, (float) startX / texture.getWidth(), (float) startY / texture.getHeight(),
                (float) width / texture.getWidth(), (float) height / texture.getHeight());
    }

    /**
     * Constructs a TextureRegion representing the entire texture.
     *
     * @param texture The texture.
     */
    public TextureRegion(Texture texture) {
        this(texture, 0f, 0f, 1f, 1f);
    }

    /**
     * Gets the UV coordinates of the texture region.
     *
     * @return The UV coordinates.
     */
    public float[] getUvs() {
        return uvs.clone(); // Returning a copy to maintain immutability
    }

    /**
     * Gets the texture containing the region.
     *
     * @return The texture.
     */
    public Texture getTexture() {
        return texture;
    }
}
