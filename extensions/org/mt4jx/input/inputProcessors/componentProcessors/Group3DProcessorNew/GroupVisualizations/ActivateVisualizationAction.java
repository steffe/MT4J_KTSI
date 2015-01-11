/***********************************************************************
*   MT4j Copyright (c) 2008 - 2012, C.Ruff, Fraunhofer-Gesellschaft All rights reserved.
*
*   This file is part of MT4j.
*
*   MT4j is free software: you can redistribute it and/or modify
*   it under the terms of the GNU Lesser General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   MT4j is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*   GNU Lesser General Public License for more details.
*
*   You should have received a copy of the GNU Lesser General Public License
*   along with MT4j.  If not, see <http://www.gnu.org/licenses/>.
*
************************************************************************/
package org.mt4jx.input.inputProcessors.componentProcessors.Group3DProcessorNew.GroupVisualizations;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotate3DProcessor.Cluster3DExt;
import org.mt4j.input.inputProcessors.componentProcessors.rotate3DProcessor.IVisualizeMethodProvider;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

public class ActivateVisualizationAction implements IGestureEventListener {

	private Cluster3DExt cluster;
	
	private IVisualizeMethodProvider methodProvider;
	
	public ActivateVisualizationAction(Cluster3DExt cluster,IVisualizeMethodProvider methodProvider)
	{		
		this.cluster = cluster;
		this.methodProvider = methodProvider;
		this.cluster.setVisualizeProvider(null);
	}
	public boolean processGestureEvent(MTGestureEvent ge) {
		if(ge instanceof DragEvent)
		{
			DragEvent tapEv = (DragEvent)ge;
			switch(tapEv.getId())
			{
			case DragEvent.GESTURE_STARTED:
				cluster.setVisualizeProvider(methodProvider);
				break;
			case DragEvent.GESTURE_ENDED:				
				cluster.setVisualizeProvider(null);
				break;
			default: break;
			}
		}
		return false;
	}

}
