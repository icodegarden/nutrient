package io.github.icodegarden.nutrient.exchange.nio;


import io.github.icodegarden.nutrient.exchange.Protocol;
import io.github.icodegarden.nutrient.exchange.ProtocolParams;
import io.github.icodegarden.nutrient.lang.exception.remote.RemoteException;
import io.github.icodegarden.nutrient.nio.NioClient;
import io.github.icodegarden.nutrient.nio.pool.NioClientPool;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class NioProtocol implements Protocol {
//	private static final Logger log = LoggerFactory.getLogger(NioProtocol.class);

	private final NioClientPool nioClientPool;

	public NioProtocol(NioClientPool nioClientPool) {
		this.nioClientPool = nioClientPool;
	}

	@Override
	public <R> R exchange(ProtocolParams params) throws RemoteException {
		NioClient nioClient = nioClientPool.getElseSupplier(params.getIp(), params.getPort());
		return nioClient.request(params.getBody(), params.getTimeout());
	}

}
