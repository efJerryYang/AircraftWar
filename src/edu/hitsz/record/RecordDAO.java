package edu.hitsz.record;

import java.util.List;

public interface RecordDAO {
    List<Record> getAllRecords();

    Record getByRank(int rank);

    Record getByName(String name);

    Record addRecord(Record record);

    Record deleteByRank(int rank);

    Record deleteByName(String name);

    void saveRecord();
}
