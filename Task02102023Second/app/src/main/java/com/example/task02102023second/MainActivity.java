package com.example.task02102023second;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private int fragmentCount = 0;

    public void incrementFragmentCount() {
        fragmentCount++;
        ((TextView) findViewById(R.id.fragmentCounterText)).setText(getResources().getString(R.string.fragment_counter_text, fragmentCount));
    }

    public void decrementFragmentCount() {
        fragmentCount--;
        ((TextView) findViewById(R.id.fragmentCounterText)).setText(getResources().getString(R.string.fragment_counter_text, fragmentCount));
    }

    public static class FragmentDetailsButton extends Fragment {

        public static final String ARG_TEXT = "text";

        public static FragmentDetailsButton create(String text) {
            FragmentDetailsButton fragmentDetailsButton = new FragmentDetailsButton();
            Bundle args = new Bundle();
            args.putString(ARG_TEXT, text);
            fragmentDetailsButton.setArguments(args);
            return fragmentDetailsButton;
        }

        public FragmentDetailsButton() {
            super(R.layout.fragment_details_button);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            Button button = view.findViewById(R.id.button);
            String text = getArguments().getString(ARG_TEXT);
            button.setText(text);

            button.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.container_item, FragmentDetailsText.create(text)).addToBackStack("BLA").commit());
            return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.getRootView().findViewById(R.id.backButton).setVisibility(View.GONE);
        }

        @Override
        public void onResume() {
            super.onResume();
            ((MainActivity) getActivity()).incrementFragmentCount();
        }

        @Override
        public void onPause() {
            super.onPause();
            ((MainActivity) getActivity()).decrementFragmentCount();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

    }

    public static class FragmentDetailsText extends Fragment {

        public static final String ARG_TEXT = "text";

        public static FragmentDetailsText create(String text) {
            FragmentDetailsText fragmentDetailsButton = new FragmentDetailsText();
            Bundle args = new Bundle();
            args.putString(ARG_TEXT, text);
            fragmentDetailsButton.setArguments(args);
            return fragmentDetailsButton;
        }

        public FragmentDetailsText() {
            super(R.layout.fragment_details_text);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            TextView textView = view.findViewById(R.id.text);
            textView.setText(getArguments().getString(ARG_TEXT));
            return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.getRootView().findViewById(R.id.backButton).setVisibility(View.VISIBLE);
        }

        @Override
        public void onResume() {
            super.onResume();
            ((MainActivity) getActivity()).incrementFragmentCount();
        }

        @Override
        public void onPause() {
            super.onPause();
            ((MainActivity) getActivity()).decrementFragmentCount();
        }
    }

    public static class FragmentMenu extends Fragment {

        public FragmentMenu() {
            super(R.layout.fragment_menu);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.findViewById(R.id.button_1).setOnClickListener(view1 -> {
                ((MainActivity) getActivity()).show("Text 1");
            });
            view.findViewById(R.id.button_2).setOnClickListener(view1 -> {
                ((MainActivity) getActivity()).show("Text 2");
            });
            view.findViewById(R.id.button_3).setOnClickListener(view1 -> {
                ((MainActivity) getActivity()).show("Text 3");
            });

            return view;
        }

        @Override
        public void onResume() {
            super.onResume();
            ((MainActivity) getActivity()).incrementFragmentCount();
        }

        @Override
        public void onPause() {
            super.onPause();
            ((MainActivity) getActivity()).decrementFragmentCount();
        }
    }

    @Override
    public void onBackPressed() {

        findViewById(R.id.backButton).setVisibility(View.GONE);
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            Resources res = getResources();
            boolean isPortrait = res.getBoolean(R.bool.is_portrait);
            if (isPortrait) {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void show(String text) {
        getSupportFragmentManager().popBackStack("BLA", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_item, FragmentDetailsButton.create(text)).addToBackStack("BLA").commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.fragmentCounterText)).setText(getResources().getString(R.string.fragment_counter_text, fragmentCount));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_list, new FragmentMenu()).commit();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                show("111111");
            }
        }

        (findViewById(R.id.backButton)).setOnClickListener(view1 -> {
            onBackPressed();
        });
    }
}