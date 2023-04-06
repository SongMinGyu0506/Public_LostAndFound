package com.example.lostandfind.activity.Main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main2.Main2UpdateActivity;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

public class MainUpdateActivity extends AppCompatActivity {
    private final static String TAG = "MainUpdateActivity";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Post post;

    TextView lostDate, postDate, confirm_btn;
    EditText title, contents, location;
    Spinner spinner;
    ImageView lostDate_btn, img, image1;
    ImageButton delete_image; // 이미지 삭제 버튼
    private Uri imageUri;
    String imageName;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_update);

        initializeView(); // view 초기화
        setActionbar();     // Actionbar 관련 설정
        setCategory();  // category 관련 설정
        getIntentData();    // 넘어오는 Intent data get
        setOriPost();   // 원래 포스트에서 입력됐던 값 set

        confirm_btn.setOnClickListener(onClickListener);
        lostDate_btn.setOnClickListener(onClickListener);
        img.setOnClickListener(onClickListener);
        delete_image.setOnClickListener(onClickListener);
    }

    //버튼 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.confirm_btn:
                    updatePost();
                    break;
                case R.id.lostDate_btn:
                    setCalendar();
                    break;
                case R.id.select_image:
                    setImage();
                    break;
                case R.id.delete_image: // 이미지 삭제
                    delImage();
                    break;
            }
        }
    };

    private void setOriPost() {
        title.setText(post.getTitle());
        contents.setText(post.getContents());
        location.setText(post.getLocation());
        lostDate.setText(post.getLostDate());

        int index;
        switch(post.getCategory()){
            case "가방":
                index = 0;
                break;
            case "귀금속":
                index = 1;
                break;
            case "도서":
                index = 2;
                break;
            case "스포츠용품":
                index = 3;
                break;
            case "악기":
                index = 4;
                break;
            case "의류":
                index = 5;
                break;
            case "전자기기":
                index = 6;
                break;
            case "지갑":
                index = 7;
                break;
            case "카드":
                index = 8;
                break;
            case "현금":
                index = 9;
                break;
            case "휴대폰":
                index = 10;
                break;
            case "기타":
                index = 11;
                break;
            default:
                index = 11;
                break;
        }
        spinner.setSelection(index);

        imageName = post.getImage();
        // 이미지 없는 글이면 수정 페이지에서 invisible
        if (imageName == "") {
            image1.setVisibility(View.INVISIBLE);
            delete_image.setVisibility(View.INVISIBLE);
        }
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("photo/"+post.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(image1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image1.setImageResource(R.drawable.kumoh_symbol);
            }
        });
    }

    private void updatePost() {
        String upTitle, upContents, upLocation, upLostDate, upPostDate, upCategory, upName, upImageName, upWriterUID, upWriterEmail;

        upTitle = title.getText().toString();
        upContents = contents.getText().toString();
        upLocation = location.getText().toString();
        upLostDate = lostDate.getText().toString();
        upCategory = spinner.getSelectedItem().toString();
        upPostDate = post.getPostDate();    // 바꾸지 않음
        upName = post.getName();            // 바꾸지 않음
        upImageName = post.getImage();      // 일단 default 원래 올렸던 이미지
        upWriterUID = post.getWriterUID();  // 바꾸지 않음
        upWriterEmail = post.getWriterEmail();

        //원래 올렸던 이미지 != 새로 올린 이미지
        if (post.getImage() != imageName && imageName != "") {
            StorageReference storageRef = storage.getReference();
            StorageReference photoRef = storageRef.child("photo/" + imageName);
            UploadTask uploadTask = photoRef.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Developer Error Log: Image Upload Error", e);

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainUpdateActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                }
            });
            upImageName = imageName;    //새 이미지 이름
        }
        // 이미지 삭제한 경우
        if (imageName == "")
            upImageName = "";

        Post upPost = new Post(upImageName, upTitle, upCategory, upLocation, upLostDate, upPostDate, upContents, upWriterEmail, upName, upWriterUID);

        if (title.length() > 0 && contents.length() > 0) {

            db.collection("Posts").document(post.getId())
                    .set(upPost)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            toast("게시글 수정 완료");
                        }
                    });

            finish();
        }
    }

    public void initializeView()
    {
        toolbar = findViewById(R.id.toolbar);
        title = (EditText)findViewById(R.id.title);
        contents = (EditText)findViewById(R.id.contents);
        location = (EditText)findViewById(R.id.location);
        lostDate = (TextView)findViewById(R.id.lostDate);
        spinner = (Spinner)findViewById(R.id.category);
        img = (ImageView)findViewById(R.id.select_image);
        image1 = findViewById(R.id.image1);
        delete_image = findViewById(R.id.delete_image);

        confirm_btn = (TextView)findViewById(R.id.confirm_btn);
        lostDate_btn = (ImageView)findViewById(R.id.lostDate_btn);
    }

    private void setActionbar(){
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    private void getIntentData(){
        Intent intent = getIntent();
        post = (Post)intent.getSerializableExtra("upDatePost");
    }

    //카테고리 관련 설정
    private void setCategory(){
        ArrayList<String> categoryArr = new ArrayList<String>();
        categoryArr.add("가방");
        categoryArr.add("귀금속");
        categoryArr.add("도서");
        categoryArr.add("스포츠용품");
        categoryArr.add("악기");
        categoryArr.add("의류");
        categoryArr.add("전자기기");
        categoryArr.add("지갑");
        categoryArr.add("카드");
        categoryArr.add("현금");
        categoryArr.add("휴대폰");
        categoryArr.add("기타");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, categoryArr);
        spinner.setAdapter(adapter);
    }

    //날짜 선택 관련 설정
    private void setCalendar(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog;

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // 달력 아이콘 눌러서 선택한 날짜를 텍스트뷰에 바인딩
                lostDate.setText(year + "/" + (month+1) + "/" + day);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    //앨범에서 이미지 선택
    private void setImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activityResult.launch(intent);
    }

    // 불러온 이미지 삭제
    private void delImage() {
        imageName = "";
        image1.setVisibility(View.INVISIBLE);
        delete_image.setVisibility(View.INVISIBLE);
    }

    //사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();      // Uri 추출
                        imageName = imageUri.getLastPathSegment();  // 파일 path에서 파일명만 가져오기
                        image1.setImageURI(imageUri);

                        image1.setVisibility(View.VISIBLE);
                        delete_image.setVisibility(View.VISIBLE);
                    }
                }
            }
    );

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //토스트 메소드 간략화
    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // EditText가 아닌 다른 곳 터치 시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}