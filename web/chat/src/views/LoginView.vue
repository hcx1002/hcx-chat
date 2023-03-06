<template>
  <div class="content">
    <van-nav-bar title="登录注册" fixed left-arrow @click-left="onClickLeft" />
    <div class="h-screen flex justify-center items-center bg-blue-400">
      <div class="bg-gray-100 w-5/6 rounded-3xl shadow-xl">
        <div
          class="rounded-t-3xl h-16 mb-2 flex justify-center items-center font-bold text-xl tracking-widest"
        >
          {{ type ? "登录" : "注册" }}
        </div>
        <van-form @submit="onSubmit">
          <van-cell-group inset>
            <van-field
              v-model="username"
              placeholder="用户名"
              :rules="[{ required: true, message: '请填写用户名' }]"
            />
            <van-field
              v-model="password"
              type="password"
              placeholder="密码"
              readonly 
              clickable 
              :rules="[{ required: true, message: '请填写密码' }]"
              @touchstart.stop="show = true"
            />
          </van-cell-group>
          <div style="margin: 16px">
            <van-button round block type="primary" native-type="submit">
              {{ type ? "登录" : "注册" }}
            </van-button>
          </div>
        </van-form>
        <div class="rounded-b-3xl h-10 mt-2 text-center text-xs">
          <div v-if="type">
            还没有账号？<span class="text-red-500" @click="change">注册</span>
          </div>
          <div v-else>
            已经有账号？<span class="text-red-500" @click="change">登录</span>
          </div>
        </div>
      </div>
    </div>
    <van-number-keyboard
      v-model="password"
      :show="show"
      :maxlength="6"
      @blur="show = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, toRefs, watch } from "vue";
import { useAuthStore, useChatStore } from "@/stores";
import { useRouter } from "vue-router";
const { userLogin, userRegis } = useAuthStore();
const { ws } = toRefs(useChatStore());
const router = useRouter();
const username = ref("");
const password = ref("");
const type = ref(true);
const show = ref(false);
watch(ws, (value) => {
  console.log(value);
});
const onSubmit = async (values: string) => {
  if (type.value) await userLogin(username.value, password.value);
  else await userRegis(username.value, password.value);
  router.replace({ name: "home" });
};
function onClickLeft() {
  router.replace({ name: "home" });
}
function change() {
  type.value = !type.value;
}

</script>

<style scoped>
:deep(.van-cell) {
  @apply py-3;
}
</style>
