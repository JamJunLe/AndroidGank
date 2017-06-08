package com.flyman.app.androidgank.contract;

import com.flyman.app.androidgank.base.presenter.IListPresneter;
import com.flyman.app.androidgank.base.view.IListView;

public interface DailyContract {
    interface View extends IListView {};
    interface Presenter extends IListPresneter{};
}
