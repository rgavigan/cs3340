'''
Name: Riley Emma Gavigan
Student Number: 251150776
Student ID: rgavigan
CS 3340B - Assignment 3 - Single Source Shortest Path Algorithm
'''
import sys

# Class to representa  node in the graph
class Node:
    # Instance variables
    u = None
    d = None

    # Constructor that initializes the node and the distance
    def __init__(self, u, d):
        self.u = u
        self.d = d
    
    # Method to get the node number
    def get_node(self):
        return self.u
    
    # Method to get the distance
    def get_distance(self):
        return self.d
    
    # Method to set the distance
    def set_distance(self, d):
        self.d = d

    # Method to print out the node in string format
    def __str__(self):
        return f"{self.u}>"

# Class to represent an edge in the graph
class Edge:
    # Instance variables
    u = None
    v = None
    weight = None

    # Constructor that initializes the endpoint nodes and the weight of the edge
    def __init__(self, u, v, weight):
        self.u = u
        self.v = v
        self.weight = weight

    # Method to get the first endpoint node of the edge
    def get_u(self):
        return self.u

    # Method to get the second endpoint node of the edge
    def get_v(self):
        return self.v
    
    # Method to get the weight of the edge
    def get_weight(self):
        return self.weight
    
    # Method to print out the edge in string format
    def __str__(self):
        return f"    Edge: {self.u.__str__()} -> {self.v.__str__()} <Weight: {self.weight}>"

# Class to represent a minimum heap that will be used in SSSP Algorithm
class Heap:
    # Instance variables
    A = [] # array of the keys [sorted by Node.distance]
    H = [] # heap that holds the indices for A
    max_val = None
    heap_max = None

    # Initialize a heap with the array of keys and the number of keys
    def heap(self, keys, n):
        # Set edge_array to the keys array [will contain all distances of nodes]
        self.A = keys

        # Add an element at start of edge array to make indexing by 1
        self.A.insert(0, None)

        # Set the max to n (nodes are from 1 -> n)
        self.max_val = n

        # Set the heap to an array of 2 * max elements
        self.H = [None] * (2 * self.max_val)

        # Set the heap max to 2 * max
        self.heap_max = 2 * self.max_val

        # Call heapify on the heap
        self.heapify()
    
    # Method to heapify the heap with bottom-up-approach
    def heapify(self):
        # Loop from 1 to n
        for i in range (1, self.max_val + 1):
            # Set heap[i + n - 1] to i
            self.H[i + self.max_val - 1] = i

        # Loop from n - 1 to 1
        for i in range (self.max_val - 1, 0, -1):
            # if A[H[2i]] < A[H[2i + 1]]
            if self.A[self.H[2 * i]].get_distance() < self.A[self.H[2 * i + 1]].get_distance():
                self.H[i] = self.H[2 * i]
            # if A[H[2i]] >= A[H[2i + 1]]
            else:
                self.H[i] = self.H[2 * i + 1]

    # Method that returns true if element with id is in heap, false otherwise
    def in_heap(self, id):
        # Comparison edge
        compare_edge = Edge(0, 0, 0)

        # Loop through the edge array
        for i in range(1, len(self.A)):
            # If the id of the node is equal to passed in id
            if self.A[i].get_node() == id:
                return True
        # Return false if id edge is not found in edge array
        return False
    
    # Method that returns true if heap is empty, false otherwise
    def is_empty(self):
        # If the heap is empty
        if self.H[1] == 0:
            return True
        # If the heap is not empty
        else:
            return False
        
    # Method that returns the minimum key in the heap (weight)
    def min_key(self):
        return self.A[self.H[1]].get_distance()
    
    # Method that returns the minimum id of the heap
    def min_id(self):
        return self.H[1]
    
    # Method that returns the key of the element with id in the heap
    def key(self, id):
        return self.A[id].get_distance()
    
    # Method that deletes the element with the minimum key from the heap
    def delete_min(self):
        # Create an edge to hold the minimum edge
        removed_edge = Node(0, float("inf"))

        # Set A[1] to be removed edge
        self.A[0] = removed_edge

        # Set heap[heap[1] + max - 1] to 0
        self.H[self.H[1] + self.max_val - 1] = 0

        # Create edge v to reference A[H[1]]
        v = self.A[self.H[1]]

        # Set i to floor((H[1] + n - 1) / 2)
        i = (self.H[1] + self.max_val - 1) // 2

        # Reheapify
        while i >= 1:
            # If A[H[2i]] < A[H[2i + 1]]
            if self.A[self.H[2 * i]].get_distance() < self.A[self.H[2 * i + 1]].get_distance():
                self.H[i] = self.H[2 * i]
            # If A[H[2i]] >= A[H[2i + 1]]
            else:
                self.H[i] = self.H[2 * i + 1]
            
            # Set i to floor(i / 2)
            i = i // 2
        
        # Return removed edge
        return v
    
    # Method that decreases the key of the element with id to new_key if its current key is greater than new_key
    def decrease_key(self, id, new_key):
        # Get the new key
        self.A[id].set_distance(new_key)

        # Set i to floor((id + max - 1) / 2)
        i = (id + self.max_val - 1) // 2

        # Reheapify
        while i >= 1:
            # If A[H[2i]] < A[H[2i + 1]]
            if self.A[self.H[2 * i]].get_distance() < self.A[self.H[2 * i + 1]].get_distance():
                self.H[i] = self.H[2 * i]
            # If A[H[2i]] >= A[H[2i + 1]]
            else:
                self.H[i] = self.H[2 * i + 1]
            
            # Set i to floor(i / 2)
            i = i // 2
    
    # Method to print out the heap contents
    def print_heap(self):
        # Print out the heap
        for i in range(1, len(self.H)):
            print(self.H[i], end = " ")
        print()

        # Print out the nodes in A
        for i in range(1, len(self.A)):
            print(self.A[i].get_node(), self.A[i].get_distance(), end = " ")
        print()


# Method to perform Dijkstra's Algorithm on the graph using a heap data structure
def dijkstra(keys, source):
    # Fill edges array with edges from the adjacency list
    edges = []
    for i in range(1, len(keys)):
        for j in range(1, len(keys[i])):
            edges.append(Edge(keys[i][j][0], i, keys[i][j][1]))
    
    # Create array with source set to 0 and the rest set to infinity
    nodes = []
    for i in range(1, len(keys)):
        if i == source:
            nodes.append(Node(i, 0))
        else:
            nodes.append(Node(i, float("inf")))
    
    # Create min heap of the nodes
    heap = Heap()
    heap.heap(nodes, len(nodes))

    # Create array to hold the shortest path
    shortest_path = []

    # While the heap is not empty
    while len(shortest_path) < len(keys) - 1:
        # Delete the minimum element from the heap
        u = heap.delete_min()

        # Add the node to the shortest path
        shortest_path.append(u)

        # For each edge adjacent to node u
        for i in range(0, len(keys[u.get_node()])):
            # Get the node v
            v = keys[u.get_node()][i][0]

            # If v is in the heap
            if heap.in_heap(v):
                # If the distance of u plus the weight of the edge is less than the distance of v
                if u.get_distance() + keys[u.get_node()][i][1] < heap.key(v):
                    # Decrease the key of v to the new distance
                    heap.decrease_key(v, u.get_distance() + keys[u.get_node()][i][1])
        
    # Print the shortest path
    print("Shortest Path Tree Edges With Shortest Path Weights:")
    for i in range(1, len(shortest_path)):
        print(f"({shortest_path[i - 1].get_node()},{shortest_path[i].get_node()}) : {shortest_path[i].get_distance()}")
    

        
# Main Method to run the program with input file name
def main():
    # Get the input file
    input_lines = sys.stdin.readlines()

    # Read an integer from the first line and store as # of vertices
    num_vertices = int(input_lines[0])

    # Create an empty adjacency list
    adjacency_list = []

    # Create an empty list for each vertex
    for i in range(num_vertices + 1):
        adjacency_list.append([])

    # Read the remaining lines of the file and fill in the adjacency list
    for i in range(1, len(input_lines)):
        # Split the line by spaces
        line = input_lines[i].split()

        # Add the edge to the adjacency list representation of the graph
        adjacency_list[int(line[0])].append([int(line[1]), int(line[2])]) # source: [destination, weight]

    # Print out adjacency list
    print("Adjacency List Representation of Graph:")
    for i in range(1, len(adjacency_list)):
        print("Node:", i, "-> Edges:", end = " ")
        for j in range(0, len(adjacency_list[i])):
            print(f"[Node: {adjacency_list[i][j][0]}, Weight: {adjacency_list[i][j][1]:<2}]", end = " ")
        print()
    print()

    # Run Dijkstra's algorithm
    dijkstra(adjacency_list, 1)

main()