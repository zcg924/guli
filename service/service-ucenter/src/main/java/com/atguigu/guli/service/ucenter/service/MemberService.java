package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginInfoVo;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author zcg
 * @since 2020-03-02
 */
public interface MemberService extends IService<Member> {
    Integer countRegisterByDay(String day);
    //用户注册
    void register(RegisterVo registerVo);
    //用户登录
    String login(LoginVo loginVo);
    //用户信息获取
    LoginInfoVo getLoginInfoByJwtToken(String jwtToken);
    //根据openid返回用户信息
    Member getByOpenid(String openid);
    //根据会员id获取会员信息
    MemberDto getMemberDtoByMemberId(String memberId);
}
