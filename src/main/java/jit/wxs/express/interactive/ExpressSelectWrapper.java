package jit.wxs.express.interactive;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单筛选条件
 * @author jitwxs
 * @since 2018/5/13 15:30
 */
public class ExpressSelectWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 订单号
     */
    private String id;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 号码
     */
    private String tel;

    /**
     * 姓名
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 配送员名
     */
    private String staffName;

    /**
     * 是否删除
     */
    private Boolean hasDelete;

    /**
     * 几日内的订单
     */
    private Integer includeDay;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Boolean getHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(Boolean hasDelete) {
        this.hasDelete = hasDelete;
    }

    public Integer getIncludeDay() {
        return includeDay;
    }

    public void setIncludeDay(Integer includeDay) {
        this.includeDay = includeDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
