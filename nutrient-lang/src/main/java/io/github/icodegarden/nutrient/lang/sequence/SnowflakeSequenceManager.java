package io.github.icodegarden.nutrient.lang.sequence;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class SnowflakeSequenceManager implements SequenceManager {

	private final static long START_STMP = 1288834974657L;
//    private final static long START_STMP = 1480166465631L;

	/**
	 * 以下3项不是final，可以视情况调整参数，例如把SEQUENCE_BIT=10，MACHINE_BIT=6，DATACENTER_BIT=6来增加支持的实例数，但降低每个实例每毫秒产生的数量
	 */
	private static long SEQUENCE_BIT = 12; // 序列号占用的位数 默认每个实例1毫秒可产生4096个
	private static long DATACENTER_BIT = 5;// 数据中心占用的位数，默认0-31
	private static long MACHINE_BIT = 5; // 机器标识占用的位数，默认0-31

	private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
	private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
	private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

	private final static long MACHINE_LEFT = SEQUENCE_BIT;
	private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
	private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

	private final long datacenterId;
	private final long machineId;
	private volatile long sequence = 0L;// 本时间戳的seq 例如0-4095
	private volatile long lastStmp = -1L;

	private volatile long currentId = -1;

	public SnowflakeSequenceManager(long datacenterId, long machineId) {
		if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
			throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		}
		if (machineId > MAX_MACHINE_NUM || machineId < 0) {
			throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
		}
		this.datacenterId = datacenterId;
		this.machineId = machineId;
	}

//	0 1 2 3 ... 31
//	32 33 34 ... 63
//	64 65 66 ... 95
//	96 97 98 ... 127
	public static long extractDatacenterId(long value) {
		return value / (1 << DATACENTER_BIT);
	}

	public static long extractMachineId(long value) {
		return value % (1 << MACHINE_BIT);
	}

	@Override
	public long currentId() {
		if (currentId == -1) {
			return nextId();
		}
		return currentId;
	}

	@Override
	public synchronized long nextId() {
		long currStmp = getNewstmp();
		if (currStmp < lastStmp) {
			throw new IllegalArgumentException("Clock moved backwards.  Refusing to generate id");
		}

		if (currStmp == lastStmp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0L) {
				currStmp = getNextMill();
			}
		} else {
			sequence = 0L;
		}

		lastStmp = currStmp;

		currentId = (currStmp - START_STMP) << TIMESTMP_LEFT | datacenterId << DATACENTER_LEFT
				| machineId << MACHINE_LEFT | sequence;
		return currentId;
	}

	private long getNextMill() {
		long mill = getNewstmp();
		while (mill <= lastStmp) {
			mill = getNewstmp();
		}
		return mill;
	}

	private long getNewstmp() {
		return System.currentTimeMillis();
	}
}