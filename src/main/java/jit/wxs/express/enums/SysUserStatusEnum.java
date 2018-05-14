package jit.wxs.express.enums;

/**
 * 用户状态枚举
 * @author jitwxs
 * @since 2018/5/13 20:03
 */
public enum SysUserStatusEnum {
    /**
     * 在职
     */
    ACTIVE("在职", 0),

    /**
     * 冻结
     */
    FREEZE("冻结", 1),

    /**
     * 离职
     */
    LEAVE("离职", 2);

    private String name;
    private int index;

    SysUserStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (SysUserStatusEnum userStatusEnum : SysUserStatusEnum.values()) {
            if (userStatusEnum.getIndex() == index) {
                return userStatusEnum.name;
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
