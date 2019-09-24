package book.yong.cn.book.adapter;

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
import book.yong.cn.book.pojo.Bookshelf;

/**
 * 书架grid适配器
 *
 * @author yong
 * @time 2019/8/3 10:42
 */
public class BookshelfGridAdapter extends RecyclerView.Adapter<BookshelfGridAdapter.InnerHolder> {
    private List<Bookshelf> bookshelfList;

    //图片点击
    private ImageView imageView;
    //图片bg
    private ImageView[] imageBg;
    //图片选中
    private ImageView[] imageSelect;
    //是否长按
    public Boolean isOnLong = false;
    //是否全选
    public Boolean isSelectAll = false;

    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    public BookshelfGridAdapter(List<Bookshelf> bookshelfList) {
        this.bookshelfList = bookshelfList;
        imageBg = new ImageView[bookshelfList.size()];
        imageSelect = new ImageView[bookshelfList.size()];
    }

    @NonNull
    @Override
    public BookshelfGridAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_bookshelf_grid, null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookshelfGridAdapter.InnerHolder innerHolder, final int i) {
        final View itemView = innerHolder.itemView;
        final int position = bookshelfList.size() - i - 1;

        if (i == bookshelfList.size()) {
            innerHolder.setAddBook();
            imageView = innerHolder.itemView.findViewById(R.id.bookshelf_click);
        } else {
            innerHolder.setDate(bookshelfList.get(position));
        }

        if (position > -1) {
            imageBg[position] = itemView.findViewById(R.id.bookshelf_batch_img);
            imageSelect[position] = itemView.findViewById(R.id.bookshelf_batch_select);
        }

        //避免recyclerView的缓存机制对用户操作的影响
        if (isOnLong) {
            setBatchImgSelect();
            setLast();
        }
        if (isSelectAll) {
            setSelectAll();
        }

        if (recyclerViewOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position > -1 && isOnLong) {
                        if (imageSelect[position].isSelected()) {
                            imageBg[position].setSelected(false);
                            imageSelect[position].setSelected(false);
                        } else {
                            imageBg[position].setSelected(true);
                            imageSelect[position].setSelected(true);
                        }
                        recyclerViewOnItemClickListener.onItemClick(v, position + 10000);
                    }
                    if (!isOnLong) {
                        if (imageView != null) {
                            imageView.setVisibility(View.VISIBLE);
                        }
                        recyclerViewOnItemClickListener.onItemClick(v, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (position > -1) {
                        //默认长按的被选中
                        imageBg[position].setSelected(true);
                        imageSelect[position].setSelected(true);
                        recyclerViewOnItemClickListener.onLongItemClick(v, position);
                    }
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (bookshelfList != null) {
            return bookshelfList.size() + 1;
        }
        return 0;
    }

    /**
     * 批量选择操作
     */
    public void setBatchImgSelect() {
        for (int i = 0; i < imageSelect.length; i++) {
            if (imageSelect[i] != null && imageBg[i] != null) {
                imageSelect[i].setVisibility(View.VISIBLE);
                imageBg[i].setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 取消
     */
    public void setCancelSelect() {
        for (int i = 0; i < imageSelect.length; i++) {
            if (imageSelect[i] != null && imageBg[i] != null) {
                imageSelect[i].setVisibility(View.GONE);
                imageBg[i].setVisibility(View.GONE);
            }
        }
    }

    public void setLast() {
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * 全选
     */
    public void setSelectAll() {
        for (int i = 0; i < imageSelect.length; i++) {
            if (imageSelect[i] != null && imageBg[i] != null) {
                imageBg[i].setSelected(true);
                imageSelect[i].setSelected(true);
            }
        }
    }

    /**
     * 取消全选
     */
    public void setNotSelectAll() {
        for (int i = 0; i < imageSelect.length; i++) {
            if (imageSelect[i] != null && imageBg[i] != null) {
                imageBg[i].setSelected(false);
                imageSelect[i].setSelected(false);
            }
        }
    }

    /**
     * 获取选中数量
     *
     * @return
     */
    public int getSelectCount() {
        int count = 0;
        for (int i = 0; i < bookshelfList.size(); i++) {
            if (imageSelect[i] != null && imageSelect[i].isSelected()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取选中数量
     *
     * @return
     */
    public int[] getSelectArray() {
        int[] array = new int[bookshelfList.size()];
        int count = 0;
        for (int i = 0; i < bookshelfList.size(); i++) {
            if (imageSelect[i] != null && imageSelect[i].isSelected()) {
                array[count] = i;
                count++;
            } else if (imageSelect[i] != null && count < bookshelfList.size()) {
                array[count] = -1;
                count++;
            }
        }
        return array;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        InnerHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bookshelf_Img);
            textView = itemView.findViewById(R.id.book_name);
        }

        public void setDate(Bookshelf bookshelf) {
            imageView.setImageBitmap(bookshelf.getBitmap());
            textView.setText(bookshelf.getName());
        }

        public void setAddBook() {
            imageView.setImageResource(R.mipmap.add);
            textView.setVisibility(View.GONE);
        }
    }
}
