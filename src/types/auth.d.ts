export interface UserInfo {
  userId: string;
  userName: string;
  emailId: string;
  emailDomain: string;
  joinDate: string;
}

export interface SignupRequest {
  userId: string;
  userPwd: string;
  userName: string;
  emailId: string;
  emailDomain: string;
}

export interface LoginRequest {
  userId: string;
  userPwd: string;
}

export interface LoginResponse {
  userInfo: UserInfo;
  'refresh-token': string;
  'access-token': string;
}
