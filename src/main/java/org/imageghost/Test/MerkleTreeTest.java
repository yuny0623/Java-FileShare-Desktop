package org.imageghost.Test;

import org.imageghost.Wallet.MerkleTree.MerkleTree;
import org.imageghost.Wallet.MerkleTree.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MerkleTreeTest {

    @Before
    public void 셋업(){
        /*
            머클트리 객체 생성
         */
    }

    @Test
    public void 머클트리생성테스트(){
        ArrayList<String> dataBlocks = new ArrayList<>();
        dataBlocks.add("Captain America");
        dataBlocks.add("Iron Man");
        dataBlocks.add("God of thunder");
        dataBlocks.add("Doctor strange");
        Node root = MerkleTree.generateTree(dataBlocks);
        MerkleTree.printLevelOrderTraversal(root);
    }

    @Test
    public void 머클트리복원테스트(){
        // given


        // when


        // then


    }

}
