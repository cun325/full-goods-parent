package org.example.api.util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图像转换工具类
 */
public class ImageConverter {

    /**
     * 将SVG文件转换为JPG文件
     *
     * @param svgFilePath SVG文件路径
     * @param jpgFilePath JPG文件路径
     * @param quality     JPG质量 (0.0-1.0)
     * @throws IOException        文件读写异常
     * @throws TranscoderException 转换异常
     */
    public static void convertSvgToJpg(String svgFilePath, String jpgFilePath, float quality)
            throws IOException, TranscoderException {
        // 创建JPEG转换器
        JPEGTranscoder transcoder = new JPEGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, quality);

        // 读取SVG文件
        Path svgPath = Paths.get(svgFilePath);
        if (!Files.exists(svgPath)) {
            throw new IOException("SVG文件不存在: " + svgFilePath);
        }
        
        byte[] svgBytes = Files.readAllBytes(svgPath);

        // 创建输入和输出对象
        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgBytes));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outputStream);

        // 执行转换
        transcoder.transcode(input, output);

        // 将结果写入JPG文件
        Path jpgPath = Paths.get(jpgFilePath);
        Files.createDirectories(jpgPath.getParent()); // 确保目录存在
        Files.write(jpgPath, outputStream.toByteArray());

        // 关闭流
        outputStream.close();
    }

    /**
     * 将SVG文件转换为JPG文件（默认质量0.9）
     *
     * @param svgFilePath SVG文件路径
     * @param jpgFilePath JPG文件路径
     * @throws IOException        文件读写异常
     * @throws TranscoderException 转换异常
     */
    public static void convertSvgToJpg(String svgFilePath, String jpgFilePath)
            throws IOException, TranscoderException {
        convertSvgToJpg(svgFilePath, jpgFilePath, 0.9f);
    }

    /**
     * 主方法，用于测试转换功能
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 项目资源路径
        String basePath = "/Users/peng/Downloads/full-goods-parent/full-goods-api/src/main/resources/static/images";

        // 需要转换的SVG文件列表
        String[][] svgFiles = {
                {"icons/cart.svg", "icons/cart.jpg"},
                {"icons/fruit-detail.svg", "icons/fruit-detail.jpg"},
                {"icons/order-status.svg", "icons/order-status.jpg"},
                {"icons/payment-success.svg", "icons/payment-success.jpg"},
                {"categories/imported.svg", "categories/imported.jpg"},
                {"categories/seasonal.svg", "categories/seasonal.jpg"},
                {"categories/tropical.svg", "categories/tropical.jpg"},
                {"banners/banner1.svg", "banners/banner1.jpg"},
                {"banners/banner2.svg", "banners/banner2.jpg"},
                {"banners/banner3.svg", "banners/banner3.jpg"}
        };

        System.out.println("开始转换SVG文件为JPG格式...");

        for (String[] filePair : svgFiles) {
            String svgFile = basePath + "/" + filePair[0];
            String jpgFile = basePath + "/" + filePair[1];

            try {
                convertSvgToJpg(svgFile, jpgFile);
                System.out.println("成功转换: " + filePair[0] + " -> " + filePair[1]);
            } catch (Exception e) {
                System.err.println("转换失败: " + filePair[0] + " - " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("转换完成!");
    }
}