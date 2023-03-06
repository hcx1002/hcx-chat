import { ref } from "vue";
import { defineStore } from "pinia";
import { getServiceEnvConfig } from "@/config/env-config";
import { showToast, showLoadingToast } from "vant";
import { useRouter } from "vue-router";
export const useChatStore = defineStore("chat-store", () => {
  const ws = ref<WebSocket>();
  const isConnect = ref(false);
  const count = ref(0);
  const loading = ref(false);
  const messageList = ref<Msg.Result[]>([]);
  const router = useRouter();
  function socketInIt() {
    loading.value = true;
    showLoadingToast({
      message: "进入中...",
      forbidClick: true,
      className: "hcx-popup",
    });
    ws.value = new WebSocket(getServiceEnvConfig("ws").url);
    ws.value.onopen = onopen;
    ws.value.onmessage = message;
    ws.value.onerror = error;
    ws.value.onclose = close;
  }
  //接收数据
  function message(this: WebSocket, ev: MessageEvent<any>) {
    console.log("接收数据");
    const result = JSON.parse(ev.data);
    result.data = JSON.parse(result.data);
    messageList.value?.push(result);
    console.log(messageList.value);
    if (result.code === 1) count.value = result.data.onlineCount;
  }
  //连接成功后回调
  function onopen(this: WebSocket, ev: Event) {
    loading.value = false;
    console.log("连接成功后回调");
    isConnect.value = true;
    showToast({ message: "连接成功", position: "top", className: "hcx-popup" });
    router.push({ name: "chat" });
  }
  // 错误的回调
  function error(this: WebSocket, ev: Event) {
    loading.value = false;
    console.log("错误的回调");
    console.log(this,ev);
    
    showToast({
      message: "请先登录",
      position: "top",
      className: "hcx-popup",
    });
    router.push({ name: "login" });
  }
  // 断开的回调
  function close(this: WebSocket, ev: Event) {
    isConnect.value = false;
    console.log("断开的回调");
  }

  // 关闭的回调
  function closeWs() {
    console.log("关闭ws");
    ws.value?.close();
  }
  // 发送消息
  function sendMsg(value: string) {
    if (!ws.value)
      showToast({
        message: "连接已断开",
        position: "top",
        className: "hcx-popup",
      });
    ws.value?.send(
      JSON.stringify({
        type: 1,
        value,
      })
    );
  }

  return {
    socketInIt,
    closeWs,
    sendMsg,
    loading,
    ws,
    messageList,
    isConnect,
    count,
  };
});
