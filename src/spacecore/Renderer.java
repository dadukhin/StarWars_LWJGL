package spacecore;





import static org.lwjgl.opengl.GL11.glScalef;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.*;
public class Renderer {
	Terrain terrain;
	public TerrainShader shader;
	public Renderer(Terrain terrain)
	{   
		this.terrain = terrain;
		shader = new TerrainShader();
	
		
	}
	public void prepare()
	{
		
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		
		
	
	}
	public void render()
	{
		
		
		
	
	
		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
	   
	
		GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
	
		
		
	}
}
