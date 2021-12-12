package response.message.content;

import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileMessage extends Message {
    private final String filePath;
    private final File downloadFile;
    private final char[] buffer = new char[4096];

    public FileMessage(String filePath) {
        this.filePath = filePath;
        this.downloadFile = null;
    }

    public FileMessage(File downloadFile) {
        this.filePath = null;
        this.downloadFile = downloadFile;
    }

    @Override
    protected String getContentType() {
        log.info("Guess content type.");

        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
            return "text/html";
        } else if (filePath.endsWith(".txt") || filePath.endsWith(".java")) {
            return "text/plain";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".class")) {
            return "application/octet-stream";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".mpeg")) {
            return "video/mpeg";
        } else if (filePath.endsWith(".ts")) {
            return "video/MP2T";
        } else if (filePath.endsWith(".avi")) {
            return "video/x-msvideo";
        } else {
            return "text/plain";
        }
    }

    @Override
    protected String getContent() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));) {
            int readNo = 0;

            while ((readNo = bufferedReader.read(buffer)) != -1) {
                content.append(new String(buffer, 0, readNo));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }
}
