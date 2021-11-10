package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

import java.util.Objects;

public class Crypto implements Asset {
    private String id;
    private String name;

    public Crypto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public AssetType getType() {
        return AssetType.CRYPTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crypto crypto = (Crypto) o;
        return id.equals(crypto.id) && name.equals(crypto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

