package vcpkgUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class ProcessWorker {
    public ArrayList<String> wrapProcessOutput(Process process) throws IOException {
        ArrayList<String> wrappedLinesFromOutput = new ArrayList<>();
        Reader readerFromVcpkg = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReaderFromVcpkg = new BufferedReader(readerFromVcpkg);
        String lineFromOutput;
        while ((lineFromOutput = bufferedReaderFromVcpkg.readLine()) != null && !lineFromOutput.isEmpty()) {
            wrappedLinesFromOutput.add(lineFromOutput);
        }
        return wrappedLinesFromOutput;
    }
}
