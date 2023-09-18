package io.lazyfury.utils.error;


public class ProjectErrorCode {
    public static final ErrorCode SUCCESS = new ErrorCode(200, "success");
    public static final ErrorCode FAIL = new ErrorCode(400, "fail");
    public static final ErrorCode NOT_FOUND = new ErrorCode(404, "not found");
    public static final ErrorCode METHOD_NOT_ALLOWED = new ErrorCode(405, "method not allowed");
    public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "internal server error");

    public static final ErrorCode ACCESS_DENIED = new ErrorCode(403, "access denied");

    public static final ErrorCode INVALID_PARAMETER = new ErrorCode(1000, "invalid parameter");

    //    user
    public static final ErrorCode USER_NOT_FOUND = new ErrorCode(2000, "user not found");
    public static final ErrorCode USER_ALREADY_EXIST = new ErrorCode(2001, "user already exist");
    public static final ErrorCode USER_NOT_LOGIN = new ErrorCode(2002, "user not login");
    public static final ErrorCode USER_NOT_AUTHORIZED = new ErrorCode(2003, "user not authorized");

    public static final ErrorCode USER_PASSWORD_NOT_MATCH = new ErrorCode(2004, "user password not match");

    public static final ErrorCode USER_PASSWORD_TOO_SHORT = new ErrorCode(2005, "user password too short");

    public static final ErrorCode USER_PASSWORD_TOO_LONG = new ErrorCode(2006, "user password too long");

    //    用户权限
    public static final ErrorCode USER_PERMISSION_DENIED = new ErrorCode(2007, "user permission denied");

    //    token
    public static final ErrorCode TOKEN_NOT_FOUND = new ErrorCode(3000, "token not found");
    public static final ErrorCode TOKEN_EXPIRED = new ErrorCode(3001, "token expired");
    public static final ErrorCode TOKEN_INVALID = new ErrorCode(3002, "token invalid");

    //    file
    public static final ErrorCode FILE_NOT_FOUND = new ErrorCode(4000, "file not found");
    public static final ErrorCode FILE_TOO_LARGE = new ErrorCode(4001, "file too large");
    public static final ErrorCode FILE_TYPE_NOT_ALLOWED = new ErrorCode(4002, "file type not allowed");

}
