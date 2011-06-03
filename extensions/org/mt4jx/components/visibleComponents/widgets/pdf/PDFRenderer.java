/***********************************************************************
 *   MT4j Extension: PDF
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License (LGPL)
 *   as published by the Free Software Foundation, either version 3
 *   of the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the LGPL
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package org.mt4jx.components.visibleComponents.widgets.pdf;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import processing.core.PImage;

import com.sun.pdfview.Cache;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * @author Uwe Laufs
 *
 */
public class PDFRenderer {
	public static RenderedPDFPage render(File pdf, double scaleFactor, int pageNumber) throws IOException {
        PDFFile pdffile = null;
		File file = pdf;
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		pdffile = new PDFFile(buf);

        PDFPage page = pdffile.getPage(pageNumber);
		int w = (int)(page.getBBox().getWidth());
		int h = (int)(page.getBBox().getHeight());
        Rectangle clip = new Rectangle(0,0,w,h);
        Image img = page.getImage(
        		(int)(w*scaleFactor),(int)(h*scaleFactor), //width & height
                clip, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
                );
        RenderedPDFPage result = new RenderedPDFPage(pdf, new PImage(img), pageNumber, w*h);
        return result;
	}
	
	public static RenderedPDFPage render(File pdf, int width, int height, int pageNumber) throws IOException {
        PDFFile pdffile = null;
		File file = pdf;
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		pdffile = new PDFFile(buf);

        PDFPage page = pdffile.getPage(pageNumber);
		int w = (int)(page.getBBox().getWidth());
		int h = (int)(page.getBBox().getHeight());
		
        Rectangle clip = new Rectangle(0,0,(int)page.getBBox().getWidth(),(int)page.getBBox().getHeight());
        Image img = page.getImage(
        		width, height, //width & height
                clip, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
                );
        RenderedPDFPage result = new RenderedPDFPage(pdf, new PImage(img), pageNumber, w*h);
        return result;
	}
	public static RenderedPDFPage loadImage(File pdf) throws IOException {
		return render(pdf, 1, 0);
	}
}
