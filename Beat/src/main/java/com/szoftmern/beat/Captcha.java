package com.szoftmern.beat;

import javafx.scene.control.Label;

import java.util.*;

public class Captcha {
    public static void setMathProblem(Label numbers) {
        Random rand = new Random();
        int r1 = rand.nextInt(10) + 10;
        Random random = new Random();
        int r2 = random.nextInt(10) + 10;
        numbers.setText(r1 + " + " + r2 + " = ");
    }
    public static String getSum(Label numbers) {
        int r1 = Integer.parseInt(numbers.getText().substring(0,2));
        int r2 = Integer.parseInt(numbers.getText().substring(5,7));
        int sum = r1 + r2;
        return Integer.toString(sum);
    }
}
