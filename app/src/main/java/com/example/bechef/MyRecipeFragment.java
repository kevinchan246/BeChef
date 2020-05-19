package com.example.bechef;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyRecipeFragment extends Fragment {


    private View v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_recipe, container, false);
        configureImageButton();
        return v;

    }

    //method to configure the imageButtons
    private void configureImageButton(){
        ImageButton create = (ImageButton) v.findViewById(R.id.add_recipe);

        create.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MyRecipeFragment.this.getActivity(), CreateRecipeActivity.class);
                startActivity(intent);
            }
        });
    }


}
