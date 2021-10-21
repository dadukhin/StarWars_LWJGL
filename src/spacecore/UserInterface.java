package spacecore;

import static org.lwjgl.opengl.GL11.*;

public class UserInterface
{
    // Constructor
    public UserInterface()
    {
        
    }
    
    // Render
    public void Render(float RealVelocity, float TargetVelocity, float MaxVelocity, float posX, float posZ, float width, float height)
    {
        // Render a speed background bar
        glPushMatrix();
        glLoadIdentity();
            // Normalized scale
            float RScale = 1f - (RealVelocity / MaxVelocity);
            float TScale = 1f - (TargetVelocity / MaxVelocity);
            
            // Draw the speed bar
            glColor3f(1f / 255f, 36f / 255f, 59f / 255f);
            glBegin(GL_QUADS);
                glVertex2f(width * .8f + 32f, 200f);
                glVertex2f(width * .8f + 64f, 200f);
                glVertex2f(width * .8f + 64f, 32f);
                glVertex2f(width * .8f + 32f, 32f);
            glEnd();
            
            // Real velocity
            //glColor3f(157f / 255f, 167f / 255f, 178f / 255f);
            glColor3f(95,158,160);
            glBegin(GL_QUADS);
                glVertex2f(width * .8f + 37f, 195f);
                glVertex2f(width * .8f + 50f, 195f);
                glVertex2f(width * .8f + 50f, 37f + 158f * RScale);
                glVertex2f(width * .8f + 37f, 37f + 158f * RScale);
            glEnd();
            
            // Real velocity
           // glColor3f(197f / 255f, 197f / 255f, 197f / 255f);
            glColor3f(0,255,255);
            glBegin(GL_QUADS);
                glVertex2f(width * .8f + 50f, 195f);
                glVertex2f(width * .8f + 59f, 195f);
                glVertex2f(width * .8f + 59f, 37f + 158f * TScale);
                glVertex2f(width * .8f + 50f, 37f + 158f * TScale);
            glEnd();
           
            
            glColor3f(0,255,255);
            glBegin(GL_QUADS);
                glVertex2f(0, 0);
                glVertex2f(0, 100);
                glVertex2f(100, 100);
                glVertex2f(100, 0);
            glEnd();
            
            glColor3f(255,0,0);
            glBegin(GL_QUADS);
                glVertex2f(posX * 100 - 2, 100 - (posZ * 100 - 2));
                glVertex2f(posX * 100 - 2, 100 - (posZ * 100 + 2));
                glVertex2f(posX * 100 + 2, 100 - (posZ * 100 + 2));
                glVertex2f(posX * 100 + 2 ,100 - (posZ * 100 - 2));
            glEnd();
           
        
        // Pop off the matrix
        glPopMatrix();
    }
}
