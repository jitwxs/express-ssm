package jit.wxs.express.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户反馈表
 * </p>
 *
 * @author jitwxs
 * @since 2018-04-23
 */
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 反馈类型（1订单反馈；2意见反馈；3BUG反馈）
     */
    private Integer type;
    /**
     * 反馈信息
     */
    private String message;
    /**
     * 反馈状态。0：等待处理；1：处理完成
     */
    private Integer status;
    /**
     * 负责人
     */
    private String staffId;
    /**
     * 处理结果
     */
    private String result;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
        return "Feedback{" +
        ", id=" + id +
        ", name=" + name +
        ", tel=" + tel +
        ", type=" + type +
        ", message=" + message +
        ", status=" + status +
        ", staffId=" + staffId +
        ", result=" + result +
        ", createDate=" + createDate +
        ", updateDate=" + updateDate +
        "}";
    }
}
