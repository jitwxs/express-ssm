package jit.wxs.express.enums;

/**
 * 支付状态枚举
 * @author jitwxs
 * @date 2018/5/2 14:14
 */
public enum PaymentStatusEnum {
    /**
     * 等待付款
     */
    WAIT_BUYER_PAY("等待付款", 1),

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     * （1)订单已创建，但用户未付款，调用关闭交易接口
     * （2）付款成功后，订单金额已全部退款【如果没有全部退完，仍是TRADE_SUCCESS状态】
     */
    TRADE_CLOSED("交易关闭", 2),

    /**
     * 交易支付成功
     * （1）用户付款成功
     */
    TRADE_SUCCESS("交易成功", 3),

    /**
     * 交易结束，不可退款
     * （1）退款日期超过可退款期限后
     */
    TRADE_FINISHED("交易结束", 4);

    private String name;
    private int index;

    PaymentStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (PaymentStatusEnum expressStatusEnum : PaymentStatusEnum.values()) {
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
