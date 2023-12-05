package com.utlis;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;


/**
 * @Description:
 * @DATE: 2023/11/1  19:53
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@Component
public class JpgUtil {
    @Resource
    AliOSSManager aliOSSManager;

    public String VideoToPicture(String path) throws Exception {
        InputStream inputStream = aliOSSManager.getInputStream(path);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(inputStream);
        ff.start();
        ff.setFrameNumber(3);
        Frame frame = ff.grabImage();
        File directory = new File("C:\\Users\\22577\\Desktop\\实验1\\images");
        File tempFile = File.createTempFile("temp", ".jpg",directory);
        ImageIO.write(frameToBufferedImage(frame), "jpg", tempFile);

        System.out.println(tempFile.getCanonicalPath());
        ff.close();
        ff.stop();
        FileInputStream fileInputStream = new FileInputStream(tempFile.getCanonicalPath());
        String url = aliOSSManager.putFile(AliOSSManager.getKey(null, null, ".jpg"), fileInputStream);
        tempFile.delete();
        return url;

    }

    /**
     * 帧转为流
     */
    private static RenderedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }


}
