package org.imageghost.Wallet.MerkleTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MerkleTree {
    /*
        참고할 만한 블로그:
        https://www.pranaybathini.com/2021/05/merkle-tree.html

        머클 트리는 대용량 파일의 무결성을 검증하는데 쓰일 수 있다
        조각으로 나눈 뒤 -> 해쉬 돌리고 -> 트리 구성.
        특정 블락만 hash가 깨졌으면 그 블락만 따로 전달받으면 됨.
     */
    public static Node generateTree(ArrayList<String> dataBlocks){
        ArrayList<Node> childNodes = new ArrayList<>();
        for(String message: dataBlocks){
            childNodes.add(new Node(null, null, HashAlgorithm.generateHash(message)));
        }
        return buildTree(childNodes);
    }

    public static Node buildTree(ArrayList<Node> children){
        ArrayList<Node> parents = new ArrayList<>();
        while(children.size() != 1){
            int index = 0, length = children.size();
            while(index < length){
                Node leftChild = children.get(index);
                Node rightChild = null;

                if((index + 1) < length){
                    rightChild = children.get(index + 1);
                }else{
                    rightChild = new Node(null, null, leftChild.getHash());
                }

                String parentHash = HashAlgorithm.generateHash(leftChild.getHash() + rightChild.getHash());
                parents.add(new Node(leftChild, rightChild, parentHash));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }

    public static void printLevelOrderTraversal(Node root){
        if(root == null){
            return;
        }
        if((root.getLeft() == null && root.getRight() == null)){
            System.out.println(root.getHash());
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while(!queue.isEmpty()){
            Node node = queue.poll();
            if (node != null) {
                System.out.println(node.getHash());
            }else{
                System.out.println();
                if(!queue.isEmpty()){
                    queue.add(null);
                }
            }

            // Preorder Traversal
            if(node != null && node.getLeft() != null){
                queue.add(node.getLeft());
            }

            if(node != null && node.getRight() != null){
                queue.add(node.getRight());
            }
        }
    }
}
