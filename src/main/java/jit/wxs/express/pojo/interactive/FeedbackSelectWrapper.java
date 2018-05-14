package jit.wxs.express.pojo.interactive;

/**
 * 反馈筛选条件
 * @author jitwxs
 * @since 2018/5/14 14:36
 */
public class FeedbackSelectWrapper {
    /**
     * 反馈类型
     */
    private Integer type;

    /**
     * 反馈状态
     */
    private Integer status;

    /**
     * 反馈人姓名
     */
    private String name;

    /**
     * 反馈人手机号码
     */
    private String tel;

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
}
