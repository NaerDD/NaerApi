package com.naer.project.model.vo;

import com.naer.naerApiCommon.model.entity.InterfaceInfo;
import lombok.Data;

/**
 * @ClassName InterfaceVo
 * @Description TODO
 * @Author lish
 * @Date 2023/4/27 9:32
 */
@Data
public class InterfaceVo extends InterfaceInfo {

    /*调用次数*/
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}
