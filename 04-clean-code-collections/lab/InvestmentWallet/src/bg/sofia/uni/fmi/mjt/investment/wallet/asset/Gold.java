package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

import java.util.Objects;

public class Gold implements Asset {

    private String id;
    private String name;

    @Override
    public String toString() {
        return "Gold{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Gold(String id, String name) {
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
        return AssetType.GOLD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gold gold = (Gold) o;
        return id.equals(gold.id) && name.equals(gold.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}


