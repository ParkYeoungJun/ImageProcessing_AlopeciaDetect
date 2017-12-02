package com.soongsil.alopeciadetect.objects;

import io.realm.RealmObject;

/**
 * Created by Park on 2017-12-03.
 */

public class QuestionRealmObj extends RealmObject {

    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
