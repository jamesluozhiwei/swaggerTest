package com.lzw.swagger.controller;

import com.lzw.swagger.entity.ApiResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author James
 * 测试控制层
 */
@Api(tags = "TestController",description = "控制层描述")
@RestController
public class TestController {

    /**
     * 测试接口
     * @param id
     * @return 返回自定义 统一响应消息对象
     */
    @ApiOperation(value = "测试接口",notes = "用于测试swagger的restful API文档")
    @RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public ApiResponseObject test(@PathVariable("id")int id){
        return new ApiResponseObject().setData(id);
    }

    /**
     * POST测试
     * @param params
     * @return 返回自定义 统一响应消息对象
     */
    @ApiOperation(value = "POST测试",notes = "用于测试使用实体作为参数的效果，通过produces指定contentType",produces = "application/json;charset=utf-8")
    @RequestMapping(value = "/save/XXX",method = RequestMethod.POST)
    public ApiResponseObject saveXXX(@RequestBody ApiResponseObject params){
        return new ApiResponseObject().setData(params);
    }

    /**
     * 多参数测试
     * @param code
     * @param msg
     * @return 返回自定义 统一响应消息对象
     */
    @ApiOperation(value = "多参数测试",notes = "用于测试多个参数的效果")
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value = "响应code",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name="msg",value = "响应msg",required = true,dataType = "string",paramType = "query")
    })
    @RequestMapping(value = "/test/params",method = RequestMethod.PUT)
    public ApiResponseObject testParams(int code,String msg){
        return new ApiResponseObject().setCode(code).setMsg(msg);
    }

}
