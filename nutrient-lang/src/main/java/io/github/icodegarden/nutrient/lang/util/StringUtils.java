package io.github.icodegarden.nutrient.lang.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class StringUtils {
	private static final Pattern TPATTERN = Pattern.compile("[A-Z0-9]");
	
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 驼峰转小写,userFullName -> userfullname
	 */
	public static String camelToLower(String str) {
		return camelToLower(str, "");
	}

	/**
	 * 驼峰转小写,允许指定连接符
	 */
	public static String camelToLower(String str, String connectStr) {
		Matcher matcher = TPATTERN.matcher(str);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(sb, connectStr + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 下划线连接串转驼峰,user_full_name -> userFullName 
	 */
	public static String snakeToCamel(String snakeCase) {  
        if (snakeCase == null || snakeCase.isEmpty()) {  
            return snakeCase;  
        }  
  
        StringBuilder camelCase = new StringBuilder();  
        boolean shouldCapitalize = false;  
  
        for (char c : snakeCase.toCharArray()) {  
            if (c == '_') {  
                shouldCapitalize = true;  
            } else {  
                if (shouldCapitalize) {  
                    camelCase.append(Character.toUpperCase(c));  
                    shouldCapitalize = false;  
                } else {  
                    camelCase.append(Character.toLowerCase(c));  
                }  
            }  
        }  
  
        return camelCase.toString();  
    }  
}
