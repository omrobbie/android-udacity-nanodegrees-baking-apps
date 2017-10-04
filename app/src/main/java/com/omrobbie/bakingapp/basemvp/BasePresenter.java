package com.omrobbie.bakingapp.basemvp;

public interface BasePresenter<T extends BaseView> {
    void onAttach(T BaseView);
    void onDetach();
}
