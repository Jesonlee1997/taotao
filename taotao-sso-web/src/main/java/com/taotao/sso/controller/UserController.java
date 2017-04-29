package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JesonLee
 * on 2017/4/28.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${TOKEN_KEY}")
    private String TT_TOKEN;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
        return userService.checkData(param, type);
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user) {
        return userService.register(user);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username,
                              String password,
                              HttpServletResponse response,
                              HttpServletRequest request) {
        TaotaoResult result = userService.login(username, password);
        //把Token写入Cookie
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, TT_TOKEN, result.getData().toString());
        }
        return result;
    }

    /*@RequestMapping(
            value = "/user/token/{token}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {
        TaotaoResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(result) + ")";

        }
        return JsonUtils.objectToJson(result);
    }*/

    @RequestMapping(
            value = "/user/token/{token}",
            method = RequestMethod.GET
            )
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        TaotaoResult result = userService.getUserByToken(token);
        //判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    @RequestMapping("/user/logout/{token}")
    @ResponseBody
    public TaotaoResult logout(@PathVariable String token) {
        return userService.logout(token);
    }

}
