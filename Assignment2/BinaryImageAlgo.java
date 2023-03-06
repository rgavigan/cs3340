/*
 * Name: Riley Emma Gavigan
 * Student #: 251150776
 * Student ID: rgavigan
 * CS 3340B - Assignment 2
 * Binary Image Algorithm (9b)
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
 
 public class BinaryImageAlgo{
     
    public static void main(String[] args){
        // Initialize variables
        int[][] image = null;
        int[] sets = null;
        int[] chars = null;

        // Image dimensions
        int numCols = 72; // INSERT NUMBER OF COLUMNS HERE
        int numRows = 75; // INSERT NUMBER OF ROWS HERE

        InputStream inputStream = System.in;
        inputStream.mark(0);
        try {
            // Get the file from input
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            
            // Set up image and flag arrays for 72x75 image girl.img.txt (72 columns, 75 rows)
            image = new int[numRows + 1][numCols + 1];
            
            // Initialize a string to read each line
            String string;
            
            // Read through each line; for each + set image at them coords to 1 and flag to true
            for(int i = 0; input.ready(); i++){
                // Read line-by-line
                string = input.readLine();
                for(int j = 0; j < numCols; j++){
                    // If it is a plus, set to 1 and true
                    if(string.charAt(j) == '+'){
                        image[i][j] = 1;
                    }
                }
            }
            
            // Close the BufferedReader
            input.close();
        }
        // Catch FileNotFound / IO exception
        catch (Exception e){
        e.printStackTrace();
        }
        
        /*
        * PART 1 PRINTING - the input binary image
        */
        System.out.println("--------PART 1--------\n");
        for(int i = 0; i < numRows; i++){
            // Print each line
            for(int j = 0; j < numCols; j++){
                System.out.print(image[i][j]);
            }
            // New line for each line being finished
            System.out.println();
        }
        // Print a newline at the end of part 1
        System.out.println("\n\n\n");
        

        // Create the UnionFind and connect components
        uandf imageSet = new uandf(numRows * numCols + 1);
        // Loop through the number of rows
        for(int i = 0; i < numRows; i++){
            // Loop through the number of columns
            for(int j = 0; j < numCols; j++){
                // If the image is 1, make a set and union with the left and top if they are 1 (can ignore other directions since we go from top left to bottom right)
                if (image[i][j] == 1){
                    // Want to get the current index (row # * number of columns) + column in row + 1 (1-based indexing)
                    imageSet.makeSet((i * numCols + j) + 1);
                    // If the element to the left is 1 (means it was a +)
                    if (j > 0 && image[i][j - 1] == 1){
                        // Union with the left
                        imageSet.unionSets(i * numCols + (j - 1) + 1, i * numCols + j + 1);
                    }
                    // If the element above is 1
                    if (i > 0 && image[i - 1][j] == 1){
                        // Union with the top
                        imageSet.unionSets((i - 1) * numCols + j + 1, i * numCols + j + 1);
                    }
                }
            }
        }
        
        // Initialize the sets and chars arrays for part 3 based on final sets
        sets = new int[imageSet.finalSets() + 1];
        chars = new int[sets.length];
        
        /*
        * PART 2 PRINTING - connected component image with a-z representing the sets
        */
        System.out.println("--------PART 2--------\n");
        int k;
        // Loop through the number of rows
        for(int i = 0; i < numRows; i++){
            // Loop through the number of columns
            for(int j = 0; j < numCols; j++){
                // Assign a character to k at the current index of the image
                k = imageSet.findSet(i * numCols + j + 1) + 96;
                
                // If k is 96, print space
                if (k == 96){
                    System.out.print(' ');
                }
                // If k is not 96, print the character and increment the set
                else{
                    System.out.print((char)k);
                    sets[k - 97]++;
                }
            }
            // Print a newline after each line has been processed
            System.out.println();
        }
        // Print a newline after finishing part 2
        System.out.println();
        
        // Sort the lists for part 3
        int[] sortedSets = new int[sets.length];

        // Copying sets into sorted sets
        for(int i = 0; i < sets.length; i++){
            sortedSets[i] = sets[i];
        }
        // Copying sorted sets into chars
        for(int i = 0; i < sortedSets.length; i++){
            chars[i] = sortedSets[i];
        }
        // Sorting sorted sets
        Arrays.sort(sortedSets);
        
        /*
        * PART 3 PRINTING - list sorted by component size. each line contains the size and label of the component.
        */
        System.out.println("--------PART 3--------\n");
        System.out.println("Label" + "\t " + "Size");
        for(int i = 0; i < sortedSets.length; i++){
            for(int j = 0; j < chars.length; j++){
                if(sortedSets[i] == chars[j]){
                    // Print out the character and it's size
                    System.out.println((char)(j + 97) + "\t " + sortedSets[i]);
                    chars[j] = -1;
                    break;
                }
            }
        }
        System.out.println("\n\n\n");
        
        /*
        * PART 4 PRINTING - the input binary image with small components removed (size < 2)
        */
        System.out.println("--------PART 4--------\n");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // Assign the character to k
                k = imageSet.findSet(i * numCols + j + 1) + 96;
                
                // If size is less than 2 (1 or less), remove it
                if (k == 96 || sets[k - 97] < 2){
                    System.out.print(' ');
                }
                
                // Otherwise print out the character
                else
                    System.out.print((char) k);
            }
            System.out.println();
        }
        System.out.println("\n\n\n");

        /*
        * PART 5 PRINTING - the input binary image with components smaller than 12 removed
        */
        System.out.println("--------PART 5--------\n");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // Assign the character to k
                k = imageSet.findSet(i * numCols + j + 1) + 96;
                
                // If size is less than 12 (11 or less), remove it
                if (k == 96 || sets[k - 97] < 12){
                    System.out.print(' ');
                }
                
                // Otherwise print out the character
                else
                    System.out.print((char)k);
            }
            System.out.println();
        }
        System.out.println();
    }
}