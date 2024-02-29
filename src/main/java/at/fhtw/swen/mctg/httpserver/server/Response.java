package at.fhtw.swen.mctg.httpserver.server;

import at.fhtw.swen.mctg.httpserver.http.ContentType;
import at.fhtw.swen.mctg.httpserver.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Response {
    private int status;
    private String message;
    private ContentType contentType;
    private String content;

    public Response(HttpStatus httpStatus, ContentType contentType, String content) {
        this.status = httpStatus.code;
        this.message = httpStatus.message;
        this.contentType = contentType;
        this.content = content;
    }

    public String get() {

        String localDatetime = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("UTC")));
        return "HTTP/1.1 " + this.status + " " + this.message + "\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "Connection: close\r\n" +
                "Date: " + localDatetime + "\r\n" +
                "Expires: " + localDatetime + "\r\n" +
                "Content-Type: " + this.contentType + "\r\n" +
                "Content-Length: " + this.content.length() + "\r\n" +
                "\r\n" +
                this.content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + this.status +
                ", message='" + this.message + '\'' +
                ", contentType='" + this.contentType + '\'' +
                ", content='" + this.content + '\'' +
                '}';
    }


}
