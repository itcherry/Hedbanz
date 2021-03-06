package com.transcendensoft.hedbanz.presentation.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */
public abstract class MvpViewHolder<P extends BasePresenter> extends RecyclerView.ViewHolder {
    protected P presenter;

    public MvpViewHolder(View itemView) {
        super(itemView);
    }

    public void bindPresenter(P presenter){
        this.presenter = presenter;
        presenter.bindView(this);
    }

    public void unbindPresenter(){
        presenter = null;
    }
}
