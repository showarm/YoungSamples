package info.ipeanut.youngsamples.third.viewgroup.directionalViewPager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import info.ipeanut.youngsamples.R;
import info.ipeanut.youngsamples.third.viewgroup.directionalViewPager.DirectionalViewPager;


public class DirectionalActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directoinal);

        //Set up the pager
        final DirectionalViewPager pager = (DirectionalViewPager)findViewById(R.id.pager);
        pager.setAdapter(new TestFragmentAdapter(getSupportFragmentManager()));

        //crush
//        pager.setPageTransformer(false, new DefaultTransformer());

        //Bind to control buttons
        ((Button)findViewById(R.id.horizontal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setOrientation(DirectionalViewPager.HORIZONTAL);
            }
        });
        ((Button)findViewById(R.id.vertical)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setOrientation(DirectionalViewPager.VERTICAL);
            }
        });
    }
}
