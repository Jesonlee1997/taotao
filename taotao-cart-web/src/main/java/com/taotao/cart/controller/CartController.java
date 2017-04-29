package com.taotao.cart.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理Controller TODO:使用redis替代Cookie
 * Created by JesonLee
 * on 2017/4/29.
 */
@Controller
public class CartController {

    @Value("${CART_KEY}")
    private String CART_KEY;

    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;

    @Autowired
    private ItemService itemService;

    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId,
                              @RequestParam(defaultValue = "1") Integer num,
                              HttpServletRequest request,
                              HttpServletResponse response)
    {
        //取购物车商品列表
        List<TbItem> cartItemList = getCartItemList(request);

        //商品在购物车中是否存在
        boolean flag = false;
        for (TbItem tbItem : cartItemList) {
            //如果存在数量增加
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(tbItem.getNum() + num);
                flag = true;
                break;
            }
        }

        //如果不存在，添加一个商品
        if (!flag) {
            //调用服务取商品信息
            TbItem tbItem = itemService.getItemById(itemId);

            //设置购买的商品数量
            tbItem.setNum(num);

            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }

            //把商品添加到购物车
            cartItemList.add(tbItem);
        }


        //把购物车列表写入Cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIRE, true);

        //返回添加成功页面
        return "cartSuccess";
    }

    private List<TbItem> getCartItemList(HttpServletRequest request) {
        //从cookie中取购物车商品列表
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        if (StringUtils.isBlank(json)) {
            //如果没有内容，返回一个空的列表
            return new ArrayList<>();
        }
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;
    }

    @RequestMapping("/cart/cart")
    //返回购物车页面
    public String showCartList(HttpServletRequest request) {
        //从Cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);

        //把购物车列表传递给jsp
        request.setAttribute("cartList", cartItemList);

        return "cart";

    }

    @RequestMapping(value = "/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updataItemNum(@PathVariable Long itemId,
                                      @PathVariable Integer num,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
    {
        //从cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //查询到对应的商品
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                //更新商品数量
                tbItem.setNum(num);
                break;
            }
        }
        //把购物车列表写入Cookie
        CookieUtils.setCookie(request,
                response, CART_KEY,
                JsonUtils.objectToJson(cartItemList),
                CART_EXPIRE, true);
        return TaotaoResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    {
        //从cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);

        //找到对应的商品，删除商品
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                cartItemList.remove(tbItem);
                break;
            }
        }

        //把购物车列表写入Cookie
        CookieUtils.setCookie(request, response, CART_KEY,
                JsonUtils.objectToJson(cartItemList), CART_EXPIRE, true);

        //重定向购物车列表页面
        return "redirect:/cart/cart.html";
    }
}
