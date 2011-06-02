/***********************************************************************
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package ch.mitoco;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class PuzzleScene extends AbstractScene{

	private AbstractShape currentTarget;
	private Vector3D[] gridCorners;
	private Vector3D currentVec;
	private PuzzleFactory pf;
	private String[] diffChoices = {"2","3","4","5","6",};
	/** The images path. */
	private String imagesPath = "advanced"+AbstractMTApplication.separator+"puzzle"+AbstractMTApplication.separator+"data"+AbstractMTApplication.separator;
	private String currentChosen = "Pyramids.jpg";
	/** The images names. */
	/*private String[] imagesNames = new String[]{
			"Pyramids.jpg", 
			"Grass.jpg",
			"Heidelberg.jpg"	
			};
	*/
	private MTComponent puzzleGroup;
	//
	private int horizontalTiles = 2;
	private int verticalTiles = 2;
	private MTList list,diffList;
	private ArrayList<MTRectangle> mtGrids = new ArrayList<MTRectangle>();
	private MTRoundRectangle loadingScreen;
	
	public PuzzleScene(AbstractMTApplication mtApplication, String name) {
		
		super(mtApplication, name);
		if (!MT4jSettings.getInstance().isOpenGlMode()){
			System.err.println(this.getName() + " is only usable with the OpenGL renderer.");
		}

		this.setClearColor(new MTColor(55,55,55));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		this.getCanvas().setDepthBufferDisabled(true); //to avoid display errors because everything is 2D
		
		MTBackgroundImage background = new MTBackgroundImage(mtApplication, mtApplication.loadImage(imagesPath + "webtreats_wood-pattern1-512d.jpg") , true);
		this.getCanvas().addChild(background);
		
		this.puzzleGroup = new MTComponent(mtApplication);
		this.getCanvas().addChild(puzzleGroup);
		
		//Puzzle tile factory
		this.pf = new PuzzleFactory(getMTApplication());
		
		IFont font = FontManager.getInstance().createFont(mtApplication, "SansSerif", 16, MTColor.WHITE, false);
		
		//New Puzzle button
		MTRoundRectangle r = getRoundRectWithText(0, 0, 120, 35, "New Puzzle", font);
		r.registerInputProcessor(new TapProcessor(getMTApplication()));
		r.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(r));
		r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					if (list.isVisible()){
						list.setVisible(false);
					}else{
						list.setVisible(true);
					}
				}
				return false;
			}
		});
		r.setPositionGlobal(new Vector3D(r.getWidthXY(TransformSpace.GLOBAL)/2f + 3 , r.getHeightXY(TransformSpace.GLOBAL)/2f + 3));
		this.getCanvas().addChild(r);
		//New Button for selecting Difficulty
		MTRoundRectangle r2 = getRoundRectWithText(50,50,120,35,"Difficulty",font);
		r2.registerInputProcessor(new TapProcessor(getMTApplication()));
		r2.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(r2));
		r2.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					if (diffList.isVisible()){
						diffList.setVisible(false);
					}else{
						diffList.setVisible(true);
					}
				}
				return false;
			}
		});
		r2.setPositionGlobal(new Vector3D(mtApplication.getWidth()-63, 20));
		this.getCanvas().addChild(r2);
		//Image list
		float cellWidth = 180;
		float cellHeight = 40;
		MTColor cellFillColor = new MTColor(MTColor.BLACK);
		MTColor cellPressedFillColor = new MTColor(new MTColor(105,105,105));
		//replacing with custom method
		list = new MTList(getMTApplication(), r.getWidthXY(TransformSpace.GLOBAL) + 5, 0, cellWidth+2, getImagesNames().length* cellHeight + getImagesNames().length*3);
		System.out.println(r.getWidthXY(TransformSpace.GLOBAL) + 5);
		list.setNoFill(true);
		list.setNoStroke(true);
		list.unregisterAllInputProcessors();
		list.setAnchor(PositionAnchor.UPPER_LEFT);
//		list.setPositionGlobal(Vector3D.ZERO_VECTOR);
		list.setVisible(false);
        for (String imageName : getImagesNames()) {
            list.addListElement(this.createListCell(imageName, font, cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
        }
		this.getCanvas().addChild(list);
		//Difficulty List
		diffList = new MTList(getMTApplication(), mtApplication.getWidth()-150, 35, cellWidth+2, getDiffChoices().length* cellHeight + getDiffChoices().length*3);
		diffList.setNoFill(true);
		diffList.setNoStroke(true);
		diffList.unregisterAllInputProcessors();
		diffList.setAnchor(PositionAnchor.UPPER_LEFT);
//		list.setPositionGlobal(Vector3D.ZERO_VECTOR);
		diffList.setVisible(false);
        for (String choices : getDiffChoices()) {
        	diffList.addListElement(this.createListCell2(choices, font, cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
        }
        this.getCanvas().addChild(diffList);
		//Loading window
		
		this.loadingScreen = getRoundRectWithText(0, 0, 130, 45, "  Loading...", font);
		this.loadingScreen.setFillColor(new MTColor(0,0,0,200));
		this.loadingScreen.setStrokeColor(new MTColor(0,0,0,200));
		this.loadingScreen.setPickable(false);
		this.loadingScreen.setPositionGlobal(MT4jSettings.getInstance().getWindowCenter());
		this.loadingScreen.setVisible(false);
		this.getCanvas().addChild(loadingScreen);
	
	}
	
	
	private MTRoundRectangle getRoundRectWithText(float x, float y, float width, float height, String text, IFont font){
		MTRoundRectangle r = new MTRoundRectangle(getMTApplication(), x, y, 0, width, height, 12, 12);
		r.unregisterAllInputProcessors();
		r.setFillColor(MTColor.BLACK);
		r.setStrokeColor(MTColor.BLACK);
		MTTextArea rText = new MTTextArea(getMTApplication(), font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(text);
		r.addChild(rText);
		rText.setPositionRelativeToParent(r.getCenterPointLocal());
		return r;
	}
	
	
	private MTListCell createListCell(final String imageName, IFont font, float cellWidth, float cellHeight, final MTColor cellFillColor, final MTColor cellPressedFillColor){
		final MTListCell cell = new MTListCell(getMTApplication(), cellWidth, cellHeight);
		cell.setFillColor(cellFillColor);
		MTTextArea listLabel = new MTTextArea(getMTApplication(), font);
		listLabel.setNoFill(true);
		listLabel.setNoStroke(true);
		listLabel.setText(imageName);
		cell.addChild(listLabel);
		listLabel.setPositionRelativeToParent(cell.getCenterPointLocal());
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(getMTApplication(), 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getTapID()) { 
				case TapEvent.TAP_DOWN:
					cell.setFillColor(cellPressedFillColor);
					break;
				case TapEvent.TAP_UP:
					cell.setFillColor(cellFillColor);
					break;
				case TapEvent.TAPPED:
					//System.out.println("Button clicked: " + label);
					setCurrentChosen(imageName);
					cell.setFillColor(cellFillColor);
					list.setVisible(false);
					loadingScreen.setVisible(true);
					registerPreDrawAction(new IPreDrawAction() {
						public void processAction() {
							getMTApplication().invokeLater(new Runnable() {
								public void run() {
									
									loadNewPuzzle(getCurrentChosen(), getHorizontalTiles(), getVerticalTiles());
									loadingScreen.setVisible(false);
								}
							});
						}
						public boolean isLoop() {return false;}
					});
					break;
				}
				return false;
			}
		});
		return cell;
	}
	
	private MTListCell createListCell2(final String imageName, IFont font, float cellWidth, float cellHeight, final MTColor cellFillColor, final MTColor cellPressedFillColor){
		final MTListCell cell = new MTListCell(getMTApplication(), cellWidth, cellHeight);
		cell.setFillColor(cellFillColor);
		MTTextArea listLabel = new MTTextArea(getMTApplication(), font);
		listLabel.setNoFill(true);
		listLabel.setNoStroke(true);
		listLabel.setText(imageName);
		
		cell.addChild(listLabel);
		cell.setName(imageName);
		listLabel.setPositionRelativeToParent(cell.getCenterPointLocal());
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(getMTApplication(), 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getTapID()) { 
				case TapEvent.TAP_DOWN:
					cell.setFillColor(cellPressedFillColor);
					break;
				case TapEvent.TAP_UP:
					cell.setFillColor(cellFillColor);
					break;
				case TapEvent.TAPPED:
					cell.setFillColor(cellFillColor);
					setHorizontalTiles(Integer.parseInt(cell.getName()));
					setVerticalTiles(Integer.parseInt(cell.getName()));
					cell.setFillColor(cellFillColor);
					diffList.setVisible(false);
					loadingScreen.setVisible(true);
					registerPreDrawAction(new IPreDrawAction() {
						public void processAction() {
							getMTApplication().invokeLater(new Runnable() {
								public void run() {
									
									loadNewPuzzle(getCurrentChosen(), getHorizontalTiles(), getVerticalTiles());
									loadingScreen.setVisible(false);
								}
							});
						}
						public boolean isLoop() {return false;}
					});break;
				}
				return false;
			}
		});
		return cell;
	}
	private void loadNewPuzzle(String imageName, int horizontalTiles, int verticalTiles){
		for (MTComponent c : puzzleGroup.getChildren()){
			c.destroy();
		}
		PImage p = getMTApplication().loadImage(imagesPath + imageName);
		//Create a MTImage;thumbnail to show full picture,first create another PImage and resize it
		PImage pThumbnail = getMTApplication().loadImage(imagesPath + imageName);
		pThumbnail.resize(200, 200);
		MTImage thumbnail = new MTImage(getMTApplication(),pThumbnail);
		thumbnail.setPositionGlobal(new Vector3D(getMTApplication().getWidth()-200,getMTApplication().getHeight()-200,0));
		thumbnail.setDisplayCloseButton(true);
		//end
		AbstractShape[] tiles = pf.createTiles(p, this.getHorizontalTiles(), this.getVerticalTiles());
        for (final AbstractShape sh : tiles) {
        	
            //Delay to smooth the animation because of loading hickups
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
                                            puzzleGroup.addChild(sh);
                                            //changing starting pos of titles
                                            //sh.tweenTranslateTo(x, y, 0, 400, 0f, 1.0f);
                                            sh.setPositionGlobal(new Vector3D((float)getMTApplication().getWidth()/2-450,(float)getMTApplication().getHeight()/2-300,0.0f));
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
            //Start tiles as rotated or not
            //sh.rotateZ(sh.getCenterPointRelativeToParent(), ToolsMath.getRandom(0, 359));
            
            //adding listener for each tile
            sh.addInputListener(new IMTInputEventListener() {
			@Override
				public boolean processInputEvent(MTInputEvent inEvt) {
					// TODO Auto-generated method stub
					if(inEvt instanceof MTInputEvent)
					{
						AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
						InputCursor cursor = cursorInputEvt.getCursor();
						IMTComponent3D target = cursorInputEvt.getTarget();
						currentTarget =  (AbstractShape) cursorInputEvt.getTarget();
						setPuzzleBounds(currentTarget);
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
        }
    	//Snap grid
        float anchorX = (float)getMTApplication().getWidth()/2-420;
        float anchorY = (float)getMTApplication().getHeight()/2-350;
        float snapWidth = pf.getTileWidth();//;
        float snapHeight =  pf.getTileHeight();//
       
        mtGrids.clear();//clear array everytime to prevent previous grids from appearing
        	
        	
        for(float i = anchorX; i < snapWidth*getHorizontalTiles(); i += snapWidth)
    		{
    			for(float j = anchorY; j < snapHeight*getVerticalTiles(); j += snapHeight)
    			{
    				mtGrids.add(new MTRectangle(getMTApplication(), i, j, snapWidth, snapHeight));
    			
    			}
    		}
		for(MTRectangle r : mtGrids)
		{
			r.setPickable(false);
			r.setNoFill(true);
			r.setStrokeColor(MTColor.LIME);
			puzzleGroup.addChild(r);
			
		}
		puzzleGroup.addChild(thumbnail);
      
		
	}
	
	//custom method added
	private void setPuzzleBounds(AbstractShape target)
	{
	//this method will get the bounding shape of the tile and we manaully minus the coords to get the actual piece corners
		BoundsZPlaneRectangle recBound = new BoundsZPlaneRectangle(new MTRectangle(getMTApplication(),  pf.getTileWidth(), pf.getTileHeight()));
		target.setBounds(recBound);
		Vector3D[] vec = target.getBounds().getVectorsGlobal();
	
		
	//red dots representing the 4 bounds of the puzzle piece, uncomment to see dots		
	/*for(int i =0;i<vec.length;i++)
		{
		
			MTEllipse cir = new MTEllipse(getMTApplication(), vec[i], 5, 5);
			cir.setPickable(false);
			cir.setFillColor(MTColor.RED);
			target.addChild(cir);
	
		}
	*/
	}
	private String[] getImagesNames()
	{
		String[] nameArray;
		File file = new File(System.getProperty("user.dir")+"\\examples\\advanced\\puzzle\\data");
		//return all file ending with .jpg extension in folder
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".jpg");
		    }
		};
		
		nameArray = file.list(filter);
		
		
		return nameArray;
	}
	
	public void onEnter() {
		
	}
	
	public void onLeave() {	}


	private void setDiffChoices(String[] diffChoices) {
		this.diffChoices = diffChoices;
	}


	private String[] getDiffChoices() {
		return diffChoices;
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


	private void setCurrentChosen(String currentChosen) {
		this.currentChosen = currentChosen;
	}


	private String getCurrentChosen() {
		return currentChosen;
	}



	
	

}
