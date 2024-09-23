import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;


class Players
{
    public int id;
    public String piece;
    public boolean chance;

    public Players(int id, String piece)
    {
        this.id = id;
        this.piece = piece;
        chance = false;
    }
}



class Board
{
    private int board_size;
    private String[][] board;

    public Board(int row,int col)
    {
        board_size = row * col;

        board = new String[row+1][col+1];

        for(int i=0; i<board_size; i++)
        {
            for(int j=0; j<board_size; j++)
            {
                board[row][col] = "-";
            }
        }

    }
    public void printBoard(int row, int col)
    {
        System.out.println();
        for(int rowSecNum = 1; rowSecNum<=col; rowSecNum++){
            System.out.print("  " + rowSecNum + " ");
        }
        System.out.println();
        for(int i=0; i<row; i++) {
            for(int rowSec = 0; rowSec<col; rowSec++){
                System.out.print(" ---");
            }
            System.out.println();
            for(int j=0; j<col; j++) {
                System.out.print("| " + board[row][col] + " ");
            }
            System.out.print("| ");
            System.out.println();
        }
        for(int rowSec = 0; rowSec<col; rowSec++){
            System.out.print(" ---");
        }
        System.out.println();
        for(int rowSecNum = 1; rowSecNum<=col; rowSecNum++){
            System.out.print("  " + rowSecNum + " ");
        }
        System.out.println();
    }

    public int insert(int[] position, String c)
    {
        int row = position[0];
        int col = position[1];

        //if position is occupied return -1
        //unble to insert at this position
        if(board[row][col] != "-") {
            return -1;
        }

        board[row][col] = c;
        return 0;
    }

    //to check game tie
    public boolean boardISFull()
    {
        for(int i=0; i<board_size; i++)
        {
            for(int j=0; j<board_size; j++)
            {
                //if none position is left empty
                if(board[i][j] == "-")
                    return  false;
            }
        }
        return true;
    }

    //to check if somebody won, will return their character
    //victory is the int needed to win in a row
    public String playerWon(int victory)
    {
        int count = 0;
        String value = "-";

        for(int i=0; i<board_size; i++)
        {
            //for new row put count as zero
            //count will be used to count number of continuous occurence of any
            //piece in a row
            count= 0;


            for(int j=0; j<board_size; j++)
            {

                //if position is empty put count as 0
                if(board[i][j] == "-")
                {
                    count = 0;
                    continue;
                }

                //take the initial value and count further
                if(count == 0)
                {
                    value = board[i][j];
                    count++;
                }

                else
                {

                    //if value is same as previous then increment count
                    //else update value of count and variable
                    if( value == board[i][j])
                    {
                        count++;
                    }
                    else
                    {
                        count = 1;
                        value = board[i][j];
                    }

                    // made number of required elements continuosly to win
                    //returns that piece
                    if(count == victory) {
                        return value;
                    }
                }
            }
        }

        return "-";
    }
}




class GameLogic
{
    private ArrayList<Players> players;
    private int number_of_players;
    private int victory_number;
    Scanner sc = new Scanner(System.in);
    private Board board;

    //n is the number of players
    public GameLogic(int n)
    {
        players = new ArrayList<>();
        number_of_players = n;
        buildPlayers();
    }


//sets all of the players pieces and names
    public void buildPlayers()
    {
        int i=0;
        System.out.println("\nEnter the first and last name for each player");

        HashMap<String, Integer> characters = new HashMap<>();
        String[] playerPiece = {"a","b","c","d","e"};

        while(i< number_of_players)
        {
            String c;

            do
            {
                System.out.print("Player " + (i+1) + " : ");
                c = sc.nextLine();
            } while (characters.containsKey(c));

            players.add( new Players(i+1, playerPiece[i]));
            characters.put(playerPiece[i], 1);

            i++;
        }

        System.out.println();
        victory_number = 3;

    }



    public void playGame(int row, int col) {

        board = new Board(row, col);

        boolean gameEnd = false;
        players.get(0).chance = true;

        System.out.println("Enter Position on board for players");


        while (!gameEnd) {
            board.printBoard(row,col);


            //for each player in list get their position input, insert into board
            //and check if they won
            for (Players player : players) {
                if (!player.chance)
                    continue;


                //id starts from 1
                int player_index = player.id - 1;

                System.out.print("Player " + player.id + " (" + player.piece + ") : ");



                while (true) {
                    row = sc.nextInt();
                    col = sc.nextInt();


                    int position[] = {row-1, col-1};


                    if (row < 0 || col < 0 || row > number_of_players+1 || col > number_of_players+1) {
                        System.out.print("This position does not exist on board: ");
                    } else if (board.insert(position, player.piece) != -1) {
                        System.out.print("This position is already occupied on board: ");
                    } else
                        break;
                }

                //set the players turn to false and next player to true
                players.get(player_index).chance = false;
                player_index++;

                //if reached the end of list make first player turn true
                if (player_index == number_of_players)
                    players.get(0).chance = true;

                else
                    players.get(player_index).chance = true;

                break;
            }


            //updates game status
            gameEnd = isGameEnded();
        }

        board.printBoard(row, col);
    }


    public boolean isGameEnded()
    {
        //game end if its a tie, or a player has won

        String result = board.playerWon(victory_number);

        if(result == "-")
            return false;

        //if the returned character matches any of players
        for(Players player: players)
        {
            if(result == player.piece)
            {
                System.out.println("\nPlayer " + player.id + " has WON !!!");
                return true;
            }
        }

        if( board.boardISFull())
        {

            System.out.println("\nIt's a TIE !!!");
            return true;

        }

        return false;
    }
}
    class scoreBoard {
        private int wins;
        private int loss;
        private int ties;
        private String[] scores;


    }


public class Main
{

    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nTIC TAC TOE");

        int wins = 0;
        int losses = 0;
        int ties = 0;


        int row = 0;
        int col = 0;

        int players;
        do
        {
            System.out.print("Number of players: ");
            players = sc.nextInt();
        }
        while(players < 2 || players > 5);

        do{
            System.out.println("Please enter the diminsions of the board.");
            System.out.print("Enter the number of rows  -> ");
            row = sc.nextInt();
            System.out.print("Enter the number of columns  -> ");
            col = sc.nextInt();
        }while(row < 3 || row > 11 && col < 3 || col > 16);

        GameLogic game = new GameLogic(players);
        game.playGame(row,col);


        sc.close();
    }
}