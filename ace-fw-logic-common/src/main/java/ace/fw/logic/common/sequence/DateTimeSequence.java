package ace.fw.logic.common.sequence;

import ace.fw.util.DateUtils;
import ace.fw.util.LocalHostUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/9 17:53
 * @description 与snowflake算法区别, 返回字符串id, 占用更多字节, 但直观从id中看出生成时间(主要用于订单号 、 流水号等生成)
 * 注：TODO 如果有多个IP的后三位都一样，有可能会产生一样记录
 * <p>
 * 用法：建议只生成一次当前实例
 * IdCodeGenerator idCodeGenerator = new IdCodeGenerator(255, "1");
 * idCodeGenerator.nextId();
 * idCodeGenerator.nextId();
 * idCodeGenerator.nextId();
 * idCodeGenerator.nextId();
 * idCodeGenerator.nextId();
 */
@Slf4j
public class DateTimeSequence {

    private long workerId;//ip（三位IP）
    private String businessCode;//业务编码号
    private volatile long sequence = 0L;

//    private long twepoch = 1288834974657L;

    private long workerIdBits = 8L;//最后三位ip（最大为255）
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long sequenceBits = 11L;

    private long workerIdShift = sequenceBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private volatile long lastTimestamp = timeGen();

    private static int ip = 0;

    static {
        String ipStr = LocalHostUtils.getIpCode().substring(LocalHostUtils.getIpCode().length() - 3, LocalHostUtils.getIpCode().length());
        ip = Integer.parseInt(ipStr);
    }

    /**
     * @param businessCode 业务编码号 如：0、1、2
     */
    public DateTimeSequence(String businessCode) {

        int workerId = ip;//Integer.parseInt(ip);

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (StringUtils.isBlank(businessCode)) {
            throw new IllegalArgumentException("businessCode can't be empty");
        }

        this.workerId = workerId;
        this.businessCode = businessCode;
        log.info(String.format("IdCodeGenerator starting. timestamp is %s, businessCode is %s, workerId is %d, worker id bits %d, sequence bits %d", DateUtils.FROMAT_yyyyMMddHHmmss, businessCode, workerId, workerIdBits, sequenceBits));
    }

    /**
     * @param businessCode 业务编码号 如：0、1、2
     */
    public DateTimeSequence(long workerId, String businessCode) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (StringUtils.isBlank(businessCode)) {
            throw new IllegalArgumentException("businessCode can't be empty");
        }
        this.workerId = workerId;
        this.businessCode = businessCode;
        log.info(String.format("IdCodeGenerator starting. timestamp is %s, businessCode is %s, workerId is %d, worker id bits %d, sequence bits %d", DateUtils.FROMAT_yyyyMMddHHmmss, businessCode, workerId, workerIdBits, sequenceBits));
    }

    public String nextId() {
        long timestamp;
        long seqNum = 0;
        synchronized (this) {
            timestamp = timeGen();
            if (timestamp < lastTimestamp) {
                log.error(String.format("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
                throw new RuntimeException
                        (String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            seqNum = sequence;
            lastTimestamp = timestamp;
        }

        long suffix = (workerId << workerIdShift) | seqNum;
        String datePrefix = DateUtils.format(timestamp, DateUtils.FROMAT_yyyyMMddHHmmssSS);
        return new StringBuilder(25)
                .append(businessCode)
                .append(datePrefix)
                .append(suffix)
                .toString();
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String args[]) {
        DateTimeSequence dateTimeSequence = new DateTimeSequence("A");
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
        System.out.println(dateTimeSequence.nextId());
    }

}