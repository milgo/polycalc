/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.krzysiek.polynomial;

import java.util.Vector;

/**
 * Polynomial implements a mathematical polynomial:
 * c[0] + c[1] * x + c[2] * x^2 + ....
 *
 * This class is based on code published in Object-Oriented Implementation of
 * Numerical Methods by Didier H. Besset.
 * The code has been adapted to the OSP framework by Wolfgang Christian.
 */
public class Polynomial {

  /**
   * Polynomial coefficients.
   */
  protected double[] coefficients;

  /**
   * Constructs a polynomial with the given coefficients.
   * @param coef polynomial coefficients.
   */
  public Polynomial(double[] coef) {
    coefficients = coef;
  }

  /**
   * Gets a clone of the polynomial coefficients c:
   * c[0] + c[1] * x + c[2] * x^2 + ....
   *
   * @return double[]
   */
 /* public double[] getCoefficients() {
    return(double[]) coefficients.clone();
  }*/

  /**
   * Constructs a polynomial with the given coefficients.
   * @param coef polynomial coefficients.
   */
  public Polynomial(String[] coef) {
    coefficients = new double[coef.length];
    for(int i = 0, n = coef.length;i<n;i++) {
      try {
        coefficients[i] = Double.parseDouble(coef[i]);
      } catch(NumberFormatException ex) {
        coefficients[i] = 0;
      }
    }
  }
  
  public Polynomial(String str){
  	String[] exp_split = splitStringToPolynomial(str, " "); //rodzielam lancuch na poszczegolne wspolczynniki
  	Vector coefs_split = new Vector(); // robie w wektorze bo nie wiem ile bedzie wspolczynnikow
  	int numCoefs = 0; //liczba wspolczynnikow
  	
  	for(int i=0; i<exp_split.length; i++){ //przelatuje po wspołczynnikach w petli
  		try{
  			Double d = new Double(Double.parseDouble(exp_split[i])); //parsuje (zamieniam na liczbe) kazdy ze wspolczynnikow			
  			coefs_split.addElement(d); // zapisuje wspolczynnik do vectora
  		}catch(Exception e){ //jesli nie liczba to wyskoczy wyjątek i pominie
  			continue;
  		}
  		numCoefs++; //mam kolejny wspolczynnik, inaczej zwiekszyla sie liczba wpolczynnikow, liczy ile jest wspolczynnikow np "1 2 5 6" to numCoefs=4
  	}

  	if(numCoefs == 0){ //jesli mam zero wspolczynnik to:
  		this.coefficients = new double[1]; //musze miec cos w tablicy bo wywali wyjątek wiec robie tablice jedno elementowa
  		this.coefficients[0] = 0; //moj jedyny wspolczynnik to zero
  		return; //konczymy
  	}
  	this.coefficients = new double[numCoefs]; //tworze tablice o znanje liczbie wspolczynnikow do dalszego uzytku
  	for(int i=0; i<numCoefs; i++){ //przepisuje wspolczynniki z vectora do tablicy coef
  		Double d = (Double)(coefs_split.elementAt(i)); //pobieram kolejny element z vecotora i zapisuje do tablicyw odwrotnej kolejniosci    		
  		this.coefficients[numCoefs - i - 1] = d.doubleValue(); //zmieniam kolejnosc pirwszy jest najbardziej znaczacy
  	}
 }

  /**
   * Evaluates a polynomial using the given coefficients.
   *
   * @param x
   * @param coeff the polynomial coefficients.
   */
  public static double evalPolynomial(final double x, final double[] coeff) {
    int n = coeff.length-1;
    double y = coeff[n];
    for(int i = n-1;i>=0;i--) {
      y = coeff[i]+(y*x);
    }
    return y;
  }

  /**
   *
   * @param r double    number added to the polynomial.
   * @return Polynomial
   */
  public Polynomial add(double r) {
    int n = coefficients.length;
    double coef[] = new double[n];
    coef[0] = coefficients[0]+r;
    for(int i = 1;i<n;i++) {
      coef[i] = coefficients[i];
    }
    return new Polynomial(coef);
  }

  /**
   * Adds the given polynomial to this polynomial.
   * @param p Polynomial
   * @return Polynomial
   */
  public Polynomial add(Polynomial p) {
    int n = Math.max(p.degree(), degree())+1;
    double[] coef = new double[n];
    for(int i = 0;i<n;i++) {
      coef[i] = coefficient(i)+p.coefficient(i);
    }
    return new Polynomial(coef);
  }

  /**
   * Gets the coefficient value at the desired position
   * @param n int    the position of the coefficient to be returned
   * @return double the coefficient value
   * @version 1.2
   */
  public double coefficient(int n) {
    return n<coefficients.length ? coefficients[n] : 0;
  }

  /**
   * Gets the degree of this polynomial function.
   * @return int degree of this polynomial function
   */
  public int degree() {
    return coefficients.length-1;
  }

  /**
   * Gets the derivative of this polynomial.
   * @return Polynomial the derivative.
   */
  public Polynomial derivative() {
    int n = degree();
    if(n==0) {
      double coef[] = {0};
      return new Polynomial(coef);
    }
    double coef[] = new double[n];
    for(int i = 1;i<=n;i++) {
      coef[i-1] = coefficients[i]*i;
    }
    return new Polynomial(coef);
  }

  /**
   * Divides this polynomial by a constant.
   * @param r double
   * @return Polynomial
   */
  public Polynomial divide(double r) {
    return multiply(1/r);
  }

  /**
   * Divides this polynomial by another polynomial.
   *
   * The remainder is dropped.
   *
   * @param p Polynomial
   * @return Polynomial
   */
  public Polynomial divide(Polynomial p) {
    return divideWithRemainder(p)[0];
  }

  /**
   * Divides this polynomial by another polynomial.
   *
   * @param p polynomial
   * @return polynomial array containing the answer and remainder
   */
  public Polynomial[] divideWithRemainder(Polynomial p) {
    Polynomial[] answer = new Polynomial[2];
    int m = degree();
    int n = p.degree();
    if(m<n) {
      double[] q = {0};
      answer[0] = new Polynomial(q);
      answer[1] = p;
      return answer;
    }
    double[] quotient = new double[m-n+1];
    double[] coef = new double[m+1];
    for(int k = 0;k<=m;k++) {
      coef[k] = coefficients[k];
    }
    double norm = 1/p.coefficient(n);
    for(int k = m-n;k>=0;k--) {
      quotient[k] = coef[n+k]*norm;
      for(int j = n+k-1;j>=k;j--) {
        coef[j] -= quotient[k]*p.coefficient(j-k);
      }
    }
    double[] remainder = new double[n];
    for(int k = 0;k<n;k++) {
      remainder[k] = coef[k];
    }
    answer[0] = new Polynomial(quotient);
    answer[1] = new Polynomial(remainder);
    return answer;
  }

  /**
   * Multipiels this polynomial by a constant.
   * @param r double
   * @return Polynomial
   */
  public Polynomial multiply(double r) {
    int n = coefficients.length;
    double coef[] = new double[n];
    for(int i = 0;i<n;i++) {
      coef[i] = coefficients[i]*r;
    }
    return new Polynomial(coef);
  }

  /**
   * Multiplies this polynomial by another polynomial.
   * @param p Polynomial
   * @return Polynomial
   */
  public Polynomial multiply(Polynomial p) {
    int n = p.degree()+degree();
    double[] coef = new double[n+1];
    for(int i = 0;i<=n;i++) {
      coef[i] = 0;
      for(int k = 0;k<=i;k++) {
        coef[i] += p.coefficient(k)*coefficient(i-k);
      }
    }
    return new Polynomial(coef);
  }
  
  /**
   * Substracts a constant from this polynomial.
   *
   * @param r the constant
   * @return Polynomial
   */
  public Polynomial subtract(double r) {
    return add(-r);
  }

  /**
   * Subtracts another polynomial from this polynomial.
   * @return Polynomial
   * @param p Polynomial
   */
  public Polynomial subtract(Polynomial p) {
    int n = Math.max(p.degree(), degree())+1;
    double[] coef = new double[n];
    for(int i = 0;i<n;i++) {
      coef[i] = coefficient(i)-p.coefficient(i);
    }
    return new Polynomial(coef);
  }

  // zamieniam wielomian na stringa wersja reprezentatywna np.: 5x^3 + 6x^2...
  public String toString() {
	  int deg = this.degree();
      if (deg ==  0) return "" + doubleToString(coefficients[0], 2);
      if (deg ==  1) return doubleToString(coefficients[1], 2) + "x + " + doubleToString(coefficients[0], 2);
      String s = doubleToString(coefficients[deg], 2) + "x^" + deg;
      for (int i = deg-1; i >= 0; i--) {
          if      (coefficients[i] == 0) continue;
          else if (coefficients[i]  > 0) s = s + " + " + doubleToString( coefficients[i], 2);
          else if (coefficients[i]  < 0) s = s + " - " + doubleToString(-coefficients[i], 2);
          if      (i == 1) s = s + "x";
          else if (i >  1) s = s + "x^" + i;
      }
      return s;
  }

  /**
   * Evaluates the polynomial for the specified variable value.
   * @param x double    value at which the polynomial is evaluated
   * @return double polynomial value.
   */
  public double evaluate(double x) {
    int n = coefficients.length;
    double answer = coefficients[--n];
    while(n>0) {
      answer = answer*x+coefficients[--n];
    }
    return answer;
  }
  
  // zmieniam wspolczynniki wielomianu na stringa np.: 1 2 3 4
  public String coefsToString() {
	  int deg = this.degree();
      if (deg ==  0) return "" + doubleToString(coefficients[0], 2);
      if (deg ==  1) return doubleToString(coefficients[1], 2) + "" + doubleToString(coefficients[0], 2);
      String s = doubleToString(coefficients[deg], 2) + "";
      for (int i = deg-1; i >= 0; i--) {
          if      (coefficients[i] == 0) continue;
          else if (coefficients[i]  > 0) s = s + " " + doubleToString(coefficients[i], 2);
          else if (coefficients[i]  < 0) s = s + " -" + doubleToString(-coefficients[i], 2);
      }
      return s;
  }

//J2me nie ma klasy Scanner wiec trzeba zaimplementowac wlasny split
  public static String[] splitStringToPolynomial(String splitStr, String delimiter) {  
      StringBuffer token = new StringBuffer();  
      Vector tokens = new Vector();  
      // split  
      char[] chars = splitStr.toCharArray();  
      for (int i=0; i < chars.length; i++) {  
          if (delimiter.indexOf(chars[i]) != -1) {  
              // we bumbed into a delimiter  
              if (token.length() > 0) {  
                  tokens.addElement(token.toString());  
                  token.setLength(0);  
              }  
          } else {  
              token.append(chars[i]);  
          }  
      }  
      // don't forget the "tail"...  
      if (token.length() > 0) {  
          tokens.addElement(token.toString());  
      }  
      // convert the vector into an array  
      String[] splitArray = new String[tokens.size()];  
      for (int i=0; i < splitArray.length; i++) {  
          splitArray[i] = (String) tokens.elementAt(i);  
      }  
      return splitArray;  
  } 
  
  public static String doubleToString(double num, int numDecim){
	    long p=1;
	    for(int i=0; i<numDecim; i++)p*=10;
	    return ""+((double)(int)(p * num + 0.5) / p);
	}
}
