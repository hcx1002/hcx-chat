<template>
  <div class="content">
    <div class="h-12 fixed top-0 flex bg-[#f7f7f7] items-center view">
      <van-icon
        size="25"
        class="px-2 cursor-pointer"
        name="arrow-left"
        @click="onClickLeft"
      />
      <div class="flex-1 text-center text-lg font-bold">
        幻彩希聊天群（{{ count }}）
      </div>
      <van-popover
        v-model:show="showPopover"
        :actions="actions"
        @select="onSelect"
        placement="bottom-end"
        key="1"
      >
        <template #reference>
          <van-icon name="ellipsis" size="25" class="px-2 cursor-pointer" />
        </template>
      </van-popover>
    </div>
    <!-- 聊天内容区域 -->
    <div
      class="h-screen bg-[#ededed] pb-16 pt-12 overflow-x-hidden overflow-y-scroll chat-view"
      @scroll="scroll"
    >
      <div v-for="item in messageList" :key="item.date">
        <!-- 加入 -->
        <div
          v-if="item.code === 1"
          class="text-sm text-gray-500 text-center my-3"
        >
          <van-tag color="#ccc" round type="primary" size="medium">{{
            "欢迎 " + item.data.user.username + " 加入群聊"
          }}</van-tag>
        </div>
        <!-- 信息内容 -->
        <div v-else-if="item.code === 2" class="flex my-3">
          <div class="w-20 text-center">
            <van-image
              v-if="item.data.isMe === 0"
              class="w-12 h-12 rounded-md message animate__animated animate__fadeIn"
              :src="item.data.user.avatar"
              radius="5"
              :show-loading="false"
            />
          </div>
          <div class="flex-1">
            <div
              v-if="item.data.isMe === 0"
              class="truncate text-gray-500 text-xs ml-1"
            >
              {{ item.data.user.username }}
            </div>
            <div
              class="p-3 rounded-md text-base relative message animate__animated animate__fadeIn"
              :class="[
                item.data.isMe === 0 ? 'bg-white' : 'bg-[#95eb6c] float-right',
              ]"
            >
              {{ item.data.value }}
              <div class="message-icon-left" v-if="item.data.isMe === 0">
                <van-icon name="play" color="#FFF" size="22" />
              </div>
              <div class="message-icon-right" v-if="item.data.isMe === 1">
                <van-icon name="play" color="#95eb6c" size="22" />
              </div>
            </div>
          </div>
          <div class="w-20 text-center">
            <van-image
              v-if="item.data.isMe === 1"
              class="w-12 h-12 rounded-md"
              :src="item.data.user.avatar"
              radius="5"
              :show-loading="false"
            />
          </div>
        </div>
      </div>
      <!-- 断开提示 -->
      <div v-if="!isConnect" class="my-3 flex justify-center items-center">
        <div class="bg-white rounded-lg py-1 px-3">
          当前已断开<span @click="connect" class="text-red-600"> 重新连接</span>
        </div>
      </div>
    </div>
    <div
      class="fixed bottom-0 py-2 flex bg-[#f7f7f7] items-center view transition-all"
    >
      <van-icon
        size="30"
        class="px-2 cursor-pointer"
        class-prefix="icon"
        name="yuyin"
      />
      <van-field
        class="rounded-md transition-all"
        v-model="message"
        type="textarea"
        rows="1"
        autosize
        placeholder=""
        @keyup.enter.stop="send"
        :formatter="formatter"
        ref="messageInput"
      />
      <van-icon size="32" class="px-2 cursor-pointer" name="smile-o" />
      <div
        v-if="message"
        class="mr-2 w-20 flex items-center animate__animated myAni2"
      >
        <van-button
          class="w-full h-full"
          size="small"
          type="success"
          @click="send"
          >发送</van-button
        >
      </div>

      <van-icon
        v-else
        size="30"
        class="pr-2 cursor-pointer myAni1"
        name="add-o"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onUnmounted, ref, toRefs, watch } from "vue";
import { useRouter } from "vue-router";
import { useChatStore, useAuthStore } from "@/stores";
import type { FieldInstance, PopoverAction } from "vant";
const router = useRouter();
const message = ref("");
const messageInput = ref<FieldInstance>();
const showPopover = ref(false);
const scrollElement = ref<any>();
const actions = [{ text: "退出登录", value: "logout" }];
const { logoutAuth } = useAuthStore();
const { sendMsg, closeWs, socketInIt } = useChatStore();
const { messageList, isConnect, count } = toRefs(useChatStore());
watch(scrollElement, (value) => {
  scrollElement.value = value;
});
watch(messageList.value, (value) => {
  if (value) {
    setTimeout(() => {
      if (scrollElement.value)
        scrollElement.value.target.scrollTop =
          scrollElement.value.target.scrollHeight -
          scrollElement.value.target.offsetHeight;
    }, 100);
  }
});
function formatter(s: string) {
  s = s.replace(/\n/g, '')
  return s.replace(/\s/g, '')
}
function send() {
  if (message.value) sendMsg(message.value);
  message.value = "";
  console.log(message.value);

  messageInput.value?.focus();
}
function onClickLeft() {
  router.replace({ name: "home" });
}
function connect() {
  socketInIt();
}
function scroll(e: any) {
  scrollElement.value = e;
}
function onSelect(action: PopoverAction, index: number) {
  if (action.value === "logout") logoutAuth();
  router.replace({ name: "home" });
}
onUnmounted(() => {
  closeWs();
});
</script>

<style scoped>
:deep(.van-nav-bar__content) {
  @apply bg-[#f7f7f7];
}
:deep(.van-badge__wrapper) {
  @apply text-black;
}
:deep(.van-cell) {
  padding: 8px;
  font-size: 20px;
}
.view {
  @apply z-50;
  width: inherit;
}
.chat-view::-webkit-scrollbar {
  width: 10px;
}
.message {
  word-wrap: break-word;
  word-break: break-all;
  width: fit-content;
}

.message-icon-right {
  @apply absolute;
  right: -14px;
  top: 10px;
}
.message-icon-left {
  @apply absolute;
  left: -14px;
  top: 10px;
  transform: rotate(180deg);
}
.myAni1 {
  animation: fadeInLeft;
  animation-duration: 0.5s;
}
.myAni2 {
  animation: fadeInRight;
  animation-duration: 0.5s;
}
</style>
