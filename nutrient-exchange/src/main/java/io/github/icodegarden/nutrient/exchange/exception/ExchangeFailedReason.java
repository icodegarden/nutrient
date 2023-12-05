package io.github.icodegarden.nutrient.exchange.exception;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@Getter
@ToString
public class ExchangeFailedReason implements Serializable {
	private static final long serialVersionUID = 1248382809990979544L;

	public static final String KEYWORD_CLINET_CONNECT_FAILED = "ClientConnectFailed";
	public static final String KEYWORD_REQUEST_TIMEOUT = "RequestTimeout";
	public static final String KEYWORD_CLIENT_INVALID = "ClientInvalid";
	public static final String KEYWORD_CLIENT_EXCEPTION = "ClientException";
	public static final String KEYWORD_SERVER_REJECTED = "ServerRejected";
	public static final String KEYWORD_SERVER_EXCEPTION = "ServerException";

	/**
	 * 服务端主动返回处理逻辑不成功，属于交互成功
	 */

	private String keyword;
	private String statusCode;
	private String desc;

	/**
	 * 用于Kryo序列化需要无参构造
	 */
	private ExchangeFailedReason() {

	}

	private ExchangeFailedReason(String keyword, String statusCode, String desc) {
		this.keyword = keyword;
		this.statusCode = statusCode;
		this.desc = desc;
		if (desc != null && desc.length() > 200) {
			this.desc = new StringBuilder(203).append(desc.substring(0, 200)).append("...").toString();
		}
	}

	/**
	 * 未发起请求就失败
	 */
	public static ExchangeFailedReason clientException(String desc, Exception e) {
		if (desc == null || desc.isEmpty()) {
			desc = e != null ? e.getClass().getName() : "";
		}
		return new ExchangeFailedReason(KEYWORD_CLIENT_EXCEPTION, "417", desc);
	}

	/**
	 * 连接失败
	 */
	public static ExchangeFailedReason clientConnectFailed(String desc, Exception e) {
		if (desc == null || desc.isEmpty()) {
			desc = e != null ? e.getClass().getName() : "";
		}
		return new ExchangeFailedReason(KEYWORD_CLINET_CONNECT_FAILED, "417", desc);
	}

	public static ExchangeFailedReason requestTimeout(String desc, Exception e) {
		if (desc == null || desc.isEmpty()) {
			desc = e != null ? e.getClass().getName() : "";
		}
		return new ExchangeFailedReason(KEYWORD_REQUEST_TIMEOUT, "408", desc);
	}

	public static ExchangeFailedReason clientInvalid(String desc, String statusCode, Exception e) {
		if (desc == null || desc.isEmpty()) {
			desc = e != null ? e.getClass().getName() : "";
		}
		return new ExchangeFailedReason(KEYWORD_CLIENT_INVALID, statusCode, desc);
	}

	public static ExchangeFailedReason serverRejected(String desc, Exception e) {
		if (desc == null || desc.isEmpty()) {
			desc = e != null ? e.getClass().getName() : "";
		}
		return new ExchangeFailedReason(KEYWORD_SERVER_REJECTED, "403", desc);
	}

	public static ExchangeFailedReason serverException(String desc, Exception e) {
		if (desc == null || desc.isEmpty()) {
			desc = e != null ? e.getClass().getName() : "";
		}
		return new ExchangeFailedReason(KEYWORD_SERVER_EXCEPTION, "500", desc);
	}

}