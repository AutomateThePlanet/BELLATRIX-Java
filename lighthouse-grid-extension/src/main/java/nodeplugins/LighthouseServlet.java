package nodeplugins;

import com.google.common.net.MediaType;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import static java.lang.Thread.*;

public class LighthouseServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LighthouseServlet.class.getName());

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String lighthouseArs = req.getHeader("lighthouse");

        executeCommand("cmd.exe /c lighthouse " + lighthouseArs);

        sleep(2000);

        File file = getTheNewestFile("json");

        String reportContent = Files.readString(Path.of(file.getPath()));
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(reportContent);
        out.flush();

        deleteReports();
    }

    private File getTheNewestFile(String ext) {
        String currentJarDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File theNewestFile = null;
        File dir = new File(currentJarDir);
        var files = Arrays.stream(dir.listFiles()).filter(f -> f.getName().endsWith(ext)).toArray(File[]::new);

        if (files.length > 0) {
            File lastModifiedFile = files[0];
            for (int i = 1; i < files.length; i++) {
                if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                    lastModifiedFile = files[i];
                }
            }
            theNewestFile = files[0];
        }

        return theNewestFile;
    }

    private void deleteReports() {
        String currentJarDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File theNewestFile = null;
        File dir = new File(currentJarDir);
        var files = Arrays.stream(dir.listFiles()).filter(f -> !f.getName().endsWith("jar")).toArray(File[]::new);

        if (files.length > 0) {
            for (var file : files) {
                file.delete();
            }
        }
    }

    private String identifyFileContentType(File file) throws IOException {
        String contentType = Files.probeContentType(file.toPath());
        return contentType != null ? contentType : MediaType.OCTET_STREAM.toString();
    }

    private void executeCommand(String command) {
        try {
            log(command);
            Process process = Runtime.getRuntime().exec(command);
            logOutput(process.getInputStream(), "");
            logOutput(process.getErrorStream(), "Error: ");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendError(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(msg);
    }

    private void logOutput(InputStream inputStream, String prefix) {
        new Thread(() -> {
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                synchronized (this) {
                    log(prefix + scanner.nextLine());
                }
            }
            scanner.close();
        }).start();
    }

    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SSS");

    public synchronized void log(String message) {
        System.out.println(format.format(new Date()) + ": " + message);
    }
}