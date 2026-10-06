package org.example.kaifangyuanzi;

import org.example.kaifangyuanzi.exam.Q3.ToolBox;
import org.example.kaifangyuanzi.exam.Q6.Order;
import org.example.kaifangyuanzi.exam.Q7.FileSplitMerge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BasicExercisesTests {
    @TempDir Path dir;

    @Test
    void sortsByAmountThenOrderId() {
        List<Order> orders = new ArrayList<>(List.of(
                new Order("O004", "Charlie", 200, List.of()),
                new Order("O001", "Alice", 150, List.of()),
                new Order("O005", "Bob", 300, List.of()),
                new Order("O003", "Alice", 200, List.of())));
        Collections.sort(orders);
        assertEquals(List.of("O005", "O003", "O004", "O001"), orders.stream().map(Order::getOrderId).toList());
    }

    @Test
    void factorialChecksIntegerRange() {
        ToolBox box = new ToolBox();
        assertEquals(1, box.calculateFactorial(0));
        assertEquals(24, box.calculateFactorial(4));
        assertEquals(479001600, box.calculateFactorial(12));
        assertThrows(IllegalArgumentException.class, () -> box.calculateFactorial(-1));
        assertThrows(IllegalArgumentException.class, () -> box.calculateFactorial(13));
    }

    @Test
    void smallChunksRoundTripWithoutExceedingLimit() throws Exception {
        byte[] data = new byte[16385];
        new Random(42).nextBytes(data);
        Path source = dir.resolve("data.bin");
        Files.write(source, data);
        Path parts = dir.resolve("parts");
        FileSplitMerge.splitFile(source.toString(), parts.toString(), 1024);
        try (var files = Files.list(parts)) {
            for (Path part : files.filter(p -> p.getFileName().toString().matches(".*\\.part\\d+")).toList()) {
                assertTrue(Files.size(part) <= 1024);
            }
        }
        Path merged = dir.resolve("merged.bin");
        FileSplitMerge.mergeFile(parts.toString(), "data.bin", merged.toString());
        assertArrayEquals(data, Files.readAllBytes(merged));
        assertEquals(0, Objects.requireNonNull(parts.toFile().list()).length);
    }

    @Test
    void repeatedSplitReplacesOldParts() throws Exception {
        Path source = dir.resolve("data.bin");
        Path parts = dir.resolve("parts");
        Files.write(source, new byte[24000]);
        FileSplitMerge.splitFile(source.toString(), parts.toString(), 10000);
        byte[] data = {1, 2, 3};
        Files.write(source, data);
        FileSplitMerge.splitFile(source.toString(), parts.toString(), 10000);
        assertFalse(Files.exists(parts.resolve("data.bin.part2")));
        Path merged = dir.resolve("merged.bin");
        FileSplitMerge.mergeFile(parts.toString(), "data.bin", merged.toString());
        assertArrayEquals(data, Files.readAllBytes(merged));
    }

    @Test
    void missingPartPreservesInputAndExistingOutput() throws Exception {
        Path source = dir.resolve("data.bin");
        Path parts = dir.resolve("parts");
        Files.write(source, new byte[4000]);
        FileSplitMerge.splitFile(source.toString(), parts.toString(), 1024);
        Files.delete(parts.resolve("data.bin.part4"));
        Path output = dir.resolve("merged.bin");
        Files.write(output, new byte[]{9});
        assertThrows(IOException.class, () -> FileSplitMerge.mergeFile(parts.toString(), "data.bin", output.toString()));
        assertTrue(Files.exists(parts.resolve("data.bin.part1")));
        assertArrayEquals(new byte[]{9}, Files.readAllBytes(output));
    }

    @Test
    void ignoresBackupFilesAndSortsNumericSuffixes() throws Exception {
        Path parts = Files.createDirectory(dir.resolve("parts"));
        for (int i = 12; i > 0; i--) Files.write(parts.resolve("a.part" + i), new byte[]{(byte) i});
        Files.writeString(parts.resolve("a.part1.bak"), "backup");
        Path output = dir.resolve("merged.bin");
        FileSplitMerge.mergeFile(parts.toString(), "a", output.toString());
        assertArrayEquals(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12}, Files.readAllBytes(output));
        assertTrue(Files.exists(parts.resolve("a.part1.bak")));
    }

    @Test
    void emptyFileAndSingleByteChunksRoundTrip() throws Exception {
        for (byte[] data : List.of(new byte[0], new byte[]{1,2,3})) {
            Path source = dir.resolve("a");
            Files.write(source, data);
            FileSplitMerge.splitFile(source.toString(), dir.resolve("parts").toString(), 1);
            Path output = dir.resolve("out");
            FileSplitMerge.mergeFile(dir.resolve("parts").toString(), "a", output.toString());
            assertArrayEquals(data, Files.readAllBytes(output));
        }
    }
}
