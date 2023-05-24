package adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import fragments.*;

/**
 * Created by Carlos Chica.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    private final Fragment[] tabFragments;

    public ViewPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabFragments = new Fragment[]{
                new FragmentFavoritePeliculas(),
                new FragmentFavoriteSeries()
        };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabFragments[position];
    }

    @Override
    public int getCount() {
        return tabFragments.length;
    }
}
