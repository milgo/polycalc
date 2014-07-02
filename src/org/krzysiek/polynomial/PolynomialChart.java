package org.krzysiek.polynomial;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public class PolynomialChart extends Canvas{

	private int W = 100;
	private int H = 100;

	private int RX = 20;
	private int RY = 20;
	
	private double zoomX = 1.0;
	private double zoomY = 1.0;
	
	private Point[] points = null;
	
    public Command zoomInXCommand = new Command("Powiêksz oœ X", Command.OK, 2);
    public Command zoomOutXCommand = new Command("Pomniejsz oœ X", Command.OK, 2);
    
    public Command zoomInYCommand = new Command("Powiêksz oœ Y", Command.OK, 2);
    public Command zoomOutYCommand = new Command("Pomniejsz oœ Y", Command.OK, 2);
     
	class Point {
		public double x, y;
		public Point(){}
		public Point(double x, double y){
			this.x = x;
			this.y = y;
		}
	}
	
  private Point[] evaluatePolynomial(String exp, double from, double to, double step){
	  if(from > to)return null;

	  int length = (int)((to - from)/step);
	  
	  Point[] points = new Point[length];
	  Polynomial p = new Polynomial(exp);
	  for(int i=0; i<length; i++){
		  double x = (from + i*step);
		  points[i] = new Point(x, p.evaluate(x));
		  //System.out.println("data("+points[i].x+"): " + points[i].y);
	  }
	  return points;
  }
  
  private Point transformPointOnScreen(Point p, Point origin, double sx, double sy){
	  Point tp = new Point(origin.x + (p.x * sx), origin.y + (p.y * sy));
	  return tp;
  }
  
  private void plotPoints(Graphics gr, Point[] p, double sx, double sy){
	  
	  	if(p==null)return;
	   
	    gr.setColor(255, 255, 255);
	    gr.fillRect(0, 0, W, H);
	    
		Point origin = new Point(W/2, H/2);
		
		int x_div = W/RX;
		int y_div = H/RY;

		gr.setColor(200, 200, 200);
		for(int y=0; y<y_div/2; y++){
			gr.drawLine(0, (int)origin.y - y*RY, (int)W, (int)origin.y - y*RY);
			gr.drawLine(0, (int)origin.y + y*RY, (int)W, (int)origin.y + y*RY);
			//String s = doubleToString(((H/2-y*Y_DIV)/sy), 2);
			//gr.drawChars(s.toCharArray(), 0, s.length(), (int)(W/2)+5, (int)(y*Y_DIV)-3);
		}
		
		//double xSteps = W/20;
		for(int x=0; x<x_div/2; x++){
			gr.drawLine((int)origin.x - x*RX, 0, (int)origin.x - x*RX, H);
			gr.drawLine((int)origin.x + x*RX, 0, (int)origin.x + x*RX, H);
			//s = doubleToString((-(W/2-x*X_DIV)/sx), 2);
			//gr.drawChars(s.toCharArray(), 0, s.length(), (int)(x*X_DIV)-8, (int)(H/2)+20);
		}
		
	    //draw axes
		gr.setColor(0, 0, 0);
	    gr.drawLine((int)origin.x+RX, (int)origin.y-5, (int)origin.x+RX, (int)origin.y+5);
	    gr.drawLine((int)origin.x-5, (int)origin.y-RY, (int)origin.x+5, (int)origin.y-RY);
		gr.drawLine((int)origin.x, 0, (int)origin.x, H);
		gr.drawLine(0, (int)origin.y, W, (int)origin.y);
		
		String s = null;
	    int xPrev=-1, yPrev=-1;
	    gr.setColor(255, 0, 0);
	    for (int i=0; i<p.length; i++)
	    {
	    	Point sp = transformPointOnScreen(p[i], origin, sx, sy);
	    	if(sp.x > 0 && sp.x < W && sp.y > 0 && sp.y < H){
		    	if (xPrev!=-1){
		    		gr.drawLine(xPrev, yPrev, (int)sp.x, (int)sp.y);
		    	}
	    	}
	      xPrev=(int)sp.x; yPrev=(int)sp.y;
	    }
	    
	    gr.setColor(0, 0, 0);
		s = Polynomial.doubleToString(((RY)/sy), 2);
		gr.drawString(s, (int)origin.x+5, (int)origin.y-RY-16, 0);
		s = Polynomial.doubleToString(((RX)/sx), 2);
		gr.drawString(s, (int)origin.x+RX+2, (int)origin.y+14, 0);
  }

  public PolynomialChart(Kalkulator k, int w, int h, String polyStr, int from, int to, 
		  double step, double zoomX, double zoomY)
  {
	this.W = w;
    this.H = h+30;
    System.out.println(w+", "+h);
    this.zoomX = zoomX;
    this.zoomY = zoomY;
    points = evaluatePolynomial(polyStr, from, to, step);
    this.addCommand(Kalkulator.getBack);
    this.addCommand(zoomInXCommand);
    this.addCommand(zoomOutXCommand);
    this.addCommand(zoomInYCommand);
    this.addCommand(zoomOutYCommand);
    this.setCommandListener(k);
  }

	protected void paint(Graphics gr) {
		if(points == null)return;
		plotPoints(gr, points, zoomX, zoomY);
	}
	
	public void zoomInX(){
		zoomX /= 2;
	}
	
	public void zoomOutX(){
		zoomX *= 2;
	}
	
	public void zoomInY(){
		zoomY /= 2;
	}
	
	public void zoomOutY(){
		zoomY *= 2;
	}
	
	
	
	/*public void commandAction(Command c, Displayable d) {
		if(c == zoomInX){
			
		}
		if(c == zoomOutX){
			zoomX *= 2;
		}
		if(c == zoomInY){
			zoomY /= 2;
		}
		if(c == zoomOutY){
			zoomY *= 2;
		}
	}*/

}
