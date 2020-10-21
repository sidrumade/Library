package com.example.library3.ui.about;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.library3.R;

public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;
    TextView tv_devname=null;
    TextView tv_devemail = null;
    TextView tv_devcontact = null;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.about_fragment, container, false);
        tv_devname = root.findViewById(R.id.about_devname);
        tv_devemail = root.findViewById(R.id.about_devemail);
        tv_devcontact = root.findViewById(R.id.about_devcontact);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getDevname().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_devname.setText(s);
            }
        });
        mViewModel.getDevEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_devemail.setText(s);
            }
        });
        mViewModel.getDevContact().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_devcontact.setText(s);
            }
        });

    }

}