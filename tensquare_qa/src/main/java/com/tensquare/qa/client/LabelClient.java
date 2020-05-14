package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//用于声明应该创建具有该接口的REST客户端（例如，用于自动连接到另一个组件）
//@FeignClient("tensquare-base")//tensquare-base:9001--http://DESKTOP-1CG46P9:9001
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)//一旦熔断器打开，则执行该的对象
public interface LabelClient {

    /**
     * 根据id查询一个标签-用于Feign
     * 注意：方法名和资源路径和label一样
     * http://DESKTOP-1CG46P9:9001//label/{id}
     * @param id
     * @return
     */
    @GetMapping("/label/{id}")
    ResultDTO listById(@PathVariable("id") String id);
}
