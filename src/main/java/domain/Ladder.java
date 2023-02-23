package domain;

import exception.Error;
import util.StoolGenerator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class Ladder {
    private static final int MIN_HEIGHT = 1;
    private static final int MAX_HEIGHT = 100;

    private final List<Level> levels;

    private Ladder(List<Level> level) {
        this.levels = level;
    }

    public static Ladder of(int height, int participantSize, StoolGenerator stoolGenerator) {
        validate(height);

        return Stream.generate(() -> new Level(participantSize, stoolGenerator))
                .limit(height)
                .collect(collectingAndThen(toList(), Ladder::new));
    }

    private static void validate(int height) {
        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new IllegalArgumentException(Error.HEIGHT_RANGE_FROM_1_TO_100.getMessage());
        }
    }

    public int getResult(int position) {
        return getResult(position, 0);
    }

    public int getResult(int position, int currentLevel) {
        if (currentLevel == getHeight()) {
            return position;
        }

        int nextPosition = nextPosition(position, currentLevel);
        return getResult(nextPosition, currentLevel + 1);
    }

    private int nextPosition(int position, int currentLevel) {
        Stool right = getStool(position, currentLevel);
        Stool left = getStool(position - 1, currentLevel);

        if (right.isExist()) {
            return position + 1;
        }
        if (left.isExist()) {
            return position - 1;
        }

        return position;
    }

    private Stool getStool(int position, int currentLevel) {
        if (position < 0 || position >= getColumnSize()) {
            return Stool.EMPTY;
        }

        return levels.get(currentLevel)
                .getStools()
                .get(position);
    }

    public int getHeight() {
        return levels.size();
    }

    public int getColumnSize() {
        return levels.get(0).size();
    }

    public List<Level> getLevels() {
        return Collections.unmodifiableList(levels);
    }
}
