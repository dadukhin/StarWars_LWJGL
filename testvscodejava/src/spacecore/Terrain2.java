package spacecore;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import de.matthiasmann.twl.utils.PNGDecoder;


public class Terrain2 {
	 private static final String WINDOW_TITLE = "Terrain!";
	    private static final int[] WINDOW_DIMENSIONS = {1200, 650};
	    private static final float ASPECT_RATIO = (float) WINDOW_DIMENSIONS[0] / (float) WINDOW_DIMENSIONS[1];
	    
	    /** The shader program that will use the lookup texture and the height-map's vertex data to draw the terrain. */
	    private static int shaderProgram;
	    /** The texture that will be used to find out which colours correspond to which heights. */
	    private static int lookupTexture;
	    /** The display list that will contain the height-map's vertex data. */
	    public static int heightmapDisplayList;
	    /**
	     * The points of the height. The first dimension represents the z-coordinate. The second dimension represents the
	     * x-coordinate. The float value represents the height.
	     */
	    public static float[][] data;
	    /** Whether the terrain should vary in height or be displayed on a grid. */
	    private static boolean flatten = false;
public void setUpHeightmap() {
        try {
            // Load the heightmap-image from its resource file
            BufferedImage heightmapImage = ImageIO.read(new File("res/textures/heightmaps/heightmap.png"));
            // Initialise the data array, which holds the heights of the heightmap-vertices, with the correct dimensions
            data = new float[heightmapImage.getWidth()][heightmapImage.getHeight()];
            // Lazily initialise the convenience class for extracting the separate red, green, blue, or alpha channels
            // an int in the default RGB color model and default sRGB colourspace.
            Color colour;
            // Iterate over the pixels in the image on the x-axis
            for (int z = 0; z < data.length; z++) {
                // Iterate over the pixels in the image on the y-axis
                for (int x = 0; x < data[z].length; x++) {
                    // Retrieve the colour at the current x-location and y-location in the image
                    colour = new Color(heightmapImage.getRGB(z, x));
                    // Store the value of the red channel as the height of a heightmap-vertex in 'data'. The choice for
                    // the red channel is arbitrary, since the heightmap-image itself only has white, gray, and black.
                    data[z][x] = colour.getRed();
                }
            }
            // Create an input stream for the 'lookup texture', a texture that will used by the fragment shader to
            // determine which colour matches which height on the heightmap
            FileInputStream heightmapLookupInputStream = new FileInputStream("res/images/heightmap_lookup.png");
            // Create a class that will give us information about the image file (width and height) and give us the
            // texture data in an OpenGL-friendly manner
            PNGDecoder decoder = new PNGDecoder(heightmapLookupInputStream);
            // Create a ByteBuffer in which to store the contents of the texture. Its size is the width multiplied by
            // the height and 4, which stands for the amount of bytes a float is in Java.
            ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            // 'Decode' the texture and store its data in the buffer we just created
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            // Make the contents of the ByteBuffer readable to OpenGL (and unreadable to us)
            buffer.flip();
            // Close the input stream for the heightmap 'lookup texture'
            heightmapLookupInputStream.close();
            // Generate a texture handle for the 'lookup texture'
            lookupTexture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, lookupTexture);
            // Hand the texture data to OpenGL
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Use the GL_NEAREST texture filter so that the sampled texel (texture pixel) is not smoothed out. Usually
        // using GL_NEAREST will make the textured shape appear pixelated, but in this case using the alternative,
        // GL_LINEAR, will make the sharp transitions between height-colours ugly.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // Generate a display list handle for the display list that will store the heightmap vertex data
        heightmapDisplayList = glGenLists(1);
        // TODO: Add alternative VBO rendering for pseudo-compatibility with version 3 and higher.
        glNewList(heightmapDisplayList, GL_COMPILE);
        // Scale back the display list so that its proportions are acceptable.
        Random SurfaceRand = new Random(123456);

        glScalef(0.9f, 0.3f, 0.9f);
        
        // Iterate over the 'strips' of heightmap data.
        for (int z = 0; z < data.length - 1; z++) {
            // Render a triangle strip for each 'strip'.
            glBegin(GL_TRIANGLE_STRIP);
           
            for (int x = 0; x < data[z].length; x++) {
                // Take a vertex from the current strip
            	
            	GL11.glColor3f(0.8f, 0.8f, 0.5f + 0.5f * (SurfaceRand.nextFloat()));
                glVertex3f(x, data[z][x], z);
                // Take a vertex from the next strip
                GL11.glColor3f(0.8f, 0.8f, 0.5f + 0.5f * (SurfaceRand.nextFloat()));;
                glVertex3f(x, data[z + 1][x], z + 1);
            }
            glEnd();
        }
        glEndList();
    }
}



