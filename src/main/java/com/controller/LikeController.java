package com.controller;

import com.constants.RedisConstants;
import com.domain.Video;
import com.result.CommonResult;
import com.service.VideoService;
import com.utlis.RedisUtil;
import com.utlis.SpringSecurityUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @DATE: 2023/11/29  17:04
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@RestController
@RequestMapping("/like")
public class LikeController {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private VideoService videoService;
    @PutMapping("/{videoId}")
    public CommonResult like(@PathVariable Long videoId) {
        List<String> keys = new ArrayList<>();
        keys.add(RedisConstants.VIDEO_LIKE + videoId);
        keys.add(RedisConstants.VIDEO_LIKE_NUM + videoId);
        Object likeNum = redisUtil.get(RedisConstants.VIDEO_LIKE_NUM + videoId);
        if (ObjectUtils.isEmpty(likeNum)){
            Video video = videoService.getById(videoId);
            redisUtil.set(RedisConstants.VIDEO_LIKE_NUM + videoId,video.getLikesNum(),60*60);
        }
        Long userId = SpringSecurityUtil.getUserId();
        redisUtil.like(keys,userId.toString());
        HashMap<String, Object> map = new HashMap<>();
        Object currentLikeNum = redisUtil.get(RedisConstants.VIDEO_LIKE_NUM + videoId);
        boolean isLike = redisUtil.isMember(RedisConstants.VIDEO_LIKE + videoId, userId.toString());
        map.put("likeNum",currentLikeNum);
        map.put("isLike",isLike);
        return CommonResult.operateSuccess(map);
    }


}
