package book.yong.cn.book.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.myInterface.RecyclerViewOnItemClickListener;
import book.yong.cn.book.pojo.Classify;

public class ClassifyGridViewAdapter extends RecyclerView.Adapter<ClassifyGridViewAdapter.InnerHolder> {
    private Context context;
    private List<Classify> classifyList;

    public ClassifyGridViewAdapter(Context context, List<Classify> classifyList) {
        this.context = context;
        this.classifyList = classifyList;
    }


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_classify_grid, null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerHolder innerHolder, int i) {
        innerHolder.setDate(classifyList.get(i));

        View itemView = innerHolder.itemView;

        if (recyclerViewOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = innerHolder.getLayoutPosition();
                    recyclerViewOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (classifyList != null) {
            return classifyList.size();
        }
        return 0;
    }

    //声明接口
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private TextView classifyName;
        private TextView classifyCount;
        private ImageView classifyImg;
        private ImageView classifyImgBig;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);

            classifyName = itemView.findViewById(R.id.classify_name);
            classifyCount = itemView.findViewById(R.id.classify_count);
            classifyImg = itemView.findViewById(R.id.classify_img);
            classifyImgBig = itemView.findViewById(R.id.classify_img_big);
        }

        public void setDate(Classify classify) {
            classifyName.setText(classify.getClassifyName());
            classifyCount.setText(classify.getClassifyCount());
            classifyImg.setImageResource(classify.getClassifyImgId());
            classifyImgBig.setImageResource(classify.getClassifyImgIdBig());
        }
    }
}
