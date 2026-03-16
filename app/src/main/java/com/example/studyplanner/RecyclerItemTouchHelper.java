package com.example.studyplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyplanner.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback
{
    private ToDoAdapter adapter;
    public RecyclerItemTouchHelper(ToDoAdapter adapter)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position=viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this Task?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(adapter.getContext(), R.color.sky_blue));

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(adapter.getContext(), R.color.sky_blue));
        }
        else {
            adapter.editItem(position);
        }


    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY,int actionState,boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView , viewHolder,dX,dY,actionState,isCurrentlyActive);
        Drawable icon;
        ColorDrawable background;
        View itemview=viewHolder.itemView;
        int backgrounfCornerOffset=20;
        if(dX>0)
        {
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_edit_24);
            background=new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.blue));
        }
        else
        {
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_auto_delete_24);
            background=new ColorDrawable(Color.RED);
        }
        int iconMargin=(itemview.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop=itemview.getTop()+(itemview.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom=iconTop+icon.getIntrinsicHeight();
        if(dX>0)
        {
            int iconleft=itemview.getLeft()+iconMargin;
            int iconright=itemview.getLeft()+iconMargin+icon.getIntrinsicWidth();
            icon.setBounds(iconleft,iconTop,iconright,iconBottom);
            background.setBounds(itemview.getLeft(),itemview.getTop(),itemview.getLeft()+((int)dX)+backgrounfCornerOffset,itemview.getBottom());
        }
        else if(dX<0)
        {//swiping left
            int iconleft=itemview.getRight()-iconMargin-icon.getIntrinsicWidth();
            int iconright=itemview.getRight()-iconMargin;
            icon.setBounds(iconleft,iconTop,iconright,iconBottom);
            background.setBounds(itemview.getRight()+((int)dX)-backgrounfCornerOffset,itemview.getTop(),itemview.getRight(),itemview.getBottom());
        }
        else
        {
            background.setBounds(0,0,0,0);
        }

    background.draw(c);
    icon.draw(c);
}
}