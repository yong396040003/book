package book.yong.cn.book.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.pojo.Book;

public class BookListBaseAdapter extends BaseAdapter {
    private Context context;
    //json转换为实体类
    private List<Book> books;
    private Book book;

    //img
    private ImageView img;
    //book_name
    private TextView book_name;
    //category_author
    private TextView category_author;
    //synopsis
    private TextView synopsis;
    //number
    private TextView number;

    /**
     * 有参构造
     *
     * @param context
     * @param books
     */
    public BookListBaseAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        if (books != null) {
            return books.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.book_list, null);
        }
        img = convertView.findViewById(R.id.img);
        book_name = convertView.findViewById(R.id.book_name);
        category_author = convertView.findViewById(R.id.category_author);
        synopsis = convertView.findViewById(R.id.synopsis);
        number = convertView.findViewById(R.id.number);

        book = books.get(position);
        if (position + 1 <= 3) {
            number.setTextColor(Color.rgb(255, 255, 255));
            number.setBackgroundResource(R.drawable.round);
        } else if (3 < position + 1 && position + 1 <= 10) {
            number.setTextColor(Color.rgb(0, 0, 0));
            number.setBackgroundResource(R.drawable.roud1);
        } else {
            number.setTextColor(Color.rgb(0, 0, 0));
            number.setBackgroundResource(R.drawable.round2);
        }
        number.setText(position + 1 + "");
        img.setImageBitmap(book.getImg());
        book_name.setText(book.getName());
        category_author.setText(book.getCategory() + " | " + book.getAuthor());
        if ("        ".equals(book.getSynopsis())) {
            synopsis.setText("无");
        } else {
            synopsis.setText(book.getSynopsis());
        }
        return convertView;
    }
}
