/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 *  Description: This class is the entry point for the BBoard application.
 *  It initializes a bulletin board and starts its execution.
*/

package VBOARD.vboard;

import java.io.FileNotFoundException;

class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Please provide the user file and chat history file as arguments.");
                return;
            }
            BBoard myBoard = new BBoard();
            myBoard.loadUsers(args[0]);
            myBoard.loadHistory(args[1]);
            myBoard.run();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + e.getMessage());
        }
	}
}
