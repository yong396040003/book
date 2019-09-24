package book.yong.cn.book.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Bookshelf;

public class BookshelfBaseAdapter extends BaseAdapter {
    private List<Bookshelf> bookshelves;

    private Context context;

    public BookshelfBaseAdapter(Context context, List<Bookshelf> bookshelves) {
        this.context = context;
        this.bookshelves = bookshelves;
    }

    @Override
    public int getCount() {
        return bookshelves.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.bookshelf_list, null);
        }

        ImageView imageView = convertView.findViewById(R.id.img);
        TextView name = convertView.findViewById(R.id.name);
        TextView catalogue = convertView.findViewById(R.id.catalogue);
        TextView data = convertView.findViewById(R.id.data);

        if (bookshelves.get(position).getBitmap() != null) {
            imageView.setImageBitmap(bookshelves.get(position).getBitmap());
        }
        name.setText(bookshelves.get(position).getName());
        catalogue.setText(bookshelves.get(position).getCatalogue());
        data.setText(bookshelves.get(position).getData());

        return convertView;
    }
}
