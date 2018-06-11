package jit.wxs.express.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.express.controller.GlobalFunction;
import jit.wxs.express.dto.FeedbackDto;
import jit.wxs.express.enums.FeedbackTypeEnum;
import jit.wxs.express.interactive.FeedbackSelectWrapper;
import jit.wxs.express.interactive.Msg;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.service.FeedbackService;
import jit.wxs.express.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private GlobalFunction globalFunction;

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
    public Map listFeedback(Integer rows, Integer page, FeedbackSelectWrapper fsw, @RequestParam(defaultValue = "createDate") String order) {
        // Get请求中文编码
        try {
            fsw.setName(globalFunction.iso8859ToUtf8(fsw.getName()));
            fsw.setStaffName(globalFunction.iso8859ToUtf8(fsw.getStaffName()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 得到筛选条件
        EntityWrapper<Feedback> feedbackWrapper = globalFunction.getFeedbackWrapper(fsw);
        Page<Feedback> selectPage = feedbackService.selectPage(new Page<>(page, rows,order,false), feedbackWrapper);

        List<FeedbackDto> list = globalFunction.feedback2dto(selectPage.getRecords());

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
        feedback.setStaffId(globalFunction.getUserId());
        feedback.setResult(content);
        // 0代表未处理，1代表已处理；默认为0
        feedback.setStatus(1);

        feedbackService.updateById(feedback);

        return Msg.ok();
    }
}
