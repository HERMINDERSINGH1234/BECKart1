package com.example.beckart.view;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.beckart.R;
import com.example.beckart.ViewModel.AddProductViewModel;
import com.example.beckart.databinding.ActivityAddProductBinding;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.utils.Constant.PICK_IMAGE;
import static com.example.beckart.utils.Constant.READ_EXTERNAL_STORAGE_CODE;
import static com.example.beckart.utils.ImageUtils.getRealPathFromURI;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, LifecycleOwner {

    private static final String TAG = "AddProductActivity";
    private ActivityAddProductBinding binding;
    private AddProductViewModel addProductViewModel;
    private Uri selectedImage;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.add_product));

        addProductViewModel = ViewModelProviders.of(this).get(AddProductViewModel.class);

        binding.btnSelectImage.setOnClickListener(this);

        populateSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            addProduct(filePath);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addProduct(String pathname) {
        String nameString = binding.txtName.getText().toString().trim();
        String priceString = binding.txtPrice.getText().toString().trim();
        String quantityString = binding.txtQuantity.getText().toString().trim();
        String supplierString = binding.txtSupplier.getText().toString().trim();
        String categoryString = binding.categorySpinner.getSelectedItem().toString().toLowerCase();

        // Check if there are no empty values
        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierString)
                || TextUtils.isEmpty(categoryString)) {
            Toast.makeText(this, getString(R.string.required_data), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, RequestBody> map = new HashMap<>();
        map.put("name", toRequestBody(nameString));
        map.put("price", toRequestBody(priceString));
        map.put("quantity", toRequestBody(quantityString));
        map.put("supplier", toRequestBody(supplierString));
        map.put("category", toRequestBody(categoryString));

        if(TextUtils.isEmpty(pathname)){
            Toast.makeText(this, "No picture is chosen", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pathname
        File file = new File(pathname);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        addProductViewModel.addProduct(map, photo).observe(this, responseBody -> {
            try {
                if (responseBody != null) {
                    Toast.makeText(AddProductActivity.this, responseBody.string() + "", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(adapter);
    }

    private void getImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AddProductActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    AddProductActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
            } else {

                try {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE);
                } catch (Exception exp) {
                    Log.i("Error", exp.toString());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            binding.imageOfProduct.setImageURI(selectedImage);

            filePath = getRealPathFromURI(this, selectedImage);
            Log.d(TAG, "onActivityResult: " + filePath);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSelectImage) {
            getImageFromGallery();
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }
}
