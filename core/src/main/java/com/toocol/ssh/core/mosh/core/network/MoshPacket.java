package com.toocol.ssh.core.mosh.core.network;

import com.toocol.ssh.core.mosh.core.crypto.ByteOrder;
import com.toocol.ssh.core.mosh.core.crypto.Crypto;

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/4/30 17:29
 * @version: 0.0.1
 */
public final class MoshPacket {
    static final int ADDED_BYTES = 8 /* seqno/nonce */ + 4 /* timestamp */;

    public enum Direction {
        TO_SERVER(0),
        TO_CLIENT(1)
        ;
        private final long idx;

        Direction(long idx) {
            this.idx = idx;
        }
    }

    private static final long DIRECTION_MASK = 1L << 63;
    private static final long SEQUENCE_MASK = ~DIRECTION_MASK;

    private final long seq = Crypto.unique();
    private final Direction direction;
    private final byte[] payload;
    private final short timestamp;
    private final short timestampReply;

    public MoshPacket(byte[] payload, Direction direction, short timestamp, short timestampReply) {
        this.payload = payload;
        this.direction = direction;
        this.timestamp = timestamp;
        this.timestampReply = timestampReply;
    }

    public Crypto.Message toMessage() {
        long directionSeq = (direction.idx << 63) | (seq & SEQUENCE_MASK);

        byte[] text = new byte[4 + payload.length];
        System.arraycopy(timestampsMerge(), 0, text, 0, 4);
        System.arraycopy(payload, 0, text, 4, payload.length);

        return new Crypto.Message(new Crypto.Nonce(directionSeq), text);
    }

    private byte[] timestampsMerge() {
        byte[] timestampBytes = ByteOrder.htoBe16(timestamp);
        byte[] timestampReplyBytes = ByteOrder.htoBe16(timestampReply);
        byte[] target = new byte[4];
        System.arraycopy(timestampBytes, 0, target, 0, 2);
        System.arraycopy(timestampReplyBytes, 0, target, 2, 2);
        return target;
    }
}
