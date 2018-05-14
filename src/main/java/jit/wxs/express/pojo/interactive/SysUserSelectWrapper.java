package jit.wxs.express.pojo.interactive;

import java.io.Serializable;

/**
 * 用户筛选条件
 * @author jitwxs
 * @since 2018/5/13 20:09
 */
public class SysUserSelectWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String sex;

    private String tel;

    private String address;

    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
