package com.sb.dev.steganographer;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MainActivity extends AppCompatActivity
    {
        private Fragment mFragment;
        private FragmentManager mFragmentManager;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                BottomNavigationView navigationView= findViewById(R.id.navigation);
                navigationView.inflateMenu(R.menu.menu_navigation);
                mFragmentManager=getSupportFragmentManager();

                navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                        {
                            int id=item.getItemId();
                            switch (id)
                                {
                                    case R.id.nav_bar_recents:
                                        mFragment=new RecentsFragment();
                                        break;
                                    case R.id.nav_bar_encode:
                                        mFragment=new EncodeFragment();
                                        break;
                                    case R.id.nav_bar_decode:
                                        mFragment=new DecodeFragment();
                                        break;

                                }
                            final FragmentTransaction transaction=mFragmentManager.beginTransaction();
                                    transaction.replace(R.id.frame,mFragment).commit();
                            return true;
                        }
                });
            }
    }
