package io.github.icodegarden.nutrient.lang.spec.response;

import java.util.Objects;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class OpenApiResponse extends ApiResponse {

	private String biz_code;
	private String biz_content;
	private String sign;

	public static OpenApiResponse success(String biz_code, String biz_content) {
		OpenApiResponse apiResponse = new OpenApiResponse();
		apiResponse.setCode(ApiResponse.CODE_SUCCESS);
		apiResponse.setMsg(ApiResponse.MSG_SUCCESS);
		apiResponse.setBiz_code(biz_code);
		apiResponse.setBiz_content(biz_content);

		return apiResponse;
	}

	public static OpenApiResponse fail(String biz_code, ErrorCodeException e) {
		OpenApiResponse apiResponse = new OpenApiResponse();
		apiResponse.setCode(e.getCode());
		apiResponse.setMsg(e.getMsg());
		apiResponse.setSub_code(e.getSub_code());
		apiResponse.setSub_msg(e.getSub_msg());
		apiResponse.setBiz_code(biz_code);

		return apiResponse;
	}

	public String getBiz_code() {
		return biz_code;
	}

	public void setBiz_code(String biz_code) {
		this.biz_code = biz_code;
	}

	public String getBiz_content() {
		return biz_content;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(biz_code, biz_content, sign);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpenApiResponse other = (OpenApiResponse) obj;
		return Objects.equals(biz_code, other.biz_code) && Objects.equals(biz_content, other.biz_content)
				&& Objects.equals(sign, other.sign);
	}

	@Override
	public String toString() {
		return "OpenApiResponse [biz_code=" + biz_code + ", biz_content=" + biz_content + ", sign=" + sign
				+ ", toString()=" + super.toString() + "]";
	}

}
