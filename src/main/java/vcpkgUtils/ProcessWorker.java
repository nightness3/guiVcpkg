package vcpkgUtils;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessWorker {
    public ArrayList<String> wrapProcessOutput(Process process, Label statusLabel) throws IOException {
        ArrayList<String> wrappedLinesFromOutput = new ArrayList<>();
        Reader readerFromVcpkg = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReaderFromVcpkg = new BufferedReader(readerFromVcpkg);
        CompletableFuture<Void> readFromBuffer = CompletableFuture.runAsync(() -> {
            try {
                String lineFromOutput = bufferedReaderFromVcpkg.readLine();
                while (lineFromOutput != null && !lineFromOutput.isEmpty()) {
                    wrappedLinesFromOutput.add(lineFromOutput);
                    lineFromOutput = bufferedReaderFromVcpkg.readLine();
                }
                bufferedReaderFromVcpkg.close();
                readerFromVcpkg.close();
            } catch (IOException ignored) {

            }
        });
        try {
            readFromBuffer.get(3000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            Platform.runLater(() -> {
                statusLabel.setText("Exceeding the time of working");
                statusLabel.setTextFill(Color.RED);
            });
        }
        return wrappedLinesFromOutput;
    }
}