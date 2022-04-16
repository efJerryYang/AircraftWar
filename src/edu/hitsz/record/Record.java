package edu.hitsz.record;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Record {
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private int rank;
    private String username;
    private String level;
    private int score;
    private String datetime;

    public Record() throws NoSuchAlgorithmException {
        this.datetime = Record.getDateTime();
        this.username = Record.generateUsername(datetime);
        this.score = (int) Math.round(Math.random() * 10000);
        this.level = "MEDIUM";
    }
    public Record(String name,int score) throws NoSuchAlgorithmException {
        this.datetime = Record.getDateTime();
        this.username = name;
        this.score = score;
        this.level = "MEDIUM";
    }
    public static String getDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static String generateUsername(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update((string + Math.random()).getBytes(StandardCharsets.UTF_8));
        String username = String.format("%064x", new BigInteger(1, digest.digest()));
        username = username.substring(0, 8).toUpperCase(Locale.ROOT);
        return username;
    }

//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String datetime = Record.getDateTime();
//        String name = Record.generateUsername(datetime);
//        System.out.println("datetime: " + datetime);
//        System.out.println("username: " + name);
//    }

    public static String prettyPrintHeader(boolean printHeader) {
        String header = String.format("%4s\t%8s\t%6s\t%6s\t%20s", "rank", "name", "level", "score", "datetime");
        if (printHeader) {
            System.out.println(header);
        }
        return header;
    }

    public String prettyPrintRecord(boolean printRecord) {
        String record = String.format("%4d\t%8s\t%6s\t%6d\t%20s", rank, username, level, score, datetime);
        if (printRecord) {
            System.out.println(record);
        }
        return record;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getlevel() {
        return level;
    }

    public void setlevel(String level) {
        this.level = level;
    }
}
