import java.util.Arrays;

public class WeatherForecaster {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getsWarmerIn(new int[]{3, 4, 5, 1, -1, 2, 6, 3})));
        System.out.println(Arrays.toString(getsWarmerIn(new int[]{3, 4, 5, 6})));
        System.out.println(Arrays.toString(getsWarmerIn(new int[]{3, 6, 9})));
    }

    public static int[] getsWarmerIn(int[] temperatures) {
        int[] daysOfWait = new int[temperatures.length];
        Arrays.fill(daysOfWait, 0);
        for (int i = 0; i < temperatures.length; i++) {
            for (int j = i + 1; j < temperatures.length; j++) {
                if (temperatures[j] > temperatures[i]) {
                    daysOfWait[i] = j - i;
                    break;
                }
            }
        }
        return daysOfWait;
    }
}