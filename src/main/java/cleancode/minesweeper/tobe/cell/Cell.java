package cleancode.minesweeper.tobe.cell;

public abstract class Cell {

    protected static final String FLAG_SIGN = "⚑";
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged;
    protected boolean isOpened;

    public abstract boolean isLandMine();

    public abstract boolean hasLandMineCount();

    public abstract String getSign();

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        isOpened = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened; // 닫혀있는지 깃발이 꽂혀있거나 or 진짜 열었거나
    }

    public boolean isOpened() {
        return isOpened;
    }
}

// 참고) 정적 팩토리 메소드
// new Cell(..)과 같이 객체를 만들 수도 있지만, 정적 팩토리 메소드로 이름을 주면서 만드는 것도 좋다.
// ( 비슷한 형태의 정적 팩토리 메서드 여러개가 생기면, 그에 따른 적절한 이름들을 각각 줄수도 있고, 검증 같은 것도 그에 맞게 해줄 수 있기 때문이다. )
