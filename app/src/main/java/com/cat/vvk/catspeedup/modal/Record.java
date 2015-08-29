package com.cat.vvk.catspeedup.modal;

/**
 * Created by vivek-pc on 8/8/2015.
 */
public class Record {

    private int id;
    private long createdDate;
    private float timeTaken;
    private int numDigit;
    private int isCorrect;

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    private String recordType;

    public int getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public float getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(float timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getNumDigit() {
        return numDigit;
    }

    public void setNumDigit(int numDigit) {
        this.numDigit = numDigit;
    }
}
