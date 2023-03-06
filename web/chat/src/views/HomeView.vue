<template>
  <div class="flex justify-center items-center h-screen bg content">
    <div class="w-3/5">
      <div class=" text-white text-3xl">
        <div class="text-center my-8">开放</div>
        <div class="text-center mb-3">自由</div>
        <div class="text-center mb-5 ">安全 </div>
      </div>
      <van-button
      icon="fire-o"
      :loading="loading"
      type="primary"
      size="large"
      :loading-text="loadingText"
      :text="loadingText"
      @click="join"
    />
    </div>
    
  </div>
</template>

<script setup lang="ts">
import { ref, toRefs } from "vue";
import { useChatStore } from "@/stores";
const loadingText = ref("进入聊天室");
const { socketInIt } = useChatStore();
const { loading } = toRefs(useChatStore());
function join() {
  socketInIt();
}
</script>

<style scoped lang="scss">
.bg{
  background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
}
</style>
