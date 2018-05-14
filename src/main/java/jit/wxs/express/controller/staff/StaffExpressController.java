package jit.wxs.express.controller.staff;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.express.controller.GlobalFunction;
import jit.wxs.express.dto.ExpressDto;
import jit.wxs.express.enums.ExpressStatusEnum;
import jit.wxs.express.interactive.ExpressSelectWrapper;
import jit.wxs.express.interactive.Msg;
import jit.wxs.express.pojo.Express;
import jit.wxs.express.pojo.ExpressPayment;
import jit.wxs.express.service.ExpressPaymentService;
import jit.wxs.express.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jitwxs
 * @since 2018/5/14 17:38
 */
@RestController
@RequestMapping("/staff/express")
public class StaffExpressController {
    @Autowired
    private ExpressService expressService;
    @Autowired
    private ExpressPaymentService expressPaymentService;
    @Autowired
    private GlobalFunction globalFunction;

    /**
     * 获取订单的状态列表
     * @author jitwxs
     * @since 2018/5/2 9:58
     */
    @GetMapping("/status")
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

    /**
     * 获取所有等待接单的订单
     * @author jitwxs
     * @since 2018/5/14 17:39
     */
    @RequestMapping("/list")
    public Map listExpress(Integer rows, Integer page, ExpressSelectWrapper esw) {
        // Get请求中文编码
        try {
            esw.setName(globalFunction.iso8859ToUtf8(esw.getName()));
            esw.setAddress(globalFunction.iso8859ToUtf8(esw.getAddress()));
            esw.setStaffName(globalFunction.iso8859ToUtf8(esw.getStaffName()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 得到筛选条件
        EntityWrapper<Express> expressWrapper = globalFunction.getExpressWrapper(esw);
        // 设置仅查找等待接单的订单
        expressWrapper.eq("status", ExpressStatusEnum.WAIT_DIST.getIndex());
        Page<Express> selectPage = expressService.selectPage(new Page<>(page, rows), expressWrapper);

        List<ExpressDto> list = globalFunction.express2dto(selectPage.getRecords());

        Map<String,Object> map = new HashMap<>();
        map.put("total", selectPage.getTotal());
        map.put("rows", list);
        return map;
    }

    /**
     * 获取所有个人的订单
     * @author jitwxs
     * @since 2018/5/14 17:39
     */
    @RequestMapping("/selfList")
    public Map listSelfExpress(Integer rows, Integer page, ExpressSelectWrapper esw) {
        // Get请求中文编码
        try {
            esw.setName(globalFunction.iso8859ToUtf8(esw.getName()));
            esw.setAddress(globalFunction.iso8859ToUtf8(esw.getAddress()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 得到筛选条件
        EntityWrapper<Express> expressWrapper = globalFunction.getExpressWrapper(esw);
        // 设置查找当前用户的订单
        expressWrapper.eq("staff", globalFunction.getUserId());
        Page<Express> selectPage = expressService.selectPage(new Page<>(page, rows), expressWrapper);

        List<ExpressDto> list = globalFunction.express2dto(selectPage.getRecords());

        Map<String,Object> map = new HashMap<>();
        map.put("total", selectPage.getTotal());
        map.put("rows", list);
        return map;
    }

    /**
     * 获取单个订单详情
     * @author jitwxs
     * @since 2018/5/2 14:04
     */
    @GetMapping("/{id}")
    public Msg getById(@PathVariable String id) {
        Express express = expressService.selectById(id);
        ExpressDto expressDto = globalFunction.express2dto(express);

        return Msg.ok(null,expressDto);
    }

    /**
     * 接单
     * @author jitwxs
     * @since 2018/5/14 17:45
     */
    @PostMapping("")
    public Msg acceptExpress(String[] ids) {
        for(String id : ids) {
            Express express = expressService.selectById(id);

            express.setStaff(globalFunction.getUserId());
            express.setStatus(ExpressStatusEnum.TRANSPORT.getIndex());
            expressService.updateById(express);
        }
        return Msg.ok();
    }

    /**
     * 确认订单
     * @author jitwxs
     * @since 2018/5/13 17:51
     */
    @PostMapping("/confirm")
    public Msg confirmExpress(ExpressPayment payment) {
        String id = payment.getExpressId();

        Express express = expressService.selectById(id);
        express.setStatus(ExpressStatusEnum.COMPLTE.getIndex());
        expressService.updateById(express);

        expressPaymentService.updateById(payment);

        return Msg.ok();
    }

    /**
     * 异常订单
     * @author jitwxs
     * @since 2018/5/13 17:51
     */
    @PostMapping("/error")
    public Msg errorExpress(String[] ids, String text) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            // 只有订单状态为TRANSPORT时才要确认
            if(ExpressStatusEnum.TRANSPORT.getName().equals(ExpressStatusEnum.getName(express.getStatus()))) {
                express.setStatus(ExpressStatusEnum.ERROR.getIndex());
                express.setStaffRemark(text);
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }
}
