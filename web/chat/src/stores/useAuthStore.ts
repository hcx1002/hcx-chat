import { defineStore } from "pinia";
import { login, regis, logout, getUserInfo } from "@/api";
interface AuthState {
  /** 用户信息 */
  userInfo: User.Info;
  /** 用户token */
  token: string;
  /** 登录的加载状态 */
  loginLoading: boolean;
}
export const useAuthStore = defineStore("auth-store", {
  state: (): AuthState => ({
    userInfo: JSON.parse(localStorage.getItem("userInfo") || "{}"),
    token: localStorage.getItem("token") || "",
    loginLoading: false,
  }),
  getters: {
    isLogin(state) {
      return Boolean(state.token);
    },
  },
  actions: {
    async userLogin(username: string, password: string) {
      const { data } = await login(username, password);
      localStorage.setItem("token", data || "");
      this.token = data || "";
      await this.getUserInfoByToken();
    },
    async userRegis(username: string, password: string) {
      const { data } = await regis(username, password);
      localStorage.setItem("token", data || "");
      this.token = data || "";
      await this.getUserInfoByToken();
    },
    async getUserInfoByToken() {
      if (this.token) {
        const { data } = await getUserInfo();
        if (data) {
          localStorage.setItem("userInfo", JSON.stringify(data));
          this.userInfo = data;
        }
      }
    },
    async logoutAuth() {
      await logout();
      this.clearAuth()
    },
    clearAuth() {
      localStorage.removeItem("userInfo");
      localStorage.removeItem("token");
      this.$reset();
    },
  },
});
