package vip.huancaixi.hcxchat.ws;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import vip.huancaixi.hcxchat.module.user.entity.User;
import vip.huancaixi.hcxchat.module.user.entity.UserData;

import java.util.Map;

/**
 * WebSocket 握手的前置拦截器
 *
 * @author kong
 * @since: 2022-2-11
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

	// 握手之前触发 (return true 才会握手成功 )
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
			Map<String, Object> attr) {
		log.info("握手之前触发：" + StpUtil.getTokenValue());
		// 未登录情况下拒绝握手
		if(!StpUtil.isLogin()) {
			log.info("未授权客户端，连接失败："+request.getRemoteAddress());
			return false;
		}
		User user = (User) StpUtil.getTokenSession().get("loginUser");
		System.out.println(user);
		UserData userData = new UserData();
		BeanUtil.copyProperties(user,userData);
		// 标记 userId，握手成功
		attr.put("user", userData);
		return true;
	}

	// 握手之后触发
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		log.info("握手之后触发");
	}

}
