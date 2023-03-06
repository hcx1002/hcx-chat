import { createRouter, createWebHistory } from "vue-router";
import HomeView from '@/views/HomeView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/LoginView.vue"),
    },
    {
      path: "/chat",
      name: "chat",
      component: () => import("@/views/ChatView.vue"),
    },
  ],
});

router.beforeEach((to , from , next)=>{
  next()
})

export default router;
