/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009, C.Ruff, Fraunhofer-Gesellschaft All rights reserved.
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
package store;


import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.font.VectorFontCharacter;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKey;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

/**
 * A multitouch keyboard using vector graphics.
 * 
 * @author Christopher Ruff
 */
public class MTNumKeyboard extends MTRoundRectangle {
	
	/** The pa. */
	private PApplet pa;
	
	/** The key font. */
	private IFont keyFont;
	
	/** The key list. */
	private ArrayList<MTKey> keyList;
	
	/** The shift changers. */
	private ArrayList<MTKey> shiftChangers;
	
	/** The shift pressed. */
	private boolean shiftPressed;
	
	/** The key click action. */
	private KeyClickAction keyClickAction;
	
	/** The text input acceptors. */
	private List<ITextInputListener> textInputAcceptors;
	
	private boolean hardwareInput;
	
	/**
	 * Creates a new keyboard without an text input acceptor.
	 * 
	 * @param pApplet the applet
	 */
	public MTNumKeyboard(PApplet pApplet) {
		super( pApplet,0,0,0, 230, 200, 10,10);

		this.pa = pApplet;
		//Set drawing mode
		this.setDrawSmooth(true);
		
		//FIXME TEST
		this.setHardwareInputEnabled(true);
		
		this.setName("unnamed mt-keyboard");
		this.textInputAcceptors = new ArrayList<ITextInputListener>();
		
		if (MT4jSettings.getInstance().isOpenGlMode())
			this.setUseDirectGL(true);
		
		//TODO textareas an keyboard andocken lassen zum schreiben, oder doppelklick auf textarea?
		//TODO buttons textarea clear machen
		//TODO keyboard durch button heranimated
		
		MTColor keyColor = new MTColor(0,0,0,255);
		
		// INIT FIELDS
		//Load the Key font
		keyFont = FontManager.getInstance().createFont(pa, 
				"keys.svg", 30, keyColor); 
		
		keyList 		= new ArrayList<MTKey>();
		shiftChangers 	= new ArrayList<MTKey>();
		shiftPressed 	= false;
		keyClickAction 	= new KeyClickAction();
		
//		/*
		//TODO load button only once!
		MTSvgButton keybCloseSvg = new MTSvgButton(pa, MT4jSettings.getInstance().getDefaultSVGPath()
				+ "keybClose.svg");
		//Transform
		keybCloseSvg.scale(0.8f, 0.8f, 1, new Vector3D(0,0,0));
		keybCloseSvg.translate(new Vector3D(150,0,0));
		keybCloseSvg.setSizeXYGlobal(40, 40);
		keybCloseSvg.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
		keybCloseSvg.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					onCloseButtonClicked();
				}
				return false;
			}
		});
		this.addChild(keybCloseSvg);
//		*/
	


		KeyInfo[] keyInfos = this.getKeysLayout();
		
		//CREATE THE KEYS \\
		for (int i = 0; i < keyInfos.length; i++) {
			KeyInfo keyInfo = keyInfos[i];
			VectorFontCharacter fontChar = (VectorFontCharacter) keyFont.getFontCharacterByUnicode(keyInfo.keyfontUnicode);
			//FIXME expensive..
			MTKey key = new MTKey(pa,fontChar.getGeometryInfo(), keyInfo.charUnicodeToWrite, keyInfo.charUnicodeToWriteShifted);
//			key.setGeometryInfo(fontChar.getGeometryInfo());
			key.setName(fontChar.getName());
			key.setPickable(true);
			key.setFillColor(keyColor);
			key.unregisterAllInputProcessors();
			
			key.setOutlineContours(fontChar.getContours());
			if (MT4jSettings.getInstance().isOpenGlMode()){
				key.setUseDirectGL(true);
				//Use the display lists already created for the font characters of the key-font
				key.getGeometryInfo().setDisplayListIDs(fontChar.getGeometryInfo().getDisplayListIDs());
				key.setUseDisplayList(true);
			}

			scaleKey(key, 40);
			key.setPositionRelativeToParent(keyInfo.position);

			keyList.add(key); 
			key.setGestureAllowance(TapProcessor.class, true);
			key.registerInputProcessor(new TapProcessor(pa));
			key.addGestureListener(TapProcessor.class, keyClickAction);
			
			//Add keys that change during SHIFT to a list
			if (keyInfo.visibilityInfo 			== KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED){
				shiftChangers.add(key);
			}else if (keyInfo.visibilityInfo 	== KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED){
				key.setVisible(false);
				shiftChangers.add(key);
			}
			
			fontChar = null;
			this.addChild(key); 
		}
		
		//Draw this component and its children above 
		//everything previously drawn and avoid z-fighting
		this.setDepthBufferDisabled(true);
	}
	
	
	protected void onCloseButtonClicked() {
		this.close();
	}


	private KeyInfo[] getKeysLayout(){
	return getGermanLayout();
	}
	
	
	private KeyInfo[] getGermanLayout(){
		ArrayList<KeyInfo> keyInfos = new ArrayList<KeyInfo>();
		
		float lineY = 30;
		float advanceMent = 42;
		float startX = 0;
//		keyInfos.add(new KeyInfo("^", "^", "^", new Vector3D(startX,lineY), 			  KeyInfo.NORMAL_KEY)); //ESC key
		
		keyInfos.add(new KeyInfo("1", "1", "1", new Vector3D(startX+1*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("2", "2", "2", new Vector3D(startX+2*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("3", "3", "3", new Vector3D(startX+3*advanceMent,lineY), KeyInfo.NORMAL_KEY));
		
		lineY=lineY+advanceMent;
		
		keyInfos.add(new KeyInfo("4", "4", "4", new Vector3D(startX+1*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("5", "5", "5", new Vector3D(startX+2*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("6", "6", "6", new Vector3D(startX+3*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		
		lineY = lineY+advanceMent;
		
		keyInfos.add(new KeyInfo("7", "7", "7", new Vector3D(startX+1*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("8", "8", "8", new Vector3D(startX+2*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("9", "9", "9", new Vector3D(startX+3*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
	
		lineY = lineY+advanceMent;
		keyInfos.add(new KeyInfo(".", ".", ".", new Vector3D(startX+1*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("0", "0", "0", new Vector3D(startX+2*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo(",", ",", ",", new Vector3D(startX+3*advanceMent,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		keyInfos.add(new KeyInfo("z", "back", "back", new Vector3D(startX+4*advanceMent+15,lineY), KeyInfo.KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED));
		
		
		
		///////////
		//Special keys
		//keyInfos.add(new KeyInfo("z", "back", "back", 	new Vector3D(580,35),  KeyInfo.NORMAL_KEY));//Backspace
		//keyInfos.add(new KeyInfo("v", "\t", "\t", 	new Vector3D(62,77),  KeyInfo.NORMAL_KEY)); //Tab
		//keyInfos.add(new KeyInfo("j", "shift", "shift", new Vector3D(78,120), KeyInfo.NORMAL_KEY)); //Shift
		//keyInfos.add(new KeyInfo("f", "\n", "\n", 		new Vector3D(615, 105),KeyInfo.NORMAL_KEY)); //Enter
		
		return keyInfos.toArray(new KeyInfo[keyInfos.size()]);
	}
	
	
	private void scaleKey(MTKey key, float scale){
//		Vector3D scalingPoint = new Vector3D(0,0,0);
		Vector3D scalingPoint = key.getCenterPointLocal();
		key.scale(1/key.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), 1/key.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), 1, scalingPoint);
		key.scale(scale, scale, 1, scalingPoint);
	}
	
	
	private boolean setWidthRelativeToParent(float width){
		if (width > 0){
			Vector3D centerPoint;
			if (this.hasBounds()){
				centerPoint = this.getBounds().getCenterPointLocal();
				centerPoint.transform(this.getLocalMatrix());
			}else{
				centerPoint = this.getCenterPointGlobal();
				centerPoint.transform(this.getGlobalInverseMatrix());
			}
			this.scale(1/this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 1/this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 1, centerPoint);
			this.scale(width, width, 1, centerPoint);
//			this.scale(1/this.getWidthLocal(), 1/this.getWidthLocal(), 1, centerPoint, TransformSpace.RELATIVE_TO_SELF);
//			this.scale(width, width, 1, centerPoint, TransformSpace.RELATIVE_TO_SELF);
			return true;
		}else
			return false;
	}
	
	
	@Override
	protected void setDefaultGestureActions() {
		super.setDefaultGestureActions();
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
	
	
	/**
	 * The Class KeyInfo.
	 * 
	 * @author C.Ruff
	 */
	private class KeyInfo{
		
		/** The keyfont unicode. */
		String keyfontUnicode;
		
		/** The char unicode to write. */
		String charUnicodeToWrite;
		
		/** The char unicode to write shifted. */
		String charUnicodeToWriteShifted;
		
		/** The position. */
		Vector3D position;
		
		/** The visibility info. */
		int visibilityInfo;
		
		/** The Constant NORMAL_KEY. */
		public static final int NORMAL_KEY 								= 0;
		
		/** The Constant KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED. */
		public static final int KEY_ONLY_VISIBLE_WHEN_SHIFT_NOTPRESSED 	= 1;
		
		/** The Constant KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED. */
		public static final int KEY_ONLY_VISIBLE_WHEN_SHIFT_PRESSED 	= 2;
		
		/**
		 * Instantiates a new key info.
		 * 
		 * @param keyfontUnicode the keyfont unicode
		 * @param charUnicodeToWrite the char unicode to write
		 * @param charUnicodeToWriteShifted the char unicode to write shifted
		 * @param position the position
		 * @param visibilityInfo the visibility info
		 */
		public KeyInfo(String keyfontUnicode, String charUnicodeToWrite, String charUnicodeToWriteShifted, Vector3D position, int visibilityInfo) {
			super();
			this.keyfontUnicode = keyfontUnicode;
			this.charUnicodeToWrite = charUnicodeToWrite;
			this.charUnicodeToWriteShifted = charUnicodeToWriteShifted;
			this.position = position;
			this.visibilityInfo = visibilityInfo;
		}
		
	}
	
	
	
	/**
	 * The Class KeyClickAction.
	 * 
	 * @author C.Ruff
	 */
	private class KeyClickAction implements IGestureEventListener {
		/** The key press indent. */
		private int keyPressIndent;
		
		/**
		 * Instantiates a new key click action.
		 */
		public KeyClickAction(){
			keyPressIndent = 3;
		}
		
		public boolean processGestureEvent(MTGestureEvent g) {
			if (g instanceof TapEvent){
				TapEvent clickEvent = (TapEvent)g;
				IMTComponent3D clicked = clickEvent.getTarget();
				if (clicked != null && clicked instanceof MTKey){
					MTKey clickedKey = (MTKey)clicked;
					switch (clickEvent.getTapID()) {
					case TapEvent.TAP_DOWN:
						pressKey(clickedKey);
						onKeyboardButtonDown(clickedKey, shiftPressed);
						break;
					case TapEvent.TAP_UP:
						unpressKey(clickedKey);
						onKeyboardButtonUp(clickedKey, shiftPressed);
						break;
					case TapEvent.TAPPED:
						unpressKey(clickedKey);
						onKeyboardButtonClicked(clickedKey, shiftPressed);
						break;
					default:
						break;
					}//switch
				}//instance of key
			}//instanceof clickevent
			return false;
		}//method
		
		
		private void pressKey(MTKey clickedKey) {
			clickedKey.setPressed(true);
			float keyHeight = clickedKey.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
			float keyWidth 	= clickedKey.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			
			setSizeXYRelativeToParent(clickedKey, keyWidth-keyPressIndent, keyHeight-keyPressIndent);
			
			if (clickedKey.getCharacterToWrite().equals("shift")){
				shiftPressed = true;
				// Make certain keys visible / not visible when shift pressed!
				for (MTKey key: shiftChangers){
					key.setVisible(!key.isVisible());
				}
			}
		}

		private void unpressKey(MTKey clickedKey){
			clickedKey.setPressed(false);
			float kHeight = clickedKey.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
			float kWidth = clickedKey.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			setSizeXYRelativeToParent(clickedKey, kWidth+keyPressIndent, kHeight+keyPressIndent);
			
			//System.out.println("Button CLICKED: " + clickedKey.getCharacterToWrite());
			if (clickedKey.getCharacterToWrite().equals("shift")){
				shiftPressed = false;
				//Set shift visible keys visible/not visible
				for (MTKey key: shiftChangers){
					key.setVisible(!key.isVisible());
				}
			}
		}
		
		/**
		 * Sets the size xy relative to parent.
		 * 
		 * @param shape the shape
		 * @param width the width
		 * @param height the height
		 * 
		 * @return true, if successful
		 */
		private boolean setSizeXYRelativeToParent(AbstractShape shape, float width, float height){
			if (width > 0 && height > 0){
				Vector3D centerPoint;
				if (shape.hasBounds()){
					centerPoint = shape.getBounds().getCenterPointLocal();
					centerPoint.transform(shape.getLocalMatrix()); //TODO neccessary?
				}else{
					centerPoint = shape.getCenterPointGlobal();
					centerPoint.transform(shape.getGlobalInverseMatrix());
				}
				shape.scale(width* (1/shape.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)), height*(1/shape.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)), 1, centerPoint);
				return true;
			}else
				return false;
		}
	}//class

	/**
	 * Called after keyboard button pressed.
	 * 
	 * @param clickedKey the clicked key
	 * @param shiftPressed the shift pressed
	 */
	protected void onKeyboardButtonDown(MTKey clickedKey, boolean shiftPressed){
		ITextInputListener[] listeners = this.getTextInputListeners();
		for (int i = 0; i < listeners.length; i++) {
			ITextInputListener textInputListener = listeners[i];
			if (clickedKey.getCharacterToWrite().equals("back")){
				textInputListener.removeLastCharacter();
			}else if (clickedKey.getCharacterToWrite().equals("shift")){
					//no nothing
			}else{
				String charToAdd = shiftPressed ? clickedKey.getCharacterToWriteShifted() : clickedKey.getCharacterToWrite();
				textInputListener.appendCharByUnicode(charToAdd);
			}
		}
	}
	
	/**
	 * Keyboard button up.
	 * 
	 * @param clickedKey the clicked key
	 * @param shiftPressed the shift pressed
	 */
	protected void onKeyboardButtonUp(MTKey clickedKey, boolean shiftPressed){
//		keyboardButtonClicked(clickedKey, shiftPressed);
	}
	
	/**
	 * Keyboard button clicked.
	 * 
	 * @param clickedKey the clicked key
	 * @param shiftPressed the shift pressed
	 */
	protected void onKeyboardButtonClicked(MTKey clickedKey, boolean shiftPressed){
		
	}
	
	
	public synchronized void addTextInputListener(ITextInputListener textListener){
		if (!this.textInputAcceptors.contains(textListener)){
			this.textInputAcceptors.add(textListener);
		}
	}
	
	public synchronized ITextInputListener[] getTextInputListeners(){
		return this.textInputAcceptors.toArray(new ITextInputListener[this.textInputAcceptors.size()]);
	}
	
	public synchronized void removeTextInputListener(ITextInputListener textListener){
		if (this.textInputAcceptors.contains(textListener)){
			this.textInputAcceptors.remove(textListener);
		}
	}
	
	

	public void close(){
		this.closeKeyboard();
	}
	
	protected void closeKeyboard(){
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		Animation keybCloseAnim = new Animation("Keyboard Fade", new MultiPurposeInterpolator(width, 1, 300, 0.2f, 0.5f, 1), this);
		keybCloseAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
//					keyboard.setWidthXYRelativeToParent(currentVal);
					setWidthRelativeToParent(currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		keybCloseAnim.start();
	}

	
	protected void closeButtonClicked(){
		this.close();
	}
	
	
	public void setHardwareInputEnabled(boolean hardwareInput){
		try {
			PApplet app = getRenderer();
			if (hardwareInput) {
				app.registerKeyEvent(this);
			}else{
				app.unregisterKeyEvent(this);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		this.hardwareInput = hardwareInput;
	}
	
	public boolean isHardwareInputEnabled(){
		return this.hardwareInput;
	}


	public void keyEvent(KeyEvent e){
		if (this.isEnabled()){
			if (e.getID()!= KeyEvent.KEY_PRESSED) return;

			String keyCode = String.valueOf(e.getKeyChar());
			//System.out.println("Key input: " + keyCode);
			ITextInputListener[] listeners = this.getTextInputListeners();
			for (int i = 0; i < listeners.length; i++) {
				ITextInputListener textInputListener = listeners[i];
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					textInputListener.removeLastCharacter();
				}else if (e.getKeyCode() == KeyEvent.VK_SHIFT 
						|| e.getKeyCode() == KeyEvent.VK_ALT
						|| e.getKeyCode() == KeyEvent.VK_ALT_GRAPH
						|| e.getKeyCode() == KeyEvent.VK_CONTROL
				){
					//do nothing
				}else{
					textInputListener.appendCharByUnicode(keyCode);
				}
			}
		}
	} 

	
	@Override
	protected void destroyComponent() {
		super.destroyComponent();
		
		keyFont = null;
		keyList.clear();
		shiftChangers.clear();
		textInputAcceptors.clear();
		
		if (this.isHardwareInputEnabled()){
			try {
				getRenderer().unregisterKeyEvent(this);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
	/**
	 * Snap to keyboard.
	 * 
	 * @param mtKeyboard the mt keyboard
	 */
	public void snapToKeyboard(MTTextArea textArea){
		//OLD WAY
//		this.translate(new Vector3D(30, -(getFont().getFontAbsoluteHeight() * (getLineCount())) + getFont().getFontMaxDescent() - borderHeight, 0));
		this.addChild(textArea);
		textArea.setPositionRelativeToParent(new Vector3D(40, -textArea.getHeightXY(TransformSpace.LOCAL)*0.5f));
	}

	
}
