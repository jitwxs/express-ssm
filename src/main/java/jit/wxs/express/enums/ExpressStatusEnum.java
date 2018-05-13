package jit.wxs.express.enums;

/**
 * 快递状态枚举
 * @author jitwxs
 * @date 2018/5/2 14:14
 */
public enum ExpressStatusEnum {
    /**
     * 等待支付
     */
    WAIT_PAYMENT("等待支付", 0),

    /**
     * 等待派送
     */
    WAIT_DIST("等待派送", 1),

    /**
     * '派送中
     */
    TRANSPORT("派送中", 2),

    /**
     * 订单完成
     */
    COMPLTE("订单完成", 3),

    /**
     * '订单异常
     */
    ERROR("订单异常", 4);

    private String name;
    private int index;

    ExpressStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (ExpressStatusEnum expressStatusEnum : ExpressStatusEnum.values()) {
            if (expressStatusEnum.getIndex() == index) {
                return expressStatusEnum.name;
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
