package Wallet;

public class SymmetricKey implements EncKey{
    private String AESKey;
    private String description;

    @Override
    public String getKey() {
        return this.AESKey;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
