import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file1 = new File("D:\\directory.txt");
        File file2 = new File("D:\\find.txt");
        ArrayList<String> searchArray = new ArrayList<>();
        ArrayList<String> targetArray = new ArrayList<>();

        Scanner scanner1 = new Scanner(new FileReader(file1));
        Scanner scanner2 = new Scanner(new FileReader(file2));

        while (scanner1.hasNextLine()) {
            searchArray.add(scanner1.nextLine());
        }

        while (scanner2.hasNextLine()) {
            targetArray.add(scanner2.nextLine().trim());
        }

        Duration linearSearchDuration = linearSearchAlgo(searchArray, targetArray);
        boolean flag;

        Instant instant1 = Instant.now();
        flag = bubbleSort(searchArray, linearSearchDuration);
        Instant instant2 = Instant.now();
        Duration bubbleSearchDuration = Duration.between(instant1, instant2);

        if (flag) {
            jumpSearchAlgo(searchArray, targetArray, bubbleSearchDuration);
        } else {
            linearSearchAlgo2(searchArray, targetArray, bubbleSearchDuration);
        }
    }

    public static Duration linearSearchAlgo(ArrayList<String> searchArray, ArrayList<String> targetArray) {
        Instant instant1 = Instant.now();
        String[] phoneBook = getStringArray(searchArray);
        int counter = 0;

        System.out.println("Start searching (linear search)...");

        for (String str : targetArray) {
            for (String s : phoneBook) {
                if (s.contains(str)) {
                    counter++;
                }
            }
        }

        Instant instant2 = Instant.now();
        Duration d = Duration.between(instant1, instant2);
        long minutes = d.toMinutes();
        long seconds = d.toSeconds();
        long millis = d.toMillisPart();

        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", counter, targetArray.size(),
                minutes,  seconds, millis);
        System.out.println();
        System.out.println();
        return d;
    }

    public static void linearSearchAlgo2(ArrayList<String> searchArray,
                                         ArrayList<String> targetArray, Duration bubbleSearchDuration) {
        Instant instant1 = Instant.now();
        String[] phoneBook = getStringArray(searchArray);
        int counter = 0;

        for (String str : targetArray) {
            for (String s : phoneBook) {
                if (s.contains(str)) {
                    counter++;
                }
            }
        }

        Instant instant2 = Instant.now();
        Duration d = Duration.between(instant1, instant2);
        long minutes = d.toMinutes();
        long seconds = d.toSeconds();
        long millis = d.toMillisPart();

        long sMinutes = bubbleSearchDuration.toMinutes();
        long sSeconds = bubbleSearchDuration.toSeconds();
        long sMillis = bubbleSearchDuration.toMillisPart();

        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", counter, targetArray.size(),
                minutes + sMinutes, seconds + sSeconds, millis + sMillis);
        System.out.println();
        System.out.printf("Sorting time: %d min. %d sec. %d ms. " +
                "- STOPPED, moved to linear search", sMinutes, sSeconds, sMillis);
        System.out.println();
        System.out.printf("Searching time: %d min. %d sec. %d ms.", minutes, seconds, millis);
    }

    public static void jumpSearchAlgo(ArrayList<String> searchArray, ArrayList<String> targetArray, Duration sortingDuration) {
        Instant instant1 = Instant.now();
        String[] phoneBook = getStringArray(searchArray);
        int counter = 0;

        for (String str : targetArray) {
            counter += jumpSearch(phoneBook, str);
        }

        Instant instant2 = Instant.now();
        Duration searchingDuration = Duration.between(instant1, instant2);
        long minutes = searchingDuration.toMinutes();
        long seconds = searchingDuration.toSeconds();
        long millis = searchingDuration.toMillisPart();

        long sMinutes = sortingDuration.toMinutes();
        long sSeconds = sortingDuration.toSeconds();
        long sMillis = sortingDuration.toMillisPart();

        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.", counter, targetArray.size(),
                minutes + sMinutes, seconds + sSeconds, millis + sMinutes);
        System.out.println();
        System.out.printf("Sorting time: %d min. %d sec. %d ms.", sMinutes, sSeconds, sMillis);
        System.out.println();
        System.out.printf("Searching time: %d min. %d sec. %d ms.", minutes, seconds, millis);
    }

    public static HashMap<String, String> getList(ArrayList<String> list) {
        HashMap<String, String> list2 = new HashMap<>();

        for (String str : list) {
            String[] strings = new String[2];

            Pattern telNumber = Pattern.compile("\\d[0-9]*\\d");
            Matcher telNoMatcher = telNumber.matcher(str);

            Pattern name = Pattern.compile("\\s[a-zA-Z]*\\s?[a-zA-Z]*");
            Matcher nameMatcher = name.matcher(str);

            if (telNoMatcher.find()) {
                strings[0] = telNoMatcher.group();
            }
            if (nameMatcher.find()) {
                strings[1] = nameMatcher.group().trim();
            }
            list2.put(strings[1], strings[0]);
        }
        return list2;
    }

    public static String[] getStringArray(ArrayList<String> array) {
        ArrayList<String> list = new ArrayList<>();
        String[] list2  = new String[array.size()];

        for (String str : array) {
            String[] strings = new String[2];
            StringBuilder stringBuilder = new StringBuilder();

            Pattern telNumber = Pattern.compile("\\d[0-9]*\\d");
            Matcher telNoMatcher = telNumber.matcher(str);

            Pattern name = Pattern.compile("\\s[a-zA-Z]*\\s?[a-zA-Z]*");
            Matcher nameMatcher = name.matcher(str);

            if (telNoMatcher.find()) {
                strings[0] = telNoMatcher.group();
            }
            if (nameMatcher.find()) {
                strings[1] = nameMatcher.group().trim();
            }

            stringBuilder.append(strings[1]).append(" ").append(strings[0]);
            list.add(stringBuilder.toString());
        }
        for (int i = 0; i < list.size(); i++) {
            list2[i] = list.get(i);
        }
        return list2;
    }

    public static boolean bubbleSort(ArrayList<String> searchArray, Duration linearSearchDuration) {
        System.out.println("Start searching(bubble sort + jump search)...");

        String[] array = getStringArray(searchArray);
        boolean flag = true;
        Instant instantStart = Instant.now();
        Instant instantFinish;
        Duration d;

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                /* if a pair of adjacent elements has the wrong order it swaps them */
                if (array[i].compareTo(array[j]) > 0) {
                    String temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
            instantFinish = Instant.now();
            d = Duration.between(instantStart, instantFinish);

            if (d.toSeconds() > linearSearchDuration.toSeconds() * 10) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static int jumpSearch(String[] array, String target) {
        int counter = 0;
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* Calculating the jump length over array elements */
        int jumpLength = (int) Math.sqrt(array.length);

        /* Finding a block where the element may be present */
        while (currentRight < array.length - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(array.length - 1, currentRight + jumpLength);

            if (array[currentRight].compareTo(target) >= 0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* Doing linear search in the found block */
        counter += backwardSearch(array, target, prevRight, currentRight);

        return counter;
    }

    public static int backwardSearch(String[] array, String target, int leftExcl, int rightIncl) {
        int found = 0;
        for (int i = rightIncl; i >= leftExcl; i--) {
            if (array[i].contains(target)) {
                found += 1;
            }
        }
        return found;
    }
}
