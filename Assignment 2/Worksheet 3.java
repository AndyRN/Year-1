/// Q1.

while(x != 0){
	x = x - 1;
}


/// Q2.

// x = y
x = 0;
n = 0;

while(y != 0) {
	x = x + 1;		<< x = y
	n = n + 1;		<< n = y
	y = y - 1;		<< y = 0
} // sets 'x' and 'n' to the same as 'y'

while(n != 0) {
	y = y + 1;		<< y restored from 'n'
	n = n - 1;		<< n = 0
} // 'y' restored from 'n'

// x = x + y
n = 0;

while(y != 0) {
	x = x + 1;		<< x = x + y
	n = n + 1;		<< n = y
	y = y - 1;		<< y = 0
} // sets 'x' and 'n' to the same as 'y'

while(n != 0) {
	y = y + 1;		<< y = n
	n = n - 1;		<< n = 0
} // 'y' restored from 'n'

// x = x - y
n = 0;
m = 0;

while(y != 0) {
	n = n + 1;		<< n = y
	m = m + 1;		<< m = y
	y = y - 1;		<< y = 0
} // sets 'n' and 'm' to the same as 'y'

while(n != 0) {
	y = y + 1;		<< y = n
	n = n - 1;		<< n = 0
} // 'y' restored from 'n'

while(m != 0) {
	x = x - 1;		<< x = x - y
	m = m - 1;		<< m = 0
} // takes the value of 'y' from 'x' using 'm's value


/// Q3.

n = x;
while(n != 0) {		<< if
	// (*statements*)
	n = 0;
} // will only execute once


/// Q4.

n = x;
while(n != 0) {		<< if
	// (*statements*)
	n = 0;
	m = 0;	// stops the other loop from executing
} // will only execute once
while(m != 0) {		<< else
	// (*statements*)
	m = 0;
} // if previous loop didn't execute, this runs once


/// Q5.

n = x;	// makes a copy of the original value of 'x'
x = 0;	// ready so 'y' can be put into it any amount of times
z = y;	// makes a copy of 'y' to retain original value
if(y != 0) {
	while(z != 0) {
		x = x + n;
		z = z - 1;
	} // ends when multiplication is complete
} // if 'y' is 0 then 'x' will remain 0 too


/// Q6.

n = y; // makes a copy of 'y' so 'n' can be used as the counter.
if(n > x){ 
	x = 0; // if the denominator is greater than the numerator, answer will always be 0.
} else if(n == x) { 
	x = 1; // if they are equal, answer will always be 1.
} else {
	z = 1; // amount of times 'y' goes into 'x'.
	while(x != z) {
		n = n + y; // this represents the value after 'y' has been added to itself.
		z = z + 1; // increments number of multiplications 
		if(n = x) {
			x = z; // sets 'x' to the answer of the integer division.
		} else if(n > x) {
			z = z - 1; // for integer divison.
			x = z; // sets 'x' to the answer of the integer division.
		}
	} // stops once 'x' has been set to the amount of multiplications.
} // end.

/// Q7.

5 basic operations



