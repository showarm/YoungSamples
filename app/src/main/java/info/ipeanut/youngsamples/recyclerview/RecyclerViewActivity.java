package info.ipeanut.youngsamples.recyclerview;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import info.ipeanut.youngsamples.SampleAdapter;
import info.ipeanut.youngsamples.SampleBean;
import info.ipeanut.youngsamples.recyclerview.Gallery.GalleryRecyclerActivity;
import info.ipeanut.youngsamples.recyclerview.swipecard.demo.SwipeCardActivity;
import info.ipeanut.youngsamples.recyclerview.windowimageview.WivDemoActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshaosina on 15/12/8.
 */
public class RecyclerViewActivity extends ListActivity {

    private static String[] names = {
            "windowimageview",
            "GalleryRecyclerView",
            "SwipeCard",

    };
    private static Class[] cls = {
            WivDemoActivity.class,
        GalleryRecyclerActivity.class,
        SwipeCardActivity.class,
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
