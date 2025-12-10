package com.huah.leetcode;

import java.util.*;

// 表示二维点的简单类
class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

    // 计算到另一个点的欧氏距离平方
    public double distanceSquaredTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx * dx + dy * dy;
    }
}

public class NearestPointsFinder {

    /**
     * 查找距离目标点最近的最多10个点（已过滤异常点）
     *
     * @param target   目标点
     * @param points   候选点列表
     * @param maxDist  最大允许距离（超过则视为异常点）
     * @return 最近的最多10个点，按距离升序排列
     */
    public static List<Point> findTop10Closest(
            Point target,
            List<Point> points,
            double maxDist) {

        if (points == null || points.isEmpty() || maxDist < 0) {
            return new ArrayList<>();
        }

        double maxDistSq = maxDist * maxDist;

        // 使用最大堆：堆顶是当前第10近的点（距离最大）
        // 存储 [距离平方, 点]，按距离平方降序
        PriorityQueue<Map.Entry<Double, Point>> maxHeap = 
            new PriorityQueue<>((a, b) -> Double.compare(b.getKey(), a.getKey()));

        for (Point p : points) {
            double distSq = target.distanceSquaredTo(p);

            // 跳过异常点
            if (distSq > maxDistSq) {
                continue;
            }

            if (maxHeap.size() < 10) {
                maxHeap.offer(new AbstractMap.SimpleEntry<>(distSq, p));
            } else if (distSq < maxHeap.peek().getKey()) {
                // 当前点比堆顶更近，替换
                maxHeap.poll();
                maxHeap.offer(new AbstractMap.SimpleEntry<>(distSq, p));
            }
        }

        // 提取结果并按距离升序排序
        List<Point> result = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            result.add(maxHeap.poll().getValue());
        }
        // 因为堆是最大堆，弹出顺序是“远→近”，所以要反转
        Collections.reverse(result);
        return result;
    }

    // ===== 测试示例 =====
    public static void main(String[] args) {
        Point target = new Point(0.0, 0.0);
        List<Point> points = Arrays.asList(
            new Point(1, 1),
            new Point(2, 2),
            new Point(10, 10),     // 异常点（距离≈14.14 > 5）
            new Point(0.5, 0.5),
            new Point(3, 3),
            new Point(0.1, 0.1),
            new Point(5, 5),       // 距离≈7.07 > 5 → 异常
            new Point(-1, -1),
            new Point(0, 1),
            new Point(1, 0),
            new Point(100, 100),   // 明显异常
            new Point(0.2, 0.3)
        );

        double maxDist = 5.0;
        List<Point> top10 = findTop10Closest(target, points, maxDist);

        System.out.println("距离目标点 " + target + " 最近的点（已过滤异常）:");
        for (Point p : top10) {
            double dist = Math.sqrt(target.distanceSquaredTo(p));
            System.out.printf("  %s, 距离=%.2f%n", p, dist);
        }
    }
}