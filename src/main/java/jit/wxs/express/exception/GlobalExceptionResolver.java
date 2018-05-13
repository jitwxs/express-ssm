package jit.wxs.express.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jitwxs
 * @date 2018/4/23 10:13
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        logger.error("发生异常", e);

        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("error/500");

        return modelAndView;
    }
}
