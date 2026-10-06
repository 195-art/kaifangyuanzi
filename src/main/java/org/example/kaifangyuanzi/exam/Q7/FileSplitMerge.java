package org.example.kaifangyuanzi.exam.Q7;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

public class FileSplitMerge {
    private static final int BUFFER_SIZE = 8192;

    private static List<Path> findParts(Path dir, String fileName) throws IOException {
        Pattern pattern = Pattern.compile(Pattern.quote(fileName) + "\\.part([1-9]\\d*)");
        try (var files = Files.list(dir)) {
            List<Path> parts = files.filter(Files::isRegularFile)
                    .filter(path -> pattern.matcher(path.getFileName().toString()).matches()).toList();
            return new ArrayList<>(parts);
        }
    }

    public static void splitFile(String sourceFilePath, String outputDirPath, long chunkSize) throws IOException {
        Path source = Path.of(sourceFilePath);
        if (!Files.isRegularFile(source)) throw new FileNotFoundException("源文件不存在：" + source);
        if (chunkSize <= 0) throw new IllegalArgumentException("分片大小必须大于0字节");
        Path outputDir = Path.of(outputDirPath);
        Files.createDirectories(outputDir);
        Path staging = Files.createTempDirectory(outputDir, ".split-");
        String fileName = source.getFileName().toString();
        int partNum = 0;
        long total = 0;
        try {
            try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(source))) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int next = input.read();
                do {
                    partNum++;
                    long written = 0;
                    try (BufferedOutputStream output = new BufferedOutputStream(
                            Files.newOutputStream(staging.resolve(fileName + ".part" + partNum)))) {
                        if (next != -1) {
                            output.write(next);
                            written++;
                        }
                        while (written < chunkSize && next != -1) {
                            int length = input.read(buffer, 0, (int) Math.min(buffer.length, chunkSize - written));
                            if (length == -1) break;
                            output.write(buffer, 0, length);
                            written += length;
                        }
                    }
                    total += written;
                    next = input.read();
                } while (next != -1);
            }
            for (Path old : findParts(outputDir, fileName)) Files.delete(old);
            try (var files = Files.list(staging)) {
                for (Path part : files.toList()) {
                    Files.move(part, outputDir.resolve(part.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            Files.writeString(outputDir.resolve(fileName + ".parts"), partNum + "\n" + total);
            System.out.println("分片完成，共生成 " + partNum + " 个分片文件");
        } finally {
            try (var files = Files.list(staging)) {
                for (Path file : files.toList()) Files.deleteIfExists(file);
            }
            Files.deleteIfExists(staging);
        }
    }

    public static void mergeFile(String targetDirPath, String originalFileName, String mergedFilePath) throws IOException {
        Path dir = Path.of(targetDirPath);
        if (!Files.isDirectory(dir)) throw new FileNotFoundException("分片目录不存在：" + dir);
        List<Path> parts = findParts(dir, originalFileName);
        if (parts.isEmpty()) throw new IOException("未找到分片文件");
        String prefix = originalFileName + ".part";
        try {
            parts.sort(Comparator.comparingInt(path -> Integer.parseInt(path.getFileName().toString().substring(prefix.length()))));
            for (int i = 0; i < parts.size(); i++) {
                int number = Integer.parseInt(parts.get(i).getFileName().toString().substring(prefix.length()));
                if (number != i + 1) throw new IOException("分片不完整，缺少第 " + (i + 1) + " 片");
            }
        } catch (NumberFormatException e) {
            throw new IOException("分片编号无效", e);
        }
        Path manifest = dir.resolve(originalFileName + ".parts");
        long expectedSize = -1;
        if (Files.exists(manifest)) {
            try {
                List<String> values = Files.readAllLines(manifest);
                if (values.size() != 2 || Integer.parseInt(values.get(0)) != parts.size()) {
                    throw new IOException("分片数量不完整");
                }
                expectedSize = Long.parseLong(values.get(1));
                if (expectedSize < 0) throw new IOException("分片记录无效");
            } catch (NumberFormatException e) {
                throw new IOException("分片记录无效", e);
            }
        }
        Path merged = Path.of(mergedFilePath).toAbsolutePath().normalize();
        for (Path part : parts) {
            if (part.toAbsolutePath().normalize().equals(merged)) throw new IOException("输出文件不能覆盖分片");
        }
        if (manifest.toAbsolutePath().normalize().equals(merged)) throw new IOException("输出文件不能覆盖分片记录");
        Files.createDirectories(merged.getParent());
        Path temp = Files.createTempFile(merged.getParent(), ".merge-", ".tmp");
        try {
            long total = 0;
            try (BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(temp))) {
                byte[] buffer = new byte[BUFFER_SIZE];
                for (Path part : parts) {
                    try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(part))) {
                        int length;
                        while ((length = input.read(buffer)) != -1) {
                            output.write(buffer, 0, length);
                            total += length;
                        }
                    }
                }
            }
            if (expectedSize != -1 && total != expectedSize) throw new IOException("分片大小不完整");
            try {
                Files.move(temp, merged, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, merged, StandardCopyOption.REPLACE_EXISTING);
            }
            for (Path part : parts) Files.delete(part);
            Files.deleteIfExists(manifest);
            System.out.println("合并完成，已删除分片文件");
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    public static String getFileMD5(String filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream input = Files.newInputStream(Path.of(filePath))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) != -1) md.update(buffer, 0, length);
        }
        return HexFormat.of().formatHex(md.digest());
    }

    public static void main(String[] args) throws Exception {
        String source = "test_big_file.txt";
        try (OutputStream output = new BufferedOutputStream(Files.newOutputStream(Path.of(source)))) {
            byte[] data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\n".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            long remaining = 5L * 1024 * 1024;
            while (remaining > 0) {
                int length = (int) Math.min(remaining, data.length);
                output.write(data, 0, length);
                remaining -= length;
            }
        }
        splitFile(source, "split_parts", 1024 * 1024);
        mergeFile("split_parts", source, "merged_file.txt");
        System.out.println("文件是否完全一致：" + getFileMD5(source).equals(getFileMD5("merged_file.txt")));
    }
}
