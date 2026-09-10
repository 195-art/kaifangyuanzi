package org.example.kaifangyuanzi.exam.Q3;

import java.util.Scanner;

public class ToolBox {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("【欢迎使用多功能工具箱】");

        loop: while(true){
            System.out.println("请选择功能:");
            int menu = scanner.nextInt();
            switch (menu){
                case 1:
                    System.out.print("请输入分数：");
                    int score = scanner.nextInt();

                    String gradeIf;
                    if (score >= 90) {
                        gradeIf = "A";
                    } else if (score >= 80) {
                        gradeIf = "B";
                    } else if (score >= 70) {
                        gradeIf = "C";
                    } else if (score >= 60) {
                        gradeIf = "D";
                    } else {
                        gradeIf = "E";
                    }

                    String gradeSwitch;
                    int level = score / 10;
                    switch (level) {
                        case 10:
                        case 9:
                            gradeSwitch = "A";
                            break;
                        case 8:
                            gradeSwitch = "B";
                            break;
                        case 7:
                            gradeSwitch = "C";
                            break;
                        case 6:
                            gradeSwitch = "D";
                            break;
                        default:
                            gradeSwitch = "E";
                            break;
                    }
                    System.out.println("成绩评定 (if‑else)：" + gradeIf);
                    System.out.println("成绩评定 (switch)：" + gradeSwitch);
                    continue;

                case 2:
                    System.out.print("请输入金字塔层数：");
                    int n = scanner.nextInt();
                    for (int i = 1; i <= n; i++) {
                        for (int k = 1; k <= n - i; k++) {
                            System.out.print(" ");
                        }
                        for (int j = 1; j <= 2 * i - 1; j++) {
                            if (i == 1 || i == n || j == 1 || j == 2*i -1) {
                                System.out.print("*");
                            } else {
                                System.out.print(" ");
                            }
                        }
                        System.out.println();
                    }
                    continue;

                case 3:
                    System.out.print("计算阶乘：");
                    int num = scanner.nextInt();
                    int res = cal(num);
                    System.out.println("计算结果：" + res);
                    continue;

                case 4:
                    System.out.println("再见");
                    break loop;

                default:
                    System.out.println("输入无效，请重新选择");
                    continue;
            }
        }
        scanner.close();
    }

    public static int cal(int num){
        if(num == 1 || num == 0 ){
            return 1;
        }
        return num * cal(num-1);
    }
}
