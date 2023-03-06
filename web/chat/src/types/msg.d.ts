declare namespace Msg {
  interface Result {
    code: number;
    data: Data;
    date: number;
  }
  interface Data {
    sid?: string;
    type: number;
    user: User;
    onlineCount?: number;
    value?: string;
    isMe?: number;
  }
  interface User {
    id: string;
    avatar: string;
    username: string;
    vip: number;
  }
}
