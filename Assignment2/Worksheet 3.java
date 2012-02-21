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

x = 0;
x = x + 1;

while(x != 0) {
	// statements
	x = x - 1;
} // will only execute once and only if 'x' isn't 0


/// Q4.

x = 0;
x = x + 1;
n = 0;
n = n + 1;

while(x != 0) {
	// (*statements*)
	x = x - 1;
	n = n - 1;		<< stops the other loop from executing
} // will only execute once and only if 'x' isn't 0

while(n != 0) {
	// (*statements*)
	n = n - 1;
} // if previous loop didn't execute, this runs once


/// Q5.

n = 0;
m = 0;
c = 0;
c = c + 1;

while(c != 0) {
	m = m + 1;		<< m = x
	if(m == x) {
		c = c - 1;
	} // stops loop
} // sets the value of 'm' to be the same as 'x'
x = 0;		<< ready so 'y' can be put into it any amount of times

while(y != 0) {
	c = c + 1;
	while(c != 0) {
		n = n + 1;		<< n = m
		if(n == m) {
			c = c - 1;
		}
	} // copies value of 'm' into 'n'
	while(n != 0) {
		x = x + 1;
		n = n - 1;
	} // adds the original value of 'x' to 'x' from 'n'
	y = y - 1;
	z = z + 1;
} // ends when y multiplication is complete

c = c + 1;
while (c != 0) {
	y = y + 1;
	if (y == z) {
		c = c - 1;
	}
} // restores value of 'y' from 'z'


/// Q6.








