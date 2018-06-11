package jit.wxs.express.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单支付表
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
public class ExpressPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId(type = IdType.INPUT)
    private String expressId;
    /**
     * 支付方式
     */
    private Integer type;
    /**
     * 支付状态
     */
    private Integer status;
    /**
     * 线上支付金额
     */
    private Double onlinePayment;
    /**
     * 线上支付第三方的流水号
     */
    private String onlinePaymentNum;
    /**
     * 线上收款方
     */
    private String onlineSeller;
    /**
     * 线下支付金额
     */
    private Double offlinePayment;
    /**
     * 备注
     */
    private String remark;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @TableField(update = "now()")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getOnlinePayment() {
        return onlinePayment;
    }

    public void setOnlinePayment(Double onlinePayment) {
        this.onlinePayment = onlinePayment;
    }

    public String getOnlinePaymentNum() {
        return onlinePaymentNum;
    }

    public void setOnlinePaymentNum(String onlinePaymentNum) {
        this.onlinePaymentNum = onlinePaymentNum;
    }

    public String getOnlineSeller() {
        return onlineSeller;
    }

    public void setOnlineSeller(String onlineSeller) {
        this.onlineSeller = onlineSeller;
    }

    public Double getOfflinePayment() {
        return offlinePayment;
    }

    public void setOfflinePayment(Double offlinePayment) {
        this.offlinePayment = offlinePayment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
