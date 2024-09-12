package cleancode.studycafe.tobeme.define;

import cleancode.studycafe.tobeme.exception.AppException;
import cleancode.studycafe.tobeme.io.InputHandler;
import cleancode.studycafe.tobeme.io.OutputHandler;

import java.util.Arrays;

public enum StudyCafePassType {

    HOURLY("1", "시간 단위 이용권"),
    WEEKLY("2", "주 단위 이용권"),
    FIXED("3", "1인 고정석");

    private final String code;
    private final String description;

    private static final InputHandler inputHandler = new InputHandler();
    private static final OutputHandler outputHandler = new OutputHandler();

    StudyCafePassType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static StudyCafePassType findBy(String code) {
        return Arrays.stream(StudyCafePassType.values())
            .filter(v -> v.code.equals(code))
            .findAny()
            .orElseThrow(() -> new AppException("잘못된 입력입니다."));
    }

    public boolean isLockerAvailable() {
        return this == FIXED;
    }
}
