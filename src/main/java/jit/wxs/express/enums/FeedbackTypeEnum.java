package jit.wxs.express.enums;

/**
 * 反馈类型枚举
 * @author jitwxs
 * @since 2018/5/14 14:00
 */
public enum FeedbackTypeEnum {
    /**
     * 订单反馈
     */
    EXPRESS("订单反馈", 1),

    /**
     * 意见反馈
     */
    SUGGEST("意见反馈", 2),

    /**
     * BUG反馈
     */
    BUG("BUG反馈", 3);

    private String name;
    private int index;

    FeedbackTypeEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (FeedbackTypeEnum feedbackTypeEnum : FeedbackTypeEnum.values()) {
            if (feedbackTypeEnum.getIndex() == index) {
                return feedbackTypeEnum.name;
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
