package bg.sofia.uni.fmi.mjt.investment.wallet.exception;

public class WalletException extends RuntimeException {
    public WalletException() {
        super();
    }

    public WalletException(String errorMessage) {
        super(errorMessage);
    }
}
