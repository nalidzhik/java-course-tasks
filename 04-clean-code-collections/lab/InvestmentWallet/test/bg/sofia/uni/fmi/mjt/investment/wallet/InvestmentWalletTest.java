package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Crypto;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.Quote;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvestmentWalletTest {

    private static final Asset ETH_ASSET = new Crypto("ETH", "Ethereum");

    @Test
    public void testDepositSuccessfullyReturnsBalance() {
        Wallet wallet = new InvestmentWallet(null);

        double actual = wallet.deposit(100);
        assertEquals(100, actual, 0.0001);

        actual = wallet.deposit(50);
        assertEquals(150, actual, 0.0001);
    }

    @Test
    public void testDepositThrowsExceptionWhenCashIsNegative() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.deposit(-50);
        }, "IllegalArgumentException was expected");
    }

    @Test
    public void testWithdrawThrowsExceptionWhenCashIsNegative() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.withdraw(-70);
        }, "IllegalArgumentException was expected");
    }

    @Test
    public void testWithdrawThrowsExceptionWhenBalanceIsInsufficientToProceed() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(InsufficientResourcesException.class, () -> {
            wallet.withdraw(100);
        }, "InsufficientResourcesException was expected");
    }

    @Test
    public void testWithdrawSuccessfullyReturnsBalance() throws InsufficientResourcesException {
        Wallet wallet = new InvestmentWallet(null);

        wallet.deposit(1000);
        double actual = wallet.withdraw(180);
        assertEquals(820, actual, 0.0001);

        actual = wallet.withdraw(50);
        assertEquals(770, actual, 0.0001);
    }

    @Test
    public void testBuyThrowsExceptionWhenAssetIsNull() throws WalletException {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.buy(null, 3, 79);
        }, "IllegalArgumentException was expected");
    }

    @Test
    public void testBuyThrowsExceptionWhenQuantityIsNegative() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.buy(ETH_ASSET, -9, 34);
        }, "IllegalArgumentException was expected");
    }

    @Test
    public void testBuyThrowsExceptionWhenMaxPriceIsNegative() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.buy(ETH_ASSET, 1, -34);
        }, "IllegalArgumentException was expected");
    }

    @Test
    public void testBuyThrowsExceptionWhenQuoteIsNull() {
        QuoteService quoteService = new QuoteServiceImpl();
        Wallet wallet = new InvestmentWallet(quoteService);

        assertThrows(UnknownAssetException.class, () -> {
            wallet.buy(ETH_ASSET, 1, 34);
        }, "UnknownAssetException was expected");
    }

    @Test
    public void testBuyThrowsExceptionWhenQuoteAskPriceIsHigherThanMaxPrice() {
        QuoteServiceImpl quoteService = new QuoteServiceImpl();
        quoteService.addQuote(ETH_ASSET, new Quote(100, 110));
        Wallet wallet = new InvestmentWallet(quoteService);

        assertThrows(OfferPriceException.class, () -> {
            wallet.buy(ETH_ASSET, 1, 89);
        }, "OfferPriceException was expected");
    }

    @Test
    public void testBuyThrowsExceptionWhenBalanceIsNotEnough() {
        QuoteServiceImpl quoteService = new QuoteServiceImpl();
        quoteService.addQuote(ETH_ASSET, new Quote(100, 110));
        Wallet wallet = new InvestmentWallet(quoteService);

        assertThrows(InsufficientResourcesException.class, () -> {
            wallet.buy(ETH_ASSET, 1, 200);
        }, "InsufficientResourcesException was expected");
    }

    @Test
    public void testBuySuccessfullyReturnsAcquisition() throws WalletException {
        QuoteServiceImpl quoteService = new QuoteServiceImpl();
        quoteService.addQuote(ETH_ASSET, new Quote(100, 110));
        Wallet wallet = new InvestmentWallet(quoteService);

        wallet.deposit(200);
        Acquisition actual = wallet.buy(ETH_ASSET, 1, 200);

        assertEquals(100, actual.getPrice(), 0.0001);
        assertEquals(1, actual.getQuantity());
        assertEquals(ETH_ASSET, actual.getAsset());
    }

    @Test
    public void testGetValuationThrowsExceptionWhenAssetIsNull() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(IllegalArgumentException.class, () -> {
            wallet.getValuation(null);
        }, "IllegalArgumentException was expected");
    }

    @Test
    public void testGetValuationThrowsExceptionWhenAssetIsNotInWallet() {
        Wallet wallet = new InvestmentWallet(null);

        assertThrows(UnknownAssetException.class, () -> {
            wallet.getValuation(ETH_ASSET);
        }, "UnknownAssetException was expected");
    }

    @Test
    public void testGetValuationThrowsExceptionWhenQuoteIsNull() {
        QuoteService quoteService=new QuoteServiceImpl();
        Wallet wallet = new InvestmentWallet(quoteService);

        assertThrows(UnknownAssetException.class, () -> {
            wallet.getValuation(ETH_ASSET);
        }, "UnknownAssetException was expected");
    }

    @Test
    public void testGetValuationSuccessfullyReturnValuation() throws WalletException {
        QuoteServiceImpl quoteService = new QuoteServiceImpl();
        quoteService.addQuote(ETH_ASSET, new Quote(100, 110));
        Wallet wallet = new InvestmentWallet(quoteService);

        double actualBalance=wallet.deposit(300);
        assertEquals(300,actualBalance,0.0001);

        wallet.buy(ETH_ASSET,1,200);
        double actualValuation=wallet.getValuation(ETH_ASSET);

        assertEquals(110,actualValuation,0.0001);

        quoteService.addQuote(ETH_ASSET,new Quote(100,120));
        actualValuation=wallet.getValuation(ETH_ASSET);

        assertEquals(120,actualValuation,0.0001);
    }
}
