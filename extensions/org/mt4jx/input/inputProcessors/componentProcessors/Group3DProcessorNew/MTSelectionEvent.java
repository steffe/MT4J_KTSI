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
package org.mt4jx.input.inputProcessors.componentProcessors.Group3DProcessorNew;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.input.MTEvent;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.ILassoable;

public class MTSelectionEvent extends MTEvent {

	public static final int SELECTION_STARTED = 1;
	
	public static final int SELECTION_ENDED = 2;
	
	public static final int SELECTION_UPDATED = 3;
	
	private int id;
	
	private ArrayList<MTComponent> selectedComps;
	
	public MTSelectionEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public MTSelectionEvent(Object source,		
			int id, ArrayList<MTComponent> selectedComps) {
		super(source);	
		this.id = id;
		this.selectedComps = selectedComps;
	}

	public void setSelectedComps(ArrayList<MTComponent> selectedComps) {
		this.selectedComps = selectedComps;
	}

	public ArrayList<MTComponent> getSelectedComps() {
		return selectedComps;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
