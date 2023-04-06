package com.example.lostandfind.activity.Main;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
import com.example.lostandfind.query.main.MainCreateQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainCreateActivity extends AppCompatActivity {
    private final static String TAG = "MainCreateActivity";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    Toolbar toolbar;

    TextView lostDate, confirm_btn;
    Spinner spinner;

    ImageView lostDate_btn, img, image1;
    ImageButton delete_image; // 이미지 삭제 버튼
    private Uri imageUri;
    String imageName = "", name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_create);

        initializeView();

        storage = FirebaseStorage.getInstance();

        setActionbar();
        setCategory();
        getUserData();

        confirm_btn.setOnClickListener(onClickListener);
        lostDate_btn.setOnClickListener(onClickListener);

//        toast("image name1: " + imageName);
        // 이미지
        img.setOnClickListener(onClickListener);
        delete_image.setOnClickListener(onClickListener);
    }

    // 버튼 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.confirm_btn: // 등록
                    postRegister();
                    break;
                case R.id.lostDate_btn: // 날짜 선택
                    setCalendar();
                    break;
                case R.id.select_image: // 이미지 선택
                    setImage();
                    break;
                case R.id.delete_image: // 이미지 삭제
                    delImage();
                    break;
            }
        }
    };

    // view 초기화 find view by id
    public void initializeView()
    {
        toolbar = findViewById(R.id.toolbar);
        confirm_btn = (TextView)findViewById(R.id.confirm_btn);
        lostDate = (TextView)findViewById(R.id.lostDate);
        lostDate_btn = (ImageView)findViewById(R.id.lostDate_btn);
        spinner = (Spinner)findViewById(R.id.category);

        img = findViewById(R.id.select_image);
        image1 = findViewById(R.id.image1);
        delete_image = findViewById(R.id.delete_image);
    }

    // actionbar 관련 설정
    private void setActionbar(){
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    // 현재 시각
    private String getCurTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String getTime = dateFormat.format(date);

        return getTime;
    }

    // 앨범에서 이미지 선택
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

    // 카테고리 관련 설정
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

    // 날짜 선택 관련 설정
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

    // 사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();      // Uri 추출
                        imageName = imageUri.getLastPathSegment();  // 파일 path에서 파일명만 가져오기
                        image1.setImageURI(imageUri);
                        //toast("image name: " + imageName);

                        // 사진 선택하면 이미지 보이게
                        image1.setVisibility(View.VISIBLE);
                        delete_image.setVisibility(View.VISIBLE);
                    }
                }
            }
    );

    // 이미지 업로더
    private void imageUpload(Uri imageUri, String imageName) {

    }

    // 게시글 업데이트
    private void postRegister(){
        final String title = ((EditText) findViewById(R.id.title)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contents)).getText().toString();
        final String location = ((EditText) findViewById(R.id.location)).getText().toString();
        final String lostDate = ((TextView) findViewById(R.id.lostDate)).getText().toString();
        final String postDate = getCurTime();
        final String category = spinner.getSelectedItem().toString();
        final String userName = name;
        final String n_image = imageName;
        //toast("image Name3: "+imageName);

        if (title.length() > 0 && contents.length() > 0) {
            Post post = new Post(n_image, title, category, location, lostDate, postDate, contents, user.getEmail(), userName, user.getUid());
            uploader(post);
            finish();
        } else {
            toast("제목과 내용을 입력하세요.");
        }
    }

    //게시글 업로더
    private void uploader(Post post){
        if (imageName.length() > 0){
            StorageReference storageRef = storage.getReference();
            StorageReference photoRef = storageRef.child("photo/"+imageName);
            UploadTask uploadTask  = photoRef.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"Developer Error Log: Image Upload Error",e);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(com.example.lostandfind.activity.Main.MainCreateActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                }
            });
        }

        db.collection("Posts").add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        toast("게시글 등록 성공!");
                        Log.d(TAG, "document written by ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toast("게시글 등록 실패!");
                        Log.w(TAG, "Error lost post document", e);
                    }
                });
    }

    //유저 닉네임 get
    public void getUserData() {
        DocumentReference dRef = db.collection("Users").document(user.getUid());
        dRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            name = (documentSnapshot.toObject(UserData.class).getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Developer: Error: ",e);
                    }
                });
    }


    // 타이틀 메뉴의 'x' 클릭 시
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

    // 토스트 메소드 간략화
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