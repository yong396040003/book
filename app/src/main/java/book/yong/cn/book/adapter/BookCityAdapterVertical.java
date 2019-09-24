package book.yong.cn.book.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.BookDetailActivity;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.pojo.Book;

/**
 * bookCity垂直适配器
 *
 * @author yong
 * @time 2019/8/19 17:16
 */
public class BookCityAdapterVertical extends RecyclerView.Adapter<BookCityAdapterVertical.InnerHolder> {
    private Context context;
    private List<Book> bookList;

    public BookCityAdapterVertical(Context context, List<Book> bookList) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.book_city_book, null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder innerHolder, final int i) {
        innerHolder.setData(bookList.get(i));
        View view = innerHolder.itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, BookDetailActivity.class);
                JSONObject jsonObject = JsonUtil.pojoToJson(bookList.get(i));
                intent.putExtra("book", jsonObject.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (bookList != null) {
            return bookList.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private ImageView imageView;
        private TextView name;
        private TextView author_category;
        private TextView synopsis;

        InnerHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.book_city_v);
            imageView = itemView.findViewById(R.id.book_city_img);
            name = itemView.findViewById(R.id.book_city_name);
            author_category = itemView.findViewById(R.id.book_city_author_category);
            synopsis = itemView.findViewById(R.id.book_city_synopsis);

            linearLayout.setVisibility(View.VISIBLE);
        }

        public void setData(Book book) {
            imageView.setImageBitmap(book.getImg());
            name.setText(book.getName());
            String authorAndCategory = book.getAuthor() + " | " + book.getCategory();
            author_category.setText(authorAndCategory);
            synopsis.setText(book.getSynopsis());
        }
    }
}
