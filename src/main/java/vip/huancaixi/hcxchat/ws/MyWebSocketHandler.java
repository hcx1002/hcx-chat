package vip.huancaixi.hcxchat.ws;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vip.huancaixi.hcxchat.module.user.entity.User;
import vip.huancaixi.hcxchat.module.user.entity.UserData;
import vip.huancaixi.hcxchat.module.user.service.IUserService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理 WebSocket 连接
 *
 * @author kong
 * @since: 2022-2-11
 */
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    /**
     * 固定前缀
     */
    private static final String USER_ID = "user_id_";
    /**
     * 当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 存放Session集合，方便推送消息
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketSession> webSocketSessionMaps = new ConcurrentHashMap<>();

    // 监听：连接开启
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // put到集合，方便后续操作
        UserData user = (UserData) session.getAttributes().get("user");
        webSocketSessionMaps.put(USER_ID + user.getId(), session);
        addOnlineCount();
        // 给个提示
        String tips = "Web-Socket 连接成功，sid=" + session.getId() + "，userId=" + user.getId() + "，当前在线人数为：" + getOnlineCount();
        System.out.println(tips);
        JSONObject object = JSONUtil.createObj().putOnce("sid", session.getId()).putOnce("user", user).putOnce("onlineCount", getOnlineCount()).putOnce("type", 0);
//        sendMessage(session, WebSocketMsg.join(JSONUtil.toJsonStr(object)));
        sendAllJoinMessage(object);
    }

    // 监听：连接关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 从集合移除
        UserData user = (UserData) session.getAttributes().get("user");
        webSocketSessionMaps.remove(USER_ID + user.getId());
        // 给个提示
        String tips = "Web-Socket 连接关闭，sid=" + session.getId() + "，userId=" + user.getId();
        System.out.println(tips);
        subOnlineCount();
    }

    // 收到消息
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("sid为：" + session.getId() + "，发来：" + message);
        UserData user = (UserData) session.getAttributes().get("user");
        JSONObject object = JSONUtil.parseObj(message);
        JSONObject payload = JSONUtil.parseObj(object.getStr("payload"));
        System.out.println(payload);
        if (StrUtil.hasBlank(payload.getStr("value"))) return;

//        User user = (User) StpUtil.getTokenSession().get("loginUser");
        payload.putOnce("user", user);
        payload.putOnce("isMe", 0);
        sendAllMessage(user.getId(), payload);
    }

    // 向指定客户端推送消息
    public static void sendMessage(WebSocketSession session, String message) {
        try {
            System.out.println("向sid为：" + session.getId() + "，发送：" + message);
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 向指定用户推送消息
    public static void sendMessage(long userId, String message) {
        WebSocketSession session = webSocketSessionMaps.get(USER_ID + userId);
        if (session != null) {
            sendMessage(session, message);
        }
    }

    public static void sendAllMessage(String id, JSONObject message) {
        webSocketSessionMaps.forEach((userId, session) -> {
            JSONObject obj = JSONUtil.createObj();
            BeanUtil.copyProperties(message,obj);
            try {
                if (userId.equals(USER_ID + id))
                    obj.set("isMe", 1);
                session.sendMessage(new TextMessage(WebSocketMsg.send(JSONUtil.toJsonStr(obj))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void sendAllJoinMessage(JSONObject message) {
        webSocketSessionMaps.forEach((userId, session) -> {
            try {
                session.sendMessage(new TextMessage(WebSocketMsg.join(JSONUtil.toJsonStr(message))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        onlineCount.getAndDecrement();
    }
}

