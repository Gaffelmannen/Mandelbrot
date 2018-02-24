package nu.gaffelmannen.apps.mandelbrot;

import java.awt.Color;

public class Mandelbrot {

	public static int mand(Complex z0, int max) 
	{
        Complex z = z0;
        for (int t = 0; t < max; t++) 
        {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }
	
    public static void main(String[] args) 
    {
    		double xc   = -0.5d;
	    double yc   = -1.0d;
	    double size = 0.9d;
    	
	    	try
	    	{
		    xc   = Double.parseDouble(args[0]);
		    yc   = Double.parseDouble(args[1]);
		    size = Double.parseDouble(args[2]);
	    	}
	    	catch(Exception e) { }
    	
        int N   = 512;
        int maxNumberOfIterations = 255;

        Picture pic = new Picture(N, N);
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++) 
            {
				double x0 = xc - size/2 + size*i/N;
				double y0 = yc - size/2 + size*j/N;
				Complex z0 = new Complex(x0, y0);
				
				int iterations = mand(z0,maxNumberOfIterations);
				
				Color color = ((iterations==maxNumberOfIterations) ? Color.BLACK : new Color(175, (iterations*2) % 254, 0) );
				
				pic.set(i, N-1-j, color);
            }
        }
        
        pic.show();
    }
}