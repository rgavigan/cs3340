/*
 *  Name: Riley Emma Gavigan
 *  Student ID: 251150776
 *  UWO ID: rgavigan
 *  Assignment: CS 3340B Assignment 1
 *  Question: 7a)
 */

public class asn1_a {
    // Function to calculate fib-like numbers
    public static long recursiveFibLike(int n) {
        // Base case: return 2 for n = 0 and n = 1
        if (n <= 1) {
            return 2;
        }
        // Recursive case: Nn = N(n - 1) + N(n - 2)
        return recursiveFibLike(n - 1) + recursiveFibLike(n - 2);
    }

    public static void main(String args[]) {
        // Slower recursive function
        for (int i = 0; i <= 10; i++) {
            // Get i * 5, calculate it's fibonacci-like value, then print result
            int val = i * 5;
            long res = recursiveFibLike(val);
            System.out.println("N(" + val + "): " + res);
        }
    }
}
