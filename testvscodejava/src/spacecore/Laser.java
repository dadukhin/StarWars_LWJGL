package spacecore;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

public class Laser {
private float velocity=0.5f;
FloatBuffer Buffer;
float[] QMatrix;
Vector3f Position;
float i = 0;
float x = 0;
float y = 0;
float z = 0;
Sphere s = new Sphere();
public Laser(FloatBuffer buffer, float[] qMatrix, Vector3f position) {
	this.Buffer = buffer;
	this.QMatrix = qMatrix;
	x = position.x; //for some reason vector3f is passed by reference and not by duplicate
	y=position.y;
	z=position.z;
}

public void render() {
	GL11.glPushMatrix();
	GL11.glTranslatef(x,y,z);
	Buffer.put(QMatrix);
    Buffer.position(0);
     
    GL11.glMultMatrix(Buffer);
	
	GL11.glLineWidth(5.0f);
    GL11.glBegin(GL11.GL_LINES);
    GL11.glColor3f(0.8f, 0, 0);
    GL11.glVertex3f(0, 0.25f, i);
    GL11.glVertex3f(0, 0.25f, 5+i);
    GL11.glEnd();
    
    
   // s.draw(0.01f, 30, 30);
   // GL11.glEnd();
    
    
    GL11.glPopMatrix();

	
}
public void update()
{
	i+=velocity;
}


}
