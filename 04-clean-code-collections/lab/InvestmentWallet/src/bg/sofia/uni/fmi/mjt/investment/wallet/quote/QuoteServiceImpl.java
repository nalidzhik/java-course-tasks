package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.util.HashMap;
import java.util.Map;

public class QuoteServiceImpl implements QuoteService {

    private Map<Asset, Quote> quotes;

    public QuoteServiceImpl() {
        this.quotes = new HashMap<>();
    }

    public void addQuote(Asset asset, Quote quote) {
        this.quotes.put(asset, quote);
    }

    @Override
    public Quote getQuote(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("The asset must not be null");
        }

        return this.quotes.get(asset);
    }
}
