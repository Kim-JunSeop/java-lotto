package gmbs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WinningResultTest {
    @Test
    @DisplayName("상금합 구하기")
    void calculate() {
        // given
        List<Ranking> lottoNumbersList = new ArrayList<>();
        lottoNumbersList.add(Ranking.FIFTH);
        lottoNumbersList.add(Ranking.FIFTH);
        lottoNumbersList.add(Ranking.FOURTH);


        // when

        // then
        int expect = Ranking.FIFTH.getPrize() * 2 + Ranking.FOURTH.getPrize();
        WinningResult winningResult = new WinningResult(lottoNumbersList);
        assertThat(winningResult.calculatePrizeSum()).isEqualTo(expect);
    }
}