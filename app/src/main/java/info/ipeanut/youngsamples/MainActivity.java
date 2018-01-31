package info.ipeanut.youngsamples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import info.ipeanut.youngsamples.recyclerview.RecyclerViewActivity;
import info.ipeanut.youngsamples.supportappnavigation.AppNavHomeActivity;
import info.ipeanut.youngsamples.supportv4.SupportFourDemos;
import info.ipeanut.youngsamples.third.ownview.ThirdOwnViewActivity;
import info.ipeanut.youngsamples.third.view.ThirdViewActivity;
import info.ipeanut.youngsamples.third.viewgroup.ThirdViewGroupActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

  private static String[] names = {
      "Support4Demos", "SupportAppNavigation", "ThirdView", "ThirdViewGroup", "ThirdOwnView",
      "RecyclerView"
  };
  private static Class[] cls = {
      SupportFourDemos.class, AppNavHomeActivity.class, ThirdViewActivity.class,
      ThirdViewGroupActivity.class, ThirdOwnViewActivity.class, RecyclerViewActivity.class
  };
  private List<SampleBean> samples = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prepateSamples();
    setListAdapter(new SampleAdapter(this, samples));
  }

  @Override protected void onListItemClick(ListView l, View v, int position, long id) {
    startActivity(((SampleBean) (getListAdapter().getItem(position))).intent);
  }

  private void prepateSamples() {
    samples = new ArrayList<>();
    for (int i = 0; i < names.length; i++) {
      Intent intent = new Intent();
      intent.setClass(this, cls[i]);
      SampleBean sb = new SampleBean(names[i], intent);
      samples.add(sb);
    }
  }
}
