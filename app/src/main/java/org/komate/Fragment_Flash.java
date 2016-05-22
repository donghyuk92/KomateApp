package org.komate;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gellston on 2016-05-21.
 */
public class Fragment_Flash extends Fragment {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flash, container,false);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                //ft.setCustomAnimations( R.anim.leftin, 0, 0, R.anim.leftout);
                ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft.replace(R.id.mainframe, new Fragment_Webview()).commit();
            }
        }, SPLASH_TIME_OUT);


        return view;
    }
}
