package org.mt4jx.components.visibleComponents.widgets;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import com.badlogic.gdx.awesomium.Awesomium;
import com.badlogic.gdx.awesomium.JSArguments;
import com.badlogic.gdx.awesomium.WebView;
import com.badlogic.gdx.awesomium.WebViewListener;

public class MTWebBrowser extends MTRoundRectangle {
	
	private MTWebView mtWebView;
	private MTKeyboard keyboard;
	private MTTextField navbar;
	private BrowserInputKeyboardListener browserInput;
	private NavBarInputKeyboardListener navInput;
	private MTComponent clippedChildContainer;

	public MTWebBrowser(PApplet pApplet, int width, int height) {
//		super(pApplet, 0, 0, 0, width, height);
		super(pApplet, 0,0,0, width, height, 25,25);
		
		clippedChildContainer = new MTComponent(pApplet);
//		clippedChildContainer.setChildClip(new Clip(pApplet, 0,0, width, height));
		MTRoundRectangle clipShape = new MTRoundRectangle(pApplet, 0,0,0, width, height, 25,25);
		clipShape.setNoStroke(true);
		clippedChildContainer.setChildClip(new Clip(clipShape));
		this.addChild(clippedChildContainer);
		
		
		int borderHorizontal = 25;
		int borderTop = 100;
		this.mtWebView = new MTWebView(pApplet, width-2*borderHorizontal, height - borderTop);
		clippedChildContainer.addChild(mtWebView);
		mtWebView.translate(new Vector3D(borderHorizontal, borderTop - borderHorizontal));
		
		float hOffset = 55;
		float vOffset = 22.5f;
		
		MTImageButton left = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "arrow_left_32x32.png"));
//		left.scale(2, 2, 1, left.getCenterPointLocal(), TransformSpace.LOCAL);
		left.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					mtWebView.getWebView().goToHistoryOffset(-1);
					break;	
				default:
					break;
				}
			return false;
			}
		});
		clippedChildContainer.addChild(left);
		left.setNoStroke(true);
		left.translate(new Vector3D(hOffset * 1, vOffset, 0));
		
		MTImageButton right = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "arrow_right_32x32.png"));
//		right.scale(2, 2, 1, right.getCenterPointLocal(), TransformSpace.LOCAL);
		
		right.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					mtWebView.getWebView().goToHistoryOffset(1);
					break;	
				default:
					break;
				}
			return false;
			}
		});
		
		clippedChildContainer.addChild(right);
		right.setNoStroke(true);
		right.translate(new Vector3D(hOffset * 2, vOffset, 0));
		
		MTImageButton reload = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "loop_32x32.png"));
//		reload.scale(2, 2, 1, reload.getCenterPointLocal(), TransformSpace.LOCAL);
		reload.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					mtWebView.getWebView().reload();
					break;	
				default:
					break;
				}
			return false;
			}
		});
		
		clippedChildContainer.addChild(reload);
		reload.setNoStroke(true);
		reload.translate(new Vector3D(hOffset * 3, vOffset, 0));
		
		
		MTImageButton stop = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "minus_alt_32x32.png"));
//		stop.scale(2, 2, 1, stop.getCenterPointLocal(), TransformSpace.LOCAL);
		stop.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					mtWebView.getWebView().stop();
					break;	
				default:
					break;
				}
			return false;
			}
		});
		
		
		clippedChildContainer.addChild(stop);
		stop.setNoStroke(true);
		stop.translate(new Vector3D(hOffset * 4, vOffset, 0));
		
		MTImageButton home = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "home_32x32.png"));
//		home.scale(2, 2, 1, home.getCenterPointLocal(), TransformSpace.LOCAL);
		home.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					mtWebView.loadURL("http://www.statusboard.ch/user", "", "", "");
					break;	
				default:
					break;
				}
			return false;
			}
		});
		
		
		clippedChildContainer.addChild(home);
		home.setNoStroke(true);
		home.translate(new Vector3D(hOffset * 5, vOffset, 0));
		
		MTImageButton close = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "x_alt_32x32.png"));
//		close(2, 2, 1, stop.getCenterPointLocal(), TransformSpace.LOCAL);
		close.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch(te.getTapID()) {
				case TapEvent.TAPPED:
					destroy();
					break;	
				default:
					break;
				}
			return false;
			}
		});
		
		clippedChildContainer.addChild(close);
		close.setNoStroke(true);
		close.translate(new Vector3D(hOffset * 13.5f, vOffset, 0));
		
		
		//Add a keyboard
		keyboard = new BKeyboard(pApplet);
		keyboard.setFillColor(new MTColor(80,80,80,130));
		keyboard.setStrokeColor(MTColor.BLACK);
		this.addChild(keyboard);
//		keyboard.translate(new Vector3D(50,height-height/4f));
		keyboard.setPositionRelativeToParent(getCenterPointLocal());
		keyboard.setVisible(false);
		browserInput = new BrowserInputKeyboardListener();
		keyboard.addTextInputListener(browserInput);

		
		//add we weblistener //INFO we can apparantly only set 1 listener overall
		getWebView().setListener(new WebViewListener() {
			@Override
			public void onWebViewCrashed() {}
			@Override
			public void onRequestMove(int x, int y) {}
			@Override
			public void onRequestDownload(String url) {			}
			@Override
			public void onReceiveTitle(String title, String frameName) {
			}
			@Override
			public void onPluginCrashed(String pluginName) {
			}
			@Override
			public void onOpenExternalLink(String url, String source) {
				getWebView().loadURL(url, "", "", "");
			}
			@Override
			public void onGetPageContents(String url, String contents) {
			}
			@Override
			public void onFinishLoading() {
			}
			@Override
			public void onDOMReady() {
			}
			@Override
			public void onChangeTooltip(String tooltip) {
			}
			@Override
			public void onChangeTargetURL(String url) {
			}
			@Override
			public void onChangeKeyboardFocus(boolean isFocused) {
				if (isFocused){
					keyboard.setVisible(true);
					keyboard.removeTextInputListener(navInput);
					keyboard.addTextInputListener(browserInput);
				}
			}
			@Override
			public void onChangeCursor(int cursor) {
			}
			@Override
			public void onCallback(String objectName, String callbackName,	JSArguments args) {
			}
			@Override
			public void onBeginNavigation(String url, String frameName) {
				navbar.setText(url);
			}
			@Override
			public void onBeginLoading(String url, String frameName, int statusCode, String mimeType) {
			}
		});
		
		this.navInput = new NavBarInputKeyboardListener();
		
		navbar = new MTTextField(pApplet, 0, 0, 250, 25, FontManager.getInstance().getDefaultFont(pApplet));
		navbar.unregisterAllInputProcessors();
		navbar.removeAllGestureEventListeners();
		navbar.setText("http://www.google.com");
		navbar.translate(new Vector3D(hOffset*6, 25));
		clippedChildContainer.addChild(navbar);
		
		navbar.registerInputProcessor(new TapProcessor(pApplet));
		navbar.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					keyboard.setVisible(true);
					navbar.setEnableCaret(true);
					keyboard.removeTextInputListener(browserInput);
					keyboard.addTextInputListener(navInput);
					//getWebView().unfocus();
				}
				return false;
			}
		});
		
		
		this.setDepthBufferDisabled(true); //To prevent z-fighting
		
	}
	
	
	private int getModifiers(String unicode) {
		int modifiers = 0;
		if(unicode.equals("shift")){
			modifiers |= Awesomium.AWE_MOD_SHIFT_KEY;
		}
		return modifiers;
	}
	
	public WebView getWebView(){
		return mtWebView.getWebView();
	}
	
	private class NavBarInputKeyboardListener implements ITextInputListener{
		@Override
		public void setText(String text) {
			navbar.setText(text);	
		}
		
		@Override
		public void removeLastCharacter() {
			navbar.removeLastCharacter();
		}

		@Override
		public void clear() {
			navbar.clear();
		}
		
		@Override
		public void appendText(String text) {
			navbar.appendText(text);	
		}
		
		@Override
		public void appendCharByUnicode(String unicode) {
			if (unicode.equals("\n")){
				navbar.setEnableCaret(false);
				getWebView().loadURL(navbar.getText(), "", "", "");
				keyboard.setVisible(false);
			}else{
				navbar.appendCharByUnicode(unicode);
			}
		}
	}
	
	private class BrowserInputKeyboardListener implements ITextInputListener{
		@Override
		public void setText(String text) {	}
		
		@Override
		public void removeLastCharacter() {
			if (isVisible()){
				getWebView().injectKeyDown(Awesomium.AWE_AK_BACK, getModifiers(""), false);
				getWebView().injectKeyUp(Awesomium.AWE_AK_BACK, getModifiers(""), false);
			}else{
				getWebView().goToHistoryOffset(-1);
			}
		}

		@Override
		public void clear() {	}
		
		@Override
		public void appendText(String text) {	}
		
		@Override
		public void appendCharByUnicode(String unicode) {
			//System.out.println(unicode);
			if(unicode.equals("shift")){
				getWebView().injectKeyDown(Awesomium.AWE_AK_SHIFT, getModifiers(unicode), false);
				getWebView().injectKeyUp(Awesomium.AWE_AK_SHIFT, getModifiers(unicode), false);
			}
			else if(unicode.equals("\n")){
				getWebView().injectKeyDown(Awesomium.AWE_AK_RETURN, getModifiers(unicode), false);
				getWebView().injectKeyUp(Awesomium.AWE_AK_RETURN, getModifiers(unicode), false);
				keyboard.setVisible(false);
			}
			else{
				char chr = unicode.charAt(0);
				//boolean upper = Character.isUpperCase(chr);
				getWebView().injectKeyTyped(chr);
			}
		}
	}
	
	private class BKeyboard extends MTKeyboard{
		public BKeyboard(PApplet pApplet) {
			super(pApplet);
		}
		
		@Override
		protected void onCloseButtonClicked() {
			this.setVisible(false);
		}
	}
	
	
	@Override
	public void setSizeLocal(float width, float height) {
		super.setSizeLocal(width, height);
		if (MT4jSettings.getInstance().isOpenGlMode() && clippedChildContainer.getChildClip() != null && clippedChildContainer.getChildClip().getClipShape() instanceof MTRoundRectangle){ 
			MTRoundRectangle clipRect = (MTRoundRectangle)clippedChildContainer.getChildClip().getClipShape();
			//clipRect.setVertices(Vertex.getDeepVertexArrayCopy(this.getVerticesLocal()));
			clipRect.setVertices(this.getVerticesLocal());
		}
	}
	
//	@Override
//	public void setWidthLocal(float width) {
//		super.setWidthLocal(width);
//		if (MT4jSettings.getInstance().isOpenGlMode() && clippedChildContainer.getChildClip() != null && clippedChildContainer.getChildClip().getClipShape() instanceof MTRectangle){ 
//			MTRectangle clipRect = (MTRectangle)clippedChildContainer.getChildClip().getClipShape();
//			//clipRect.setVertices(Vertex.getDeepVertexArrayCopy(this.getVerticesLocal()));
//			clipRect.setVertices(this.getVerticesLocal());
//		}
//	}
//	
//	@Override
//	public void setHeightLocal(float height) {
//		super.setHeightLocal(height);
//		if (MT4jSettings.getInstance().isOpenGlMode() && clippedChildContainer.getChildClip() != null && clippedChildContainer.getChildClip().getClipShape() instanceof MTRectangle){ 
//			MTRectangle clipRect = (MTRectangle)clippedChildContainer.getChildClip().getClipShape();
//			//clipRect.setVertices(Vertex.getDeepVertexArrayCopy(this.getVerticesLocal()));
//			clipRect.setVertices(this.getVerticesLocal());
//		}
//	}
	

}
