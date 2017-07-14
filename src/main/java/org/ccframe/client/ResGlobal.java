package org.ccframe.client;

/**
 * 国际化资源框架.
 * 基础框架异常使用国际化，业务扩展代码不做要求.
 * 
 * @author JIM
 */
public final class ResGlobal {
    private ResGlobal() {
    }
    public static final String ERRORS_USER_DEFINED = "message"; //自定义内容异常
    public static final String ERRORS_EXCEPTION = "exception"; //系统异常
    public static final String ERRORS_SQL_ERROR = "sqlError"; //SQL异常

    public static final String ERRORS_SESSION_INVALIDATE = "errors.session.invalidate";
    public static final String ERRORS_LOGIN_VALIDATE_CODE = "errors.login.validate";
    public static final String ERRORS_LOGIN_PASSWORD = "errors.login.password";
    public static final String ERRORS_LOGIN_FREEZE = "errors.login.freeze";

    public static final String ERRORS_DUPLICATE_LOGIN_ID = "errors.regist.duplicateLoginId";
    public static final String ERRORS_DUPLICATE_ACTIVE_EMAIL = "errors.regist.duplicateActiveEmail";
    public static final String ERRORS_DUPLICATE_ACTIVE_MOBILE = "errors.regist.duplicateActiveMobile";
}
