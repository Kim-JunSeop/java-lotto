package gmbs.controller;

import gmbs.domain.LottoAmount;
import gmbs.domain.LottoNumber;
import gmbs.domain.LottoNumbers;
import gmbs.domain.LottoTicket;
import gmbs.domain.WinningNumbers;
import gmbs.domain.WinningResult;
import gmbs.view.InputView;
import gmbs.view.OutputView;

public class LottoController {
    public void start() {
        LottoAmount amount = inputAmount();

        LottoTicket lottoTicket = buyTickets(amount);

        WinningNumbers winningNumbers = createWinningNumbers();

        WinningResult winningResult = getWinningResult(lottoTicket, winningNumbers);

        printResult(amount, winningResult);
    }

    private LottoAmount inputAmount() {
        try {
            return new LottoAmount(InputView.inputAmount());
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return inputAmount();
        }
    }

    private LottoTicket buyTickets(LottoAmount amount) {
        int ticketCount = amount.calculateLottoCount();
        OutputView.printTicketCount(ticketCount);

        LottoTicket lottoTicket = new LottoTicket(ticketCount);
        OutputView.printTicket(lottoTicket);
        return lottoTicket;
    }

    private WinningNumbers createWinningNumbers() {
        LottoNumbers inputLottoNumbers = getInputLottoNumbers();
        LottoNumber bonusNumber = getBonusNumber();

        return getWinningNumbers(inputLottoNumbers, bonusNumber);
    }

    private LottoNumbers getInputLottoNumbers() {
        try {
            return new LottoNumbers(InputView.inputWinningNumbers());
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return getInputLottoNumbers();
        }
    }

    private LottoNumber getBonusNumber() {
        try {
            return LottoNumber.of(InputView.inputBonusBall());
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return getBonusNumber();
        }
    }

    private WinningNumbers getWinningNumbers(LottoNumbers lottoNumbers, LottoNumber bonusNumber) {
        try {
            return new WinningNumbers(lottoNumbers, bonusNumber);
        } catch (IllegalArgumentException e) {
            OutputView.printException(e);
            return getWinningNumbers(lottoNumbers, getBonusNumber());
        }
    }

    private WinningResult getWinningResult(LottoTicket lottoTicket, WinningNumbers winningNumbers) {
        return lottoTicket.calculateWinningStatistic(winningNumbers);
    }

    private void printResult(LottoAmount amount, WinningResult winningResult) {
        OutputView.printResultIntro();
        OutputView.printWinningStatistic(winningResult);
        OutputView.printProfit(amount.calculateProfit(winningResult.calculatePrizeSum()));
    }

}
