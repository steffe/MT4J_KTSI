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

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotate3DProcessor.Cluster3DExt;
import org.mt4j.input.inputProcessors.componentProcessors.rotate3DProcessor.IVisualizeMethodProvider;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

public class BlinkingEmissionActivateVisualizationAction implements IGestureEventListener {

	private Cluster3DExt cluster;
	
	private IVisualizeMethodProvider methodProvider;
	
	public BlinkingEmissionActivateVisualizationAction(Cluster3DExt cluster,IVisualizeMethodProvider methodProvider)
	{
		this.cluster = cluster;
		this.methodProvider = methodProvider;
		this.cluster.setVisualizeProvider(null);
	}
	public boolean processGestureEvent(MTGestureEvent ge) {
		if(ge instanceof TapEvent)
		{
			TapEvent tapEv = (TapEvent)ge;
			switch(tapEv.getId())
			{
			case TapEvent.GESTURE_STARTED:
				cluster.setVisualizeProvider(methodProvider);
				break;
			case TapEvent.GESTURE_ENDED:
				for(MTComponent groups : cluster.getChildren())
				{
					
					for(MTComponent comp : groups.getChildren())
					{
						MTTriangleMesh mesh = (MTTriangleMesh)comp;
						if(comp instanceof MTTriangleMesh)
						{
							mesh.getMaterial().setEmission(new float[]{0.f,0.f,0.f});
												
						}
					}
				}
				cluster.setVisualizeProvider(null);
				break;
			}
		}
		return false;
	}

}
