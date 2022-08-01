package com.wheel.admin.enums;

/**
 * @author longyun
 * @data 2022/7/29 18:42
 */
public enum ResultEnumCode {
    /* 成功 */
    SUCCESS("200", "请求成功"),
    SUCCESS_login("200", "用户登录成功"),
    SUCCESS_logout("200", "用户退出成功"),
    SUCCESS_CREATE_USER("200","账户注册成功，角色注册成功！"),

    /* 默认失败 */
    COMMON_FAIL("999", "请求未处理成功，请稍后再试"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID("1001", "参数无效"),
    PARAM_IS_BLANK("1002", "参数为空"),
    PARAM_TYPE_ERROR("1003", "参数类型错误"),
    PARAM_NOT_COMPLETE("1004", "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN("2001", "用户未登录"),
    USER_ACCOUNT_EXPIRED("2002", "账号已过期"),
    USER_CREDENTIALS_ERROR("2003", "密码错误"),
    USER_CREDENTIALS_EXPIRED("2004", "密码过期"),
    USER_ACCOUNT_DISABLE("2005", "账号不可用"),
    USER_ACCOUNT_LOCKED("2006", "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST("2007", "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST("2008", "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS("2009", "账号下线"),
    USER_DELETE_FAIL("2010","用户删除失败"),
    CREATE_USER_FAIL("2011","添加用户失败"),
    EDIT_USER_FAIL("2012","编辑用户失败"),
    USER_ROLE_GET_FAIL("2013","获取用户角色失败"),
    BIND_USER_ROLE_FAIL("2014","绑定用户角色失败"),

    /* 业务权限错误 */
    NO_PERMISSION("3001", "当前账号没有此权限"),

    /* 添加用户错误*/
    CREATE_USERROLE_FAIL("3002","账户添加成功，角色添加失败！"),

    /*zk 注册中心相关错误*/
    ZKDATA_NULL("4000","服务数据为空"),

    /*一个账号多个用户同时登录*/
    SESSION_SAME_LOGIN("3004","已经另一台机器登录，您被迫下线。");

    private String code;
    private String message;

    ResultEnumCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(String code) {
        for (ResultEnumCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

}
