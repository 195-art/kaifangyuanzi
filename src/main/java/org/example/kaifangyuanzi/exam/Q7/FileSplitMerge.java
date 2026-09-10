package org.example.kaifangyuanzi.exam.Q7;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileSplitMerge {

    // 缓冲区大小：8KB
    private static final int BUFFER_SIZE = 8 * 1024;

    /**
     * Task1：文件分片
     */
    public static void splitFile(String sourceFilePath, String outputDirPath, long chunkSize) throws IOException {
        File sourceFile = new File(sourceFilePath);

        // 参数校验
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new FileNotFoundException("源文件不存在或不是有效文件：" + sourceFilePath);
        }
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("分片大小必须大于0字节");
        }

        // 创建输出目录
        File outputDir = new File(outputDirPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String fileName = sourceFile.getName();

        // 输入流放到try-with-resources自动关闭
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readLen;
            int partNum = 1;
            long currentChunkWritten = 0;

            // 创建第一个分片的输出流
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(new File(outputDir, fileName + ".part" + partNum)));

            while ((readLen = bis.read(buffer)) != -1) {
                if (currentChunkWritten + readLen > chunkSize) {
                    // 填满当前分片
                    int remain = (int) (chunkSize - currentChunkWritten);
                    bos.write(buffer, 0, remain);
                    bos.flush();
                    bos.close();

                    // 切换到下一个分片
                    partNum++;
                    bos = new BufferedOutputStream(
                            new FileOutputStream(new File(outputDir, fileName + ".part" + partNum)));
                    // 写入剩余字节到新分片
                    bos.write(buffer, remain, readLen - remain);
                    currentChunkWritten = readLen - remain;
                } else {
                    // 直接写入当前分片
                    bos.write(buffer, 0, readLen);
                    currentChunkWritten += readLen;
                }
            }

            // 关闭最后一个分片的输出流
            bos.flush();
            bos.close();
            System.out.println("✅ 分片完成，共生成 " + partNum + " 个分片文件");
        }
    }

    /**
     * Task2：文件合并
     */
    public static void mergeFile(String targetDirPath, String originalFileName, String mergedFilePath) throws IOException {
        File dir = new File(targetDirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException("分片目录不存在或不是有效目录: " + targetDirPath);
        }

        File[] allFiles = dir.listFiles();
        if (allFiles == null || allFiles.length == 0) {
            throw new IOException("分片目录为空，没有文件");
        }

        List<File> partFiles = new ArrayList<>();
        String prefix = originalFileName + ".part";

        // ========== 这里修正了筛选条件 ==========
        // 去掉了错误的 endsWith(".part")，加上 isFile() 判断
        for (File file : allFiles) {
            if (file.isFile() && file.getName().startsWith(prefix)) {
                partFiles.add(file);
            }
        }

        if (partFiles.isEmpty()) {
            throw new IOException("在目录中未找到对应的分片文件");
        }

        // 按分片序号数字排序
        partFiles.sort(Comparator.comparingInt(file -> {
            String name = file.getName();
            String numStr = name.substring(prefix.length());
            return Integer.parseInt(numStr);
        }));

        // 合并写入最终文件
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mergedFilePath))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readLen;

            for (File file : partFiles) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                    while ((readLen = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, readLen);
                    }
                }
            }
            bos.flush();
        }

        // 合并完成后删除所有分片
        for (File file : partFiles) {
            file.delete();
        }

        System.out.println("✅ 合并完成，已删除所有分片文件");
    }

    /**
     * 计算文件MD5，用于一致性校验
     */
    public static String getFileMD5(String filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readLen;
            while ((readLen = fis.read(buffer)) != -1) {
                md.update(buffer, 0, readLen);
            }
        }

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Task3：测试主方法
     */
    public static void main(String[] args) {
        try {
            String sourceFile = "test_big_file.txt";
            String splitDir = "split_parts";
            String mergedFile = "merged_file.txt";
            long chunkSize = 1024 * 1024; // 每个分片1MB

            // 生成5MB测试文件
            generateTestFile(sourceFile, 5L * 1024 * 1024);

            // 执行分片
            splitFile(sourceFile, splitDir, chunkSize);

            // 执行合并
            mergeFile(splitDir, new File(sourceFile).getName(), mergedFile);

            // 一致性校验
            String sourceMD5 = getFileMD5(sourceFile);
            String mergedMD5 = getFileMD5(mergedFile);

            System.out.println("\n========== 一致性校验结果 ==========");
            System.out.println("源文件MD5：   " + sourceMD5);
            System.out.println("合并后MD5： " + mergedMD5);
            System.out.println("文件是否完全一致：" + sourceMD5.equals(mergedMD5));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成测试文件
     */
    private static void generateTestFile(String filePath, long size) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-测试内容-\n".getBytes();
            long written = 0;

            while (written < size) {
                bos.write(data);
                written += data.length;
            }
            bos.flush();
        }
        System.out.println("📄 测试文件生成完成，大小：" + size + " 字节");
    }
}
