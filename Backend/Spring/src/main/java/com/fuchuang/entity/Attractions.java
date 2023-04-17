package com.fuchuang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Attractions对象", description="")
public class Attractions implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "纬度")
    private double latitude;
    @ApiModelProperty(value = "经度")
    private double longitude;
    @ApiModelProperty(value = "Poi")
    private int poi;
    @ApiModelProperty(value = "点赞")
    private int stars;
}