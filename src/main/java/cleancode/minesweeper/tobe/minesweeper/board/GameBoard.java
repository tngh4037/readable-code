package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.board.cell.*;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class GameBoard {

  private final Cell[][] board;
  private final int landMineCount;
  private GameStatus gameStatus;

  public GameBoard(GameLevel gameLevel) {
    int rowSize = gameLevel.getRowSize();
    int colSize = gameLevel.getColSize();
    board = new Cell[rowSize][colSize];

    landMineCount = gameLevel.getLandMineCount();
    initializeGameStatus();
  }

  public void initializeGame() {
    initializeGameStatus();
    CellPositions cellPositions = CellPositions.from(board);

    initializeEmptyCells(cellPositions); // 그림판 그리기 (빈 셀 초기화)

    List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
    initializeLandMineCells(landMinePositions); // 지뢰 세팅

    List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions); // 지뢰 칸이 아닌 셀 위치 조회
    initializeNumberCells(numberPositionCandidates); // (지뢰가 아니면) 내 주변 8칸들 중에서 지뢰를 몇개가지고 있는지 체크해서 set
  }

  public void openAt(CellPosition cellPosition) {
    if (isLandMineCellAt(cellPosition)) {
      openOneCellAt(cellPosition);
      changeGameStatusToLose(); // 지뢰셀을 선택했다면 게임의 상태를 진것으로 변경
      return;
    }

    // openSurroundedCells(cellPosition); // 지뢰셀이 아니라면, 주변 셀을 연다.
    openSurroundedCells2(cellPosition); // 지뢰셀이 아니라면, 주변 셀을 연다.
    checkIfGameIsOver();
  }

  public void flagAt(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    cell.flag();

    checkIfGameIsOver();
  }

  public boolean isInvalidCellPosition(CellPosition cellPosition) {
    int rowSize = getRowSize();
    int colSize = getColSize();

    return cellPosition.isRowIndexMoreThanOrEqual(rowSize) ||
      cellPosition.isColIndexMoreThanOrEqual(colSize);
  }

  public boolean isInProgress() {
    return gameStatus == GameStatus.IN_PROGRESS;
  }

  public boolean isWinStatus() {
    return gameStatus == GameStatus.WIN;
  }

  public boolean isLoseStatus() {
    return gameStatus == GameStatus.LOSE;
  }

  public int getRowSize() {
    return board.length;
  }

  public int getColSize() {
    return board[0].length;
  }

  public CellSnapshot getSnapshot(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.getSnapshot();
  }

  private void initializeGameStatus() {
    gameStatus = GameStatus.IN_PROGRESS;
  }

  private void initializeEmptyCells(CellPositions cellPositions) {
    List<CellPosition> allPositions = cellPositions.getPositions();
    for (CellPosition position : allPositions) {
      updateCellAt(position, new EmptyCell());
    }
  }

  private void initializeLandMineCells(List<CellPosition> landMinePositions) {
    for (CellPosition position : landMinePositions) {
      updateCellAt(position, new LandMineCell());
    }
  }

  private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
    for (CellPosition candidatePosition : numberPositionCandidates) {
      int count = countNearbyLandMines(candidatePosition);
      if (count != 0) {
        updateCellAt(candidatePosition, new NumberCell(count));
      }
    }
  }

  private int countNearbyLandMines(CellPosition cellPosition) {
    int rowSize = getRowSize();
    int colSize = getColSize();

    long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
      .filter(this::isLandMineCellAt)
      .count();

    return (int) count;
  }

  private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
    return RelativePosition.SURROUNDED_POSITIONS.stream()
      .filter(cellPosition::canCalculatePositionBy)
      .map(cellPosition::calculatePositionBy)
      .filter(position -> position.isRowIndexLessThan(rowSize))
      .filter(position -> position.isColIndexLessThan(colSize))
      .toList();
  }

  private void updateCellAt(CellPosition position, Cell cell) {
    board[position.getRowIndex()][position.getColIndex()] = cell;
  }

  private void openSurroundedCells(CellPosition cellPosition) {
    if (isOpenedCell(cellPosition)) { // 이미 열렸는지 체크
      return;
    }
    if (isLandMineCellAt(cellPosition)) { // 지뢰 셀인지 체크
      return;
    }

    openOneCellAt(cellPosition);

    // 지뢰 갯수를 가지고 있는 칸이라면 멈춤
    if (doesCellHaveLandMineCount(cellPosition)) {
      return;
    }

    // 자기 주변에 있던 8개를 탐색 (재귀)
    List<CellPosition> surroundedPositions =
      calculateSurroundedPositions(cellPosition, getRowSize(), getColSize()); // 게임판을 벗어나는지 체크
    surroundedPositions.forEach(this::openSurroundedCells);
  }

  private void openSurroundedCells2(CellPosition cellPosition) {
    Deque<CellPosition> deque = new ArrayDeque<>();
    deque.push(cellPosition);

    while (!deque.isEmpty()) {
      openAndPushCellAt(deque);
    }
  }

  private void openAndPushCellAt(Deque<CellPosition> deque) {
    CellPosition currentCellPosition = deque.pop();
    if (isOpenedCell(currentCellPosition)) { // 이미 열렸는지 체크
      return;
    }
    if (isLandMineCellAt(currentCellPosition)) { // 지뢰 셀인지 체크
      return;
    }

    openOneCellAt(currentCellPosition);

    // 지뢰 갯수를 가지고 있는 칸이라면 멈춤
    if (doesCellHaveLandMineCount(currentCellPosition)) {
      return;
    }

    // 자기 주변에 있던 8개를 탐색 (재귀)
    List<CellPosition> surroundedPositions =
      calculateSurroundedPositions(currentCellPosition, getRowSize(), getColSize()); // 게임판을 벗어나는지 체크
    for (CellPosition surroundedPosition : surroundedPositions) {
      deque.push(surroundedPosition);
    }
  }

  private void openOneCellAt(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    cell.open();
  }

  private boolean isOpenedCell(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.isOpened();
  }

  private boolean isLandMineCellAt(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.isLandMine();
  }

  private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.hasLandMineCount();
  }

  private void checkIfGameIsOver() {
    if (isAllCellChecked()) {
      changeGameStatusToWin();
    }
  }

  private boolean isAllCellChecked() {
    Cells cells = Cells.from(board);
    return cells.isAllChecked(); // 모든 셀이 다 체크됐는지 확인
  }

  private void changeGameStatusToWin() {
    gameStatus = GameStatus.WIN;
  }

  private void changeGameStatusToLose() {
    gameStatus = GameStatus.LOSE;
  }

  private Cell findCell(CellPosition cellPosition) {
    return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
  }

}
