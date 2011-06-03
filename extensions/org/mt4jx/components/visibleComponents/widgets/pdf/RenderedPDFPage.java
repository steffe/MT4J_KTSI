package org.mt4jx.components.visibleComponents.widgets.pdf;

import java.awt.Image;
import java.io.File;

import processing.core.PImage;

public class RenderedPDFPage {
	private File pdf;
	private PImage image;
	private int pageNumber;
	private int numberOfPixels;
	
	protected RenderedPDFPage(File pdf, PImage image, int pageNumber, int numberOfPixels){
		this.pdf = pdf;
		this.image = image;
		this.pageNumber = pageNumber;
		this.numberOfPixels = numberOfPixels;
	}
	public File getPdf() {
		return pdf;
	}
	public Image getImage() {
		return image.getImage();
	}
	public PImage getPImage() {
		return image;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public int getNumberOfPixels() {
		return numberOfPixels;
	}
}
