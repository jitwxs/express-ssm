package jit.wxs.express.enums;

/**
 * 权限枚举
 * @author jitwxs
 * @date 2018/5/2 0:14
 */
public enum RoleEnum {
    /**
     * 管理员用户
     */
    ADMIN("admin", 0),

    /**
     * 配送用户
     */
    STAFF("staff", 1);

    private String name;
    private int index;

    RoleEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getIndex() == index) {
                return roleEnum.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
