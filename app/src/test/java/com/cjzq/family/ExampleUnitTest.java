package com.cjzq.family;

import android.util.Log;

import com.wuhanins.common.utils.INSLog;

import org.junit.Test;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void 红球() {
        //创建Set集合对像
        Set<Integer> s = new TreeSet<>();
        //创建随机数对象
        Random r = new Random();
        while (s.size() < 5) {
            int num = r.nextInt(35) + 1;
            s.add(num);
        }
        for (Integer i : s) {
            System.out.print(i + "-");
        }
    }

    @Test
    public void 蓝球() {
        //创建Set集合对像
        Set<Integer> s = new TreeSet<>();
        //创建随机数对象
        Random r = new Random();
        while (s.size() < 2) {
            int num = r.nextInt(12) + 1;
            s.add(num);
        }
        for (Integer i : s) {
            System.out.print(i + "-");
        }
    }

    @Test
    public void 福彩红球() {
        //创建Set集合对像
        Set<String> s = new TreeSet<>();
        //创建随机数对象
        Random r = new Random();
        while (s.size() < 6) {
            int num = r.nextInt(33) + 1;
            if (num < 10) {
                s.add("0" + num);
            } else {
                s.add(String.valueOf(num));
            }
        }
        for (String i : s) {
            System.out.print(i + " ");
        }
    }

    @Test
    public void 福彩蓝球() {
        //创建Set集合对像
        Set<String> s = new TreeSet<>();
        //创建随机数对象
        Random r = new Random();
        while (s.size() < 1) {
            int num = r.nextInt(16) + 1;
            if (num < 10) {
                s.add("0" + num);
            } else {
                s.add(String.valueOf(num));
            }
        }
        for (String i : s) {
            System.out.print(i);
        }
    }

    @Test
    public void 快乐8() {
        //创建Set集合对像
        Set<String> s = new TreeSet<>();
        //创建随机数对象
        Random r = new Random();
        while (s.size() < 10) {
            int num = r.nextInt(80) + 1;
            if (num < 10) {
                s.add("0" + num);
            } else {
                s.add(String.valueOf(num));
            }
        }
        for (String i : s) {
            System.out.print(i + " ");
        }
    }
}