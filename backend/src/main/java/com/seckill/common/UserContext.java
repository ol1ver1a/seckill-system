package com.seckill.common;

public class UserContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> USER_ROLE = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    public static void setUserId(Long id) { USER_ID.set(id); }
    public static Long getUserId() { return USER_ID.get(); }
    public static void setUserRole(Integer role) { USER_ROLE.set(role); }
    public static Integer getUserRole() { return USER_ROLE.get(); }
    public static void setUsername(String name) { USERNAME.set(name); }
    public static String getUsername() { return USERNAME.get(); }

    public static void clear() {
        USER_ID.remove();
        USER_ROLE.remove();
        USERNAME.remove();
    }
}
