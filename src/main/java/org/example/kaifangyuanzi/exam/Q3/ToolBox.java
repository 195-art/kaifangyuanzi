package org.example.kaifangyuanzi.exam.Q3;

import java.util.Scanner;

public class ToolBox {
    public static void main(String[] args) {
        ToolBox toolBox = new ToolBox();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("【欢迎使用多功能工具箱】");
            while (true) {
                System.out.println("1.成绩评定 2.空心金字塔 3.递归阶乘 4.退出系统");
                System.out.print("请选择功能：");
                Integer menu = readInt(scanner);
                if (menu == null || menu == 4) {
                    System.out.println("再见");
                    break;
                }
                if (menu == 1) {
                    System.out.print("请输入分数：");
                    Integer score = readInt(scanner);
                    if (score == null) break;
                    if (score < 0 || score > 100) {
                        System.out.println("分数必须在0到100之间");
                        continue;
                    }
                    String gradeIf;
                    if (score >= 90) gradeIf = "A";
                    else if (score >= 80) gradeIf = "B";
                    else if (score >= 70) gradeIf = "C";
                    else if (score >= 60) gradeIf = "D";
                    else gradeIf = "E";
                    String gradeSwitch;
                    switch (score / 10) {
                        case 10:
                        case 9: gradeSwitch = "A"; break;
                        case 8: gradeSwitch = "B"; break;
                        case 7: gradeSwitch = "C"; break;
                        case 6: gradeSwitch = "D"; break;
                        default: gradeSwitch = "E";
                    }
                    System.out.println("成绩评定（if-else）：" + gradeIf);
                    System.out.println("成绩评定（switch）：" + gradeSwitch);
                } else if (menu == 2) {
                    System.out.print("请输入金字塔层数：");
                    Integer n = readInt(scanner);
                    if (n == null) break;
                    if (n < 1 || n > 100) {
                        System.out.println("层数必须在1到100之间");
                        continue;
                    }
                    for (int i = 1; i <= n; i++) {
                        for (int k = 1; k <= n - i; k++) System.out.print(" ");
                        for (int j = 1; j <= 2 * i - 1; j++) {
                            System.out.print(i == 1 || i == n || j == 1 || j == 2 * i - 1 ? "*" : " ");
                        }
                        System.out.println();
                    }
                } else if (menu == 3) {
                    System.out.print("计算阶乘：");
                    Integer n = readInt(scanner);
                    if (n == null) break;
                    try {
                        System.out.println("计算结果：" + toolBox.calculateFactorial(n));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("输入无效，请重新选择");
                }
            }
        }
    }

    private static Integer readInt(Scanner scanner) {
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) return scanner.nextInt();
            scanner.next();
            System.out.print("请输入整数：");
        }
        return null;
    }

    public int calculateFactorial(int n) {
        if (n < 0 || n > 12) throw new IllegalArgumentException("整数阶乘仅支持0到12");
        return n <= 1 ? 1 : n * calculateFactorial(n - 1);
    }
}
