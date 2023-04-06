package com.example.lostandfind.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfind.activity.Main.MainDetailActivity;
import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.query.main.MainAdapterQuery;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    Context context;
    ArrayList<Post> postArrayList;
    //FirebaseStorage storage;
    MainAdapterQuery mainAdapterQuery;
    private OnLoadMoreListener onLoadMoreListener;

    /*
     * 생성자, 현재 액티비티와 그 액티비티의 리스트를 파라미터로 입력받음
     * storage 변수는 파이어베이스에 저장되어있는 이미지를 가져오기 위해 인스턴스를 가져옴
     * */
    public MainAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        //storage = FirebaseStorage.getInstance();
        mainAdapterQuery = new MainAdapterQuery(context);
    }
    /*
    * 초기화 함수, 어댑터에 저장되어있는 리스트를 전부 제거
    * */
    public void clear() {postArrayList.clear();}

    /*
    * 현재 액티비티에 아이템 객체들을 보유하는 뷰 홀더 객체 생성
    * */
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_recycler_item,parent,false);
        return new MainViewHolder(v);
    }

    /*
    *  뷰 홀더에 있는 내용들을 설정(바인딩)
    * */
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Post post = postArrayList.get(position); //ArrayList에 있는 post 내용들을 포지션(0~n)까지 하나씩 가져옴
        holder.tvTitle.setText(post.getTitle()); //객체 내에 있는 내용들 setText
        holder.tvContents.setText(post.getContents());
        holder.tvLocation.setText(post.getLocation());
        holder.tvLostDate.setText(post.getLostDate());
        holder.tvPostDate.setText(post.getPostDate());
        Log.d(TAG,"Developer: "+post.toString());

        mainAdapterQuery.getStorageImage(holder.ivImage, post);

        //Glide.with(context).load(ref).into(holder.ivImage);
        /*
        *  클릭했을 경우, MainInspectActivity로 이동
        * */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainDetailActivity.class);
                intent.putExtra("post",post);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    //뷰 홀더 객체
    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContents, tvLocation, tvPostDate, tvLostDate;
        ImageView ivImage;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContents = itemView.findViewById(R.id.tvContents);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPostDate = itemView.findViewById(R.id.tvPostDate);
            tvLostDate = itemView.findViewById(R.id.tvLostDate);
        }

    }

    //실험중인 소스코드들
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
}
