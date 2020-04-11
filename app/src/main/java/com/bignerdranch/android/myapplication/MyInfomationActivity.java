package com.bignerdranch.android.myapplication;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by T540p on 2017/8/10.
 */

public class MyInfomationActivity extends AppCompatActivity{
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private TextView mPetName,mSex,mBirthday,mSchool,mAcademy,mMajor;
    private LinearLayout mPetNamel,mSexl,mBirthdayl,mSchooll,mAcademyl,mMajorl,mProPhoto;
    private EditText mGrade;
    boolean permit=true;
    private String myInfoString;
    int flag,flag1;
    private String usernameID,imgurl;
    private CircleImageView picture;
    private Uri imageUri;
    private File finalFile=new File(Environment.getExternalStorageDirectory()+"/"+usernameID+".jpg");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_light));
        }
        setContentView(R.layout.personal_information);

        ExitApplication.getInstance().addActivity(this);

        initViews();

        Bmob.initialize(this,"15901fbabf4589c6bf0c5d7d4a75de92");

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        usernameID=sharedPreferences.getString("usernameID","defaultname");
        imgurl=sharedPreferences.getString("imgurl","null");

        loadPetPhoto();
        getInfo();

        mProPhoto.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             showChoosePicDialog();
                                         }
                                     });

        mPetNamel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mAlertDialog=(LinearLayout)getLayoutInflater().inflate(R.layout.alert_dialog_petname,null);
                TextView mDispalyText1=(TextView)mAlertDialog.findViewById(R.id.tv_display_text1);
                TextView mDisplayText2=(TextView)mAlertDialog.findViewById(R.id.tv_display_text2);
                TextView mTvDialogPetname=(TextView)mAlertDialog.findViewById(R.id.tv_petname_dialog);
                final EditText mEdDialogPetName=(EditText)mAlertDialog.findViewById(R.id.et_petname_dialog);
                mTvDialogPetname.setText(mPetName.getText().toString());
                mDispalyText1.setText("原昵称");
                mDisplayText2.setText("新昵称");

                AlertDialog.Builder builder =new AlertDialog.Builder(MyInfomationActivity.this);
                builder.setTitle("请输入昵称");
                builder.setView(mAlertDialog);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPetName.setText(mEdDialogPetName.getText().toString());
                        saveInfo(1);
                        Toast.makeText(MyInfomationActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create();
                builder.show();
            }
        });

        mSexl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfomationActivity.this);
                builder.setTitle("请选择性别");
                final String[] sex = {"男", "女"};
                myInfoString=mSex.getText().toString();
                for(flag=0;flag<2;flag++){
                    if(sex[flag].equals(myInfoString)){
                        flag1=flag;
                    }
                }
                builder.setSingleChoiceItems(sex, flag1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(MyInfomationActivity.this, "性别为：" + sex[which], Toast.LENGTH_SHORT).show();
                        mSex.setText(sex[which]);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        saveInfo(2);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mSex.setText(sex[flag1]);
                    }
                });
                builder.create();
                builder.show();
            }
        });

        mBirthdayl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                Dialog dialog=null;
                DatePickerDialog.OnDateSetListener dateListener=new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker,int year,int month,int dayOfMonth){
                        month++;
                        mBirthday.setText(year+"."+month+"."+dayOfMonth);
                        saveInfo(3);
                    }
                };
                dialog=new DatePickerDialog(MyInfomationActivity.this,dateListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        mSchooll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfomationActivity.this);
                builder.setTitle("请选择学校校区");
                final String[] school = {"川大江安", "川大望江", "川大华西"};
                myInfoString=mSchool.getText().toString();
                for(flag=0;flag<3;flag++){
                    if(school[flag].equals(myInfoString)){
                        flag1=flag;
                    }
                }
                builder.setSingleChoiceItems(school, flag1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(MyInfomationActivity.this, "校区为：" + school[which], Toast.LENGTH_SHORT).show();
                        mSchool.setText(school[which]);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        saveInfo(4);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mSchool.setText(school[flag1]);
                    }
                });
                builder.show();
            }
        });

        mAcademyl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mAlertDialog=(LinearLayout)getLayoutInflater().inflate(R.layout.alert_dialog_petname,null);
                TextView mDispalyText1=(TextView)mAlertDialog.findViewById(R.id.tv_display_text1);
                TextView mDisplayText2=(TextView)mAlertDialog.findViewById(R.id.tv_display_text2);
                TextView mTvDialogPetname=(TextView)mAlertDialog.findViewById(R.id.tv_petname_dialog);
                final EditText mEdDialogPetName=(EditText)mAlertDialog.findViewById(R.id.et_petname_dialog);
                mTvDialogPetname.setText(mAcademy.getText().toString());
                mDispalyText1.setText("原学院");
                mDisplayText2.setText("新学院");
                AlertDialog.Builder builder =new AlertDialog.Builder(MyInfomationActivity.this);
                builder.setTitle("请输入学院");
                builder.setView(mAlertDialog);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAcademy.setText(mEdDialogPetName.getText().toString());
                        saveInfo(5);
                        Toast.makeText(MyInfomationActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create();
                builder.show();
            }
        });


        mMajorl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mAlertDialog=(LinearLayout)getLayoutInflater().inflate(R.layout.alert_dialog_petname,null);
                TextView mDispalyText1=(TextView)mAlertDialog.findViewById(R.id.tv_display_text1);
                TextView mDisplayText2=(TextView)mAlertDialog.findViewById(R.id.tv_display_text2);
                TextView mTvDialogPetname=(TextView)mAlertDialog.findViewById(R.id.tv_petname_dialog);
                final EditText mEdDialogPetName=(EditText)mAlertDialog.findViewById(R.id.et_petname_dialog);
                mTvDialogPetname.setText(mMajor.getText().toString());
                mDispalyText1.setText("原专业");
                mDisplayText2.setText("新专业");

                AlertDialog.Builder builder =new AlertDialog.Builder(MyInfomationActivity.this);
                builder.setTitle("请输入专业");
                builder.setView(mAlertDialog);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMajor.setText(mEdDialogPetName.getText().toString());
                        saveInfo(6);
                        Toast.makeText(MyInfomationActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create();
                builder.show();
            }
        });

    }

    private void initViews(){
        mAcademy=(TextView)findViewById(R.id.tv_academy);
        mPetName=(TextView)findViewById(R.id.tv_petName1);
        mSchool=(TextView)findViewById(R.id.tv_school);
        mSex=(TextView)findViewById(R.id.tv_sex);
        mMajor=(TextView)findViewById(R.id.tv_major);
        mBirthday=(TextView)findViewById(R.id.tv_birthday);
        mAcademyl=(LinearLayout)findViewById(R.id.ll_academy);
        mPetNamel=(LinearLayout)findViewById(R.id.ll_petName1);
        mSchooll=(LinearLayout)findViewById(R.id.ll_school);
        mSexl=(LinearLayout)findViewById(R.id.ll_sex);
        mMajorl=(LinearLayout)findViewById(R.id.ll_major);
        mBirthdayl=(LinearLayout)findViewById(R.id.ll_birthday);
        mGrade=(EditText)findViewById(R.id.ev_grade);
        mProPhoto=(LinearLayout)findViewById(R.id.ll_prophoto);
        picture=(CircleImageView)findViewById(R.id.pet_picture);
    }

    private void loadPetPhoto(){
            Glide.with(this).load(imgurl).error(R.mipmap.person).into(picture);
    }

    private void showChoosePicDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(MyInfomationActivity.this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE: // 选择本地照片
                        File outputImage=new File(getExternalCacheDir(),"outputImage.jpg");
                        try{
                            if(outputImage.exists()){
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT>=24){
                            imageUri= FileProvider.getUriForFile(MyInfomationActivity.this,"com.bignerdranch.android.camera.fileprovider",outputImage);
                        }
                        else{
                            imageUri=Uri.fromFile(outputImage);
                        }
                        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PICTURE);
                        break;
                    case CHOOSE_PICTURE:// 拍照
                        if(ContextCompat.checkSelfPermission(MyInfomationActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MyInfomationActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }
                        else{
                            openAlbum();
                        }
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case TAKE_PICTURE:
                if(resultCode==RESULT_OK){
                    startPhotoZoom(imageUri);
                }
                break;
            case CHOOSE_PICTURE:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case RESULT_REQUEST_CODE:
                if(data!=null){
                    displayImage(data);
                    upload(finalFile);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PICTURE);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        startPhotoZoom(uri);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        startPhotoZoom(uri);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(Intent data){
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            picture.setImageDrawable(drawable);
        }
    }

    private void startPhotoZoom(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

//        if(finalFile.exists()){
//            finalFile.delete();
//        }
        intent.putExtra("output",Uri.fromFile(finalFile));
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void upload(File file){
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    User user=new User();
                    user.setPersonalIcon(bmobFile);
                    user.update(usernameID,new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(MyInfomationActivity.this,"头像上传成功",Toast.LENGTH_SHORT).show();
                                        imgurl=bmobFile.getFileUrl();
                                        getShared();
                                    }
                                    else{
                                        Toast.makeText(MyInfomationActivity.this,"头像上传失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }else{
                    Toast.makeText(MyInfomationActivity.this,"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.myinformation_interface,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当点击返回键以及点击重复次数为0
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 执行事件
            setResult(RESULT_OK);
            finish();

        }
        return false;
    }
    private void saveInfo(int choose){
        if(usernameID.equals("defaultname")){
            Toast.makeText(MyInfomationActivity.this,"defaultname",Toast.LENGTH_SHORT).show();
        }
        else{
            User user=new User();
            switch(choose){
                case 1:
                    user.setPetName(mPetName.getText().toString());
                    break;
                case 2:
                    user.setSex(mSex.getText().toString());
                    break;
                case 3:
                    user.setBirthday(mBirthday.getText().toString());
                    break;
                case 4:
                    user.setSchool(mSchool.getText().toString());
                    break;
                case 5:
                    user.setAcademy(mAcademy.getText().toString());
                    break;
                case 6:
                    user.setMajor(mMajor.getText().toString());
                    break;
            }
            user.setGrade(mGrade.getText().toString());
            user.update(usernameID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("bmob","更新成功");
                                SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("petName",mPetName.getText().toString()).commit();
                            }
                            else{
                                Log.i("bmob","更新失败");
                            }
                        }
                    });
        }
    }

    private void getInfo(){
        BmobQuery<User> query=new BmobQuery<User>();
        query.getObject(usernameID, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    mPetName.setText(user.getPetName());
                    mSex.setText(user.getSex());
                    mBirthday.setText(user.getBirthday());
                    mSchool.setText(user.getSchool());
                    mAcademy.setText(user.getAcademy());
                    mMajor.setText(user.getMajor());
                    mGrade.setText(user.getGrade());
                }
                else{
                    Log.i("bmob","得到个人资料失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void getShared(){
        Glide.with(this).load(imgurl).error(R.mipmap.default_personal_image).into(picture);
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("imgurl",imgurl).commit();

    }
}
