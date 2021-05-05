# By: Thomas Welborn (tdw058)
# File: FindingRoots.py
# Description: This program finds the roots of a polynomial using various methods.
# Date Created: 10/15/19


def makeDeriv(exponentsList, coefsList):
  derivCoefsList = []
  for i in range (0, (len(coefsList) - 1)):
    derivCoefsList.append(exponentsList[i] * coefsList[i])
  return derivCoefsList


def evalHorner(coefsList, x):
  xVal = x
  tempResult = 0
  tempResult += coefsList[0]
  for i in range (1, len(coefsList)):
    tempResult = tempResult * xVal + coefsList[i]
  return tempResult


def evalBisection(coefsList, rangesList):
  iterationsMax = 52
  a = rangesList[0]
  b = rangesList[1]
  mid = 0.0
  func_mid = 0.0
  func_a = 0.0

  for i in range (0, iterationsMax):
    mid = (a+b) / 2
    func_mid = evalHorner(coefsList, mid)
    func_a = evalHorner(coefsList, a)

    if ( (func_mid > 0 and func_a < 0) or (func_mid < 0 and func_a > 0) ):
      b = mid
    else:
      a = mid

    print("New interval [" + str(a) + "... " + str(b) + "]")

  print("The approximate solution found using the Bisection method", end = " ")
  print("after 52 iterations is " + str((a+b)/2) + ".")
  return


def evalNewton(coefsList, derivCoefsList, rangesList):
  userFlag = False
  interationMax = 50
  index = 0
  epsilon = 0.000000000000001
  nextX = 0
  currX = (rangesList[0] + rangesList[1]) / 2

  if (currX == 0):
    currX = .00000000001

  derivCoefs = []
  derivCoefs = derivCoefsList
  attemptCount = 0

  while (not userFlag and attemptCount < 7):
    print("The starting x0 is " + str(currX))
    for i in range(0, 50):
      nextX = currX - ( evalHorner(coefsList, currX) / evalHorner(derivCoefs, currX))
      if ( (((nextX - currX <= epsilon) and (nextX - currX >= 0)) or ((currX - nextX <= epsilon) and (currX - nextX >= 0))) and (not userFlag) ):
        userFlag = True
        print("Your root was found on iteration " + str(i) + " to be " + str(currX))
      currX = nextX
    if (not userFlag):
      print("Unfortunately no root was found with the original x0. Beginning evaluation.")
      currX = (rangesList[0] + rangesList[1]) / nextX
      attemptCount += 1

  return
    
  
  










def main():

  coefList = []
  exponentList = []
  done = "N"
  while (done != "Y"):
    try:
      coef = int (input ("Enter a coefficient: "))
      exponent = int (input ("Enter an exponent: "))
      done = input("Done (Y/N): ")
      coefList.append(coef)
      exponentList.append(exponent)
    except:
      exit

  done = "N"
  rangeList = []
  rangeLow = 0
  rangeHigh = 0
  while (done != "Y"):
    rangeLow = int (input ("Enter the lowest value for your desired range: "))
    rangeHigh = int (input ("Enter the highest value for your desired range: "))
    done = input("Done (Y/N): ")
  rangeList.append(rangeLow)
  rangeList.append(rangeHigh)
    
  for i in range (0, len(coefList)):
    print(str(coefList[i]) + "x^" + str(exponentList[i]), end = " ")

  print("\n")
  print("We will now evaluate your polynomial using Horner's method", end = " ")
  print("at x=2.")
  print("The calculated value is " + str(evalHorner(coefList, 2)))
  print("We will now evaluate your polynomial's derivative using Horner's method", end = " ")
  print("at x=2.")
  meQuickDerivList = makeDeriv(exponentList, coefList)
  print("The calculated value is " + str(evalHorner(meQuickDerivList, 2)))

  print(" ")

  print("We will now evaluate the root of your polynomial using Bisection method.")
  evalBisection(coefList, rangeList)
  print(" ")

  print("We will now evaluate the root of your polynomial using Newton's method.")
  evalNewton(coefList, meQuickDerivList, rangeList)
main()
