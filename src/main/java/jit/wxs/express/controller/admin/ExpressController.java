package jit.wxs.express.controller.admin;

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
 * 管理员订单Controller
 * @author jitwxs
 * @date 2018/5/2 0:21
 */
@RestController
@RequestMapping("/admin/express")
public class ExpressController {
    @Autowired
    private ExpressService expressService;
    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private ExpressPaymentService expressPaymentService;

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
     * 订单列表
     * @param esw 筛选条件
     * @author jitwxs
     * @since 2018/5/2 0:33
     */
    @GetMapping("/list")
    public Map listExpress(Integer rows, Integer page, ExpressSelectWrapper esw, @RequestParam(defaultValue = "createDate") String order) {
        // Get请求中文编码
        try {
            esw.setName(globalFunction.iso8859ToUtf8(esw.getName()));
            esw.setStaffName(globalFunction.iso8859ToUtf8(esw.getStaffName()));
            esw.setAddress(globalFunction.iso8859ToUtf8(esw.getAddress()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 得到筛选条件
        EntityWrapper<Express> expressWrapper = globalFunction.getExpressWrapper(esw);
        Page<Express> selectPage = expressService.selectPage(new Page<>(page, rows, order, false), expressWrapper);

        List<ExpressDto> list = globalFunction.express2dto(selectPage.getRecords());

        Map<String,Object> map = new HashMap<>(16);
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
     * 分配订单
     * @param ids 订单数组
     * @param staffId 派送员id
     * @author jitwxs
     * @since 2018/5/2 16:37
     */
    @PostMapping("/assign")
    public Msg assignExpress(String[] ids,String staffId) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            // 只有订单状态为WAIT_DIST时才要分配订单
            if(ExpressStatusEnum.WAIT_DIST.getName().equals(ExpressStatusEnum.getName(express.getStatus()))) {
                express.setStaff(staffId);
                express.setStatus(ExpressStatusEnum.TRANSPORT.getIndex());
                expressService.updateById(express);
            }
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

    /**
     * 删除订单
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/delete")
    public Msg deleteById(String[] ids) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            if(express != null) {
                // 设置删除标记为true
                express.setHasDelete(true);
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 恢复订单
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/recycle")
    public Msg recycleById(String[] ids) {
        for(String id : ids) {
            Express express = expressService.selectById(id);
            if(express != null) {
                // 设置删除标记为false
                express.setHasDelete(false);
                expressService.updateById(express);
            }
        }
        return Msg.ok();
    }

    /**
     * 彻底删除订单
     * @author jitwxs
     * @since 2018/5/2 14:05
     */
    @PostMapping("/clean")
    public Msg cleanById(String[] ids) {
        for(String id : ids) {
            expressService.deleteById(id);
        }
        return Msg.ok();
    }
}
