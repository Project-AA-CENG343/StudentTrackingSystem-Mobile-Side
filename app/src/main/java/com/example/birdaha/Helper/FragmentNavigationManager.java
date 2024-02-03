package com.example.birdaha.Helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.birdaha.Interface.NavigationManager;
import com.example.birdaha.R;


/**
 * The FragmentNavigationManager class implements NavigationManager interface
 * to manage fragment transactions within an activity.
 * It handles showing fragments within the specified container layout.
 * It uses Singleton Pattern
 */
public class FragmentNavigationManager implements NavigationManager {

    /**
     * Singleton Instance
     */
    private static FragmentNavigationManager mInstance;

    /**
     * Manages the fragments within the app's activity.
     */
    private FragmentManager mFragmentManager;

    private Fragment currFragment;

    /**
     * Retrieves the instance of FragmentNavigationManager.
     *
     * @param activity The activity where the fragment transactions will occur.
     * @return The Singleton instance of FragmentNavigationManager.
     */
    public static FragmentNavigationManager getmInstance(AppCompatActivity activity)
    {
        if(mInstance == null)
            mInstance = new FragmentNavigationManager();

        mInstance.configure(activity);
        return mInstance;
    }

    /**
     * Configures the FragmentNavigationManager with the specified activity.
     *
     * @param activity The activity where the fragment transactions will occur.
     */
    private void configure(AppCompatActivity activity)
    {
        mFragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * Shows the specified fragment within the container layout.
     *
     * @param fragmentContent The fragment to be displayed.
     * @param addToBackStack  Whether to add the fragment transaction to the back stack.
     */
    @Override
    public void showFragment(Fragment fragmentContent, boolean b) {
        currFragment = fragmentContent;
        FragmentManager fm = mFragmentManager;
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.FrameLayout_container, fragmentContent);
        ft.addToBackStack(null);

        if(b)
            ft.commitAllowingStateLoss();
        else
            ft.commit();

        fm.executePendingTransactions();
    }

    public Fragment getCurrFragment() {
        return currFragment;
    }
}
