Nachfolgender Abschnitt einf�gen in die mt4j-core Projekt.
Unter MTTextArea.class unter org.mt4j.components.visibleComponents.widgets;

	public float getTextWidth(){
		return this.getMaxLineWidth();
	}