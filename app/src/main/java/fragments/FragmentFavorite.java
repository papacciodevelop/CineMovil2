package fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.carlos.cinemovil.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import adapters.ViewPageAdapter;
/**
 * Created by Carlos Chica.
 */
public class FragmentFavorite extends Fragment {

    public FragmentFavorite() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ventana_favoritos, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.vpFavorite);
        viewPager.setAdapter(new ViewPageAdapter((getChildFragmentManager())));

        TabLayout tabLayout = rootView.findViewById(R.id.tabFavorite);
        tabLayout.setupWithViewPager(viewPager);


        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        (tabLayout.getTabAt(0)).setText("Peliculas");
        (tabLayout.getTabAt(1)).setText("Series");

        return rootView;
    }

}
