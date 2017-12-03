package com.soongsil.alopeciadetect.objects;

import io.realm.RealmObject;

/**
 * Created by Park on 2017-12-03.
 */

public class KeratinRealmObj extends RealmObject{

    private String date;
    private float result;
    private float score;

    public KeratinRealmObj() {}
    public KeratinRealmObj(String date, float result, float score) {
        this.date = date;
        this.result = result;
        this.score = score;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public float getResult() {
        return result;
    }
    public void setResult(float result) {
        this.result = result;
    }

    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }
}
