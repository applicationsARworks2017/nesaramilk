package demosell.amaresh.android.com.nesara.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import demosell.amaresh.android.com.nesara.Adapter.ViewPagerAdapter;
import demosell.amaresh.android.com.nesara.Fragment.OneFragment;
import demosell.amaresh.android.com.nesara.Fragment.ThreeFragment;
import demosell.amaresh.android.com.nesara.Fragment.TwoFragment;
import demosell.amaresh.android.com.nesara.Pojo.SubscriptionListing;
import demosell.amaresh.android.com.nesara.R;

public class UserDetailsPage extends AppCompatActivity {
    Toolbar my_detailstoolbar;
    ImageView backbutton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_page);
        my_detailstoolbar=(Toolbar)findViewById(R.id.my_detailstoolbar);
        backbutton=(ImageView)my_detailstoolbar.findViewById(R.id.backbutton);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        final int[] ICONS = new int[]{
                R.mipmap.ic_view_list_white_24dp,
                R.mipmap.ic_account_balance_wallet_white_24dp,
                R.mipmap.ic_person_white_24dp
        };

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("ID");
            // and get whatever type user account id is
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsPage.super.onBackPressed();
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "SubScriptions");
        adapter.addFragment(new TwoFragment(), "Wallet");
        adapter.addFragment(new ThreeFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }
}
