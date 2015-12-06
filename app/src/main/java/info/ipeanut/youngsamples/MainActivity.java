package info.ipeanut.youngsamples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.ipeanut.youngsamples.supportappnavigation.AppNavHomeActivity;
import info.ipeanut.youngsamples.supportv4.SupportFourDemos;

public class MainActivity extends ListActivity {

    /**
     * SupportFourDemos
     * SupportAppNavigation,
     */
    private static String[] names = {
            "Support4Demos",
            "SupportAppNavigation"

    };
    private static Class[] cls = {
            SupportFourDemos.class,
            AppNavHomeActivity.class

    };
    private List<SampleBean> samples = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepateSamples();
        setListAdapter(new SampleAdapter(this,samples));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(((SampleBean)(getListAdapter().getItem(position))).intent);
    }

    private void prepateSamples() {
        samples = new ArrayList<>();
        for (int i=0;i<names.length;i++){
            Intent intent = new Intent();
            intent.setClass(this,cls[i]);
            SampleBean sb = new SampleBean(names[i],intent);
            samples.add(sb);
        }
    }
}
