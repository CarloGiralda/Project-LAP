package LAP.Blockchain.model;

import LAP.Blockchain.dto.Block;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
@Setter
public class Blockchain {
    private ArrayList<Block> blocks;

    public Blockchain() {
        blocks = new ArrayList<>();
    }

    public List<ArrayList<Block>> findBranches() {
        List<ArrayList<Block>> branches = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        for (int i = blocks.size()-1; i >= 0; i--) {
            if (!visited.contains(blocks.get(i).getTimeStamp())) {
                ArrayList<Block> branch = traverseBranch(blocks.get(i), visited);
                branches.add(branch);
            }
        }

        return branches;
    }

    private ArrayList<Block> traverseBranch(Block block,  Set<Long> visited) {
        ArrayList<Block> branch = new ArrayList<>();
        Block currentBlock = block;

        while (currentBlock != null ) {
            branch.add(currentBlock);
            if (!visited.contains(currentBlock.getTimeStamp())) {
                visited.add(currentBlock.getTimeStamp());
            }

            // Find the next block in the branch based on the previous hash
            Block nextBlock = findBlockByHash(currentBlock.getPreviousHash());
            if (nextBlock != null) {
                currentBlock = nextBlock;
            } else {
                currentBlock = null;
            }
        }

        return branch;
    }

    private Block findBlockByHash(String targetHash) {
        for (Block block : blocks) {
            if (block.getHash().equals(targetHash)) {
                return block;
            }
        }
        return null;
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public Block getLastBlock() {
        int last = blocks.size() - 1;
        return blocks.get(last);
    }
}
