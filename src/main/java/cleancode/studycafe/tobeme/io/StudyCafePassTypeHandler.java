package cleancode.studycafe.tobeme.io;

import cleancode.studycafe.tobeme.define.StudyCafePassType;
import cleancode.studycafe.tobeme.model.StudyCafeLockerPass;
import cleancode.studycafe.tobeme.model.StudyCafePass;

import java.util.List;

public class StudyCafePassTypeHandler {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public void run(StudyCafePassType studyCafePassType) {
        List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePassesBy(studyCafePassType);
        outputHandler.showPassListForSelection(studyCafePasses);
        StudyCafePass selectedPass = inputHandler.getSelectPass(studyCafePasses);

        StudyCafeLockerPass lockerPass = getLockerPass(studyCafePassType, selectedPass);
        outputHandler.showPassOrderSummary(selectedPass, lockerPass);
    }

    private StudyCafeLockerPass getLockerPass(StudyCafePassType studyCafePassType, StudyCafePass studyCafePass) {
        if (!studyCafePassType.isLockerAvailable()) {
            return null;
        }

        StudyCafeLockerPass lockerPass = studyCafeFileHandler.readLockerPassesBy(studyCafePass);
        if (lockerPass != null) {
            outputHandler.askLockerPass(lockerPass);

            boolean lockerSelection = inputHandler.getLockerSelection();
            if (lockerSelection) {
                return lockerPass;
            }
        }

        return null;
    }
}
