import java.util.ArrayList;

public class SortingAlgorithm {
    public static void mergeSort(ArrayList<String> list) {
        // check if list has reached the base case (contains only one element)
        if (list.size() <= 1) {
            return;
        }

        // divide the list into two sublists
        int mid = list.size() / 2;
        ArrayList<String> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<String> right = new ArrayList<>(list.subList(mid, list.size()));

        // sort the left and right sublists
        mergeSort(left);
        mergeSort(right);

        // merge back together the left and right sublists
        merge(list, left, right);
    }

    public static void merge(ArrayList<String> list, ArrayList<String> left, ArrayList<String> right) {
        int i = 0, j = 0, k = 0;

        // compare the elements from both sublists
        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareToIgnoreCase(right.get(j)) <= 0)
                list.set(k++, left.get(i++));
            else
                list.set(k++, right.get(j++));
        }

        // copy the remaining elements from both sublists if any
        while (i < left.size())
            list.set(k++, left.get(i++));

        while (j < right.size())
            list.set(k++, right.get(j++));
    }
}
