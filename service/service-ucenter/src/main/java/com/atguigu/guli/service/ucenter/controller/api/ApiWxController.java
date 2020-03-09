package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.HttpClientUtils;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author zcgstart
 * @create 2020-03-06 19:16
 */
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;
    @Autowired
    private MemberService memberService;
    /**
     * 点击登录按钮显示微信登录二维码
     * @param session
     * @return
     */
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect ";

        String appid = ucenterProperties.getAppid();
        String redirecturi = ucenterProperties.getRedirecturi();
        try {
            redirecturi = URLEncoder.encode(redirecturi,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //state:该参数可用于防止csrf攻击（跨站请求伪造攻击），
        // 建议第三方带上该参数，可设置为简单的随机数加session进行校验
        String state = UUID.randomUUID().toString();
        session.setAttribute("wx-open-state", state);
        System.out.println("生产state = " + state);

        String qrcodeUrl = String.format(
                baseUrl,//原始字符串
                appid,
                redirecturi,
                state
        );
        return "redirect:" + qrcodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session){
        System.out.println("callback被调用");
        System.out.println("code" + code);//授权临时票据
        System.out.println("state" + state);//防止csrf攻击

        //1.校验code和state是否存在，不存在则拒绝请求
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(state) ){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //2.用请求参数中的state和session中存储的state比对，一致则接收回调请求
        //不一致则拒绝
        String sessionState = (String)session.getAttribute("wx-open-state");
        if(!state.equals(sessionState)){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //3.通过code参数加上AppID和AppSecret等，通过API换取access_token
        //携带授权临时票据code，和appid以及appsecret请求access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenuUrl = String.format(
                baseAccessTokenUrl,
                ucenterProperties.getAppid(),
                ucenterProperties.getAppsecret(),
                code
        );

        String result = "";
        try {
            result = HttpClientUtils.get(accessTokenuUrl);
            System.out.println("result = " + result);
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String, Object> resultMap = gson.fromJson(result, HashMap.class);

        Object errcodeObj = resultMap.get("errcode");
        if(errcodeObj != null){
            String errmsg = (String)resultMap.get("errmsg");
            Double errcode = (Double)errcodeObj;
            log.error("获取access_token失败 - " + "message: " + errmsg + ", errcode: " + errcode);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //获取access_token
        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");

        System.out.println("accessToken = " + accessToken);
        System.out.println("openid = " + openid);

        //根据access_token获取微信用户的基本信息
        Member member = memberService.getByOpenid(openid);
        if(member == null){
            //向微信的资源服务器发起请求，获取当前用户的用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            String userInfoUrl = String.format(
                    baseUserInfoUrl,
                    accessToken,
                    openid);

            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                log.error(ExceptionUtils.getMessage(e));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            HashMap<String, Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            if(resultUserInfoMap.get("errcode") != null){
                log.error("获取用户信息失败" + "，message：" + resultMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            String nickname = (String) resultUserInfoMap.get("nickname");
            String headimgurl = (String)resultUserInfoMap.get("headimgurl");
            Double sex = (Double)resultUserInfoMap.get("sex");

            member = new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setSex(sex.intValue());
            memberService.save(member);

        }
        //生成并颁发jwt
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("nickname", member.getNickname());
        claims.put("avatar", member.getAvatar());
        String token = JwtUtils.generateJWT(claims);
        return "redirect:http://localhost:3000?token=" + token;
    }
}
