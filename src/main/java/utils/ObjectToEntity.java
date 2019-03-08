package utils;

import com.google.common.base.CaseFormat;

public class ObjectToEntity {
    public static String camelToSnake(String cameledCase) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cameledCase);
    }
}
