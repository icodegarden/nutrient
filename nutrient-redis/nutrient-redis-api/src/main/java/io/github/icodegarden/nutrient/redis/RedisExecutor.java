package io.github.icodegarden.nutrient.redis;

import java.io.IOException;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface RedisExecutor extends KeyBinaryCommands, StringBinaryCommands, HashBinaryCommands, ListBinaryCommands,
		SetBinaryCommands, SortedSetBinaryCommands, BitmapBinaryCommands, GeoBinaryCommands, HyperLogLogBinaryCommands,
		ScriptingBinaryCommands, PubSubBinaryCommands, StreamBinaryCommands {

	void close() throws IOException;

}
