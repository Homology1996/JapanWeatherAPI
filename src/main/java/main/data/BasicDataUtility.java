package main.data;

import main.Constants;
import org.json.JSONObject;

import java.util.Map;

public interface BasicDataUtility {

    static boolean isStringNonNullAndNonBlank(String content) {
        return content != null && !content.isBlank();
    }

    static String getNonNullContent(String content) {
        return isStringNonNullAndNonBlank(content) ? content.trim() : Constants.EMPTY;
    }

    private static boolean isContentNumerical(String content) {
        try {
            return !Double.toString(Double.parseDouble(content)).isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    static String getNumericalValue(String content) {
        String percent = "%";
        if (content.endsWith(percent)) {
            return isContentNumerical(content.substring(0, content.length() - percent.length())) ? content : "0" + percent;
        } else {
            return isContentNumerical(content) ? content : "0";
        }
    }

    Map<String, String> toMap();

    default JSONObject toJSONObject() {
        return new JSONObject(toMap());
    }

}