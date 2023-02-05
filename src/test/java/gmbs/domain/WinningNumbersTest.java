package gmbs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WinningNumbersTest {

    private static final String DUPLICATE_ERROR = "[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다";

    private final List<Integer> numbers = getNumbers(1, 2, 3, 4, 5, 6);
    private final LottoNumbers lottoNumbers = new LottoNumbers(numbers);

    @Test
    @DisplayName("당첨번호와 보너스 번호가 중복되면 예외가 발생한다")
    void generateWinningNumbers_DuplicateWithWinningNumbers() {
        // given
        LottoNumber bonusNumber = LottoNumber.of(1);

        // when, then
        assertThatThrownBy(() -> new WinningNumbers(lottoNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DUPLICATE_ERROR);
    }

    @ParameterizedTest(name = "결과 {1} 반환")
    @MethodSource("parameterProvider")
    @DisplayName("[n]등 당첨 결과 반환")
    void findRank_First(List<Integer> list, Ranking expect) {
        // given
        LottoNumber bonusNumber = LottoNumber.of(7);
        WinningNumbers winningNumbers = new WinningNumbers(lottoNumbers, bonusNumber);
        LottoNumbers myLotto = new LottoNumbers(list);

        // when
        Ranking ranking = winningNumbers.calculateRanking(myLotto);

        // then
        assertThat(ranking).isEqualTo(expect);
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments(getNumbers(1, 2, 3, 4, 5, 6), Ranking.FIRST),
                Arguments.arguments(getNumbers(1, 2, 3, 4, 5, 7), Ranking.SECOND),
                Arguments.arguments(getNumbers(1, 2, 3, 4, 5, 8), Ranking.THIRD),
                Arguments.arguments(getNumbers(1, 2, 3, 4, 8, 9), Ranking.FOURTH),
                Arguments.arguments(getNumbers(1, 2, 3, 8, 9, 10), Ranking.FIFTH)
        );
    }

    @ParameterizedTest(name = "[{index}] LottoNumbers의 Ranking은 {1}")
    @MethodSource("parameterProvider2")
    @DisplayName("보너스 볼이 포함되는 경우")
    void findRank_Second(LottoNumbers userLottoNumbers, Ranking expect) {
        // given
        LottoNumber bonusNumber = LottoNumber.of(7);
        WinningNumbers winningNumbers = new WinningNumbers(lottoNumbers, bonusNumber);

        // when
        Ranking ranking = winningNumbers.calculateRanking(userLottoNumbers);

        // then
        assertThat(ranking).isEqualTo(expect);
    }

    private static Stream<Arguments> parameterProvider2() {
        return Stream.of(
                Arguments.arguments(new LottoNumbers(getNumbers(1, 2, 3, 7, 8, 9)), Ranking.FIFTH),
                Arguments.arguments(new LottoNumbers(getNumbers(4, 5, 6, 7, 8, 9)), Ranking.FIFTH)
        );
    }

    private static List<Integer> getNumbers(int... numbers) {
        return Arrays.stream(numbers)
                .boxed()
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }
}
