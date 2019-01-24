package io.ilss.generic;

/**
 * className ResponseMsg
 * description ResponseMsg
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 18:47
 */
public class ResponseMsg<T extends BaseData> {
    public static int SUCCESS_CODE = 1;
    public static int ERROR_CODE = 0;
    public static int OTHER_CODE = -1;
    private int code;
    private String msg;
    private T data;

    public static <U extends BaseData> ResponseMsg sendSuccess(U data) {
        ResponseMsg<U> responseMsg = new ResponseMsg<>();
        responseMsg.code = SUCCESS_CODE;
        responseMsg.data = data;
        responseMsg.msg = "Remote Call Success!";
        return responseMsg;
    }

    public static <U extends BaseData> ResponseMsg sendError(U data, String msg) {
        ResponseMsg<U> responseMsg = new ResponseMsg<>();
        responseMsg.code = ERROR_CODE;
        responseMsg.data = data;
        responseMsg.msg = "Remote Call Error";
        return responseMsg;
    }
    public static <U extends BaseData> ResponseMsg sendOther(U data, String msg) {
        ResponseMsg<U> responseMsg = new ResponseMsg<>();
        responseMsg.code = OTHER_CODE;
        responseMsg.data = data;
        responseMsg.msg = msg;
        return responseMsg;
    }

    public static void main(String[] args) {
        System.out.println(ResponseMsg.<MyObject>sendSuccess(new MyObject<String>("asdf","asfd")));
    }


    @Override
    public String toString() {
        return "ResponseMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
