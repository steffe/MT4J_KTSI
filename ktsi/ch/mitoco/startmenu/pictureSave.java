package ch.mitoco.startmenu;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class pictureSave extends Thread {

	private SceneMitoco app;
	private String path;
	
	public pictureSave(SceneMitoco app, String path){
		this.app = app;
		this.path = path;
		
		
	}
	public void run(){
		int x = 0;
		while(x<=400)
        {
		try {
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Startmenü: Saved Imageg");
//		app.getMTApplication().saveFrame(path + "Output-###.png");
		
		try {
			
			Rectangle screen = new Rectangle(app.getMTApplication().getLocationOnScreen().x, app.getMTApplication().getLocationOnScreen().y, app.getMTApplication().getWidth(), app.getMTApplication().getHeight());
			
			Robot robot = new Robot();
			
			BufferedImage image = robot.createScreenCapture(screen);
			File file = new File(path+"out.png");
			ImageIO.write(image, "png", file);
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		x++;
        }
	}
	
	
}
