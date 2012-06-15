package nu.gaffelmannen.apps.mandelbrot;

public class Complex {
	public double a;
	public double b;
	
	public Complex(double a, double b) {
		this.a = a;
		this.b = b;
	}
	
	public double GetReal() { return a; }
	public double GetImaginary() { return b; }
	
	public double abs() {    
		return Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
	}
	
	public Complex plus(Complex z) {
		Complex result = new Complex(
			this.a + z.a,
			this.b + z.b
		);
		return result;
	}
	
	public Complex times(Complex z) {
		Complex result = new Complex(
			(a * z.a) - (b * z.b),
			(a * z.b) + (b * z.a));
		return result;
	}
}
