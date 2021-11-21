package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AcquisitionImpl;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.Quote;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.util.*;

public class InvestmentWallet implements Wallet {

    private QuoteService quoteService;

    private double balance;
    private List<Acquisition> acquisitions;
    private Map<Asset, Integer> assets;

    public InvestmentWallet(QuoteService quoteService) {
        this.quoteService = quoteService;
        this.acquisitions = new ArrayList<>();
        this.assets = new HashMap<>();
    }

    @Override
    public double deposit(double cash) {
        if (cash < 0) {
            throw new IllegalArgumentException("The deposit cash cannot be negative");
        }

        this.balance += cash;
        return balance;
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        if (cash < 0) {
            throw new IllegalArgumentException("The cash must be positive");
        }

        if (cash > balance) {
            throw new InsufficientResourcesException("The cash balance is insufficient to proceed with the withdrawal");
        }

        this.balance -= cash;
        return balance;
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {
        if (asset == null) {
            throw new IllegalArgumentException("The asset must not be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity must be positive");
        }
        if (maxPrice < 0) {
            throw new IllegalArgumentException("The price must be positive");
        }

        Quote quote = this.quoteService.getQuote(asset);
        if (quote == null) {
            String msg = String.format("There is no defined quote for asset %s", asset.getName());
            throw new UnknownAssetException(msg);
        }

        if (quote.askPrice() > maxPrice) {
            throw new OfferPriceException("The asset's ask price is higher than the max price");
        }

        double transactionPrice = quantity * quote.askPrice();
        if (balance < transactionPrice) {
            throw new InsufficientResourcesException("There is not enough balance for the transaction");
        }

        this.balance -= transactionPrice;
        this.assets.put(asset, quantity);

        Acquisition current = new AcquisitionImpl(quote.askPrice(), quantity, asset);
        acquisitions.add(current);

        return current;
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        if (asset == null) {
            throw new IllegalArgumentException("The asset must not be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("The quantity must be positive");
        }
        if (minPrice < 0) {
            throw new IllegalArgumentException("The price must be positive");
        }

        Integer quantityInWallet = this.assets.get(asset);
        if (quantityInWallet == null || quantityInWallet < quantity) {
            throw new InsufficientResourcesException("There is not enough quantity");
        }

        Quote quote = this.quoteService.getQuote(asset);
        if (quote == null) {
            String msg = String.format("There is no defined quote for asset %s", asset.getName());
            throw new UnknownAssetException(msg);
        }

        if (quote.bidPrice() < minPrice) {
            throw new OfferPriceException("The asset's bid price is lower than the min price");
        }

        double transactionPrice = quantity * quote.bidPrice();

        this.balance += transactionPrice;
        this.assets.put(asset, quantityInWallet - quantity);

        return transactionPrice;
    }

    @Override
    public double getValuation() {
        double sum = 0;
        for (var entry : this.assets.entrySet()) {
            Asset asset = entry.getKey();

            double current;
            try {
                current = this.getValuation(asset);
            } catch (UnknownAssetException e) {
                current = 0;
            }

            sum += current;
        }

        return sum;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        if (asset == null) {
            throw new IllegalArgumentException("Asset cannot be null");
        }

        if (!assets.containsKey(asset)) {
            throw new UnknownAssetException("Asset is not currently in the wallet");
        }

        Quote quote = this.quoteService.getQuote(asset);
        if (quote == null) {
            String msg = String.format("There is no defined quote for asset %s", asset.getName());
            throw new UnknownAssetException(msg);
        }

        int quantity = assets.get(asset);
        return quantity * quote.bidPrice();
    }

    @Override
    public Asset getMostValuableAsset() {
        double max = 0.0;
        Asset mostValuableAsset = null;
        for (Asset asset : this.assets.keySet()) {
            double currentValuation = getValuation(asset);
            if (max < currentValuation) {
                max = currentValuation;
                mostValuableAsset = asset;
            }
        }
        return mostValuableAsset;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return List.copyOf(acquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative");
        }
        if (n > acquisitions.size()) {
            n = acquisitions.size();
        }

        Set<Acquisition> nAcquisitions = new HashSet<Acquisition>();
        for (int i = acquisitions.size() - 1; n > 0; i--, n--) {
            nAcquisitions.add(acquisitions.get(i));
        }
        return Set.copyOf(nAcquisitions);
    }
}
