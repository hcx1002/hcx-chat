import axios from "axios";
import type { AxiosError, AxiosInstance, AxiosRequestConfig } from "axios";
import { showToast, showLoadingToast, showFailToast, closeToast } from "vant";
import { useAuthStore } from "@/stores";
import { useRouter } from "vue-router";
/**
 * 封装axios请求类
 * @author Soybean<honghuangdc@gmail.com>
 */
export default class CustomAxiosInstance {
  instance: AxiosInstance;
  /**
   * @param axiosConfig - axios配置
   */
  constructor(axiosConfig: AxiosRequestConfig) {
    this.instance = axios.create(axiosConfig);
    this.setInterceptor();
  }

  /** 设置请求拦截器 */
  setInterceptor() {
    this.instance.interceptors.request.use(
      async (config) => {
        const handleConfig = { ...config };
        if (handleConfig.headers) {
          // 设置token
          const token = localStorage.getItem("token");
          if (token) handleConfig.headers.token = token;
        }
        showLoadingToast({
          message: "加载中...",
          forbidClick: true,
          className: "hcx-popup",
        });
        return handleConfig;
      },
      (axiosError: AxiosError) => {
        return axiosError;
      }
    );
    this.instance.interceptors.response.use(
      async (response) => {
        const { status } = response;
        if (status === 200 || status < 300 || status === 304) {
          const backend = response.data;
          // 请求成功
          if (backend.code === 200) {
            closeToast();
            return backend;
          } else if (backend.code === 401) {
            const { clearAuth } = useAuthStore();
            const router = useRouter();
            showToast({
              message: "登录信息已失效，请重新登录",
              position: "top",
              className: "hcx-popup",
            });
            clearAuth();
            router.replace({ name: "login" });
            return Promise.reject(backend.message);
          }
          showFailToast({ message: backend.message, className: "hcx-popup" });
          return Promise.reject(backend.message);
        }
        return response;
      },
      (axiosError: AxiosError) => {
        showFailToast({ message: axiosError.message, className: "hcx-popup" });
        return axiosError;
      }
    );
  }
}
