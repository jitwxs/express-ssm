package jit.wxs.express.enums;

/**
 * 支付方式枚举
 * @author jitwxs
 * @date 2018/5/2 14:14
 */
public enum PaymentEnum {
    /**
     * 线下支付
     */
    OFFLINE("线下支付", 0),

    /**
     * 微信支付
     */
    WECHAT("微信支付", 1),

    /**
     * 支付宝支付
     */
    ALI_PAY("支付宝支付", 2);

    private String name;
    private int index;

    PaymentEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (PaymentEnum paymentEnum : PaymentEnum.values()) {
            if (paymentEnum.getIndex() == index) {
                return paymentEnum.name;
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
