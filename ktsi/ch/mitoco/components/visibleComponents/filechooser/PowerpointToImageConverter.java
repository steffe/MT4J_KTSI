package ch.mitoco.components.visibleComponents.filechooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

/**
 * Converts Powerpoint files into images.
 */
class PowerpointToImageConverter
{
	/**
	 * Converts Powerpoint files into images.
	 * @param fileName
	 * @return The number of images generated.
	 */
public static int powerPointToImageConversion(String fileName)
{
	int numberOfSlides = 0;
try
{
//convert ppt slide into image

FileInputStream is = new FileInputStream(fileName);
SlideShow ppt = new SlideShow(is);
is.close();

Dimension pgsize = ppt.getPageSize();

Slide[] slide = ppt.getSlides();
for (int i = 0; i < slide.length; i++) {

BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
Graphics2D graphics = img.createGraphics();
//clear the drawing area
graphics.setPaint(Color.white);
//graphics.setBackground(Color.red);
graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

//render
slide[i].draw(graphics);

//save the output
FileOutputStream out = new FileOutputStream("slide-" + (i+1) + ".png");
javax.imageio.ImageIO.write(img, "png", out);
out.close();

// Record the number of slides.
numberOfSlides = slide.length;
}


}catch(Exception e){e.printStackTrace();}

return numberOfSlides;
}
}