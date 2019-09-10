import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//creates the panel added to the frame and implements all action and key listeners to form the sudoku
public class PlayArea extends JPanel implements ActionListener, KeyListener {
    List<JButton> guiSudoku= new ArrayList<>(); //creating an arraylist of buttons to add in the GUI
    public int value=0; // a global variable which stored the value entered through keyboard
    Sudoku sudoku;
    JMenuBar mb=new JMenuBar();
    JMenu menu=new JMenu("Menu");
    JMenuItem solve=new JMenuItem("Solve");
    JMenuItem reset=new JMenuItem("Reset");
    PlayGame playGame;

    public PlayArea(Sudoku sudoku){
        this.sudoku=sudoku;
        this.playGame= new PlayGame(sudoku,this);
        setLayout(new GridLayout(9, 9));
        for(int i=0;i<81;i++){
            guiSudoku.add(new JButton());
            add(guiSudoku.get(i));
            guiSudoku.get(i).setBackground(Color.WHITE);
            guiSudoku.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));;
            guiSudoku.get(i).addActionListener(this);
            guiSudoku.get(i).addKeyListener(this);
        }
        solve.addActionListener(this);
        reset.addActionListener(this);
    }

    public void resetCell(int row, int col){
        int i=row*9 + col;
        guiSudoku.get(i).setText("");
        guiSudoku.get(i).setBackground(Color.WHITE);
    }

    //to reset the GUI
    public void reset(){
        //repainting the GUI
        for(int i=0;i<81;i++){
            guiSudoku.get(i).setText("");
            guiSudoku.get(i).setBackground(Color.WHITE);
        }

        //reinitializing the board and matrix
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                sudoku.matrix[i][j]=0;
                for(int k=0;k<9;k++){
                    playGame.board[i][j][k]=0;
                }
            }
        }

        System.out.println("Do you want to enter a csv file? Enter 1 for yes, 0 otherwise");
        Scanner scan = new Scanner(System.in);
        int k = scan.nextInt();

        if(k==1){ //taking input from csv file
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

            sudoku.setValueGui(inputList);
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i=0;i<81;i++){
            if(e.getSource()==guiSudoku.get(i)){
               value = i;
            }
        }
        if(e.getActionCommand()=="Solve")
            playGame.pickNode();
        if(e.getActionCommand()=="Reset")
            reset();
    }

    @Override
    //entering the value in the clicked box
    public void keyTyped(KeyEvent keyEvent) {
        char s= keyEvent.getKeyChar();
        setGuiValue(s,value);
    }

    //setting the value in GUI when entered through csv file
    public void setGuiValue(char s, int value){
        guiSudoku.get(value).setText(Character.toString(s));
        guiSudoku.get(value).setBackground(Color.LIGHT_GRAY);
        int val = Integer.parseInt(Character.toString(s));
        int row = value/9;
        int col = value%9;
        //using the input method created
        sudoku.setValue(new Input(row,col,val));
        //sudoku.display();
    }

    //overriding to set value in GUI when entered through csv file
    public void setGuiValue(int val, int row, int col){
        setGuiValue(((Integer)val).toString().charAt(0),row*9 + col);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {}
    @Override
    public void keyReleased(KeyEvent keyEvent) {}
}
