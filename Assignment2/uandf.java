/*
 * Name: Riley Emma Gavigan
 * Student #: 251150776
 * Student ID: rgavigan
 * CS 3340B - Assignment 2
 * Disjoint ADT Structure Class
 */

// Class to represent a disjoint set data structure
public class uandf {

    // Initialize private variables
    private int[] parent;
    private int[] rank;
    private int n; // number of elements
    private boolean finalSet = false; // initially not in final set

    /*
     * Constructor for the disjoint set data structure
     * Creates UnionFind structure with n elements
     * @param n - number of elements
     * @return - None
     */
    public uandf(int n) {
        this.n = n;
        // Initialize parent and rank to n + 1 (1 -> n is 1 based indexing, want to ignore 0)
        this.parent = new int[n + 1];
        this.rank = new int[n + 1];
    }

    /*
     * make set method that creates a new set with only element i (rank is 0)
     * @param i - element
     * @return - None
     */
    public void makeSet(int i) {
        parent[i] = i;
        rank[i] = 0;
    }

    /*
     * find set method that returns the representative of the set that i is an element of
     * @param i - element
     * @return - representative of the set that i is an element of
     */
    public int findSet(int i) {
        // Not in final set
        if (finalSet == false) {
            // If i is not the parent of the set
            if (parent[i] != i) {
                // Recursively call findSet on the parent of i
                return (parent[i] = findSet(parent[i]));
            }
            // If i is the parent of the set
            else {
                return i;
            }
        }
        // In the final set -> just return the parent of i
        else {
            return parent[i];
        }
    }

    /*
     * union set method that unites the sets i and j belong to into a single union set they belong to
     * @param i - first element
     * @param j - second element
     * @return - None
     */
    public void unionSets(int i, int j) {
        // Find the root elements (the set) that i and j belong to
        int iRoot = findSet(i);
        int jRoot = findSet(j);

        // If they are already in the same set
        if (iRoot == jRoot) {
            return;
        }

        // Union by rank: attach the smaller rank tree (i) to the root of the larger rank tree (j)
        if (rank[iRoot] < rank[jRoot]) {
            parent[iRoot] = jRoot;
        } 
        // Union by rank: attach the smaller rank tree (j) to the root of the larger rank tree (i)
        else if (rank[iRoot] > rank[jRoot]) {
            parent[jRoot] = iRoot;
        } 
        // Union by rank: they are the same size so attach j to i and increment i's rank
        else {
            parent[jRoot] = iRoot;
            rank[iRoot]++;
        }
    }

    /*
     * final sets method that returns the total # of current sets, size, and finalizes the current sets
     */
    public int finalSets() {
        // Loop through the parent list (1st time)
        for (int i = 1; i < parent.length - 1; i++) {
            // If the parent of i is not 0
            if (parent[i] != 0) {
                // Find the set that i belongs to
                findSet(i);
            }
        }

        // Loop through the parent list (2nd time)
        int size = 1;
        for (int i = 1; i < parent.length - 1; i++) {
            // If the parent of i is the same as i
            if (parent[i] == i) {
                // Increment size and set parent of i to size
                size++;
                parent[i] = size;

                // Change the rank of i
                rank[i] = 1;
            }
            // If the parent of i is not the same as i
            else {
                rank[i] = 0;
            }
        }

        // Loop through the parent list (3rd time)
        for (int i = 1; i < parent.length - 1; i++) {
            // If the rank at i is 0 and the parent is not 0
            if (rank[i] == 0 && parent[i] > 0) {
                // The parent at i is the parent of the parent at i
                parent[i] = parent[parent[i]];
            }
        }

        // Set finalSet to true and return cur - 1
        finalSet = true;
        return size - 1;
    }
}
