package vip.huancaixi.hcxchat.ws;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMsg {
    private Integer code;
    private String data;
    private Date date;

    public static String join(String sid){
        return JSONUtil.toJsonStr(new WebSocketMsg(1,sid,new Date()));
    }
    public static String send(String msg){
        return JSONUtil.toJsonStr(new WebSocketMsg(2,msg,new Date()));
    }
}
