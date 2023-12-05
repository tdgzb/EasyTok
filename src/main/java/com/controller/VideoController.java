package com.controller;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.auth.dto.JwtUserDto;
import com.constants.RedisConstants;
import com.domain.Comment;
import com.domain.Video;
import com.dto.CommentDto;
import com.dto.UploadChunkFileParam;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mapper.VideoMapper;
import com.model.vo.VideoVo;
import com.oss.AliOSSProperties;
import com.result.CommonResult;
import com.service.CommentService;
import com.service.VideoService;
import com.utlis.ConverterUtil;
import com.utlis.RedisUtil;
import com.utlis.SpringSecurityUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @DATE: 2023/10/25  21:06
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@RestController
@RequestMapping("/video")
public class VideoController {
    @Resource
    AliOSSProperties aliOSSProperties;
    @Resource
    VideoService videoServicel;
    @Resource
    VideoMapper videoMapper;
    @Resource
    private CommentService commentService;
    @Resource
    private RedisUtil redisUtil;
    @GetMapping("/random")
    public CommonResult<List<VideoVo>> getVideoRandom() {
        //创建oss客户端
        OSS ossClient = new OSSClient(aliOSSProperties.getEND_POINT(),
                new DefaultCredentialProvider(aliOSSProperties.getACCESS_KEY_ID(), aliOSSProperties.getACCESS_KEY_SECRET()), new ClientConfiguration());
        Date expiration = new Date(System.currentTimeMillis() + 100 * 100 * 24 * 3600L);
        //查询视频
        List<Video> ids = videoMapper.getIdsbyLimit(0, 3);
        List<VideoVo> videoVos = ConverterUtil.convertList(ids, VideoVo.class);
        JwtUserDto currentUser = (JwtUserDto) SpringSecurityUtil.getCurrentUser();
        if (currentUser != null) {
            Long id = currentUser.getUser().getId();
            for (VideoVo videoVo : videoVos) {
                String key = RedisConstants.VIDEO_LIKE + videoVo.getId();
                boolean member = redisUtil.isMember(key, id.toString());
                if (member) {
                    videoVo.setIsLike(true);
                }
            }
        }
        //加密生成视频URL
        for (VideoVo id : videoVos) {
            URL url = ossClient.generatePresignedUrl(aliOSSProperties.getBUCKET_NAME(), id.getFileName(), expiration);
            id.setUrl(url.toString());
        }
        return CommonResult.operateSuccess(videoVos);
    }

    /**
     * 个人主页获取自己喜欢的视频
     * @return
     */
    @GetMapping("/ownLikeVideo")
    public CommonResult<List<VideoVo>> getOwnLikeVideo() {
        Long userId = SpringSecurityUtil.getUserId();
        List<Long> ids = videoMapper.selectOwnLikeVideo(userId);
        List<Video> videos = videoMapper.selectBatchIds(ids);
        List<VideoVo> videoVos = ConverterUtil.convertList(videos, VideoVo.class);
        OSS ossClient = new OSSClient(aliOSSProperties.getEND_POINT(),
                new DefaultCredentialProvider(aliOSSProperties.getACCESS_KEY_ID(), aliOSSProperties.getACCESS_KEY_SECRET()), new ClientConfiguration());
        Date expiration = new Date(System.currentTimeMillis() + 100 * 100 * 24 * 3600L);
        for (VideoVo id : videoVos) {
            URL url = ossClient.generatePresignedUrl(aliOSSProperties.getBUCKET_NAME(), id.getFileName(), expiration);
            id.setUrl(url.toString());
        }
        return CommonResult.operateSuccess(videoVos);
    }

    /**
     * 个人主义获取自己发布的视频
     */
    @GetMapping("/ownPublish")
     public CommonResult<List<VideoVo>> getOwnPublishVideo() {
        Long userId = SpringSecurityUtil.getUserId();
        List<Long> ids =videoMapper.selectwnPublishVideo(userId);
        List<Video> videos = videoMapper.selectBatchIds(ids);
        List<VideoVo> videoVos = ConverterUtil.convertList(videos, VideoVo.class);
        OSS ossClient = new OSSClient(aliOSSProperties.getEND_POINT(),
                new DefaultCredentialProvider(aliOSSProperties.getACCESS_KEY_ID(), aliOSSProperties.getACCESS_KEY_SECRET()), new ClientConfiguration());
        Date expiration = new Date(System.currentTimeMillis() + 100 * 100 * 24 * 3600L);
        for (VideoVo id : videoVos) {
            URL url = ossClient.generatePresignedUrl(aliOSSProperties.getBUCKET_NAME(), id.getFileName(), expiration);
            id.setUrl(url.toString());
        }
        return CommonResult.operateSuccess(videoVos);
    }

    /**
     * 获取视频信息
     * @param videoId
     * @return
     */
    @GetMapping("/videoInfo/{videoId}")
    public CommonResult<VideoVo> getVideoInfo(@PathVariable("videoId") Long videoId) {
        Video video = videoServicel.getById(videoId);
        VideoVo videoVo=new VideoVo();
        BeanUtils.copyProperties(video,videoVo);
        Integer likeNum =(Integer) redisUtil.get(RedisConstants.VIDEO_LIKE_NUM + videoId);
        boolean isLike = redisUtil.isMember(RedisConstants.VIDEO_LIKE + videoId, SpringSecurityUtil.getUserId().toString());
        videoVo.setIsLike(isLike);
        videoVo.setLikesNum(likeNum);
        return CommonResult.operateSuccess(videoVo);
    }


}

