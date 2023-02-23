package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.RandomStoolGenerator;
import util.StoolGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("사다리는 ")
class LadderTest {
    @DisplayName("1 이상 100 이하의 높이만 가능하다")
    @ParameterizedTest
    @ValueSource(ints = {1, 50, 100})
    void height1_100(int height) {
        Ladder ladder = initLadder(height, 5);

        assertThat(ladder.getHeight()).isBetween(1, 100);
    }

    @DisplayName("1 이상 100 이하의 높이가 아니면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {-2, 101})
    void heightNot1_100(int height) {
        assertThatThrownBy(() -> Ladder.of(height, 5, new RandomStoolGenerator()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 높이는 1부터 100까지만 가능합니다");
    }

    @DisplayName("참여자수 - 1 만큼의 열을 가진다")
    @Test
    void columnSizeParticipantsMinusOne() {
        int participantSize = 5;
        Ladder ladder = initLadder(4, participantSize);

        assertThat(ladder.getColumnSize()).isEqualTo(participantSize - 1);
    }

    @DisplayName("발판은 연속될 수 없다")
    @Test
    void stoolNotContinuous() {
        Ladder ladder = initLadder(4, 5);

        assertThat(ladder.getLevels())
                .allSatisfy(level -> assertThat(level.getStools())
                        .doesNotContainSequence(Stool.EXIST, Stool.EXIST));
    }

    @DisplayName("한 라인에는 반드시 하나의 발판이 있어야 한다")
    @Test
    void lineMustHaveStool() {
        Ladder ladder = initLadder(4, 6);

        assertThat(ladder.getLevels())
                .allSatisfy(level -> assertThat(level.getStools()).contains(Stool.EXIST));
    }

    @DisplayName("시작점과 끝 점은 1대1 매칭이 된다")
    @Test
    void runLadder() {
        List<Integer> participants = List.of(0, 1, 2, 3, 4);
        Ladder ladder = initLadder(4, participants.size());

        assertThat(participants)
                .map(ladder::getResult)
                .containsAll(participants);
    }

    private static Ladder initLadder(int height, int participantSize) {
        return Ladder.of(height, participantSize, new TestGenerator());
    }

    private static class TestGenerator implements StoolGenerator {
        List<Stool> stools = List.of(Stool.EXIST, Stool.EMPTY);
        int index = 1;

        @Override
        public Stool next() {
            index = (index + 1) % 2;
            return stools.get(index);
        }
    }
}
