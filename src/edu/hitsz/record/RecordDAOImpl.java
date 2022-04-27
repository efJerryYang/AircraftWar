package edu.hitsz.record;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 我们要确保，只要必要的时候才需要对内容进行排序：其他时候只需要维护一个isSorted标记
 * 1. 打印到屏幕的时候必须是有序的
 * 2. 写入到文件的时候必须是有序的
 * 3. 从文件读取的时候必须是有序的（但无法保证，所以必须排序）
 * 4. 其他时候都只需要在操作了recordList内容的时候
 *
 * @author JerryYang
 */

public class RecordDAOImpl implements RecordDAO {
    private final int TEST_RECORD_LENGTH = 5;
    private final String[] header = {"rank", "name", "level", "score", "datetime"};
    boolean isSorted;
    private ArrayList<Record> recordList;
    //    private ArrayList<String>[] recordStringList;
    private String recordPath;

    public RecordDAOImpl(int level) {
        String levelTitle = level == 1 ? "simple" :
                level >= 2 && level <= 3 ? "medium" :
                        level >= 4 && level <= 5 ? "difficult" :
                                "other";
        recordPath = System.getProperty("user.dir") + "/src/record/" + levelTitle + "-record.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader reader = null;
        try {
            reader = new FileReader(recordPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recordList = gson.fromJson(reader, new TypeToken<List<Record>>() {
        }.getType());
    }

    //    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
//        RecordDAOImpl recordDAO = new RecordDAOImpl();
//        Record record;
//        recordDAO.createTestRecords();
//        System.out.println("==================== getAllRecords ====================");
//        recordDAO.getAllRecords();
//        System.out.println("==================== getByRank ========================");
//        recordDAO.getByRank(3).prettyPrintRecord(true);
//        System.out.println("==================== getByName ========================");
//        try {
//            recordDAO.getByName("8DFC00EB").prettyPrintRecord(true);
//        } catch (NullPointerException e) {
//            System.out.println("Username not exists!");
//        }
//    }
    public void sortByRank() {
        recordList.sort(Comparator.comparing(Record::getScore).reversed());
        for (int i = 0; i < recordList.size(); i++) {
            recordList.get(i).setRank(i + 1);
        }
        isSorted = true;
    }

    public void sortByRank(boolean reverseRanking) {
        // reverse ranking is just the score ascending sequence
        if (reverseRanking) {
            recordList.sort(Comparator.comparing(Record::getScore));
        } else {
            recordList.sort(Comparator.comparing(Record::getScore).reversed());
        }
        for (int i = 0; i < recordList.size(); i++) {
            recordList.get(i).setRank(i + 1);
        }
        isSorted = true;
    }

    public void createTestRecords() throws NoSuchAlgorithmException, IOException {
        recordList = new ArrayList<>();
        // pretty print json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (int i = 0; i < TEST_RECORD_LENGTH; i++) {
            Record record = new Record();
            recordList.add(record);
        }
        // write to file
        FileWriter writer = new FileWriter(recordPath);
        // sort arraylist
        this.sortByRank();
        gson.toJson(recordList, writer);
        writer.flush(); //flush data to file   <---
        writer.close(); //close write
        // print result
        String jsonInString = gson.toJson(recordList);
        // load from file
        JsonElement json = gson.fromJson(new FileReader(recordPath), JsonElement.class);
        String result = gson.toJson(json);
    }

    @Override
    public List<Record> getAllRecords() {
        if (!isSorted) {
            sortByRank();
            isSorted = true;
        }
        Record.prettyPrintHeader(true);
        for (Record item : recordList) {
            item.prettyPrintRecord(true);
        }
        return recordList;
    }

    @Override
    public Record getByRank(int rank) {
        if (!isSorted) {
            sortByRank();
            isSorted = true;
        }
        return recordList.get(rank - 1);
    }

    @Override
    public Record getByName(String name) {
        if (!isSorted) {
            sortByRank();
            isSorted = true;
        }
        for (Record record : recordList) {
            if (name.equals(record.getUsername())) {
                return record;
            }
        }
        return null;
    }

    @Override
    public Record addRecord(Record record) {
        recordList.add(record);
        recordList.sort(Comparator.comparing(Record::getScore).reversed());
        for (int i = 0; i < recordList.size(); i++) {
            recordList.get(i).setRank(i + 1);
        }
        isSorted = true;
        return record;
    }

    @Override
    public Record deleteByRank(int rank) {
        isSorted = false;
        return recordList.remove(rank - 1);
    }

    @Override
    public Record deleteByName(String name) {
        if (!isSorted) {
            sortByRank();
            isSorted = true;
        }
        for (Record record : recordList) {
            if (name.equals(record.getUsername())) {
                recordList.remove(record);
                isSorted = false;
                return record;
            }
        }
        return null;
    }

    @Override
    public void saveRecord() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = null;
        try {
            writer = new FileWriter(recordPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // sort arraylist
        if (!isSorted) {
            sortByRank();
            isSorted = true;
        }
        gson.toJson(recordList, writer);
        try {
            writer.flush(); //flush data to file   <---
            writer.close(); //close write
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getHeader() {
        return header;
    }
}
// reference:
// convert java to json
// https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
// 1. Java object to JSON file; 2. Java object to JSON string
// not work ---> solved:
// https://stackoverflow.com/questions/45995067/writer-not-working-for-json-file-using-gson-json-file-is-blank-after-code-execu
// sort json
// https://stackoverflow.com/questions/19493413/how-can-i-sort-my-json-object-based-on-key
// java object to jsonArray
// gson to arraylist<T>
// https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
// find object by property
// https://stackoverflow.com/questions/17526608/how-to-find-an-object-in-an-arraylist-by-property
