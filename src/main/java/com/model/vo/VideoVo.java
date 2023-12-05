package com.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @DATE: 2023/10/31  14:02
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *作者
     */
    private String author;

    /**
     * 作者id
     */
    private Long userId;

    /**
     *
     */
    private String fileName;
    /**
     *评论数量
     */
    private Integer commentNum;

    /**
     *点赞数量
     */
    private Integer likesNum;

    /**
     *分享数量
     */
    private Integer shareNum;

    /**
     * 描述
     */
    private String description;

    /**
     * 视频路径
     */
    private String url;

    private String  imgUrl;

    /**
     * 用户是否点赞
     */
    private Boolean isLike;
}
