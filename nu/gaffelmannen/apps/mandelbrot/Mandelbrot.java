package nu.gaffelmannen.apps.mandelbrot;

import java.awt.Color;
import javax.swing.*;

public class Mandelbrot {
	
	private double xc   = -0.5d;
    private double yc   = -1.0d;
    private double size = 0.9d;
	
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
	
	public void initMandelbrot() {
		int N   = 512;
        int maxNumberOfIterations = 255;

        Picture pic = new Picture(N, N);


        while(true) {

	        for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++)  {
					double x0 = xc - size/2 + size*i/N;
					double y0 = yc - size/2 + size*j/N;
					Complex z0 = new Complex(x0, y0);
					
					int iterations = mand(z0,maxNumberOfIterations);
					
					Color color = ((iterations==maxNumberOfIterations)
						? Color.BLACK
						: new Color(175, (iterations*2) % 254, 0) );
					
					pic.set(i, N-1-j, color);
	            }
	        }
	        
	        pic.show();
	        
	        synchronized(pic) {
	            try {
	                pic.wait();
	            }
	            catch(InterruptedException e){
	                e.printStackTrace();
	            }
	        }
	        this.size += 0.01d * (double)pic.getUnitsScrolled();
	        this.xc += 0.01d * pic.getRelativeCursorPosX();
	        this.yc += 0.01d * pic.getRelativeCursorPosY();
	        System.out.println("u=" + pic.getUnitsScrolled());
	        System.out.println("x=" + pic.getRelativeCursorPosX());
	        System.out.println("y=" + pic.getRelativeCursorPosY());
        }
	}
	
    public static void main(String[] args) 
    {
    	System.out.println("Hej!");
    	
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