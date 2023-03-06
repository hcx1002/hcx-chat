import { request } from "@/config/request";

export function login(username: string, password: string) {
  return request.post<string>("user/login", { username, password });
}

export function regis(username: string, password: string) {
  return request.post<string>("user/regis", { username, password });
}
export function logout() {
  return request.get<string>("user/logout");
}
export function getUserInfo() {
  return request.get<User.Info>("user/info");
}
