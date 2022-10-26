package org.imageghost.Test;

import org.imageghost.Wallet.MerkleTree.MerkleTree;
import org.imageghost.Wallet.MerkleTree.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MerkleTreeTest {

    @Test
    public void 머클트리생성테스트(){
        // given
        ArrayList<String> dataBlocks = new ArrayList<>();
        dataBlocks.add("Captain America");
        dataBlocks.add("Iron Man");
        dataBlocks.add("God of thunder");
        dataBlocks.add("Doctor strange");

        // when
        Node root = MerkleTree.generateTree(dataBlocks);
        MerkleTree.printLevelOrderTraversal(root);

        // then
        Assert.assertNotNull(root);
    }
}
