package com.example.bechef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRecipeTab1 extends Fragment {


    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;


    private TextView tapToTakePhoto;
    private ImageView prepImage;
    private Uri image_uri;
    private EditText addIngredientEdText;
    private ListView ingredientListView;
    private ArrayList<String> ingredientList;
    private ArrayAdapter<String> arrayAdapter;


    public CreateRecipeTab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_create_recipe_tab1, null);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tapToTakePhoto = (TextView) view.findViewById(R.id.add_photo_id);
        prepImage = (ImageView) view.findViewById(R.id.prep_image);

        prepImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED){
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permission
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera();
                    }
                }
                else {
                    //system os < marshmallow
                    openCamera();
                }
            }
        });

        ingredientListView = (ListView) view.findViewById(R.id.ingredient_list);
        Button addIngredientBtn = (Button) view.findViewById(R.id.add_ingredients_btn);
        addIngredientEdText = (EditText) view.findViewById(R.id.add_ingredients_editText);

        ingredientList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), android.R.layout.simple_list_item_1, ingredientList);

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient=addIngredientEdText.getText().toString();
                ingredientList.add(ingredient);
                ingredientListView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                addIngredientEdText.setText("");
            }
        });

        ingredientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemSelected = position;
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_close_grey_24dp)
                        .setTitle("Delete item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ingredientList.remove(itemSelected);
                                arrayAdapter.notifyDataSetChanged();
                                Toast deletedMsg = Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT);
                                deletedMsg.show();
                            }
                        })
                        .setNegativeButton("No", null).show();
                return true;
            }
        });


        CreateRecipeActivity.FragmentTouchListener myTouchListener = new CreateRecipeActivity.FragmentTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    ingredientListView.dispatchTouchEvent(event);
                }
            }
        };
        ((CreateRecipeActivity) this.getActivity()).registerFragmentTouchListener(myTouchListener);



    }





    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = Objects.requireNonNull(getActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }


    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called when user tap Allow or Deny from Permission Request Popup
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getActivity(), "Permission denied", LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //called when image was captured from camera
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CreateRecipeActivity.RESULT_OK){
            prepImage.setImageURI(image_uri);
            tapToTakePhoto.setText("You can tap again to retake the photo");
        }
    }
}
