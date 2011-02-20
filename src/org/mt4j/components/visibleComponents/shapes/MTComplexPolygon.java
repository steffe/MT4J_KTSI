package org.mt4j.components.visibleComponents.shapes;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.visibleComponents.GeometryInfo;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GluTrianglulator;

import processing.core.PApplet;

public class MTComplexPolygon extends MTTriangleMesh {
	public static final int WINDING_RULE_ODD = GluTrianglulator.WINDING_RULE_ODD;
	public static final int WINDING_RULE_NONZERO = GluTrianglulator.WINDING_RULE_NONZERO;

	//TODO override the intersection/containspoint methods to only use outline?
	//-> else we do expensive checks against each triangle
	//TODO note that its expensive to re/set vertices often
	//TODO note that using setGeometryInfo directly is discouraged here
	
	public MTComplexPolygon(PApplet app, Vertex[] vertices) {
		super(app, new GeometryInfo(app, new Vertex[]{}), false);
		this.setVertices(vertices);
		this.setNoStroke(false);
	}
	
	public MTComplexPolygon(PApplet app, List<Vertex[]> contours) {
		this(app, contours, GluTrianglulator.WINDING_RULE_ODD);
	}
	
	public MTComplexPolygon(PApplet app, List<Vertex[]> contours, int windingRule) {
		super(app, new GeometryInfo(app, new Vertex[]{}), false);
		this.setVertices(contours, windingRule);
		this.setNoStroke(false);
	}
	
	
	public void setVertices(List<Vertex[]> contours, int windingRule) {
		this.setOutlineContours(contours);
		
		GluTrianglulator triangulator = new GluTrianglulator(getRenderer());
		List<Vertex> tris = triangulator.tesselate(contours, windingRule);
		triangulator.deleteTess();
		
		super.setVertices(tris.toArray(new Vertex[tris.size()]));
	}
	

	@Override
	public void setVertices(Vertex[] vertices) {
		List<Vertex[]> contours = new ArrayList<Vertex[]>();
		contours.add(vertices);
		this.setOutlineContours(contours);
		
		GluTrianglulator triangulator = new GluTrianglulator(getRenderer());
		Vertex[] tris = triangulator.tesselate(vertices);
		triangulator.deleteTess();
		
		super.setVertices(tris);
	}
	
	
	

}
