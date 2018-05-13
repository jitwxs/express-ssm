package jit.wxs.express.dto;

import jit.wxs.express.pojo.Express;

/**
 * @author jitwxs
 * @date 2018/5/2 10:30
 */
public class ExpressDto extends Express {
    /**
     * 订单状态名
     */
    private String statusName;

    /**
     * 订单配送员名称
     */
    private String staffName;

    /**
     * 订单支付方式
     */
    private String paymentType;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
