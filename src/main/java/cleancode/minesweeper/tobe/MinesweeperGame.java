package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10]; // 추측: 게임판 (가로 8 * 세로 10)
    private static Integer[][] landMineCounts = new Integer[8][10]; // 추측: 지뢰 숫자 (가로 8 * 세로 10)
    private static boolean[][] landMines = new boolean[8][10]; // 추측: 지뢰 유무 (가로 8 * 세로 10)
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        Scanner scanner = new Scanner(System.in);
        initializeGame();

        while (true) {
            showBoard();

            if (doesUserWinTheGame()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (doesUserLoseTheGame()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellInput = getCellInputFromUser(scanner);
            String userActionInput = getUserActionInputFromUser(scanner);

            int selectedColIndex = getSelectedColIndex(cellInput);
            int selectedRowIndex = getSelectedRowIndex(cellInput);

            if (doesUserChooseToPlantFlag(userActionInput)) { // 깃발 꽂기를 선택한 경우
                board[selectedRowIndex][selectedColIndex] = "⚑";
                checkIfGameIsOver();
            } else if (doesUserChooseToOpenCell(userActionInput)) { // 셀 열기를 선택한 경우
                if (isLandMineCell(selectedRowIndex, selectedColIndex)) { // 지뢰셀을 선택했다면
                    board[selectedRowIndex][selectedColIndex] = "☼"; // 지뢰 표시를 보드에 넣고
                    changeGameStatusToLose(); // 게임의 상태를 진것으로 변경
                    continue;
                } else {
                    open(selectedRowIndex, selectedColIndex); // 지뢰셀이 아니라면, 셀을 연다.
                }

                checkIfGameIsOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return landMines[selectedRowIndex][selectedColIndex];
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser(Scanner scanner) {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return scanner.nextLine();
    }

    private static String getCellInputFromUser(Scanner scanner) {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return scanner.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    // 게임이 끝났는지 체크
    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened(); // 셀이 전부 열렸는지를 체크
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellOpened() {
        boolean isAllOpend = true;
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 10; column++) {
                if (board[row][column].equals("□")) {
                    isAllOpend = false;
                }
            }
        }
        return isAllOpend;
    }

    private static int convertRowFrom(char cellInputRow) {
        return Character.getNumericValue(cellInputRow) - 1;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < 8; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < 10; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < 8; row++) { // 추측1: 그림판 그리기
            for (int col = 0; col < 10; col++) {
                board[row][col] = "□";
            }
        }
        for (int i = 0; i < 10; i++) { // 추측2: 지뢰 세팅
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true;
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                int count = 0;
                if (!isLandMineCell(row, col)) { // 지뢰가 아니면 ( 내 주변 8칸들 중에서 지뢰를 몇개가지고 있는지 체크)
                    if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) { // 내 왼쪽 위에 지뢰가 있으면 count++
                        count++;
                    }
                    if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < 10 && isLandMineCell(row - 1, col + 1)) {
                        count++;
                    }
                    if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
                        count++;
                    }
                    if (col + 1 < 10 && isLandMineCell(row, col + 1)) {
                        count++;
                    }
                    if (row + 1 < 8 && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
                        count++;
                    }
                    if (row + 1 < 8 && isLandMineCell(row + 1, col)) {
                        count++;
                    }
                    if (row + 1 < 8 && col + 1 < 10 && isLandMineCell(row + 1, col + 1)) {
                        count++;
                    }
                    landMineCounts[row][col] = count;
                    continue;
                }
                landMineCounts[row][col] = 0; // 지뢰 칸인 경우는 숫자를 셀 필요가 없기 때문에, 0으로 세팅
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        // 게임판을 벗어났는지 체크
        if (row < 0 || row >= 8 || col < 0 || col >= 10) {
            return;
        }
        // 이미 열렸는지 체크
        if (!board[row][col].equals("□")) {
            return;
        }
        // 지뢰 셀인지 체크
        if (isLandMineCell(row, col)) {
            return;
        }
        // 지뢰 갯수를 가지고 있는 칸이라면, 숫자를 보드에 표기
        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
            return;
        } else {
            // 아무것도 아닌 빈 셀인 경우, 열린 빈셀로 표기
            board[row][col] = "■";
        }

        // 자기 주변에 있던 8개를 탐색 (재귀)
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
