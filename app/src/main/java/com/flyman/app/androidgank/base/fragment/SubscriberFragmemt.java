package com.flyman.app.androidgank.base.fragment;

import com.flyman.app.androidgank.base.presenter.BasePresenter;
import com.flyman.app.util.fragment.LazyFragment;

public abstract class SubscriberFragmemt<T extends BasePresenter> extends LazyFragment{
    protected T mPresenter;
    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null)
        {
            mPresenter.onViewDestroy();
        }
    }
}
