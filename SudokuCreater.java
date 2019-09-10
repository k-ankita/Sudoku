
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
Created a GUI to solve sudoku using JButtons.
The input is available both through a csv file
and by GUI using actionListener and KeyListener.
In the console it is first asked how you want to enter input.
When the sudoku problem is given,
in order to solve the problem
the computer first tabulates a 9*9*9 matrix called board
which checks the row, column and block constraint of an empty cell
and stores the values which all are available for that cell.
After the constraints are satisfied,
the computer inserts values in those cells where only a single value is available
The board is then again computed to reevaluate the available numbers affected by the new inserted number
-the affected cells are those in the same row, column and block
After this re-evaluation, the code is again run and then there are no such cells with only 1 possibility
We take those cells with minimum possibility and then pick a number, compute the board,
and if constraints are not satisfied ny the picked value,
we backtrack to the last board position and continue till solved,
while keeping on checking the constraints.
When RESET option chosen, the grid is cleared, the matrices are reinitialized
and in the console, it is again asked if you want to enter values through csv file or through GUI
*/



//starts the program by either calling the GUI sudoku constructor or the csv sudoku constructor
public class SudokuCreater {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Do you want to enter a csv file? Enter 1 for yes, 0 otherwise");
        Scanner scan = new Scanner(System.in);
        int k = scan.nextInt();
        if(k==1) {
            readCsvFile(scan);
        }
        else{ //when entering manually
            Sudoku frame = new Sudoku();
        }
    }

    //to read csv file, or else enter sudoku through buttons(GUI)
    public static void readCsvFile(Scanner scan)  throws FileNotFoundException {

        //taking input from csv file
            System.out.println("Enter file path");
            scan.nextLine();
            String s=scan.nextLine();
            List<Input> inputList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(s))) {
                String line;
                for(int i=0;i<9;i++){
                    line = br.readLine();
                    String[] values = line.split(","); //splitting at comma
                    for(int j=0;j<9;j++){
                        if(!values[j].equals("0"))
                            inputList.add(new Input(i,j,Integer.parseInt(values[j])));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Sudoku sudoku = new Sudoku(inputList);

    }
}
