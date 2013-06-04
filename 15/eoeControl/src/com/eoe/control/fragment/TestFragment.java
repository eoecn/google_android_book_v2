package com.eoe.control.fragment;

import com.eoe.control.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment{
	
	private String newContent;
	
    public static TestFragment newInstance(String content) {
    	
        TestFragment newFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        newFragment.setArguments(bundle);
        return newFragment;

    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        newContent = args != null ? args.getString("content") : "default hello";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentlayout, container, false);
        TextView viewhello = (TextView) view.findViewById(R.id.content);
        viewhello.setText(newContent);
        return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
