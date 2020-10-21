package com.example.library3.ui.userdetails;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.library3.R;

public class UserdetailsFragment extends Fragment {

    private UserdetailsViewModel userDetailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userDetailsViewModel =
                ViewModelProviders.of(this).get(UserdetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_userdetails, container, false);
        final TextView tv_Uname = root.findViewById(R.id.tv_uname);
        final TextView tv_Email = root.findViewById(R.id.tv_email);
        final TextView tv_subscription  = root.findViewById(R.id.tv_subscription);
        final TextView tv_start_date= root.findViewById(R.id.tv_start_date);
        final TextView tv_end_date= root.findViewById(R.id.tv_end_date);

        //final TextView textView = root.findViewById(R.id.text_userdetails);
        userDetailsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        userDetailsViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_Uname.setText(s);
            }
        });
        userDetailsViewModel.getUserEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_Email.setText(s);
            }
        });
        userDetailsViewModel.getStartDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_start_date.setText(s);
            }
        });
        userDetailsViewModel.getEndDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_end_date.setText(s);

            }
        });
        userDetailsViewModel.getSubscription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_subscription.setText(s);

            }
        });


        return root;
    }
}