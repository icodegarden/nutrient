package io.github.icodegarden.nutrient.exchange.http;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import io.github.icodegarden.nutrient.lang.annotation.Nullable;

/**
 * 
 * @author Fangfang.Xu
 *
 */
abstract class UriBuilder {

	private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");

	private static final String SCHEME_PATTERN = "([^:/?#]+):";

	private static final String HTTP_PATTERN = "(?i)(http|https):";

	private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";

	private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";

	private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}:.]*[%\\p{Alnum}]*]";

	private static final String HOST_PATTERN = "(" + HOST_IPV6_PATTERN + "|" + HOST_IPV4_PATTERN + ")";

	private static final String PORT_PATTERN = "(\\{[^}]+\\}?|[^/?#]*)";

	private static final String PATH_PATTERN = "([^?#]*)";

	private static final String QUERY_PATTERN = "([^#]*)";

	private static final String LAST_PATTERN = "(.*)";
	// Regex patterns that matches URIs. See RFC 3986, appendix B
	private static final Pattern URI_PATTERN = Pattern.compile(
			"^(" + SCHEME_PATTERN + ")?" + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?"
					+ ")?" + PATH_PATTERN + "(\\?" + QUERY_PATTERN + ")?" + "(#" + LAST_PATTERN + ")?");

	public static String fromUriString(String uri) {
		Assert.notNull(uri, "URI must not be null");
		Matcher matcher = URI_PATTERN.matcher(uri);
		if (matcher.matches()) {
			String scheme = matcher.group(2);
			String userInfo = matcher.group(5);
			String host = matcher.group(6);
			String port = matcher.group(8);
			String path = matcher.group(9);
			String query = matcher.group(11);
			String fragment = matcher.group(13);
			boolean opaque = false;
			if (StringUtils.hasLength(scheme)) {
				String rest = uri.substring(scheme.length());
				if (!rest.startsWith(":/")) {
					opaque = true;
				}
			}
//			builder.scheme(scheme);
			if (opaque) {
				String ssp = uri.substring(scheme.length() + 1);
				if (StringUtils.hasLength(fragment)) {
					ssp = ssp.substring(0, ssp.length() - (fragment.length() + 1));
				}
//				builder.schemeSpecificPart(ssp);
			} else {
				if (StringUtils.hasLength(scheme) && scheme.startsWith("http") && !StringUtils.hasLength(host)) {
					throw new IllegalArgumentException("[" + uri + "] is not a valid HTTP URL");
				}
//				builder.userInfo(userInfo);
//				builder.host(host);
				if (StringUtils.hasLength(port)) {
//					builder.port(port);
				}
//				builder.path(path);
//				builder.query(query);

			}
			if (StringUtils.hasText(fragment)) {
//				builder.fragment(fragment);
			}
//			return builder;

			MultiValueMap<String, String> queryParams = query(query);
			StringBuilder sb = new StringBuilder(128).append(scheme).append("://").append(host).append(":").append(port)
					.append(path);
			int i = 0;
			for (Entry<String, List<String>> entry : queryParams.entrySet()) {
				String key = entry.getKey();
				List<String> list = entry.getValue();
				sb.append(i == 0 ? "?" : "&").append(key).append("=");
				for (int i2 = 0; i2 < list.size(); i2++) {
					sb.append(list.get(i2));
					if (i2 != list.size() - 1) {
						sb.append(",");
					}
				}
				
				i++;
			}
			return sb.toString();
		} else {
			throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
		}
	}

	private static MultiValueMap<String, String> query(@Nullable String query) {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		if (query != null) {
			Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
			while (matcher.find()) {
				String name = matcher.group(1);
				String eq = matcher.group(2);
				String value = matcher.group(3);
				queryParam(queryParams, name, (value != null ? value : (StringUtils.hasLength(eq) ? "" : null)));
			}
		} else {
			queryParams.clear();
		}

		BiFunction<String, Type, String> encoder = (s, type) -> encodeUriComponent(s, StandardCharsets.UTF_8, type);
//		PathComponent pathTo = this.path.encode(encoder);
		MultiValueMap<String, String> queryParamsTo = encodeQueryParams(queryParams, encoder);

		return queryParamsTo;
	}

	private static void queryParam(MultiValueMap<String, String> queryParams, String name, Object... values) {
		Assert.notNull(name, "Name must not be null");
		if (!ObjectUtils.isEmpty(values)) {
			for (Object value : values) {
				String valueAsString = getQueryParamValue(value);
				queryParams.add(name, valueAsString);
			}
		} else {
			queryParams.add(name, null);
		}
	}

	@Nullable
	private static String getQueryParamValue(@Nullable Object value) {
		if (value != null) {
			return (value instanceof Optional<?> optional ? optional.map(Object::toString).orElse(null)
					: value.toString());
		}
		return null;
	}

	private static String encodeUriComponent(String source, Charset charset, Type type) {
		if (!StringUtils.hasLength(source)) {
			return source;
		}
		Assert.notNull(charset, "Charset must not be null");
		Assert.notNull(type, "Type must not be null");

		byte[] bytes = source.getBytes(charset);
		boolean original = true;
		for (byte b : bytes) {
			if (!type.isAllowed(b)) {
				original = false;
				break;
			}
		}
		if (original) {
			return source;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
		for (byte b : bytes) {
			if (type.isAllowed(b)) {
				baos.write(b);
			} else {
				baos.write('%');
				char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
				char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
				baos.write(hex1);
				baos.write(hex2);
			}
		}
		return StreamUtils.copyToString(baos, charset);
	}

	private static MultiValueMap<String, String> encodeQueryParams(MultiValueMap<String, String> queryParams,
			BiFunction<String, Type, String> encoder) {
		int size = queryParams.size();
		MultiValueMap<String, String> result = new LinkedMultiValueMap<>(size);
		queryParams.forEach((key, values) -> {
			String name = encoder.apply(key, Type.QUERY_PARAM);
			List<String> encodedValues = new ArrayList<>(values.size());
			for (String value : values) {
				encodedValues.add(value != null ? encoder.apply(value, Type.QUERY_PARAM) : null);
			}
			result.put(name, encodedValues);
		});
		return CollectionUtils.unmodifiableMultiValueMap(result);
	}

	enum Type {

		SCHEME {
			@Override
			public boolean isAllowed(int c) {
				return isAlpha(c) || isDigit(c) || '+' == c || '-' == c || '.' == c;
			}
		},
		AUTHORITY {
			@Override
			public boolean isAllowed(int c) {
				return isUnreserved(c) || isSubDelimiter(c) || ':' == c || '@' == c;
			}
		},
		USER_INFO {
			@Override
			public boolean isAllowed(int c) {
				return isUnreserved(c) || isSubDelimiter(c) || ':' == c;
			}
		},
		HOST_IPV4 {
			@Override
			public boolean isAllowed(int c) {
				return isUnreserved(c) || isSubDelimiter(c);
			}
		},
		HOST_IPV6 {
			@Override
			public boolean isAllowed(int c) {
				return isUnreserved(c) || isSubDelimiter(c) || '[' == c || ']' == c || ':' == c;
			}
		},
		PORT {
			@Override
			public boolean isAllowed(int c) {
				return isDigit(c);
			}
		},
		PATH {
			@Override
			public boolean isAllowed(int c) {
				return isPchar(c) || '/' == c;
			}
		},
		PATH_SEGMENT {
			@Override
			public boolean isAllowed(int c) {
				return isPchar(c);
			}
		},
		QUERY {
			@Override
			public boolean isAllowed(int c) {
				return isPchar(c) || '/' == c || '?' == c;
			}
		},
		QUERY_PARAM {
			@Override
			public boolean isAllowed(int c) {
				if ('=' == c || '&' == c) {
					return false;
				} else {
					return isPchar(c) || '/' == c || '?' == c;
				}
			}
		},
		FRAGMENT {
			@Override
			public boolean isAllowed(int c) {
				return isPchar(c) || '/' == c || '?' == c;
			}
		},
		URI {
			@Override
			public boolean isAllowed(int c) {
				return isUnreserved(c);
			}
		};

		/**
		 * Indicates whether the given character is allowed in this URI component.
		 * 
		 * @return {@code true} if the character is allowed; {@code false} otherwise
		 */
		public abstract boolean isAllowed(int c);

		/**
		 * Indicates whether the given character is in the {@code ALPHA} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isAlpha(int c) {
			return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
		}

		/**
		 * Indicates whether the given character is in the {@code DIGIT} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isDigit(int c) {
			return (c >= '0' && c <= '9');
		}

		/**
		 * Indicates whether the given character is in the {@code gen-delims} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isGenericDelimiter(int c) {
			return (':' == c || '/' == c || '?' == c || '#' == c || '[' == c || ']' == c || '@' == c);
		}

		/**
		 * Indicates whether the given character is in the {@code sub-delims} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isSubDelimiter(int c) {
			return ('!' == c || '$' == c || '&' == c || '\'' == c || '(' == c || ')' == c || '*' == c || '+' == c
					|| ',' == c || ';' == c || '=' == c);
		}

		/**
		 * Indicates whether the given character is in the {@code reserved} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isReserved(int c) {
			return (isGenericDelimiter(c) || isSubDelimiter(c));
		}

		/**
		 * Indicates whether the given character is in the {@code unreserved} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isUnreserved(int c) {
			return (isAlpha(c) || isDigit(c) || '-' == c || '.' == c || '_' == c || '~' == c);
		}

		/**
		 * Indicates whether the given character is in the {@code pchar} set.
		 * 
		 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">RFC 3986, appendix A</a>
		 */
		protected boolean isPchar(int c) {
			return (isUnreserved(c) || isSubDelimiter(c) || ':' == c || '@' == c);
		}
	}

}
