package com.utlis;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import com.constants.RedisConstants;
import com.dto.UploadChunkFileParam;
import com.exception.BusinessException;
import com.oss.AliOSSProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description:
 * @DATE: 2023/10/1  17:03
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Slf4j
@Component
public class AliOSSManager {
    @Resource
    private AliOSSProperties aliOSSProperties;
    private volatile Map imap = new HashMap<>();
    private volatile OSSClient ossClient = null;
    @Resource
    private RedisUtil redisUtil;


    /**
     * 分片上传
     *
     * @param param 上传参数
     * @return
     */
    public void uploadChunk(UploadChunkFileParam param) throws IOException {
//        if (ObjectUtil.isEmpty(param.getKey())) {
//            String key = getKey(null, param.getIdentifier(), param.getFilename());
//            param.setKey(key);
//        }
      uploadChunk(param.getUploadId(), param.getKey(), param.getFile(), param.getChunkNumber(), param.getCurrentChunkSize(), param.getTotalChunks());
    }

    /**
     * 分片上传
     * 1、检查文件是否上传
     * 2、检查文件是否第一次上传，第一次上传创建上传id uploadId
     * 3、检查是否是断点续传，如果是返回已上传的分片
     * 4、分片上传到阿里云OSS上，并记录上传信息到Redis
     * 5、判断是否已上传完成，已完成：合并所有分片为源文件
     *
     * @param uploadId   上传id
     * @param key        文件在OSS上的key
     * @param file       文件分片
     * @param chunkIndex 分片索引
     * @param chunkSize  分片大小
     * @return
     */
    public void uploadChunk(String uploadId, String key, MultipartFile file, Integer chunkIndex,
                            long chunkSize, Integer totalChunk) throws IOException {

        ossClient = initOSS();
//        if (this.uploadId == null) {
//            this.uploadId = uploadChunkInit(file,key);
//        }
//        key = getKey(null, null, FileUtil.getExtensionName(file.getOriginalFilename()));
//        System.out.println("-----------------");
//        Object o = redisUtil.get("uploadId" + key);
//        if (o==null){
//            this.uploadId = uploadChunkInit(file,key);
//            redisUtil.set("uploadId" + key,this.uploadId);
//        }else {
//            this.uploadId=o.toString();
//        }
        System.out.println("--------------");
        System.out.println(uploadId);
        // 上传分片
        PartETag partETag = uploadChunkPart(uploadId, key, file.getInputStream(), chunkIndex, chunkSize, totalChunk);
        System.out.println("端点+"+partETag.getPartNumber());
        ArrayList<PartETag> partETagList =(ArrayList<PartETag>) redisUtil.get("partETagList" + uploadId);
        if (partETagList==null){
             partETagList = new ArrayList<PartETag>();
            redisUtil.set("partETagList"+uploadId,partETagList);
        }
//       ConverterUtil.convertList(o)
         partETagList.add(partETag);
        redisUtil.incr(RedisConstants.File_UPLOAP_COUNT + uploadId);
        System.out.println("----------------");
        System.out.println(redisUtil.get(RedisConstants.File_UPLOAP_COUNT + uploadId));
        if (redisUtil.get(RedisConstants.File_UPLOAP_COUNT + uploadId) == totalChunk) {
            uploadChunkComplete(uploadId, key, partETagList);
        }

    }

    /**
     * 初始化分片上传
     *
     * @param key
     * @return 分片上传的uploadId
     */
    public String uploadChunkInit(MultipartFile file, String key) {
        if (StringUtils.isEmpty(key)) {
            key = getKey(null, null, FileUtil.getExtensionName(file.getOriginalFilename()));
        }
        return uploadChunkInit(key);
    }

    /**
     * 初始化上传id uploadId
     *
     * @param key
     * @return
     */
    public String uploadChunkInit(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new BusinessException("key不能为空");
        }
        ossClient = initOSS();
        InitiateMultipartUploadRequest uploadRequest;
        InitiateMultipartUploadResult result;
        uploadRequest = new InitiateMultipartUploadRequest(aliOSSProperties.getBUCKET_NAME(), key);
        result = ossClient.initiateMultipartUpload(uploadRequest);
//        if (uploadRequest == null) {
//            synchronized (InitiateMultipartUploadRequest.class) {
//                if (uploadRequest == null) {
//                    uploadRequest = new InitiateMultipartUploadRequest(aliOSSProperties.getBUCKET_NAME(), key);
//                    result = ossClient.initiateMultipartUpload(uploadRequest);
//                }
//                return result.getUploadId();
//            }
//        }
//        System.out.println("初始化=" + result.getUploadId());
        return result.getUploadId();

    }

    /**
     * 上传分片文件
     *
     * @param uploadId   上传id
     * @param key        key
     * @param instream   文件分片流
     * @param chunkIndex 分片索引
     * @param chunkSize  分片大小
     * @return
     */
    public PartETag uploadChunkPart(String uploadId, String key, InputStream instream,
                                    Integer chunkIndex, long chunkSize, Integer chunkCount) {
        ossClient = initOSS();
        UploadPartRequest partRequest = new UploadPartRequest();
        // 阿里云 oss 文件根目录
        partRequest.setBucketName(aliOSSProperties.getBUCKET_NAME());
        // 文件key
        partRequest.setKey(key);
        // 分片上传uploadId
        partRequest.setUploadId(uploadId);
        System.out.println("uploadId===" + uploadId);
        // 分片文件
        partRequest.setInputStream(instream);
        // 分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
        partRequest.setPartSize(chunkSize);
        System.out.println(chunkSize + "    " + chunkIndex + "   " + uploadId);
        // 分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
        System.out.println(chunkIndex);
        partRequest.setPartNumber(chunkIndex);
        // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
        UploadPartResult uploadPartResult = ossClient.uploadPart(partRequest);
        // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在redis中。
        return uploadPartResult.getPartETag();
    }

    /**
     * 文件合并
     *
     * @param uploadId  上传id
     * @param key       key
     * @param chunkTags 分片上传信息
     * @return
     */
    public CompleteMultipartUploadResult uploadChunkComplete(String uploadId, String key, List<PartETag> chunkTags) {
        ossClient = initOSS();
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(aliOSSProperties.getBUCKET_NAME(), key, uploadId, chunkTags);
        CompleteMultipartUploadResult result = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        return result;
    }


    /**
     * 根据key生成文件的访问地址
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 拼接文件访问路径。由于拼接的字符串大多为String对象，而不是""的形式，所以直接用+拼接的方式没有优势
        StringBuffer url = new StringBuffer();
        url.append("http://")
                .append(aliOSSProperties.getBUCKET_NAME())
                .append(".")
                .append(aliOSSProperties.getEND_POINT())
                .append("/").append(key);
        return url.toString();
    }

    /**
     * 根据key生成文件的访问地址（带过期时间）
     *
     * @param key
     * @return
     */
//    public static String getUrlExpire(String key) {
//        ossClient = initOSS();
//        // 生成过期时间
//        long expireEndTime = System.currentTimeMillis() + AliOSSProperties.URL_EXPIRE * 1000;
//        Date expiration = new Date(expireEndTime);// 生成URL
//        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(AliOSSProperties.BUCKET_NAME, key);
//        generatePresignedUrlRequest.setExpiration(expiration);
//        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
//        return url.toString();
//    }

    /**
     * 通过文件名获取文件流
     *
     * @param key 要下载的文件名（OSS服务器上的）
     */
    public InputStream getInputStream(String key) {
        ossClient = initOSS();

        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        return ossClient.getObject(new GetObjectRequest(aliOSSProperties.getBUCKET_NAME(), key)).getObjectContent();
    }

    /**
     * 根据key下载文件
     *
     * @param key
     */
    public void download(String key) {
        ossClient = initOSS();
        GetObjectRequest request = new GetObjectRequest(aliOSSProperties.getBUCKET_NAME(), key);
        ossClient.getObject(request);
    }

    /**
     * 根据key下载文件
     *
     * @param key
     */
    public void download(String key, String fileName) {
        ossClient = initOSS();
        GetObjectRequest request = new GetObjectRequest(aliOSSProperties.getBUCKET_NAME(), key);
        ossClient.getObject(request, new File(fileName));
    }

//    /**
//     * 删除
//     *
//     * @param key
//     */
//    public static void delete(String key) {
//        if (StringUtils.isNotEmpty(key)) {
//            ossClient = initOSS();
//            GenericRequest request = new DeleteObjectsRequest(AliOSSProperties.BUCKET_NAME).withKey(key);
//            ossClient.deleteBucket(request);
//        }
//    }

//    /**
//     * 删除
//     *
//     * @param keys
//     */
//    public static void delete(List<String> keys) {
//        if (ObjectUtil.isNotEmpty(keys)) {
//            ossClient = initOSS();
//            GenericRequest request = new DeleteObjectsRequest(AliOSSProperties.BUCKET_NAME).withKeys(keys);
//            ossClient.deleteBucket(request);
//        }
//    }


    public String uploadFile(String key,MultipartFile file) throws IOException {
        if (StringUtils.isEmpty(key)) {
            key = getKey(null, null, FileUtil.getExtensionName(file.getOriginalFilename()));
        }
       return putFile(key,file.getInputStream());
    }
    /**
     * 上传文件<>基础方法</>
     *
     * @param key         文件key
     * @param inputStream 输入流
     * @return
     */
    public   String  putFile(String key, InputStream inputStream) {
        ossClient = initOSS();

        // 上传文件最大值 MB->bytes
        long maxSize = 20 * 1024 * 1024;
//        long size = FileUtil.getInputStreamSize(inputStream);
//        if (size <= 0 || size > maxSize) {
//            throw new CustomException("请检查文件大小");
//        }
        PutObjectResult result = null;
        try {
            PutObjectRequest request = new PutObjectRequest(aliOSSProperties.getBUCKET_NAME(), key, inputStream);
            result = ossClient.putObject(request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return getUrl(key);
    }


    public Boolean checkExist(String key) {
        ossClient = initOSS();
        return ossClient.doesObjectExist(aliOSSProperties.getBUCKET_NAME(), key);
    }

    //    /**
//     * 获取上传文件的key
//     * 上传和删除时除了需要bucketName外还需要此值
//     *
//     * @param prefix   前缀（非必传），可以用于区分是哪个模块或子项目上传的文件,默认 file 文件夹
//     * @param fileName 文件名称（非必传），如果为空默认生成文件名，格式：yyyyMMdd-UUID
//     * @param suffix   后缀 , 可以是 png jpg
//     * @return
//     */
    public static String getKey(final String prefix, final String fileName, final String suffix) {
        StringBuffer keySb = new StringBuffer();
        // 前缀处理
        if (StringUtils.isNotEmpty(prefix)) {
            keySb.append(prefix);
        }
        // 文件名处理
        if (StringUtils.isBlank(fileName)) {
            LocalDateTime now = LocalDateTime.now();
            keySb.append(now.format(DateTimeFormatter.ofPattern("YYYYMMdd")));
            keySb.append("-");
            keySb.append(UUID.randomUUID());
        } else {
            keySb.append(fileName);
        }
        // 后缀处理
        if (StringUtils.isBlank(suffix)) {
            throw new NullPointerException("文件后缀不能为空");
        }
        if (suffix.contains(".")) {
            keySb.append(suffix.substring(suffix.lastIndexOf(".")));
        } else {
            keySb.append("." + suffix);
        }
        return keySb.toString();
    }

    private OSSClient initOSS() {
        if (ossClient == null) {
            synchronized (OSSClient.class) {
                if (ossClient == null) {
                    ossClient = new OSSClient(aliOSSProperties.getEND_POINT(),
                            new DefaultCredentialProvider(aliOSSProperties.getACCESS_KEY_ID(), aliOSSProperties.getACCESS_KEY_SECRET()), new ClientConfiguration());
                }
            }
        }
        return ossClient;
    }

//    private static RedisManager initRedisManager() {
//        if (redisManager == null) {
//            synchronized (RedisManager.class) {
//                if (redisManager == null) {
//                    return SpringUtils.getBean(RedisManager.class);
//                }
//            }
//        }
//        return redisManager;
//    }
}