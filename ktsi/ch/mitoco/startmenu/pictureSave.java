package ch.mitoco.startmenu;



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
			Thread.sleep(10000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Startmenü: Saved Imageg");
//		app.getMTApplication().saveFrame(path + "Output-###.png");
		
		//app.getMTApplication().saveFrame(path + "out###.png");
		//SceneMitoco.takescreenshot();
		/*
		try {
			 Toolkit toolkit = Toolkit.getDefaultToolkit();
		      Dimension oScreenSize = toolkit.getScreenSize();
			
			//Rectangle screen = new Rectangle(app.getMTApplication().getLocationOnScreen().x, app.getMTApplication().getLocationOnScreen().y, app.getMTApplication().getWidth(), app.getMTApplication().getHeight());
		      Rectangle screen = new Rectangle(oScreenSize);
			 
			Robot robot = new Robot();
			
			BufferedImage image = robot.createScreenCapture(screen);
			File file = new File(path + "out.png");
			System.out.println("pcicture Save " + SceneMitoco.getExportPath());
			ImageIO.write(image, "png", file);
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		x++;
        }
	}
	
	
}
