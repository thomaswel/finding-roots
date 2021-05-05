// Created by Thomas Welborn (tdw058) on 09/27/19
// COSC 3312 Dr. Cooper Section T/Th 2-3:30
// Programming Assignment 1 - Finding Roots
// Due 10/15/19

/*
Assignment Instructions:

Write code to find the roots of arbitrary polynomials.  The coefficients
of the polynomial should be stored in an List (Java or Python), or an Integer
array (C).  The coefficients should be user supplied and program validated.
An interval [a,b] should also be user defined and validated.

Given the coefficients for the polynomial p(x):

Calculate the the root of p(x) within [a,b] using the bisection method
Calculate the root of p(x) using Newton's method with x0=(a+b)/2
Use Horner's method to calculate p(x) and p'(x) efficiently
Handle all errors:

What will you do if the root doesn't exist?  How will you test for that?
Don't get caught in infinite loops
How will you terminate the algorithm?
How can you achieve the highest possible precision?
Bonus Points:  Implement in all three languages.

The due date for this assignment is Thursday October 10th at midnight.
*/


/**
The purpose of this program is to take a user provided arbitrary polynomial
denoted as p(x) and user provided interval denoted as [a,b]. Then calculate
the root of p(x) within range [a,b] using bisection method, calculate
the root of p(x) using Newton's method, and calculate p(x) and p'(x) using Horner's method.
*/


// Start program
import java.util.Scanner;   // for Scanner Class
import java.util.ArrayList; // for ArrayList Class
import java.io.*;           // for File Class
import java.util.Arrays;    // for printing 2-d arrays
import java.lang.Integer;   // for parsing integers
import java.lang.Double;    // for parsing doubles

public class FindingRoots
{

  public static void main (String[] args) throws IOException
  {
	boolean userDone = true;
	String tempUserInput = "";
	Scanner keyboard = new Scanner(System.in);

	/*
	For the purpose of testing this program, we will use the following random three polynomials that have
	already been graphed and confirmed to have exactly one simple root within the given range.

	Test Case 1:
	  p(x) = 3x^3 - 2x^2 - 10x - 10
	  Range: [-5, 6]
	  The simple root is located at x = 2.517
	Test Case 2:
	  p(x) = -7x^5 + 2x^3 - 24
	  Range: [-4, 8]
	  The simple root is located at x = -1.326
	Test Case 3:
	  p(x) = -30x^3 + 2x^2 + 50
	  Range: [-10, 11]
	  The simple root is located at x = 1.208
	*/

    do
	{

	  // Make polynomial
	  int[][] myPoly = makeArray();
	  System.out.println("The polynomial you have successfully created is " + polyToString(myPoly) + ".");

      // Find derivative of made polynomial
	  int[][] myDeriv = makeDeriv(myPoly);
	  System.out.println("The derivative of your polynomial was found to be " + polyToString(myDeriv) + ".");

      // Make range
	  int[] myRange = makeRange();
	  System.out.println("The range you have successfully made is " + Arrays.toString(myRange) + ".");

	  // Evaluate p(x) and p'(x) with Horner's method
	  System.out.println("We will demonstrate Horner's method by evaluating your polynomial p(x) and ");
	  System.out.println("derivative p'(x) at x = 2.");
	  double myHornerPoly = evalHorner(myPoly, 2.0);
	  System.out.println("The value of your polynomial " + polyToString(myPoly) + " was found to be " + myHornerPoly + ".");
	  double myHornerDeriv = evalHorner(myDeriv, 2.0);
	  System.out.println("The value of your derivative " + polyToString(myDeriv) + " was found to be " + myHornerDeriv + ".");

	  // Evaluate the root of p(x) with Bisection method within range
	  System.out.println("We will now use the Bisection method to find the root of your polynomial within your givin range.");
	  if (testRootExists(myPoly, myRange))
	  {
	    System.out.println("A root was found to exist within the range. Continuing to Bisection method.");
        double myBisection = evalBisection(myPoly, myRange);
        System.out.println("Your root was found to be " + myBisection + " with the Bisection method.");
        // Evaluate the root of p(x) with Newton's method
		System.out.println("We will now use Newton's method to find the root of your polynomial.");
		double myNewton = evalNewton(myPoly, myRange);
        System.out.println("Your root was found to be " + myNewton + " with Newton's method.");
      }
      else
      {
	    System.out.println("Unfortunately no simple root was found to exist within the given range. Unable to complete Bisection method");
	    System.out.println("or Newton's method.");
      }

      // Let user decide if program ends.
      System.out.println("Do you wish to evaluate another polynomial? (y/n): ");
      tempUserInput = keyboard.nextLine();
      if (tempUserInput.equals("Y") || tempUserInput.equals("y")) userDone = false;
      else userDone = true;
    } while(!userDone); // end do while
  } // end main method





  /**
  The makeArray method takes in user input to retrieve the highest power of their polynomial. This
  is important because once the highest power is known, the array to hold the polynomial can be declared
  and initialized. Then it will fill in each coefficient the user wants with each power.
  @param null
  @return A two-dimensional int array with (n) rows for each power and 2 columns, where "n" is the users highest power + 1
      and the first column is the power term, second row is the coefficent for that power term
  */
  public static int[][] makeArray() throws IOException
  {

    Scanner keyboard = new Scanner(System.in);
  	boolean userFlag = false;
  	int highestPow = 0;
  	String tempUserInput;

    do
    {
      userFlag = false;
      System.out.println("Please enter the highest power (integer) contained in your polynomial from 1 to 99: ");
  	  tempUserInput = keyboard.nextLine();

  	  try
  	  {
  	    highestPow = Integer.parseInt(tempUserInput);
      }//end try
      catch(Exception e)
      {
        System.out.println("NumberFormatException: You have entered an invalid number. Powers can only be integers. Try again.");
        continue;
      }//end catch

      System.out.println("Is the highest power " + highestPow + " correct? (enter y or n): ");
  	  if ( (keyboard.nextLine()).equals("y") || (keyboard.nextLine()).equals("Y") )
  	    userFlag = true;
  	  else
  	    userFlag = false;

    } while (highestPow < 1 || highestPow > 99 || !userFlag);
    // end do-while

    // Now that the highest power has been found, the array can be declared and initialized.
    // Then the method will return this array reference to the main method.
    // Column 1 of the int array holds the power, column 2 holds the coefficient
    int arraySize = highestPow + 1;
    int[][] myPowCoefArray = new int[arraySize][2];

    // Fill out the first column of the 2-d array with the powers from high to low
    int powerCount = highestPow;
    for (int i = 0; i < arraySize; i++)
    {
	  myPowCoefArray[i][0] = powerCount;
  	  powerCount--;
    }//end for

    System.out.println(Arrays.deepToString(myPowCoefArray));


    // Now the coefficients need to be retrieved from the user and placed into the
    // second column of the array.
    userFlag = false;
    int index = 0;
    int tempCoef = 0;
    System.out.println("Please enter the coefficient associated with the power displayed. If there is no coefficient enter 0.");

    do
    {
      System.out.println("Please enter the coefficient associated with x^" + myPowCoefArray[index][0] + " term: ");
  	  tempUserInput = keyboard.nextLine();

  	  try
  	  {
  	    tempCoef = Integer.parseInt(tempUserInput);
      }//end try
      catch(Exception e)
      {
        System.out.println("NumberFormatException: You have entered an invalid number. Remember this program only takes integers. Try again.");
        continue;
      }//end catch

      System.out.println("Is the coefficient " + tempCoef + " associated with x^" + myPowCoefArray[index][0] + " correct? (enter y or n): ");
  	  if ( (keyboard.nextLine()).equals("y") || (keyboard.nextLine()).equals("Y") )
  	  {
  		myPowCoefArray[index][1] = tempCoef;
  	    index++;
  	  }
  	  else
  	  {
  	    continue;
  	  }

      // exit condition
  	  if (index >= arraySize)
  	  {
  	    userFlag = true;
      }

      } while(!userFlag || index < arraySize); //end do-while

      System.out.println(Arrays.deepToString(myPowCoefArray));
      return myPowCoefArray;
  }//end makeArray method



  /**
  The makeRange method takes user input to create a range to test for polynomial roots within.
  @param null
  @return int[] integer range of length 2
  */
  public static int[] makeRange() throws IOException
  {
	Scanner keyboard = new Scanner(System.in);
	boolean userFlag = false;
	int[] myRange = new int[2];
	int low = 0;
	int high = 0;
	String tempLow = "";
	String tempHigh = "";

	System.out.println("We will now set up your desired integer range [a, b]");

    do
    {
      System.out.println("Please enter the lowest number in your desired range: ");
  	  tempLow = keyboard.nextLine();
  	  System.out.println("Please enter the highest number in your desired range: ");
  	  tempHigh = keyboard.nextLine();

  	  try
  	  {
  	    low = Integer.parseInt(tempLow);
  	    high = Integer.parseInt(tempHigh);
      }//end try
      catch(Exception e)
      {
        System.out.println("NumberFormatException - You have entered an invalid number. Range values can only be integers. Try again.");
        continue;
      }//end catch

      System.out.println("Is the range [" + low + ", " + high + "] correct? (enter y or n): ");
  	  if ( (keyboard.nextLine()).equals("y") || (keyboard.nextLine()).equals("Y") )
  	  {
  	    userFlag = true;
      }
  	  else
  	  {
  	    System.out.println("Remember range values can only be integers. Please try again.");
  	    userFlag = false;
      }

    } while (low >= high || !userFlag);
    // end do-while

    myRange[0] = low;
    myRange[1] = high;
    System.out.println(Arrays.toString(myRange));
    return myRange;

  } // end makeRange method




  //start make deriv method
  /**
  The makeDeriv method takes am int[][]polynomial and finds the derivative of it.
  @param int[][] poly
  @retrun int[][] the derivative of poly
  */
  public static int[][] makeDeriv(int[][] poly)
  {
    int derivLength = poly.length - 1;
    int[][] derivPoly = new int[derivLength][2];

    for (int i = 0; i < derivLength; i++)
    {
      derivPoly[i][0] = poly[i+1][0];
      derivPoly[i][1] = poly[i][0] * poly[i][1];
    }

    return derivPoly;
  }



  // start Horners evaluation method
  /**
  The evalHorner method evaluates a polynomial at a given x value.
  @param int[][] poly, double x
  @return double the evaluated y value at given x
  */
  public static double evalHorner(int[][] poly, double x)
  {
	double result = 0.0;
	result = result + poly[0][1];

	for (int i = 1; i < poly.length; i++)
	{
	  result = result * x + poly[i][1];
    }

    return result;
  }


  /**
  The testRootExists method tests if a simple root exists in a given range by taking small step sizes,
  and evaluating each x with Horner's method. Once a change in the sign is detected,
  it will return true that a simple root exists. Helper function for bisection and Newton
  @param int[][] poly, int[] range
  @return boolean
  */
  public static boolean testRootExists(int[][] poly, int[] range)
  {
	System.out.println("We will now test if a simple root exists by locating a change in the");
	System.out.println("sign of p(x) within your specified range.");
	// First test if the polynomial is just a constant, if it is return false
	if (poly.length == 1) return false;

	// If the constant is 0, then the polynomial will have a root at x=0
	if ( poly[poly.length-1][1] == 0 && range[0] <= 0 && range[1] >= 0 ) return true;

	// Set up needed variables for testing for a sign change
	// Sign strings will be "pos" and "neg"
	// If no sign change by the end of the range, then return false
	double currentX = range[0];
	double lastX = range[1];
	double currentY = evalHorner(poly, range[0]);
	String startingSign = " ";
	String currentSign = " ";

	if (currentY < 0) startingSign = "neg";
	else startingSign = "pos";


	double stepSize = 0.01;
	while (currentX <= lastX)
	{
	  currentY = evalHorner(poly, currentX);
	  System.out.println("The current x we are testing is " + currentX);
	  System.out.println("The current y we have calculated is " + currentY);
	  if (currentY == 0) return true;
	  else if (currentY > 0) currentSign = "pos";
	  else currentSign = "neg";

      if (currentSign != startingSign)
      {
		System.out.println("A simple root was found where p(x) changed sign.");
		System.out.println("The last interval tested where the sign change occured was");
		System.out.println("at x = " + currentX + ".");
        return true;
      }
      else
      {
		currentX += stepSize;
      }

    } // end while

    return false;
  } // end testRootExists method


  /**
  The evalBisection method evaluates the simple root of the given polynomial
  within the given range. This method is only carry out if the helper function testRootExists returns true
  @param int[][] poly, int[] range
  @return double
  */
  public static double evalBisection (int[][] poly, int[] range)
  {
    int iterationsMax = 52;
    double a = range[0];
    double b = range[1];
    double mid = 0.0;
    double func_mid = 0.0;
    double func_a = 0.0;

    for (int i = 0; i < iterationsMax; i++)
    {
	  mid = (a + b) / 2;
	  func_mid = evalHorner(poly, mid);
	  func_a = evalHorner(poly, a);

	  if ( (func_mid > 0 && func_a < 0) || (func_mid < 0 && func_a > 0) )
	  {
		b = mid;
      }
      else
      {
		a = mid;
      }

      System.out.println("New interval [" + a + " ... " + b + "]");

    }// end for

    System.out.println("Approximate solution after 52 iterations with the Bisection method is = " + (a+b)/2);
    return mid;

  } // end evalBisection method


  /**
  The evalNewton method evaluates the simple root of a given polynomial
  within the given range. Like the bisection method, only use this method if
  the helper function testRootExists returns true.
  @param int[][] poly, int[] range
  @return double
  */
  public static double evalNewton(int[][] poly, int[] range)
  {
	System.out.println("We will now evaluate the root of polynomial " + polyToString(poly) + " within the range " + Arrays.toString(range) + " using Newton's method.");
	boolean userFlag = false;
	int iterationMax = 50;
	int index = 0;
	double epsilon = 0.00000000000001;
	double nextX = 0.0;
	double currX = (range[0] + range[1]) / 2;
	// If the starting x is 0, set it to a small value close to 0 to avoid undefined in denominator
	if (currX == 0) currX = 0.000000000000001;
	int[][] deriv = makeDeriv(poly);
	int attemptCount = 0;

	// Do while the root is not close to the actual answer
    do
	{
	  System.out.println("The starting x0 is " + currX + ".");
	  try
	  {
	    for (int i = 0; i<50 && !userFlag; i++)
	    {
	      nextX = currX - ( evalHorner(poly, currX) / evalHorner(deriv,currX) );
	      System.out.println("The new x for iteration " + i + " is " + nextX + ".");



	      if ( ((nextX - currX <= epsilon) && (nextX - currX >= 0)) || ((currX - nextX <= epsilon) && (currX - nextX >= 0)) )
	      {
	  	    userFlag = true;
	  	    System.out.println("Your root was found on iteration " + i + ".");
          }

          currX = nextX;
        }
      } // end try
      catch (Exception e)
      {
		System.out.println("NaN was caught. Unfortunately the root is unable to be found with Newton's method.");
	    System.out.println("Beggining another evaluation with a different value for x0.");
		currX = (range[0] + range[1]) / (nextX);
		attemptCount += 1;
		continue;
      } // end catch

      if (!userFlag)
      {
		System.out.println("Unfortunately the original x0 did not result in a valid root.");
		System.out.println("Beggining another evaluation with a different value for x0.");
		currX = (range[0] + range[1]) / (nextX);
		attemptCount += 1;
	  }
    } while (!userFlag && attemptCount < 7);
    return currX;

  }// end evalNewton method



  /**
  The polyToString method converts an int[][] polynomail to string form
  @param int[][] poly
  @return string
  */
  public static String polyToString(int[][] poly)
  {
	String str = "";
	for (int i = 0; i < poly.length; i++)
	{
	  if (i == 0) str += "(" + poly[i][1] + "x^" + poly[i][0] + ")";
	  else str += "+(" + poly[i][1] + "x^" + poly[i][0] + ")";
    }
    return str;
  } // end polyToString method

} // end FindingRoots class