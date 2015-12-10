package info.ipeanut.youngsamples.third.ownview.basepage;

import android.content.Context;
import android.widget.TextView;

import info.ipeanut.youngsamples.R;

/**
 * Created by chenshaosina on 15/12/10.
 */
public class MinePage extends BasePage {

    TextView textView;
    public MinePage(Context context) {
        super(context);
    }

    @Override
    protected int layoutRes() {
        return R.layout.page_mine;
    }

    @Override
    protected void findViews() {
        textView =  findViewThroughId(R.id.textView);
    }

    @Override
    protected void initData() {

    }

}
