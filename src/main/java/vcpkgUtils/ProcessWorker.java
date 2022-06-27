package vcpkgUtils;

import javafx.scene.control.Alert;

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
    public ArrayList<String> wrapProcessOutput(Process process) throws IOException {
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
            readFromBuffer.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            Alert timeOutReadingAlert = new Alert(Alert.AlertType.WARNING);
            timeOutReadingAlert.setTitle("Time out");
            timeOutReadingAlert.setContentText("The working time with the process is too long. You may have specified the executable file incorrectly.");
            timeOutReadingAlert.showAndWait();
        }
        return wrappedLinesFromOutput;
    }
}