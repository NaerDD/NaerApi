package com.naer.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naer.naerApiCommon.model.entity.InterfaceInfo;
import com.naer.project.common.ErrorCode;
import com.naer.project.exception.BusinessException;
import com.naer.project.mapper.InterfaceInfoMapper;
import com.naer.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Naerdd
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2022-11-28 17:49:19
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }

    }

}




