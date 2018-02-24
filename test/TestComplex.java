package nu.gaffelmannen.apps.mandelbrot.test;

import static org.junit.Assert.*;

import nu.gaffelmannen.apps.mandelbrot.Complex;

import org.junit.Test;

public class TestComplex {

	@Test
	public void test_Create_ComplexNumber() 
	{
		double delta = 0.0d;
		double a = 3.0d;
		double b = 4.0d;
		
		Complex c = new Complex(a,b);
		
		assertEquals(a, c.a, delta);
		assertEquals(b, c.b, delta);
	}
	
	@Test
	public void test_absolute_of_ComplexNumber()
	{
		double delta = 0.0d;
		double a = 3.0d;
		double b = 4.0d;
		
		Complex c = new Complex(a,b);
		
		assertEquals(5.0d, c.abs(), delta);
	}
}
