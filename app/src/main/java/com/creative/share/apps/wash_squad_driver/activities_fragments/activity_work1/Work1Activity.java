package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work1;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_work2.Work2Activity;
import com.creative.share.apps.wash_squad_driver.databinding.ActivityWork1Binding;
import com.creative.share.apps.wash_squad_driver.databinding.DialogSelectImageBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.language.LanguageHelper;
import com.creative.share.apps.wash_squad_driver.models.Order_Model;
import com.creative.share.apps.wash_squad_driver.remote.Api;
import com.creative.share.apps.wash_squad_driver.share.Common;
import com.creative.share.apps.wash_squad_driver.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Work1Activity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityWork1Binding binding;
    private final int IMG1 = 1, IMG2 = 2, IMG3 = 3, IMG4 = 4, IMG5 = 5, IMG6 = 6, IMG7 = 7, IMG8 = 8;
    private int image_type = 0;
    private List<Uri> imageInsideList, imageOutsideList;

    private Uri uri = null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private SparseArray<Uri> imageInsideMap, imageOutsideMap;
    private boolean isImg1 = false, isImg2 = false, isImg3 = false, isImg4 = false, isImg5 = false, isImg6 = false, isImg7 = false, isImg8 = false;
    private Order_Model.Data data;
    private String lang;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, "en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work1);
        initView();
    }

    private void initView() {
        data = (Order_Model.Data) getIntent().getExtras().getSerializable("detials");
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        imageInsideMap = new SparseArray<>();
        imageOutsideMap = new SparseArray<>();

        imageInsideList = new ArrayList<>();
        imageOutsideList = new ArrayList<>();

        binding.setBackListener(this);


        binding.fl1.setOnClickListener(view -> CreateImageAlertDialog(IMG1));
        binding.fl2.setOnClickListener(view -> CreateImageAlertDialog(IMG2));
        binding.fl3.setOnClickListener(view -> CreateImageAlertDialog(IMG3));
        binding.fl4.setOnClickListener(view -> CreateImageAlertDialog(IMG4));
        binding.fl11.setOnClickListener(view -> CreateImageAlertDialog(IMG5));
        binding.fl22.setOnClickListener(view -> CreateImageAlertDialog(IMG6));
        binding.fl33.setOnClickListener(view -> CreateImageAlertDialog(IMG7));
        binding.fl44.setOnClickListener(view -> CreateImageAlertDialog(IMG8));


        binding.btnStep2.setOnClickListener(view -> {

            if (isImg1 && isImg2 && isImg3 && isImg4 && isImg5 && isImg6 && isImg7 && isImg8) {
                addImageInside();
                addImageOutside();
                step1();

            } else {
                if (!isImg1) {
                    Toast.makeText(this, "Choose tire image", Toast.LENGTH_SHORT).show();
                }

                if (!isImg2) {
                    Toast.makeText(this, "Choose front image", Toast.LENGTH_SHORT).show();
                }
                if (!isImg3) {
                    Toast.makeText(this, "Choose back image", Toast.LENGTH_SHORT).show();
                }
                if (!isImg4) {
                    Toast.makeText(this, "Choose trunk image", Toast.LENGTH_SHORT).show();
                }
                if (!isImg5) {
                    Toast.makeText(this, "Choose roof image", Toast.LENGTH_SHORT).show();
                }
                if (!isImg6) {
                    Toast.makeText(this, "Choose front seats image", Toast.LENGTH_SHORT).show();
                }
                if (!isImg7) {
                    Toast.makeText(this, "Choose back seats image", Toast.LENGTH_SHORT).show();
                }
                if (!isImg8) {
                    Toast.makeText(this, "Choose trunk image", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(Work1Activity.this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void step1() {
        final Dialog dialog = Common.createProgressDialog(Work1Activity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody id_part = Common.getRequestBodyText(data.getId() + "");

        RequestBody time_part = Common.getRequestBodyText((Calendar.getInstance().getTimeInMillis() / 1000) + "");
Log.e("msg",imageInsideList.size()+" "+imageOutsideList.size()+" "+data.getId());
        List<MultipartBody.Part> partimageInsideList = getMultipartBodyList(imageInsideList, "order_images_in[]");
        List<MultipartBody.Part> partimageOutsideList = getMultipartBodyList(imageOutsideList, "order_images_out[]");
        try {
            Api.getService(lang, Tags.base_url)
                    .Step1(id_part, time_part, partimageInsideList, partimageOutsideList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(Work1Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                        //  adsActivity.finish(response.body().getId_advertisement());
                        Intent intent = new Intent(Work1Activity.this, Work2Activity.class);
                        intent.putExtra("detials",data);

                        startActivityForResult(intent, 1003);
                        finish();
                    } else {
                        try {

                            Toast.makeText(Work1Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(Work1Activity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }

    private void addImageInside() {
        for (int i = 0; i < imageInsideMap.size(); i++) {
            imageInsideList.add(imageInsideMap.get(i));
        }
    }

    private void addImageOutside() {
        for (int i = 0; i < imageOutsideMap.size(); i++) {
            imageOutsideList.add(imageOutsideMap.get(i));
        }
    }

    private void CreateImageAlertDialog(int img_req) {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            image_type = 1;
            Check_CameraPermission(img_req);

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            image_type = 2;
            Check_ReadPermission(img_req);


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(this, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{read_permission}, img_req);
        } else {
            select_photo(2, img_req);
        }
    }


    private void Check_CameraPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, img_req);
        } else {
            select_photo(1, img_req);

        }

    }

    private void select_photo(int type, int img_req) {

        Intent intent = new Intent();

        if (type == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (type == 1) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);

            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            binding.icon1.setVisibility(View.GONE);
            isImg1 = true;
            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageInsideMap.put(0, uri);

                Picasso.with(this).load(uri).fit().into(binding.image1);


            } else if (image_type == 2) {

                uri = data.getData();
                imageInsideMap.put(0, uri);

                Picasso.with(this).load(uri).fit().into(binding.image1);


            }


        } else if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon2.setVisibility(View.GONE);
            isImg2 = true;

            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageInsideMap.put(1, uri);


                Picasso.with(this).load(uri).fit().into(binding.image2);

            } else if (image_type == 2) {
                uri = data.getData();
                imageInsideMap.put(1, uri);

                Picasso.with(this).load(uri).fit().into(binding.image2);


            }

        } else if (requestCode == IMG3 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon3.setVisibility(View.GONE);
            isImg3 = true;


            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageInsideMap.put(2, uri);


                Picasso.with(this).load(uri).fit().into(binding.image3);


            } else if (image_type == 2) {


                uri = data.getData();
                imageInsideMap.put(2, uri);

                Picasso.with(this).load(uri).fit().into(binding.image3);

            }

        } else if (requestCode == IMG4 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon4.setVisibility(View.GONE);
            isImg4 = true;

            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageInsideMap.put(3, uri);

                Picasso.with(this).load(uri).fit().into(binding.image4);


            } else if (image_type == 2) {

                uri = data.getData();
                imageInsideMap.put(3, uri);

                Picasso.with(this).load(uri).fit().into(binding.image4);


            }

        } else if (requestCode == IMG5 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon11.setVisibility(View.GONE);
            isImg5 = true;

            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageOutsideMap.put(0, uri);
                Picasso.with(this).load(uri).fit().into(binding.image11);


            } else if (image_type == 2) {


                uri = data.getData();
                imageOutsideMap.put(0, uri);
                Picasso.with(this).load(uri).fit().into(binding.image11);

            }

        } else if (requestCode == IMG6 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon22.setVisibility(View.GONE);
            isImg6 = true;

            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageOutsideMap.put(1, uri);

                Picasso.with(this).load(uri).fit().into(binding.image22);


            } else if (image_type == 2) {

                uri = data.getData();
                imageOutsideMap.put(1, uri);

                Picasso.with(this).load(uri).fit().into(binding.image22);


            }

        } else if (requestCode == IMG7 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon33.setVisibility(View.GONE);
            isImg7 = true;

            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageOutsideMap.put(2, uri);

                Picasso.with(this).load(uri).fit().into(binding.image33);


            } else if (image_type == 2) {


                uri = data.getData();
                imageOutsideMap.put(2, uri);

                Picasso.with(this).load(uri).fit().into(binding.image33);

            }

        } else if (requestCode == IMG8 && resultCode == Activity.RESULT_OK && data != null) {

            binding.icon44.setVisibility(View.GONE);
            isImg8 = true;

            if (image_type == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = getUriFromBitmap(bitmap);
                imageOutsideMap.put(3, uri);

                Picasso.with(this).load(uri).fit().into(binding.image44);


            } else if (image_type == 2) {

                uri = data.getData();
                imageOutsideMap.put(3, uri);

                Picasso.with(this).load(uri).fit().into(binding.image44);

            }

        } else if (requestCode == 1003 && resultCode == RESULT_OK && data != null) {
            int reason = data.getIntExtra("reason", 0);
            Intent intent = getIntent();
            if (intent != null) {
                intent.putExtra("reason", 1);
                setResult(RESULT_OK, intent);
            }
            if (reason == 1) {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1 || requestCode == IMG2 || requestCode == IMG3 || requestCode == IMG4 || requestCode == IMG5 || requestCode == IMG6 || requestCode == IMG7 || requestCode == IMG8) {
            if (grantResults.length > 0) {

                if (image_type == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        select_photo(1, requestCode);

                    } else {
                        Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                    }

                } else if (image_type == 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        select_photo(2, requestCode);
                    } else {
                        Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    @Override
    public void back() {
        finish();
    }
}
