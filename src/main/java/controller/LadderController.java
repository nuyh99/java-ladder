package controller;

import domain.Ladder;
import domain.Level;
import domain.People;
import domain.Stool;
import view.InputView;
import view.OutputView;

import java.util.List;
import java.util.stream.Collectors;

public class LadderController {
    private final InputView inputView;
    private final OutputView outputView;

    private People people;
    private Ladder ladder;

    public LadderController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void init() {
        repeat(() -> people = People.from(inputView.readNames()));
        repeat(() -> ladder = Ladder.from(inputView.readHeight(), people.size()));
    }

    private void repeat(Runnable repeatable) {
        try {
            repeatable.run();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            repeat(repeatable);
        }
    }

    public void showLadder() {
        outputView.printResult(people.getNames(), getLadder());
    }

    private List<List<Boolean>> getLadder() {
        return ladder.getLevels().stream()
                .map(this::getLevel)
                .collect(Collectors.toList());
    }

    private List<Boolean> getLevel(Level level) {
        return level.getStools().stream()
                .map(Stool::isExist)
                .collect(Collectors.toList());
    }
}
