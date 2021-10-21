
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.TrueTypeFont;

import spacecore.Laser;
import spacecore.Loader;
import spacecore.PlayerShip;
import spacecore.Renderer;
import spacecore.Terrain;
import spacecore.UserInterface;
import spacecore.World;

// Simple main application entry point
public class Main
{
    // Default settings
    public static final int DISPLAY_HEIGHT = 900;
    public static final int DISPLAY_WIDTH = 1400;
    //Terrain doop = new Terrain();
    // Renderable items
    PlayerShip TestShip;
    World TestWorld;
    UserInterface UI;
    Font awtFont = new Font("SF Distant Galaxy", 0, 24);
    // Debug var
    float Time;
    
    // Ship / camera variables
    Vector3f CameraPos = new Vector3f();
    Vector3f CameraTarget = new Vector3f();
    Vector3f CameraUp = new Vector3f();
    
    // Camera state
    boolean CameraType = true;
    TrueTypeFont ttf;
    public static Terrain terrain;
    public Renderer renderer;
	//private Rectangle ass = new Rectangle(10,10,10,10);

	
    
    public static void main(String[] args) {
        Main main = null;
        try {
            System.out.println("Keys:");
            System.out.println("down  - Shrink");
            System.out.println("up    - Grow");
            System.out.println("left  - Rotate left");
            System.out.println("right - Rotate right");
            System.out.println("esc   - Exit");
            main = new Main();
            main.create();
            main.run();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
        } finally {
            if (main != null) {
                main.destroy();
            }
        }
    }

    public Main() {
        // Do nothing...
    	
    }

    public void create() throws LWJGLException {
        
        //Display
    	
    	
        Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        Display.setVSyncEnabled(true);
        Display.setFullscreen(false);
        Display.setTitle("");
        Display.create();
        terrain = new Terrain(0, 0, new Loader(), "heightmap");
        renderer = new Renderer(terrain);
        
        //Keyboard
        Keyboard.create();
        
        //Mouse
        Mouse.setGrabbed(false);
        Mouse.create();
        
        //OpenGL
        initGL();
        resizeGL();
        
        // Create our world and ships
        TestWorld = new World();
        //doop.setUpHeightmap();
       
        TestShip = new PlayerShip();
        TestShip.world = TestWorld;
        UI = new UserInterface();
        ttf = new TrueTypeFont(awtFont, true);
      
    }
    
    public void destroy() {
        //Methods already check if created before destroying.
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public void initGL() {
        //2D Initialization
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black

    }
    
    // 2D mode
    public void resizeGL2D()
    {
        // 2D Scene
        glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, (float)DISPLAY_WIDTH, (float)DISPLAY_HEIGHT, 0.0f);
        glMatrixMode(GL_MODELVIEW);
        
        // Set depth buffer elements
        glDisable(GL_DEPTH_TEST);
    }
    
    // 3D mode
    public void resizeGL()
    {
        // 3D Scene
        glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45.0f, ((float) DISPLAY_WIDTH / (float) DISPLAY_HEIGHT), 0.1f, 500.0f);
        glMatrixMode(GL_MODELVIEW);
        
        // Set depth buffer elements
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
    }
    
    public void run()
    {
        // Keep looping until we hit a quit event
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (Display.isVisible()) {
                update();
                render();
                
            } else {
                if (Display.isDirty()) {
                    render();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
            Display.update();
            Display.sync(60);
        }
    }

    public void render()
    {
    	
        // Clear screen and load up the 3D matrix state
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        

        //glCullFace(GL_FRONT); 
        //glCullFace(GL_BACK); 
        
       
        	// CameraType = !CameraType;
        	//GL11.glEnable(GL11.GL_TEXTURE_2D);
        	
       
        
        
        // 3D render
        resizeGL();

        // Move camera to right behind the ship
        //public static void gluLookAt(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz)
        Time += 0.001f;
        float CDist = 8;
        
        // Set the camera on the back of the 
        TestShip.GetCameraVectors(CameraPos, CameraTarget, CameraUp);
        
        // Tail-plane camera
        if(CameraType)
        {
            // Extend out the camera by length
            Vector3f Dir = new Vector3f();
            Vector3f.sub(CameraPos, CameraTarget, Dir);
            Dir.normalise();
            Dir.scale(4);
            Dir.y += 0.1f;
            Vector3f.add(CameraPos, Dir, CameraPos);
            CameraPos.y += 1;
            
            // Little error correction: always make the camera above ground
            if(CameraPos.y < 0.01f)
            {
                CameraPos.y = 0.01f;
            }
           
            GLU.gluLookAt(CameraPos.x, CameraPos.y, CameraPos.z, CameraTarget.x, CameraTarget.y, CameraTarget.z, CameraUp.x, CameraUp.y, CameraUp.z);
           
          

        }
        // Overview
        else
        {
           // GLU.gluLookAt(CDist * (float)Math.cos(Time), CDist, CDist * (float)Math.sin(Time), CameraPos.x, CameraPos.y, CameraPos.z, 0, 1, 0);
        }
        
        // Always face forward
        float Yaw = (float)Math.toDegrees(TestShip.GetYaw());
        
        // Render all elements
        
        TestWorld.Render(CameraPos, Yaw);
        TestShip.Render();
        for(Laser doop: TestWorld.Lasers)
        {
        	doop.render();
        }
        
       // renderer.shader.start();
        renderer.prepare();
        
        renderer.render();
     //   renderer.shader.stop();
        
      
        
        
        
       

        // 2D GUI
       
        resizeGL2D();
        UI.Render(TestShip.GetRealVelocity(), TestShip.GetTargetVelocity(), TestShip.VEL_MAX);
        glLoadIdentity();
        GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	ttf.drawString(100, 10, "STARSHIP: FALCON");
    	ttf.drawString(100, 30, "Speed: "+ (int)(TestShip.GetRealVelocity()*100));
    	ttf.drawString(100, 50, "ALTITUDE: "+(int)TestShip.Position.y);
    	ttf.drawString(100, 70, "X: "+(int)TestShip.Position.x+" Z: "+(int)TestShip.Position.z);
    	//ass.setBounds(0,32,TestShip.coolDown,195);
    	//ShapeRenderer.draw(ass);
    	  glColor3f(1,0,0);
    	//ShapeRenderer.fill(ass);
    	GL11.glDisable(GL11.GL_BLEND); 
   
    	
    }
    
    public void update()
    {
        // Did the camera change?
       
           
        
        TestShip.Update();
        for(Laser doop: TestWorld.Lasers)
        {
        	doop.update();
        }
        
        
    }
}
