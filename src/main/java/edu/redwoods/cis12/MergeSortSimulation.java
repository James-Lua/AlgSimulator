package edu.redwoods.cis12;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MergeSortSimulation extends AlgorithmSimulation {

    private final Random obj;

    public MergeSortSimulation(AlgSimulatorController asc) {
        super("Merge Sort", "mergeSortControls.fxml", asc);
        obj = new Random();
    }

    // Generate a random hexadecimal color string
    private String randomHexColor() {
        int randNum = obj.nextInt(0xffffff + 1);
        return String.format("#%06x", randNum);
    }

    // Compare the values of two nodes
    private int compare(Node node1, Node node2) {
        int v1 = Integer.parseInt(((Button) node1).getText());
        int v2 = Integer.parseInt(((Button) node2).getText());
        return Integer.compare(v1, v2);
    }

    // Merge two sorted subarrays into a single sorted array
    private void merge(int l, int m, int r) {

        int n1 = m - l + 1;
        int n2 = r - m;

        List<Node> gn = gridPane.getChildren();
        String[] arr = new String[gn.size()];

        List<Node> L = gn.subList(l, l + n1);
        List<Node> R = gn.subList(m + 1, m + n2 + 1);

        int i = 0, j = 0;
        int k = l;

        // Merge the subarrays while maintaining sorted order
        while (i < n1 && j < n2) {
            if (compare(L.get(i), R.get(j)) < 1) {
                arr[k] = ((Button) L.get(i)).getText();
                i++;
            } else {
                arr[k] = ((Button) R.get(j)).getText();
                j++;
            }
            k++;
        }

        // Copy remaining elements from L, if any
        while (i < n1) {
            arr[k] = ((Button) L.get(i)).getText();
            i++;
            k++;
        }

        // Copy remaining elements from R, if any
        while (j < n2) {
            arr[k] = ((Button) R.get(j)).getText();
            j++;
            k++;
        }

        // Update GUI with the merged array and color each button
        String color = randomHexColor();
        Platform.runLater(() -> {
            for (int s = l; s < l + n1 + n2; s++) {
                Button b = (Button) gn.get(s);
                b.setText(arr[s]);
                b.setStyle("-fx-background-color: " + color);
            }
        });

        // Pause to visualize the merge operation
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Recursively sort the array using merge sort algorithm
    private void mergeSort(int left, int right) {
        if (left < right) {
            // Generate a random color for visual representation
            String color = randomHexColor();
            // Color the subarray at this step
            for (int i = left; i <= right; i++) {
                gridPane.getChildren().get(i).setStyle("-fx-background-color: " + color);
            }
            // Pause before merging further for visualization
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            // Find the middle point and recursively sort halves
            int middle = left + (right - left) / 2;
            mergeSort(left, middle);
            mergeSort(middle + 1, right);

            // Merge sorted halves
            merge(left, middle, right);
        }
    }

    // Start the simulation of merge sort
    @Override
    public void simulate() {
        // Start a new thread for merge sort
        try {
            new Thread(() -> mergeSort(0, gridPane.getChildren().size() - 1)).start();
        } catch (NumberFormatException|NullPointerException ignore) {
            Thread.currentThread().interrupt();
        }
    }
}
