package io.github.icodegarden.nutrient.exchange;

import java.util.List;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ParallelExchangeResult implements ExchangeResult {
	private static final long serialVersionUID = 8255692236629153404L;

	private final List<ShardExchangeResult> shardExchangeResults;

	public ParallelExchangeResult(List<ShardExchangeResult> shardExchangeResults) {
		if (shardExchangeResults == null) {
			throw new IllegalArgumentException("shardExchangeResults must not null");
		}

		this.shardExchangeResults = shardExchangeResults;
	}

	public int getShardTotal() {
		return shardExchangeResults.size();
	}

	/**
	 * 并行任务返回体是null
	 */
	@Override
	public Object response() {
		return null;
	}

	public List<ShardExchangeResult> getShardExchangeResults() {
		return shardExchangeResults;
	}
}