
// Starter code for lp1.

// Change following line to your group number
// Changed type of base to long: 1:15 PM, 2017-09-08.

import java.util.Iterator;
import java.util.ArrayList;
import java.util.ListIterator;

public class Num implements Comparable<Num>, Iterable<Long> {

	static long defaultBase = 10; // This can be changed to what you want it to
									// be.
	long base = defaultBase; // Change as needed
	private boolean isNegative = false;
	ArrayList<Long> list;
	
	/* Start of Level 1 */
	//Method that adds digits in a String to List.
	void AddStringToList(String s) throws Exception {
		list = new ArrayList<Long>();
		Num numB10 = new Num();
		numB10.setBase((long)10);
		
		for (int i = s.length() - 1; i > 0; i--) {
			numB10.getList().add(Long.parseLong("" + s.charAt(i)));

		}
		if (s.charAt(0) == '-') {
			numB10.setIsNegative(true);
		} else {
			numB10.setIsNegative(false);
			numB10.getList().add(Long.parseLong("" + s.charAt(0)));
		}
		AddStringToListUtil(numB10);
	}

	//
	void AddStringToListUtil(Num numB10) throws Exception{
		setIsNegative(numB10.getIsNegative());
		Num zero = new Num();
		zero.setBase((long)10);
		zero.convertToBase(0);
		Num baseB10 = new Num();
		baseB10.setBase((long)10);
		baseB10.convertToBase(base());
			if(zero.compareTo(numB10)== 0)
			list.add((long) 0);
		while(zero.compareTo(numB10)!= 0){
			list.add(mod(numB10, baseB10).getList().get(0));
			numB10 = divide(numB10, baseB10);
		}
	}
	Num(String s) throws Exception {
		AddStringToList(s);
	}

	// copy constructor
	Num(Num num) {
		this.base = num.base;
		this.list = new ArrayList<Long>(num.list);
		this.isNegative = num.isNegative;
	}

	Num() {
		list = new ArrayList<Long>();
	}

	//Method that converts a long number to the given base and puts the number in the list
	void convertToBase(long x) {
		list = new ArrayList<Long>();
		if (x == 0) {
			list.add(new Long(0));
			return;
		}
		if (x < 0) {
			isNegative = true;
			x = -x;
		}
		while (x != 0) {
			list.add(x % base);
			x = x / base;
		}
	}

	Num(long x) {
		convertToBase(x);
	}

	void setBase(Long base) {
		this.base = base;
	}

	void setIsNegative(boolean isNegative) {
		this.isNegative = isNegative;
	}

	boolean getIsNegative() {
		return isNegative;
	}

	/*
	 * Performs sum of two numbers
	 */
	static Num add(Num a, Num b) {
		//Checking the cases of a and b both not being positive and negative
		if (!((a.getIsNegative() && b.getIsNegative()) || (!a.getIsNegative() && !b.getIsNegative()))) {
			//Case if a is negative and b is positive
			if (a.isNegative) {
				Num temp = new Num(a);
				temp.setIsNegative(false);
				return subtract(b, temp);
			} 
			//case if a is positive and b is negative
			else {
				Num temp = new Num(b);
				temp.setIsNegative(false);
				return subtract(a, temp);
			}
		}
		Num outNum = new Num();
		outNum.setIsNegative(a.getIsNegative());
		outNum.setBase(a.base());
		long carry = 0;
		Iterator<Long> runner1 = a.iterator();
		Iterator<Long> runner2 = b.iterator();
		while (carry != 0 || runner1.hasNext() || runner2.hasNext()) {
			long itr1Val = getValue(runner1);
			long itr2Val = getValue(runner2);
			long sum = itr1Val + itr2Val + carry;
			outNum.list.add(sum % a.base());
			carry = sum / a.base();
		}
		return outNum;
	}
	static long getValue(Iterator<Long> it) {
		return it.hasNext() ? it.next() : 0;
	}
    /*
     * Performs subtraction of two numbers
     */
	static Num subtract(Num a, Num b) {
		Num outNum = new Num();
		outNum.setBase(a.base());
		//Checks if either of a and b is positive and the other is negative 
		if (a.getIsNegative() ^ b.getIsNegative()) {	
			//Case if a is negative and b is positive
			if (a.getIsNegative()) {
				Num temp = new Num(a);
				temp.setIsNegative(false);
				outNum = add(temp, b);
				outNum.setIsNegative(true);
				return outNum;
			} 
			//Case if a is positive and b is negative
			else {
				Num temp = new Num(b);
				temp.setIsNegative(false);
				return add(a, temp);
			}
		} 
		//Case if both a and b are negative
		else if (a.getIsNegative() && b.getIsNegative()) {		
			Num temp1 = new Num(a);
			temp1.setIsNegative(false);
			Num temp2 = new Num(b);
			temp2.setIsNegative(false);	
			//Checks if |a| is greater or lesser than |b| and subtracts the lesser one from larger
			if(temp1.compareTo(temp2)<=0)
			{
				outNum=subtract(temp2,temp1);
			}
			else
			{
				outNum=subtract(temp1,temp2);
				outNum.setIsNegative(true);
			}
			return outNum;
		}
		
		long borrow = 0;
		if (a.compareTo(b) < 0) {
			outNum = subtract(b, a);
			outNum.setIsNegative(true);
			return outNum;
		}
		Iterator<Long> iteratorA = a.iterator();
		Iterator<Long> iteratorB = b.iterator();
		long operand1 = getValueSub(iteratorA);
		long operand2 = getValueSub(iteratorB);
		long subtract = 0;
		while (operand1 != -1) {
			if (operand2 == -1) {
				operand2 = 0;
			}
			operand1 -= borrow;
			if (operand1 < operand2) {
				subtract = operand1 + a.base() - operand2;
				borrow = 1;
			} else {
				subtract = operand1 - operand2;
				borrow = 0;
			}
			outNum.list.add(subtract);
			operand1 = getValueSub(iteratorA);
			operand2 = getValueSub(iteratorB);
		}
		outNum.trimZeros();
		return outNum;
	}

	static long getValueSub(Iterator<Long> it) {
		return it.hasNext() ? it.next() : -1;
	}

	// Implement Karatsuba algorithm for excellence credit
	static Num product(Num a, Num b) {
		if (a.getList().size() == 1 && b.getList().size() == 1) {
			long num1 = a.getList().get(0);
			long num2 = b.getList().get(0);
			Num val = new Num();
			val.setBase(a.base());
			val.convertToBase(num1 * num2);
			return val;
		}
		long maxSize = Math.max(a.getList().size(), b.getList().size());
		int split = (int) maxSize / 2;
		int counter = 0;
		Iterator<Long> iteratorA = a.iterator();
		Iterator<Long> iteratorB = b.iterator();
		Num x0 = new Num();
		Num x1 = new Num();
		Num y0 = new Num();
		Num y1 = new Num();
		x0.setBase(a.base());
		x1.setBase(a.base());
		y0.setBase(a.base());
		y1.setBase(a.base());
		while (iteratorA.hasNext() || iteratorB.hasNext()) {
			long itr1Val = getValue(iteratorA);
			long itr2Val = getValue(iteratorB);
			if (counter < split) {
				x0.getList().add(itr1Val);
				y0.getList().add(itr2Val);
				counter++;
			} else {
				x1.getList().add(itr1Val);
				y1.getList().add(itr2Val);
			}
		}
		Num z0 = product(x0, y0);
		Num z2 = product(x1, y1);
		Num z1 = subtract(subtract(product(add(x0, x1), add(y0, y1)), z2), z0);

		Num value = add(add(leftShift(z2, 2 * split), leftShift(z1, split)), z0);
		value.trimZeros();
		if ((a.getIsNegative() && b.getIsNegative()) || (!a.getIsNegative() && !b.getIsNegative())) {
			value.setIsNegative(false);
		} else {
			value.setIsNegative(true);
		}
		return value;
	}
    //Method that trims leading zeroes in the list
	void trimZeros() {
		for (int i = this.getList().size() - 1; i >= 1; i--) {
			if (this.getList().get(i) == 0) {
				this.getList().remove(i);
			} else {
				break;
			}
		}
	}
	//Method that performs power using divide and conquer
	static Num power(Num a, long n) {
		Num out = new Num(1);
		out.setBase(a.base());
		if (n == 0) {
			return out;
		}
		if (n == 1) {
			return a;
		} else {
			out = power(a, n / 2);
			if (n % 2 == 0)
				return product(out, out);
			else
				return product(product(a, out), out);
		}
	}
    //Method that does leftshift of digits
	static Num leftShift(Num number, int numOfShifts) {
		ArrayList<Long> temp = new ArrayList<>();
		for (int i = 0; i < numOfShifts; i++) {
			temp.add((long) 0);
		}
		number.getList().addAll(0, temp);
		return number;
	}
	/* End of Level 1 */

	/* Start of Level 2 */
	//Method that performs division on two Num objects
	static Num divide(Num a, Num b) throws Exception {
		final Num zero = new Num();
		zero.setBase(a.base);
		zero.convertToBase(0);

		final Num one = new Num();
		one.setBase(a.base);
		one.convertToBase(1);

		Num outNum = new Num();
		outNum.base = a.base;

		Num dividend = new Num(a);
		Num divisor = new Num(b);
     
		if (dividend.isNegative || divisor.isNegative) {
			if (dividend.isNegative && divisor.isNegative) {
				dividend.isNegative = false;
				divisor.isNegative = false;
			}
			else if (dividend.isNegative) {
				dividend.isNegative = false;
				outNum.isNegative = true;
			} else {
				divisor.isNegative = false;
				outNum.isNegative = true;
			}
		}

		if (dividend.compareTo(divisor) == -1)
			return zero;
		else if (divisor.compareTo(zero) == 0)
			throw new Exception("Division by 0 is not possible");
		Num range = new Num(dividend);

		outNum.list = searchQuotient(dividend, divisor, zero, range, one).list;
		return outNum;
	}

	static Num searchQuotient(Num dividend, Num divisor, Num left, Num right, Num one) {
		Num mid;
		if (right.list.size() == 1) {

			long middle = (left.list.get(0) + right.list.get(0)) / 2;
			mid = new Num();
			mid.setBase(dividend.base);
			mid.convertToBase(middle);

		} else {
			mid = add(left, right);
			mid = halfOf(mid, one);

		}
		int condition1 = product(mid, divisor).compareTo(dividend);
		int condition2 = product(add(mid, one), divisor).compareTo(dividend);
		if (condition1 <= 0 && condition2 > 0)
			return mid;

		if (condition1 > 0)
			return searchQuotient(dividend, divisor, left, subtract(mid, one), one);
		else
			return searchQuotient(dividend, divisor, add(mid, one), right, one);

	}

	static Num halfOf(Num a, Num one) {
		long base = a.base;
		Num divisor = new Num();
		divisor.setBase(base);
		divisor.convertToBase(2);

		Num zero = new Num();
		zero.setBase(base);
		zero.convertToBase(0);

		Num range = new Num();
		range.setBase(base);
		range.convertToBase(base - 1);

		Num quotient = new Num();
		quotient.setBase(a.base);

		Num operand = new Num();
		operand.setBase(a.base);

		ListIterator<Long> it = a.list.listIterator(a.list.size());
		while (it.hasPrevious()) {
			operand.list.add(0, it.previous());
			operand.trimZeros();
			int comparison = operand.compareTo(divisor);
			if (comparison >= 0) {
				Num digit = searchQuotient(operand, divisor, zero, range, one);
				quotient.list.add(0, digit.list.get(0));
				operand = subtract(operand, product(divisor, digit));
				operand.trimZeros();
			} else {
				quotient.list.add(0, new Long(0));
			}
		}
		quotient.trimZeros();
		return quotient;
	}
    //Method that performs % of two numbers (remainder)
	static Num mod(Num a, Num b) throws Exception {
		return subtract(a, product(divide(a, b), b));
	}
	//Method that performs power on two NUm objects using divide and conquer
	static Num power(Num a, Num n) {
		if (n.getList().size() == 0) {
			return new Num(1);
		}
		return product(power(a, n.getList().get(0)), power(power(a, rightShift(n, 1)), n.base()));
	}
	//Method that performs square root on a number
	static Num squareRoot(Num a) throws Exception {
		if (a.getIsNegative())
			throw new Exception("Square root of -ve numbers is not defined");
		final Num one = new Num();
		one.setBase(a.base);
		one.convertToBase(1);
		Num left = one;
		Num right = new Num(a);
		Num mid = right;
		int condition1 = product(right, right).compareTo(a);
		int condition2 = product(add(right, one), add(right, one)).compareTo(a);
		while (!(condition1 <= 0 && condition2 > 0)) {
			if (condition1 > 0) {
				right = subtract(mid, one);
			} else {
				left = add(mid, one);
			}
			mid = add(left, right);
			mid = halfOf(mid, one);
			condition1 = product(mid, mid).compareTo(a);
			condition2 = product(add(mid, one), add(mid, one)).compareTo(a);
		}
		return mid;
	}
	//Method that shifts the number to right by putting zeroes
	static Num rightShift(Num num, int numOfShifts) {
		while (numOfShifts != 0) {
			num.getList().remove(0);
			numOfShifts--;
		}
		return num;
	}

	/* End of Level 2 */

	// Utility functions
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise
	public int compareTo(Num other) {
		this.trimZeros();
		other.trimZeros();
		int val = this.getIsNegative() && other.getIsNegative() ? -1
				: !this.getIsNegative() && !other.getIsNegative() ? 1 : 0;
		if (val == -1) {
			if (this.getList().size() < other.getList().size()) {
				return 1;
			} else if (this.getList().size() > other.getList().size()) {
				return -1;
			} else {
				if (compareList(other.getList()) == 0) {
					return 0;
				} else if (compareList(other.getList()) == -1) {
					return 1;
				} else {
					return -1;
				}
			}
		} else if (val == 1) {
			if (this.getList().size() < other.getList().size()) {
				return -1;
			} else if (this.getList().size() > other.getList().size()) {
				return 1;
			} else {
				if (compareList(other.getList()) == 0) {
					return 0;
				} else if (compareList(other.getList()) == -1) {
					return -1;
				} else {
					return 1;
				}
			}
		} else {
			if (other.getIsNegative()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public int compareList(ArrayList<Long> otherList) {
		int size = otherList.size() - 1;
		while (size >= 0) {
			if (otherList.get(size) < list.get(size)) {
				return 1;
			} else if (otherList.get(size) > list.get(size)) {
				return -1;
			}
			size -= 1;
		}
		return 0;
	}

	ArrayList<Long> getList() {
		return list;
	}

	//Method that compares the absolute values of the numbers
	public int compareAbsVal(Num other) {
		Num temp = new Num(other);
		temp.setIsNegative(false);
		Num temp1 = new Num(this);
		temp1.setIsNegative(false);
		return temp1.compareTo(temp);
	}

	// Output using the format "base: elements of list ..."
	// For example, if base=100, and the number stored corresponds to 10965,
	// then the output is "100: 65 9 1"
	void printList() {
		ListIterator<Long> listIterator = list.listIterator();
		StringBuilder output = new StringBuilder(base + ":");
		while (listIterator.hasNext()) {
			output.append(listIterator.next() + " ");
		}
		System.out.println(output);
	}

	// Returns number to a string in base 10
	public String toString() {
		Num base_ = new Num();
		base_.setBase((long) 10);
		base_.convertToBase(this.base);
		ListIterator<Long> itr = list.listIterator(list.size());
		Num listInBase10 = new Num();
		listInBase10.setBase((long) 10);
		Num result = new Num();
		result.setBase((long) 10);
		result.convertToBase(0);
		while (itr.hasPrevious()) {
			listInBase10.convertToBase(itr.previous());
			result = add(product(result, base_), listInBase10);
		}
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Long> itr1 = result.iterator();
		while (itr1.hasNext()) {
			stringBuilder.insert(0, itr1.next());
		}
		if (this.isNegative) {
			stringBuilder.insert(0, "-");
		}
		return stringBuilder.toString();
	}

	public long base() {
		return base;
	}

	@Override
	public Iterator<Long> iterator() {
		return list.iterator();
	}
}
