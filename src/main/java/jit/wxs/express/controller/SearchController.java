package jit.wxs.express.controller;

import jit.wxs.express.interactive.Msg;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.Feedback;
import jit.wxs.express.service.ExpressService;
import jit.wxs.express.service.FeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询Controller
 * @author jitwxs
 * @since 2018/5/14 12:44
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ExpressService expressService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private GlobalFunction globalFunction;

    /**
     * 订单查询
     * @author jitwxs
     * @since 2018/5/14 12:49
     */
    @PostMapping("")
    public Msg search(String id) {
        // 订单号为纯数字,反馈号为数字+字母
        if(StringUtils.isNumeric(id)) {
            Express express = expressService.selectById(id);
            if(express == null) {
                return Msg.error("没有查到相关信息");
            } else {
                return Msg.ok("0", globalFunction.express2dto(express));
            }
        } else {
            Feedback feedback = feedbackService.selectById(id);
            if(feedback == null) {
                return Msg.error("没有查到相关信息");
            } else {
                return Msg.ok("1", globalFunction.feedback2dto(feedback));
            }
        }
    }
}
