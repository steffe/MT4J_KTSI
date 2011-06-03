package org.mt4jx.components.visibleComponents.widgets.pdf.example;

import org.mt4j.MTApplication;

public class StartPDFExample extends MTApplication {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		initialize();
	}
	@Override
	public void startUp() {
		addScene(new PDFExampleScene(this, "PDFExampleScene"));
	}
}
