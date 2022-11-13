package org.imageghost.Wallet;

public class FileWallet implements Wallet{
    String fileOwnerPublicKey;
    String fileCipherText;
    String merkleTreeRootValue;

    public void setFileCipherText(String fileCipherText) {
        this.fileCipherText = fileCipherText;
    }

    public void setFileOwnerPublicKey(String fileOwnerPublicKey) {
        this.fileOwnerPublicKey = fileOwnerPublicKey;
    }

    public void setMerkleTreeRootValue(String merkleTreeRootValue) {
        this.merkleTreeRootValue = merkleTreeRootValue;
    }

    public String getFileCipherText() {
        return fileCipherText;
    }

    public String getMerkleTreeRootValue() {
        return merkleTreeRootValue;
    }

    public String getFileOwnerPublicKey() {
        return fileOwnerPublicKey;
    }
}
