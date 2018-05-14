package jit.wxs.express.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.express.dto.FeedbackDto;
import jit.wxs.express.enums.FeedbackTypeEnum;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.pojo.SysUser;
import jit.wxs.express.pojo.interactive.FeedbackSelectWrapper;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.FeedbackService;
import jit.wxs.express.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户反馈Controller
 * @author jitwxs
 * @since 2018/5/14 14:35
 */
@RestController
@RequestMapping("/admin/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private SysUserService userService;

    private List<FeedbackDto> feedback2dto(List<Feedback> list) {
        List<FeedbackDto> result = new ArrayList<>();
        for(Feedback feedback : list) {
            FeedbackDto dto = feedback2dto(feedback);
            result.add(dto);
        }
        return result;
    }

    private FeedbackDto feedback2dto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        BeanUtils.copyProperties(feedback, dto);
        // 设置反馈类型名
        dto.setTypeName(FeedbackTypeEnum.getName(feedback.getType()));
        // 设置处理人名
        if(StringUtils.isNotBlank(feedback.getStaffId())) {
            SysUser user = userService.selectById(feedback.getStaffId());
            dto.setStaffName(user.getUsername());
        }
        String statusName="";
        switch (feedback.getStatus()) {
            case 0:
                statusName = "未处理";
                break;
            case 1:
                statusName = "已处理";
                break;
            default:
                break;
        }
        dto.setStatusName(statusName);
        return dto;
    }

    /**
     * 构造筛选条件
     * @author jitwxs
     * @since 2018/5/13 15:50
     */
    private EntityWrapper<Feedback> getFeedbackWrapper(FeedbackSelectWrapper fsw) {
        EntityWrapper<Feedback> entityWrapper = new EntityWrapper<>();
        // 前台传递，-1表示所有状态
        if(fsw.getStatus() != null && fsw.getStatus() != -1) {
            entityWrapper.eq("status", fsw.getStatus());
        }
        // 前台传递，-1表示所有类型
        if(fsw.getType() != null && fsw.getType() != -1) {
            entityWrapper.eq("type", fsw.getType());
        }

        if(StringUtils.isNotBlank(fsw.getTel())){
            entityWrapper.eq("tel", fsw.getTel());
        }
        if(StringUtils.isNotBlank(fsw.getName())){
            entityWrapper.like("name", fsw.getName());
        }
        return entityWrapper;
    }

    /**
     * 获取所有反馈类型
     * @author jitwxs
     * @since 2018/5/14 14:46
     */
    @GetMapping("/type/list")
    public Msg listFeedbackType() {
        List<Map<String,Object>> result = new ArrayList<>();
        for(FeedbackTypeEnum enums :FeedbackTypeEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",enums.getIndex());
            map.put("name",enums.getName());
            result.add(map);
        }
        return Msg.ok(null,result);
    }

    /**
     * 获取所有反馈
     * @author jitwxs
     * @since 2018/5/14 14:35
     */
    @GetMapping("/list")
    public Map listFeedback(Integer rows, Integer page, FeedbackSelectWrapper fsw) {
        // Get请求中文编码
        try {
            String name = fsw.getName();
            if(StringUtils.isNotBlank(name)) {
                fsw.setName(new String(name.getBytes("iso8859-1"),"utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 得到筛选条件
        EntityWrapper<Feedback> feedbackWrapper = getFeedbackWrapper(fsw);
        Page<Feedback> selectPage = feedbackService.selectPage(new Page<>(page, rows), feedbackWrapper);

        List<FeedbackDto> list = feedback2dto(selectPage.getRecords());

        Map<String,Object> map = new HashMap<>();
        map.put("total", selectPage.getTotal());
        map.put("rows", list);
        return map;
    }

    /**
     * 删除反馈
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/delete")
    public Msg deleteById(String[] ids) {
        for(String id : ids) {
            feedbackService.deleteById(id);
        }
        return Msg.ok();
    }

    /**
     * 处理反馈
     * @author jitwxs
     * @since 2018/5/14 15:06
     */
    @PostMapping("")
    public Msg handleFeedback(String id, String content) {
        if(StringUtils.isBlank(id) || StringUtils.isBlank(content)) {
            return Msg.error("参数错误");
        }
        Feedback feedback = feedbackService.selectById(id);
        // 设置处理人为当前用户
        String username = (String)SecurityUtils.getSubject().getPrincipal();
        SysUser user = userService.getByUserName(username);
        feedback.setStaffId(user.getId());
        feedback.setResult(content);
        // 0代表未处理，1代表已处理；默认为0
        feedback.setStatus(1);

        feedbackService.updateById(feedback);

        return Msg.ok();
    }
}
