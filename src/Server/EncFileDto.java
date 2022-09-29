package Server;

class EncFileDto{
    private String ownerPublicKey;
    private String cipherText;
    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }
    public void setOwnerPublicKey(String ownerPublicKey){
        this.ownerPublicKey = ownerPublicKey;
    }
}