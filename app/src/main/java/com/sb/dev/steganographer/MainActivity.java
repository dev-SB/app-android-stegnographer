package com.sb.dev.steganographer;


import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity
    {
        private Fragment mFragment;
        private FragmentManager mFragmentManager;
        private static long back_pressed;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                BottomNavigationView navigationView = findViewById(R.id.navigation);
                navigationView.inflateMenu(R.menu.menu_navigation);
                mFragmentManager = getSupportFragmentManager();
                mFragment = new EncodeFragment();
                changeFragment();
                navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
                    {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item)
                            {
                                int id = item.getItemId();
                                switch (id)
                                    {
                                        case R.id.nav_bar_encode:
                                            mFragment = new EncodeFragment();
                                            break;
                                        case R.id.nav_bar_decode:
                                            mFragment = new DecodeFragment();
                                            break;

                                    }
                                changeFragment();

                                return true;
                            }
                    });

            }


        private void changeFragment()
            {
                final FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.frame, mFragment).commit();
            }

        @Override
        public void onBackPressed()
            {

                if (back_pressed + 2000 >= System.currentTimeMillis())
                    {
                        super.onBackPressed();
                    } else
                    {
                        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();
                    }
                back_pressed = System.currentTimeMillis();
            }
    }
