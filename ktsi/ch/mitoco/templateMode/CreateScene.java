package ch.mitoco.templateMode;

import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.input.gestureAction.dnd.DragAndDropAction;
import org.mt4jx.input.gestureAction.dnd.DragAndDropActionListener;
import org.mt4jx.input.gestureAction.dnd.DropTarget;

/** Hier wird der TemplateModi aufgebaut und gesteuert.
 * 
 *  @author rfeigenwinter
 *  
 *  Befindet sich noch im Aufbau, deswegen sind keine weiterführenden JavaDoc
 *  Kommentare in diesem Objekt.
 * 
 */

public class CreateScene extends AbstractScene implements DragAndDropActionListener {
	
	private AbstractMTApplication app;
	private float ch;
	private float cx;
	private float cw;
	private float tw;
	public float th;
	private float tx1;
	private float ty1;
	public float ygap;
	public int yy;
	private MTRectangle obj1;
	
	private int horizontalTiles = 2;
	private int verticalTiles = 2;
	private ArrayList<MTRectangle> mtGrids = new ArrayList<MTRectangle>();
	private MTComponent puzzleGroup;
	private MTRectangle rect;
	private MTRectangle currentTarget;

	
	public CreateScene(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		this.setClearColor(new MTColor(100, 100, 100, 255));
		this.registerGlobalInputProcessor(new CursorTracer(app, this));

		tw = mtApplication.width / 5f;
		th = 0.6f * mtApplication.height;
		
		tx1 = mtApplication.width / 10f;
		ty1 = (mtApplication.height - th) / 2f;
		
		puzzleGroup = new MTComponent(mtApplication);
		
		// Stack drop target on the left side.
		ObjectRectangle target1 = new ObjectRectangle(mtApplication, tx1, ty1, tw, th);
		
		target1.setFillColor(new MTColor(100, 100, 100, 255));
		target1.setStrokeColor(new MTColor(0, 255, 0, 255));
		//target1.setPositionGlobal(new Vector3D(150,150));
		DragAndDropAction target1Action = new DragAndDropAction(true);
		target1Action.addDragAndDropActionListener(this);
		target1.addGestureListener(DragProcessor.class, target1Action);
		target1.setName("target1");
		
		
		this.getCanvas().addChild(target1);
		
		obj1 = new MTRectangle(app, mtApplication.width / 1.3f, (mtApplication.height - th) / 2f, tw , th);
		obj1.setFillColor(new MTColor(100, 100, 100, 255));
		obj1.setStrokeColor(new MTColor(255, 0, 0, 255));
		obj1.removeAllGestureEventListeners(DragProcessor.class);
		this.getCanvas().addChild(obj1);
		
		
		int numComponents = 4;
		
		ch = app.height/(numComponents + 12);
		cw = 0.8f * tw;
		cx = (tx1 - cw)/2f;
		
		ygap = 40;
		
		MTColor[] colors = { MTColor.AQUA, MTColor.BLUE, MTColor.YELLOW, MTColor.GREEN, MTColor.GRAY };
		
		DragAndDropAction dndAction = new DragAndDropAction();
		dndAction.addDragAndDropActionListener(this);
		
		
		for (MTComponent c : puzzleGroup.getChildren()){
			c.destroy();
		}
		// Small rectangles to drag and drop on the left.
		for (yy=0; yy<numComponents; yy++) {
			System.out.println("Attribute Zeichnen"); 
			rect = new MTRectangle(app, (app.width / 1.3f) + 10, ((mtApplication.height - th) + 15) + (ygap * (yy+1)) / 2f * yy, cw, ch);
			rect.setFillColor(colors[yy%colors.length]);
			rect.setStrokeColor(MTColor.BLACK);			
			rect.addGestureListener(DragProcessor.class, dndAction);
			rect.setName("rect " + (yy+1));
			//rect.setVisible(true);
			
			final float x = ToolsMath.getRandom(0, MT4jSettings.getInstance().getWindowWidth());
            final float y = ToolsMath.getRandom(0, MT4jSettings.getInstance().getWindowHeight());
            registerPreDrawAction(new IPreDrawAction() {
                public void processAction() {
                    getMTApplication().invokeLater(new Runnable() {
                        public void run() {
                            registerPreDrawAction(new IPreDrawAction() {
                                public void processAction() {
                                    getMTApplication().invokeLater(new Runnable() {
                                        public void run() {
                                            puzzleGroup.addChild(rect);
                                            //changing starting pos of titles
                                            //sh.tweenTranslateTo(x, y, 0, 400, 0f, 1.0f);
                                            rect.setPositionGlobal(new Vector3D((float) (app.width / 1.3f) + 10, (float) ((app.height - th) + 15) + (ygap * (yy + 1)) / 2f * yy));
                                        }
                                    });
                                }

                                public boolean isLoop() {
                                    return false;
                                }
                            });
                        }
                    });
                }

                public boolean isLoop() {
                    return false;
                }
            });
			
            rect.addInputListener(new IMTInputEventListener() {
    			@Override
    				public boolean processInputEvent(MTInputEvent inEvt) {
    					// TODO Auto-generated method stub
    					if(inEvt instanceof MTInputEvent)
    					{
    						AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
    						InputCursor cursor = cursorInputEvt.getCursor();
    						IMTComponent3D target = cursorInputEvt.getTarget();
    						currentTarget =  (MTRectangle) cursorInputEvt.getTarget();
    						setBounds(currentTarget);
    						switch (cursorInputEvt.getId()) {
    						
    						case AbstractCursorInputEvt.INPUT_STARTED:
    							if(currentTarget.getStrokeColor().equals(MTColor.LIME))
    								currentTarget.setNoStroke(true);
    							System.out.println("Input detected on: " + target + " at " + cursor.getCurrentEvtPosX() + "," + cursor.getCurrentEvtPosY());
    							
    							break;
    						case AbstractCursorInputEvt.INPUT_UPDATED:
    							//System.out.println("Input updated on: " + target + " at " + cursor.getCurrentEvtPosX() + "," + cursor.getCurrentEvtPosY());		
    							break;
    						case AbstractCursorInputEvt.INPUT_ENDED:
    							System.out.println("Input ended on: " + target + " at " + cursor.getCurrentEvtPosX() + "," + cursor.getCurrentEvtPosY());
    							//'snapping' effect  every drag when it is near the grid centre point it will snap to it,adjust range according to needs.
    							for(MTRectangle g : mtGrids)
    							{
    								if(g.getCenterPointGlobal().distance2D(currentTarget.getCenterPointGlobal()) < 90)
    								{
    									if(currentTarget.isNoStroke())
    									{
    										currentTarget.setNoStroke(false);
    									}
    									currentTarget.setPositionGlobal(g.getCenterPointGlobal());
    									currentTarget.setStrokeWeight(2.5f);
    									currentTarget.setStrokeColor(MTColor.LIME);
    									//currentTarget.setPositionGlobal(new Vector3D(gVec.getX()-50,gVec.getY()-50,gVec.getZ(),gVec.getW()));
    									//target.rotateZGlobal(new Vector3D(cursor.getCurrentEvtPosX(), cursor.getCurrentEvtPosY(),0), 90);
    								}
    							}
    								
    							break;
    						default:
    							break;
    					}
    					}
    					return false;
    					}
    			});
			
			
			this.getCanvas().addChild(rect);
			
		}
		
		//Snap Grid
        float anchorX = (float)tx1;
        float anchorY = (float)ty1;
        float snapWidth = 20;//;
        float snapHeight =  20;//
       
        mtGrids.clear();//clear array everytime to prevent previous grids from appearing
        	
        	
        for(float i = anchorX; i < snapWidth*getHorizontalTiles(); i += snapWidth)
    		{
    			for(float j = anchorY; j < snapHeight*getVerticalTiles(); j += snapHeight)
    			{
    				mtGrids.add(new MTRectangle(mtApplication, i, j, snapWidth, snapHeight));
    			
    			}
    		}
		for(MTRectangle r : mtGrids)
		{
			r.setPickable(false);
			r.setNoFill(true);
			r.setStrokeColor(MTColor.LIME);
			r.setVisible(true);
			r.sendToFront();
			puzzleGroup.addChild(r);
			
		}
		
		IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, MTColor.WHITE);
		
		MTTextArea explanation = new MTTextArea(mtApplication, font);
		explanation.setNoFill(true);
		explanation.setNoStroke(true);
		explanation.setText(
				"Template Objekt, Attribut rechts nach links hierhin ziehen");
		this.getCanvas().addChild(explanation);
		explanation.setPositionGlobal(new Vector3D((cx + cw) + (mtApplication.width - cx - cw)/2f,
				ty1/2, 0));
		
		explanation.setPickable(false);
		getCanvas().addChild(puzzleGroup);
		//createAttributRectangel();
		
	}
	/**Zeichnet die Attribute in die AttributListe
	 * 
	 */
	public void createAttributRectangel(){
		
		int numComponents = 4;
		
		float ch = app.height/(numComponents + 12);
		float cw = 0.8f * tw;
		float cx = (tx1 - cw)/2f;
		
		float ygap = (app.height - (ch * numComponents))/(numComponents + 1);
		
		MTColor[] colors = { MTColor.AQUA, MTColor.BLUE, MTColor.YELLOW, MTColor.GREEN, MTColor.GRAY };
		
		DragAndDropAction dndAction = new DragAndDropAction();
		dndAction.addDragAndDropActionListener(this);
		
		// Small rectangles to drag and drop on the left.
		for (int i=0; i<numComponents; i++) {
			System.out.println("Attribute Zeichnen");
			MTRectangle rect = new MTRectangle(app, app.width / 1.3f, ygap * (i+1) + (app.height - th) / 2f * i, cw, ch);
			rect.setFillColor(colors[i%colors.length]);
			rect.setStrokeColor(MTColor.BLACK);			
			rect.addGestureListener(DragProcessor.class, dndAction);
			rect.setName("rect " + (i+1));
			rect.setVisible(true);
			//getCanvas().addChild(rect);
			
		}
		
	}
	@Override
	public void objectDroppedOnTarget(MTComponent droppedObject, DropTarget dt,
			DragEvent de) {
		System.out.printf("%s dropped on target %s\n", droppedObject.toString(), 
				dt.toString());
		
	}
	@Override
	public void objectDroppedNotOnTarget(MTComponent droppedObject, DragEvent de) {
		System.out.printf("%s dropped on no valid target\n", droppedObject.toString());
		addChildInPlace(droppedObject, this.getCanvas());
		
	}
	@Override
	public void objectEnteredTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		System.out.printf("%s has entered target %s\n", object.toString(), 
				dt.toString());
		
	}
	@Override
	public void objectExitedTarget(MTComponent object, DropTarget dt,
			DragEvent de) {
		System.out.printf("%s has exited target %s\n", object.toString(), 
				dt.toString());
		addChildInPlace(object, this.getCanvas());
		
	}
	
	/**
	 * Transfer a component to a new parent without changing the component's 
	 * global position or scaling.
	 * 
	 * @param child
	 * @param newParent
	 */
	public static void addChildInPlace(MTComponent child, MTComponent newParent) {
		if (child.getParent() != newParent) {
			// In order for the child not to jump or scale, need for the its new
			// global matrix to be the same as this.
			Matrix gmat = child.getGlobalMatrix();
			Matrix newParentGMatInverse = newParent.getGlobalInverseMatrix();
			newParent.addChild(child);
			// ParentGlobal X Local = OldGlobal
			// ==> ParentGlobalInv X ParentGlobal X Local = ParentGlobalInv X OldGlobal
			// ==> Local = ParentGlobalInv X OldGlobal.
			child.setLocalMatrix(newParentGMatInverse.mult(gmat));
		}
	}
	
	private void setBounds(AbstractShape target)
	{
	//this method will get the bounding shape of the tile and we manaully minus the coords to get the actual piece corners
		BoundsZPlaneRectangle recBound = new BoundsZPlaneRectangle(new MTRectangle(getMTApplication(),  50, 50));
		target.setBounds(recBound);
		Vector3D[] vec = target.getBounds().getVectorsGlobal();

	}

	
	private void setHorizontalTiles(int horizontalTiles) {
		this.horizontalTiles = horizontalTiles;

	}

	private int getHorizontalTiles() {
		return horizontalTiles;
	}


	private void setVerticalTiles(int verticalTiles) {
		this.verticalTiles = verticalTiles;
	}


	private int getVerticalTiles() {
		return verticalTiles;
	}
	
	
}
