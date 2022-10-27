package org.imageghost.Wallet.FileTree;

public class Node {
    public Node left;
    public Node right;
    public String cipherText;

    public Node(Node left, Node right, String cipherText){
        this.left = left;
        this.right = right;
        this.cipherText = cipherText;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
