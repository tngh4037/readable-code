package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE]; // 추측: 게임판 (가로 8 * 세로 10)
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[8][10]; // 추측: 지뢰 숫자 (가로 8 * 세로 10)
    private static final boolean[][] LAND_MINES = new boolean[8][10]; // 추측: 지뢰 유무 (가로 8 * 세로 10)
    public static final int LAND_MINE_COUNT = 10; // 게임에서 사용할 지뢰 개수
    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {

            try {
                showBoard();

                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) { // 우리가 핸들링하지 못한, 예상치 못한 예외가 발생한 경우, 프로그램이 터지지 않도록 일단 나머지를 잡는다.
                System.out.println("프로그램에 문제가 생겼습니다.");
               // e.printStackTrace(); // e.printStackTrace(); : 안티패턴 (예외 상황의 경우 로그 시스템을 통해 로그를 남겨야 한다. + 이후 별도의 조치를 취한다. )
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) { // 깃발 꽂기를 선택한 경우
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) { // 셀 열기를 선택한 경우
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) { // 지뢰셀을 선택했다면
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN; // 지뢰 표시를 보드에 넣고
                changeGameStatusToLose(); // 게임의 상태를 진것으로 변경
                return;
            }

            open(selectedRowIndex, selectedColIndex); // 지뢰셀이 아니라면, 셀을 연다.
            checkIfGameIsOver();
            return;
        }

        throw new AppException("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
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

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
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
        return Arrays.stream(BOARD)
                .flatMap(stringStream -> Arrays.stream(stringStream))
                .noneMatch(cell -> CLOSED_CELL_SIGN.equals(cell)); // noneMatch: CLOSED_CELL_SIGN 인게 하나도 없는지 체크
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }
        return rowIndex;
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
                throw new AppException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) { // 추측1: 그림판 그리기
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = "□";
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) { // 추측2: 지뢰 세팅
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) { // 지뢰 칸인 경우는 숫자를 셀 필요가 없기 때문에, 0으로 세팅
                    NEARBY_LAND_MINE_COUNTS[row][col] = 0;
                    continue;
                }

                // 지뢰가 아니면 ( 내 주변 8칸들 중에서 지뢰를 몇개가지고 있는지 체크해서 set )
                int count = countNearbyLandMines(row, col);
                NEARBY_LAND_MINE_COUNTS[row][col] = count;
            }
        }
    }

    // 근방의 지뢰들을 세는 과정을 추상화
    private static int countNearbyLandMines(int row, int col) {
        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) { // 내 왼쪽 위에 지뢰가 있으면 count++
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        // 게임판을 벗어났는지 체크
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        // 이미 열렸는지 체크
        if (!BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
            return;
        }
        // 지뢰 셀인지 체크
        if (isLandMineCell(row, col)) {
            return;
        }
        // 지뢰 갯수를 가지고 있는 칸이라면, 숫자를 보드에 표기
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            // 아무것도 아닌 빈 셀인 경우, 열린 빈셀로 표기
            BOARD[row][col] = OPENED_CELL_SIGN;
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
