<template>
  <van-config-provider theme="light">
    <router-view v-slot="{ Component }">
      <transition
        enter-active-class="animate__animated animate__fadeIn"
        leave-active-class="animate__animated animate__fadeOut"
        :duration="200"
      >
        <component :is="Component" />
      </transition>
    </router-view>
  </van-config-provider>
</template>

<script setup lang="ts">
import { RouterView } from "vue-router";
</script>

<style>
.content {
  @apply sm:w-full lg:w-2/5 m-auto w-full relative overflow-hidden;
}
.hcx-popup {
  background: var(--van-toast-background) !important;
}
</style>
