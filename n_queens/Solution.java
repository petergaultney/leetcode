import java.util.ArrayList;
import java.util.List;

public class Solution {
    
    public static boolean[][] makeEmptyBoard(int n) {
		boolean[][] board = new boolean[n][];
		for (int i = 0; i < n; ++i) {
			board[i] = new boolean[n];
		}
		return board;
	}
    
    public static String turnRowIntoSolString(boolean[] row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.length; ++i) {
            sb.append(row[i] ? "Q" : ".");
        }
        return sb.toString();
    }
    
    public static List<String> turnBoardIntoSolString(boolean[][] board) {
        List<String> sol = new ArrayList<>();
        for (boolean[] row: board) {
            sol.add(turnRowIntoSolString(row));
        }
        return sol;
    }
    
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        boolean[][] board = makeEmptyBoard(n);
        boolean[] occupied_rows = new boolean[n];
        boolean[] occupied_columns = new boolean[n];
		boolean[] occupied_se_diagonals = new boolean[n + n - 1];
		boolean[] occupied_ne_diagonals = new boolean[n + n - 1];
        addQueenToNextRow(board, 0, occupied_rows, occupied_columns,
						  occupied_se_diagonals, occupied_ne_diagonals, solutions);
        return solutions;
    }

	private static int whichSeDiagonal(int row, int col, int board_size /* n */) {
		assert(row < board_size);
		assert(col < board_size);
		// if row == n - 1 and col == 0, then it's d(0)
		// if row == 0 and col == n - 1, then it's d(n+n-1)
		// if row == 1 and col == n - 2, then it's
		int rc_diff = col - row;
		int diagonal = rc_diff + board_size - 1;
		return diagonal;
	}
	private static int whichNeDiagonal(int row, int col, int board_size /* n */) {
		assert(row < board_size);
		assert(col < board_size);
		// if row == n - 1 and col == 0, then it's d(n - 1)
		// if row == 0 and col == n - 1, then it's d(n - 1)
		// if row == 1 and col == n - 2, then it's d(n - 1)
		int diagonal = row + col;
		return diagonal;
	}
	
    private void addQueenToNextRow(
		boolean[][] board, int row,
		boolean[] occupied_rows, 
		boolean[] occupied_columns,
		boolean[] occupied_se_diagonals,
		boolean[] occupied_ne_diagonals,
		List<List<String>> solutions)
	{
        for (int col = 0; col < board.length; ++col) {
            if (!occupied_columns[col]) {
				int se_diagonal = Solution.whichSeDiagonal(row, col, board.length);
                if (!occupied_se_diagonals[se_diagonal]) {
					int ne_diagonal = Solution.whichNeDiagonal(row, col, board.length);
					if (!occupied_ne_diagonals[ne_diagonal]) {
						board[row][col] = true;
						occupied_rows[row] = true;
						occupied_columns[col] = true;
						occupied_se_diagonals[se_diagonal] = true;
						occupied_ne_diagonals[ne_diagonal] = true;
						if (row == board.length - 1) {
							// board is full and this was a total success!
							// add board to solutions!
							solutions.add(Solution.turnBoardIntoSolString(board));
						} else {
							// board not full yet
							addQueenToNextRow(board, row + 1, occupied_rows, occupied_columns,
											  occupied_se_diagonals, occupied_ne_diagonals, solutions);
						}
						// reset our helper variables for the next iteration
						board[row][col] = false;
						occupied_rows[row] = false;
						occupied_columns[col] = false;
						occupied_se_diagonals[se_diagonal] = false;
						occupied_ne_diagonals[ne_diagonal] = false;
					}
                } // else: this failed, but another column might work
            }
        }
    }

	private static boolean _testWhichDiagonal() {
		boolean all_succeeded = true;
		int test_data [][] =
			{
				{3, 3, 4, 3},
				{1, 2, 3, 3},
			};
		for (int[] test: test_data) {
			int result = Solution.whichSeDiagonal(test[0], test[1], test[2]);
			if (result != test[3]) {
				System.out.println("Failed SE Diagonal test: " + test[0] + " "
								   + test[1] + " " + test[2] + " " + test[3] + " " + result);
			}
			all_succeeded = all_succeeded && result == test[3];
		}
		return all_succeeded;
	}
	
	public static void main(String[] args) {
		if (args[0].equals("test")) {
			System.out.println("Testing SE Diagonals code: " + Solution._testWhichDiagonal());
		} else {
			Solution solver = new Solution();
			int n = Integer.parseInt(args[0]);
			List<List<String>> sols = solver.solveNQueens(n);
			if (args.length > 1 && args[1] == "print") {
				for (List<String> col: sols) {
					for (String row: col) {
						for (int si = 0; si < row.length(); ++si) {
							System.out.print(row.charAt(si) + "  ");
						}
						System.out.println("");
					}
					System.out.println("");
				}
			}
			System.out.println("There were " + sols.size() + " solutions.");
		}
	}
}


