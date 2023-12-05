package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.domain.Video;
import com.model.vo.VideoVo;
import com.result.CommonResult;
import com.service.VideoService;
import com.utlis.ConverterUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @DATE: 2023/12/4  21:19
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@RestController
@RequestMapping("/search")

public class SearchController {
    @Resource
    private VideoService videoService;
    @GetMapping("/video")
    public CommonResult<List<VideoVo>> getVideoByDescription(@RequestParam("description") String description) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Video::getDescription,description);
        List<Video> videos = videoService.list(wrapper);
        List<VideoVo> videoVos = ConverterUtil.convertList(videos, VideoVo.class);
        return CommonResult.operateSuccess(videoVos);
    }

}
