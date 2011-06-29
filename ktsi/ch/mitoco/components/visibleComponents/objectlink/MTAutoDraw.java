package ch.mitoco.components.visibleComponents.objectlink;

/**
 * 
 * Timer für automatische nachführen der Linklinien über die methode drawLinie().
 * 
 * @author tandrich
 *
 */
class MTAutoDraw extends Thread {
   /** Linker Objekt zum ausführen der drawLine Methode.*/
	private MTLinkController myObj = null;  
    
	/** Freigabe boolean.*/
	private boolean oktorun = false;
    
	/** Intervall Zeit in ms.*/	
	private int delay;

	/**
	 * Konstruktor. Intervall fix auf 2000ms eingestellt.
	 * @param myObj MTLinkController
	 */
    MTAutoDraw(final MTLinkController myObj) {
      this.myObj = myObj;
      this.delay = 2000;
      }

    /**
     * Konstruktor. 
     * @param myObj MTLinkController
     * @param delay int
     */
    MTAutoDraw(final MTLinkController myObj, final int delay) {
    System.out.println("MTAutoDraw: create: ");
      this.myObj = myObj;
      this.delay = delay;
      }

    /**
     * Methode um Intervallgeber zu starten.
     * @param go boolean
     */
    public void setOkstorun(final boolean go) {
    	oktorun = go;
    }
    
    /**
     * Thread run Methode.
     */
    public void run() {
    	System.out.println("MTAutoDraw: run: ");
    	while (true) {
        try {
          sleep(delay);
          if (oktorun) 
              myObj.drawLinie(); 
          } 
        catch (InterruptedException ignored) { 
        	
        }
        }
      }
    } 
