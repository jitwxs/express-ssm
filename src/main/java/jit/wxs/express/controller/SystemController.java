package jit.wxs.express.controller;

import jit.wxs.express.enums.ExpressStatusEnum;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.pojo.interactive.Msg;
import jit.wxs.express.service.ExpressService;
import jit.wxs.express.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author jitwxs
 * @date 2018/5/1 23:34
 */
@RestController
public class SystemController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private ExpressService expressService;

    /**
     * 提交反馈
     * @author jitwxs
     * @since 2018/5/1 23:34
     */
    @PostMapping("/feedback")
    public Msg feedback(Feedback feedback) {
        feedback.setCreateDate(new Date());
        feedbackService.insert(feedback);

        return Msg.ok();
    }

    /**
     * 下单
     * @author jitwxs
     * @since 2018/5/1 23:49
     */
//    @PostMapping("/express")
//    public Msg express(Express express) {
//
//        return Msg.ok();
//    }

    /**
     * 获取订单的状态列表
     * @author jitwxs
     * @since 2018/5/2 9:58
     */
    @GetMapping("/express/status/list")
    public Msg listExpressStatus() {
        List<Map<String,Object>> result = new ArrayList<>();
        for(ExpressStatusEnum enums :ExpressStatusEnum.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",enums.getIndex());
            map.put("name",enums.getName());
            result.add(map);
        }
        return Msg.ok(null,result);
    }
}
