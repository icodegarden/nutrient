package io.github.icodegarden.nutrient.lang.spec.sign;

import io.github.icodegarden.nutrient.lang.annotation.Nullable;
import io.github.icodegarden.nutrient.lang.spec.response.OpenApiResponse;

/**
 * 
 * @author Fangfang.Xu
 *
 */
abstract class BaseSignUtils {

	/**
	 * 
	 * @param body
	 * @return app_id=2014072300007148&biz_content={"button":[{"actionParam":"ZFB_HFCZ","actionType":"out","name":"话费充值"},{"name":"查询","subButton":[{"actionParam":"ZFB_YECX","actionType":"out","name":"余额查询"},{"actionParam":"ZFB_LLCX","actionType":"out","name":"流量查询"},{"actionParam":"ZFB_HFCX","actionType":"out","name":"话费查询"}]},{"actionParam":"http://m.alipay.com","actionType":"link","name":"最新优惠"}]}&charset=GBK&method=alipay.mobile.public.menu.add&sign_type=RSA2&timestamp=2014-07-24
	 *         03:07:50&version=1.0
	 */
	protected static String buildRequestSignParams(OpenApiRequestBody body, @Nullable String appKey) {
		// [app_id, biz_content, charset, format, method, sign_type, timestamp, token,
		// version]

		StringBuilder sb = new StringBuilder(200).append("app_id=").append(body.getApp_id());
		if (hasText(body.getBiz_content())) {
			sb.append("&biz_content=").append(body.getBiz_content());
		}
		if (hasText(body.getCharset())) {
			sb.append("&charset=").append(body.getCharset());
		}
		if (hasText(body.getFormat())) {
			sb.append("&format=").append(body.getFormat());
		}
		if (hasText(body.getMethod())) {
			sb.append("&method=").append(body.getMethod());
		}
		if (hasText(body.getRequest_id())) {
			sb.append("&request_id=").append(body.getRequest_id());
		}
		if (hasText(body.getSign_type())) {
			sb.append("&sign_type=").append(body.getSign_type());
		}
		if (hasText(body.getTimestamp())) {
			sb.append("&timestamp=").append(body.getTimestamp());
		}
		if (hasText(body.getVersion())) {
			sb.append("&version=").append(body.getVersion());
		}
		if (hasText(appKey)) {
			sb.append("&key=").append(appKey);// key排最后
		}
		return sb.toString();
	}

	protected static boolean hasText(String str) {
		return str != null && !str.isEmpty();
	}

	protected static String buildResponseSignParams(OpenApiResponse body, String appKey) {
		StringBuilder sb = new StringBuilder(150);

		if (body.getBiz_code() != null) {
			sb.append("biz_code=").append(body.getBiz_code());
		}
		if (body.getBiz_content() != null) {
			sb.append("&biz_content=").append(body.getBiz_content());
		}
		if (body.getCode() != null) {
			sb.append("&code=").append(body.getCode());
		}
		if (body.getMsg() != null) {
			sb.append("&msg=").append(body.getMsg());
		}
		if (body.getSub_code() != null) {
			sb.append("&sub_code=").append(body.getSub_code());
		}
		if (body.getSub_msg() != null) {
			sb.append("&sub_msg=").append(body.getSub_msg());
		}

		sb.append("&key=").append(appKey);// key排最后
		return sb.toString();
	}

}
