package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zcgstart
 * @create 2020-02-22 9:29
 */
@Data
public class SubjectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private Integer sort;
    private List<SubjectVo> children = new ArrayList<>();
}
