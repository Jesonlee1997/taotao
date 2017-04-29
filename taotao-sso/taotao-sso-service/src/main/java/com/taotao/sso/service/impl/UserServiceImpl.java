package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户处理Service
 * Created by JesonLee
 * on 2017/4/28.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_SESSION}")
    private String USER_SESSION;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String data, int type) {
        //设置查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        //1.判断用户名是否可用
        if (type == 1) {
            criteria.andUsernameEqualTo(data);
        } else if (type == 2) {//判断手机号是否可用
            criteria.andPhoneEqualTo(data);
        } else if (type == 3) {//判断邮箱是否可用
            criteria.andEmailEqualTo(data);
        } else {
            return TaotaoResult.build(400, "请求参数中包含非法数据");
        }

        List<TbUser> users = userMapper.selectByExample(example);
        if (users != null && users.size() > 0) {
            //查询到数据，数据不可用
            return TaotaoResult.ok(false);
        }

        //数据可用
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        //1、检查数据的有效性
        if (StringUtils.isBlank(user.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        //判断用户名是否重复
        TaotaoResult taotaoResult = checkData(user.getUsername(), 1);
        if (!(boolean) taotaoResult.getData()) {
            return TaotaoResult.build(400, "用户名重复");
        }
        //判断密码是否为空
        if (StringUtils.isBlank(user.getPassword())) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        if (StringUtils.isNotBlank(user.getPhone())) {
            taotaoResult = checkData(user.getPhone(), 2);
            if (!(boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "电话号码重复");
            }
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            taotaoResult = checkData(user.getEmail(), 3);
            if (!(boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "email重复");
            }
        }

        //2、补全pojo的属性
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //3、将密码进行MD5加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);

        //4、插入数据
        userMapper.insert(user);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        //判断用户名和密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> users = userMapper.selectByExample(example);
        if (users == null || users.size() == 0) {
            return TaotaoResult.build(400, "用户名或密码不正确");
        }

        TbUser user = users.get(0);
        //密码要经过md5加密然后再检验
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            return TaotaoResult.build(400, "用户名或密码不正确");
        }

        //生成token，使用uuid
        String token = UUID.randomUUID().toString();

        //把用户信息保存到redis，key就是token，value就是用户信息,设置key的过期时间
        user.setPassword(null);

        String key = USER_SESSION +":"+token;
        jedisClient.set(key, JsonUtils.objectToJson(user));
        jedisClient.expire(key, SESSION_EXPIRE);

        //返回登录成功，返回token。
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        String sessionKey = USER_SESSION + ":" + token;
        String json = jedisClient.get(sessionKey);
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.build(400, "用户登录已经过期");
        }
        //重置Session的过期时间
        jedisClient.expire(sessionKey, SESSION_EXPIRE);

        //把Json转换成User对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);

        return TaotaoResult.ok(user);
    }

    @Override
    public TaotaoResult logout(String token) {
        String sessionKey = USER_SESSION + ":" + token;
        jedisClient.del(sessionKey);
        return TaotaoResult.ok();
    }


}
