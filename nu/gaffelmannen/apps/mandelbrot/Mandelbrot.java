package nu.gaffelmannen.apps.mandelbrot;

import java.awt.Color;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class Mandelbrot implements Observer {
	
	public static final double DEFAULT_XC = -0.5d;
	public static final double DEFAULT_YC = -1.0d;
	public static final double DEFAULT_SIZE = 0.9d;
	public static final int MAX_NUMBER_OF_ITERATIONS = 255;
	public static final int N = 512;
	
	private double xc   = DEFAULT_XC;
    private double yc   = DEFAULT_YC;
    private double size = DEFAULT_SIZE;
	
    Picture pic;
    
	public int mand(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }
	
	public void initMandelbrot(double xc, double yc, double size) {
		this.xc = xc;
		this.yc = yc;
		this.size = size;
		initMandelbrot();
	}
	
	public void update(Observable obj, Object arg) {
		
		if(pic.getPanUpFlag()) {
        	this.yc += 0.01d;
        	pic.clearPanUpFlag();
        	calculateFractal();
        	pic.repaint();
        }
		
		if(pic.getPanDownFlag()) {
        	this.yc -= 0.01d;
        	pic.clearPanDownFlag();
        	calculateFractal();
        	pic.repaint();
        }
		
		if(pic.getPanRightFlag()) {
        	this.xc += 0.01d;
        	pic.clearPanRightFlag();
        	calculateFractal();
        	pic.repaint();
        }
		
		if(pic.getPanLeftFlag()) {
        	this.xc -= 0.01d;
        	pic.clearPanLeftFlag();
        	calculateFractal();
        	pic.repaint();
        }
		
		if(pic.getZoomFlag()) {
			this.size += 0.01d * (double)pic.getUnitsScrolled();
			pic.clearZoomFlag();
			calculateFractal();
			pic.repaint();
		}
		
		if(pic.Reset()) {
        	this.size = DEFAULT_SIZE;
        	this.xc = DEFAULT_XC;
        	this.yc = DEFAULT_YC;
        	pic.clearResetFlag();
        	calculateFractal();
        	pic.repaint();
        }
	}
	
	public void initMandelbrot() {
        pic = new Picture(N, N);
        
        pic.addObserver(this);

        calculateFractal();
        
        pic.show();
        
        synchronized(pic) {
        	try {
                pic.wait();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        
        //this.size += 0.01d * (double)pic.getUnitsScrolled();
        //this.xc += 0.01d * pic.getRelativeCursorPosX();
        //this.yc += 0.01d * pic.getRelativeCursorPosY();
        //System.out.println("u=" + pic.getUnitsScrolled());
        //System.out.println("x=" + pic.getRelativeCursorPosX());
        //System.out.println("y=" + pic.getRelativeCursorPosY());
	}

	private void calculateFractal() {
		for (int i = 0; i < N; i++) {
		    for (int j = 0; j < N; j++)  {
				double x0 = xc - size/2 + size*i/N;
				double y0 = yc - size/2 + size*j/N;
				Complex z0 = new Complex(x0, y0);
				
				int iterations = mand(z0,MAX_NUMBER_OF_ITERATIONS);
				
				Color color = ((iterations==MAX_NUMBER_OF_ITERATIONS)
					? Color.BLACK
					: new Color(175, (iterations*2) % 254, 0) );
				
				pic.set(i, N-1-j, color);
		    }
		}
	}
	
    public static void main(String[] args) 
    {
    	try {
    		Mandelbrot mandelbrot = new Mandelbrot();
	    	
    		if(args != null && args.length == 2) {
	    		mandelbrot.xc = Double.parseDouble(args[0]);
			    mandelbrot.yc = Double.parseDouble(args[1]);
			    mandelbrot.size = Double.parseDouble(args[2]);
    		}
    		
    		mandelbrot.initMandelbrot();
    	}
    	catch(Exception e) { 
    		System.out.println("Invalid arguments, all input must be numeric.");
    	}
    }
}