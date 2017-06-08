package com.flyman.app.androidgank.base.activity;

import com.flyman.app.androidgank.base.presenter.BasePresenter;

import butterknife.ButterKnife;

public abstract class SubscriberActivity<T extends BasePresenter> extends BaseActivity{
    protected T mPresenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
        {
            mPresenter.onViewDestroy();
        }
        ButterKnife.unbind(this);
    }
}
