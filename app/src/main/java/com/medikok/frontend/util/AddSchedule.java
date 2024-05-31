package com.medikok.frontend.util;

import static android.os.Environment.DIRECTORY_PICTURES;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.medikok.frontend.R;
//import com.medikok.frontend.util.MediaScanner;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddSchedule extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    //private String resultImagePath;

    //private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이 것이 필요하다(미디어 스캐닝)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.
        //mMediaScanner = MediaScanner.getInstance(getApplicationContext());


        // 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("카메라 권한이 필요합니다.")
                    .setDeniedMessage("거부하셨습니다.")
                    .setPermissions(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA)
                    .check();
        } else {
            TedPermission.create()
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("카메라 권한이 필요합니다.")
                    .setDeniedMessage("거부하셨습니다.")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .check();
        }

        findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {

                    }

                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);
            } else {
                exifDegree = 0;
            }

            String result = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());
            String filename = formatter.format(curDate);

            String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "MEDIKOK" + File.separator;
            File file = new File(strFolderName);
            if (!file.exists())
                file.mkdirs();

            File f = new File(strFolderName + "/" + filename + ".jpeg");
            result = f.getPath();

            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = "Save Error fOut";
            }

            Bitmap bitImage;
            bitImage = rotate(bitmap, exifDegree);
            // 비트맵 사진 폴더 경로에 저장
            bitImage.compress(Bitmap.CompressFormat.JPEG, 70, fOut);

            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();

                // 방금 저장된 사진을 갤러리 폴더 반영 및 최신화
                //mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".jpg");
            } catch (IOException e) {
                e.printStackTrace();
                result = "File close Error";
            }
            // 갤러리에 새 파일을 추가하도록 미디어 스캐너 호출
            MediaScannerConnection.scanFile(context, new String[]{f.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
            new UploadImageTask().execute(bitImage);

            // 이미지 뷰에 비트맵을 set하여 이미지 표현
            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> List) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };

    private static class UploadImageTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            Bitmap bitmap = bitmaps[0];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);


            // 스프링 부트 서버로 이미지를 전송하는 코드 작성
            try {
                URL url = new URL("http://172.30.76.184:8080/mysql-request/saveImage");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("AddSchedule", "URL 이용");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{\"image\":\"" + base64Image + "\"}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                if (code == 200) {
                    Log.d("AddSchedule", "성공");
                    return "Image uploaded successfully";
                } else {
                    Log.d("AddSchedule", code + " : 실패 코드");
                    return "Failed to upload image: " + code;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("AddSchedule", "오류");
                return "Exception: " + e.getMessage();
            }
        }
    }
}