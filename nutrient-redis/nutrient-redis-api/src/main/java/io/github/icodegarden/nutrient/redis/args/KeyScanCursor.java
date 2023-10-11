package io.github.icodegarden.nutrient.redis.args;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author Fangfang.Xu
 *
 * @param <K>
 */
@Getter
@ToString(callSuper = true)
public class KeyScanCursor<K> extends ScanCursor {

	private List<K> keys;

	public KeyScanCursor(Object cursor, boolean finished, List<K> keys) {
		super(cursor, finished);
		this.keys = keys;
	}
}