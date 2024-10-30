import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SensorDataProcessor {
    private double[][][] data;
    private double[][] limit;

    // change the constructor name 
    public SensorDataProcessor(double[][][] data, double[][] limit) {
        this.data = data;
        this.limit = limit;
    }

    // Calculates the average 
    private double average(double[] array) {
        double sum = 0;
        for (double value : array) {
            sum += value;
        }
        return sum / array.length;
    }

    // Method to calculate new values 
    public void calculate(double divisor) {
        int i, j, k;
        double[][][] processedData = new double[data.length][data[0].length][data[0][0].length];
        BufferedWriter writer = null;

        try {
            // Initialize BufferedWriter to write data to a file
            writer = new BufferedWriter(new FileWriter("RacingStatsData.txt"));
            
            // Process each element in the 3D data array
            for (i = 0; i < data.length; i++) {
                for (j = 0; j < data[i].length; j++) {
                    for (k = 0; k < data[i][j].length; k++) {
                        // Calculate processed data 
                        processedData[i][j][k] = data[i][j][k] / divisor - Math.pow(limit[i][j], 2.0);

                        // Check conditions to modify processed data
                        if (average(processedData[i][j]) > 10 && average(processedData[i][j]) < 50) {
                            break;
                        } else if (Math.max(data[i][j][k], processedData[i][j][k]) > data[i][j][k]) {
                            break;
                        } else if (Math.pow(Math.abs(data[i][j][k]), 3) < Math.pow(Math.abs(processedData[i][j][k]), 3)
                                && average(data[i][j]) < processedData[i][j][k] && (i + 1) * (j + 1) > 0) {
                            processedData[i][j][k] *= 2;
                        }
                    }
                }
            }

            // Write processed data to file
            for (i = 0; i < processedData.length; i++) {
                for (j = 0; j < processedData[i].length; j++) {
                    writer.write(Arrays.toString(processedData[i][j]) + "\t");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            // error message 
            System.err.println("Error writing to file: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                System.err.println("Error closing BufferedWriter: " + ex.getMessage());
            }
        }
    }
}
