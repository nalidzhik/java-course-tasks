package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Crypto;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.Quote;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteServiceImpl;

public class Main {

    public static void main(String[] args) throws WalletException {
        Asset ethereum=new Crypto("ETH", "Ethereum");

        QuoteServiceImpl quoteService=new QuoteServiceImpl();
        quoteService.addQuote(ethereum,new Quote(4000,4100));

        Wallet wallet=new InvestmentWallet(quoteService);
        wallet.deposit(5000);

        Acquisition acquisition =  wallet.buy(ethereum,1,5000);

        for (Acquisition a : wallet.getAllAcquisitions()) {
            System.out.println(a);
        }
    }
}
