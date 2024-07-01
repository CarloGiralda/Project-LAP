package LAP.Blockchain.model;

import LAP.Blockchain.dto.Block;
import LAP.Blockchain.dto.Payment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;


@Component
@Getter
@Setter
public class Blockchain {
    private ArrayList<Block> blocks;

    public Blockchain() {
        blocks = new ArrayList<>();
        generateGenesis();
    }

    public void generateGenesis() {
        Payment p = new Payment(0L, "Genesis Block","-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz6oKArbCAQxTED6Fg8Hn0pzT0HkzO2Hoqrl04i7WReZtevN5Mq1Rbk/Ix6a0sb/5w1FzmKcLF/Wc9xO98/9nOvysXkdqOAoRi5PaY3GJgPqigg6dWQyvTqh3JiWmqUqs/XLTMafiplLsnglzNsdDIn67HA67XzHtWVjURqMyRasTiUMsWj1sBLyO4IoKG+mhzvSQ9FEM9UPsSf98pV5HtygiSI4vzsEPVxkgDPc8TeCvD8UUgP7keIEgxddbjoBL3/Yjnf/XME4A+BwCPUHoJTd9zp74LriuUMdnewM7mgwFgu/HVTB4ol/+Uhcy29A/ZAx1pam8M3sryE6mfN4BeQIDAQAB-----END PUBLIC KEY-----","1000000","");
        Payment p1 = new Payment(1L, "Genesis Block", "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2MD5srCpW4yXy/jPSkdWkOohbRi/b/N5OnHtxqH0frQ0YUvAEyhMEjsLvSojRviaEH6enrgp35lLOQFsVpainB7rNi7W/dJ/m8TYoA0F60lZNpthUfi3iAGYYdYHbpxubciGm6F8uBqKDaBP5bW0cgGvh38egwy4wRGWHrIjnV0uBFlL36lDjXUG/izth0T4JHukDcqxW0gdxjo2ROYZL/pZP11wzx6Qzx/Q/qRbfAPznfSEiJSuHQn1D7KBPmSdgsWJWjEL5YaU0msNNVWhnRedpxqo0FFJj6JNv9ToJ8cLZ1py3HlDEg6jDV8M7xZ5IqwhhKoHpGifNW0zFQm9/wIDAQAB-----END PUBLIC KEY-----", "1000", "");
        Block genesis = new Block("None", p);
        genesis.addToBlock(p1);
        blocks.add(genesis);
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
