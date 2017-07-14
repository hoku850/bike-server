package org.ccframe.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

public class TextUtilApi {
	private TextUtilApi() {}

	private static Logger log = Logger.getLogger(TextUtilApi.class);

	private static final String GBK = "gbk";

	public static String cleanHTML(String h, String paramReplacement) {
		String replacement = paramReplacement;
		if (h == null) {
			return h;
		}
		if (replacement == null) {
			replacement = "";
		}
		String html = h;
		String[] htmlTag = { "" };
		String regex = "";

		html = Pattern.compile("<!--((?!<!--).)*-->", Pattern.DOTALL).matcher(html).replaceAll(replacement);
		html = Pattern.compile("<script((?!</script).)*</script>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(html).replaceAll(replacement);
		html = Pattern.compile("<style((?!</style).)*</style>",	Pattern.DOTALL | Pattern.CASE_INSENSITIVE) .matcher(html).replaceAll(replacement);

		for (int i = 0; i < htmlTag.length; i++) {
			regex = "<" + htmlTag[i] + "[^<]*>";
			html = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(html).replaceAll(replacement);
			regex = "</" + htmlTag[i] + ">";
			html = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(html).replaceAll(replacement);
		}
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("\r\n", "");
		html = html.replaceAll("\n", "");
		html = html.replaceAll("\t", "");
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		html = html.replaceAll("&lsquo;", "'");
		html = html.replaceAll("&ldquo;", "\"");
		html = html.replaceAll("&rdquo;", "\"");
		html = html.replaceAll("&amp;", "&");
		html = html.trim();
		html = HtmlUtils.htmlEscape(html);
		html = HtmlUtils.htmlUnescape(html);
		return html;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static String cutString(String src, int len, String paramPlac) {
		String plac = paramPlac;
		if (src == null) {
			return "";
		}
		if (plac == null || "".equals(plac.trim())) {
			plac = "";
		}
		try {
			char[] processChar = src.toCharArray();
			StringBuilder outSb = new StringBuilder();
			int indexCount = 0;
			for (char c : processChar) {
				if (isChinese(c)) {
					indexCount += 2;
				} else {
					indexCount += 1;
				}
				if (indexCount <= len) {
					outSb.append(c);
				}
			}
			return resotreHtml(outSb.toString()) + (src.getBytes(GBK).length > len ? plac : "");
		} catch (Exception e) {
			log.error("格式字符串出错", e);
			return src;
		}
	}

	private static String resotreHtml(String text) {
		String result = text;
		result = result.replaceAll(" ", "&nbsp;");
		result = result.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");
		result = result.replaceAll("'", "&lsquo;");
		result = result.replaceAll("\"", "&quot;");
		return result;
	}

	public static String urlEncode(String value)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "UTF-8");
	}
}
