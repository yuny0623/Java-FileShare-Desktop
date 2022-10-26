package org.imageghost.Wallet.MerkleTree;

public class Node {
    public Node left;
    public Node right;
    public String hash;

    public Node(Node left, Node right, String hash){
        this.left = left;
        this.right = right;
        this.hash = hash;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
