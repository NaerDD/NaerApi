package com.naer.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naer.naerApiCommon.model.entity.User;
import com.naer.naerApiCommon.model.entity.UserInterfaceInfo;
import com.naer.project.annotation.AuthCheck;
import com.naer.project.common.*;
import com.naer.project.constant.CommonConstant;
import com.naer.project.constant.UserConstant;
import com.naer.project.exception.BusinessException;
import com.naer.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.naer.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.naer.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.naer.project.service.UserInterfaceInfoService;
import com.naer.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Autowired
    private UserInterfaceInfoService userInterfaceInfoService;

    @Autowired
    private UserService userService;


    // region 增删改查

    /**
     * 创建
     *
     * @param userInterfaceInfoInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoInfoAddRequest, userInterfaceInfoInfo);
        // 校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfoInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfoInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(userInterfaceInfoInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newUserInterfaceInfoId = userInterfaceInfoInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoInfoUpdateRequest,
                                                         HttpServletRequest request) {
        if (userInterfaceInfoInfoUpdateRequest == null || userInterfaceInfoInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoInfoUpdateRequest, userInterfaceInfoInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfoInfo, false);
        User user = userService.getLoginUser(request);
        long id = userInterfaceInfoInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfoInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfoInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfoInfoQuery = new UserInterfaceInfo();
        if (userInterfaceInfoInfoQueryRequest != null) {
            BeanUtils.copyProperties(userInterfaceInfoInfoQueryRequest, userInterfaceInfoInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoInfoQueryRequest, userInterfaceInfoInfoQuery);
        long current = userInterfaceInfoInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInfoInfoPage);
    }

    //endregion
//    @Transactional
//    @PostMapping("/payInterface")
//    public BaseResponse<Object> payInterface(String interfaceName, String adminPsd, String payAccount, int num) {
//
//        LambdaQueryWrapper<InterfaceInfo> lqw = new LambdaQueryWrapper<InterfaceInfo>();
//        lqw.eq(InterfaceInfo::getName, interfaceName);
//        InterfaceInfo interfaceInfo = interfaceInfoService.getOne(lqw);
//
//        LambdaQueryWrapper<User> lqw1 = new LambdaQueryWrapper<User>();
//        lqw1.eq(User::getUserAccount, payAccount);
//        User user = userService.getOne(lqw1);
//        if (user == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
//        }
//        LambdaQueryWrapper<UserInterfaceInfo> lqw2 = new LambdaQueryWrapper<UserInterfaceInfo>();
//        lqw2.eq(UserInterfaceInfo::getUserId, user.getId());
//        lqw2.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfo.getId());
//        UserInterfaceInfo one = userInterfaceInfoInfoService.getOne(lqw2);
//        if (one != null) {
//            one.setLeftNum(one.getLeftNum() + num);
//            userInterfaceInfoInfoService.saveOrUpdate(one);
//        } else {
//            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
//            userInterfaceInfo.setUserId(user.getId());
//            userInterfaceInfo.setInterfaceInfoId(interfaceInfo.getId());
//            userInterfaceInfo.setLeftNum(num);
//            userInterfaceInfoInfoService.save(userInterfaceInfo);
//        }
//        return ResultUtils.success(true);
//    }
//
//    @GetMapping("/selfInterfaceData")
//    public BaseResponse<List<SelfInterfaceDateVo>> selfInterfaceData(HttpServletRequest request){
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        System.out.println(request.getSession().getId());
//        User currentUser = (User) userObj;
//        Long id = currentUser.getId();
//        LambdaQueryWrapper<UserInterfaceInfo> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(UserInterfaceInfo::getUserId,id);
//        List<UserInterfaceInfo> list = userInterfaceInfoInfoService.list(lqw);
//        List<SelfInterfaceDateVo> selfInterfaceDateVos = new ArrayList<>();
//        for (UserInterfaceInfo userInterfaceInfo : list) {
//            SelfInterfaceDateVo selfInterfaceDateVo = new SelfInterfaceDateVo();
//            BeanUtils.copyProperties(userInterfaceInfo,selfInterfaceDateVo);
//            Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
//            LambdaQueryWrapper<InterfaceInfo> lqw1 = new LambdaQueryWrapper<>();
//            lqw1.eq(InterfaceInfo::getId,interfaceInfoId);
//            InterfaceInfo one = interfaceInfoService.getOne(lqw1);
//            String name = one.getName();
//            selfInterfaceDateVo.setInterfaceName(name);
//            selfInterfaceDateVos.add(selfInterfaceDateVo);
//        }
//        return ResultUtils.success(selfInterfaceDateVos);
//    }

}
