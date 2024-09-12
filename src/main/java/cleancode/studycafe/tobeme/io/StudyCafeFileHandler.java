package cleancode.studycafe.tobeme.io;

import cleancode.studycafe.tobeme.model.StudyCafeLockerPass;
import cleancode.studycafe.tobeme.model.StudyCafePass;
import cleancode.studycafe.tobeme.define.StudyCafePassType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudyCafeFileHandler {

    private static final String PASS_LIST_CSV_PATH = "src/main/resources/cleancode/studycafe/pass-list.csv";
    private static final String LOCKER_CSV_PATH = "src/main/resources/cleancode/studycafe/locker.csv";

    public List<StudyCafePass> readStudyCafePassesBy(StudyCafePassType passType) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PASS_LIST_CSV_PATH));
            List<StudyCafePass> studyCafePasses = new ArrayList<>();
            for (String line : lines) {
                String[] values = line.split(",");
                StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
                int duration = Integer.parseInt(values[1]);
                int price = Integer.parseInt(values[2]);
                double discountRate = Double.parseDouble(values[3]);

                StudyCafePass studyCafePass = StudyCafePass.of(studyCafePassType, duration, price, discountRate);
                studyCafePasses.add(studyCafePass);
            }

            return studyCafePasses.stream()
                .filter(s -> s.getPassType() == passType)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }

    public StudyCafeLockerPass readLockerPassesBy(StudyCafePass studyCafePass) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(LOCKER_CSV_PATH));
            List<StudyCafeLockerPass> lockerPasses = new ArrayList<>();
            for (String line : lines) {
                String[] values = line.split(",");
                StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
                int duration = Integer.parseInt(values[1]);
                int price = Integer.parseInt(values[2]);

                StudyCafeLockerPass lockerPass = StudyCafeLockerPass.of(studyCafePassType, duration, price);
                lockerPasses.add(lockerPass);
            }

            return lockerPasses.stream()
                .filter(option ->
                    option.getPassType() == studyCafePass.getPassType()
                        && option.getDuration() == studyCafePass.getDuration()
                )
                .findFirst()
                .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
        }
    }

}
