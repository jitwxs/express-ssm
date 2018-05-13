package jit.wxs.express.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author jitwxs
 * @since 2018-05-02
 */
public class Express implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId(type = IdType.ID_WORKER)
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String tel;
    /**
     * 快递短信
     */
    private String message;
    /**
     * 配送地址
     */
    private String address;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 是否删除
     */
    private Boolean hasDelete;
    /**
     * 配送人员
     */
    private String staff;
    /**
     * 配送人员备注（一般用于填写异常信息）
     */
    private String staffRemark;
    /**
     * 下单时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @TableField(update = "now()")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getStaffRemark() {
        return staffRemark;
    }

    public void setStaffRemark(String staffRemark) {
        this.staffRemark = staffRemark;
    }

    public Boolean getHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(Boolean hasDelete) {
        this.hasDelete = hasDelete;
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

    @Override
    public String toString() {
        return "Express{" +
        ", id=" + id +
        ", name=" + name +
        ", tel=" + tel +
        ", message=" + message +
        ", address=" + address +
        ", remark=" + remark +
        ", status=" + status +
        ", staff=" + staff +
        ", staffRemark=" + staffRemark +
        ", createDate=" + createDate +
        ", updateDate=" + updateDate +
        "}";
    }
}
