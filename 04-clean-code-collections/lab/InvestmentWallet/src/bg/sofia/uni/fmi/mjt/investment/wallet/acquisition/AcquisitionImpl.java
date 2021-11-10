package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

public class AcquisitionImpl implements Acquisition {

    private LocalDateTime timestamp;
    private double pricePerUnit;
    private int quantity;
    private Asset asset;

    public AcquisitionImpl(double pricePerUnit, int quantity, Asset asset) {
        this.timestamp = LocalDateTime.now();
        this.asset = asset;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
    }

    @Override
    public double getPrice() {
        return this.pricePerUnit;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public Asset getAsset() {
        return this.asset;
    }

    @Override
    public String toString() {
        return "AcquisitionImpl{" +
                "timestamp=" + timestamp +
                ", pricePerUnit=" + pricePerUnit +
                ", quantity=" + quantity +
                ", asset=" + asset +
                '}';
    }
}
