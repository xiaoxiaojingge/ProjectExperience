package com.itjing.community.controller;

import com.itjing.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author: lijing
 * @Date: 2021年08月02日 21:32
 * @Description: 统计网站数据
 */
@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 统计页面
     *
     * @return
     */
    @RequestMapping(path = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage() {
        return "site/admin/data";
    }

    /**
     * 统计网站UV
     *
     * @param start
     * @param end
     * @param model
     * @return
     */
    @RequestMapping(path = "/data/uv", method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {

        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);

        // 转发是在一次请求内完成的，所以本方法的请求方式为post，转发到 data 页面，请求还是 post
        // 因此 /data 上面标有两个请求方式，可以处理两种请求
        return "forward:/data";
    }

    /**
     * 统计活跃用户
     *
     * @param start
     * @param end
     * @param model
     * @return
     */
    @RequestMapping(path = "/data/dau", method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);

        // 转发是在一次请求内完成的，所以本方法的请求方式为post，转发到 data 页面，请求还是 post
        // 因此 /data 上面标有两个请求方式，可以处理两种请求
        return "forward:/data";
    }

}
