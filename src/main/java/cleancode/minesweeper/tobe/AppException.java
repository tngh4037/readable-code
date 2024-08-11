package cleancode.minesweeper.tobe;

// 프로그램 내에서 발생할 수 있는 의도한(예상한) 예외 정의
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
