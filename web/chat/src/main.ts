import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
// 1. tailwind.css
import "@/styles/main.css";
// 3. animate.css动画库
import "animate.css";
// 4. iconfont图标库
import "@/assets/iconfont/iconfont.css";
// 解决vant桌面无法使用
import "@vant/touch-emulator";
// 2. 引入组件样式
import "vant/lib/index.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount("#app");
