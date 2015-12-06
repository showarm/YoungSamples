package info.ipeanut.youngsamples;

import android.content.Intent;

/**
 * Created by chenshao on 15/12/6.
 */
public class SampleBean {
    public String name;
    public Intent intent;

    public SampleBean(String name, Intent intent) {
        this.name = name;
        this.intent = intent;
    }
}
