package view;

import exception.Error;

import java.util.List;
import java.util.Scanner;

public class InputView {
	private final Scanner scanner = new Scanner(System.in);

	public List<String> readNames() {
		System.out.println("참여할 사람 이름을 입력하세요. (이름은 쉼표(,)로 구분하세요)");

		return splitByComma(scanner.nextLine());
	}

	public int readHeight() {
		try {
			System.out.println("\n최대 사다리 높이는 몇 개인가요?");

			return Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(Error.NOT_A_NUMBER.getMessage());
		}
	}

	public List<String> readResults() {
		System.out.println("실행 결과를 입력하세요. (결과는 쉼표(,)로 구분하세요)");

		return splitByComma(scanner.nextLine());
	}

	private List<String> splitByComma(String input) {
		return List.of(input.split(","));
	}
}
