package com.lishi.adruino.randompoetry.ui.dictionary.view;

import java.util.List;

/*
 * 每个View都只能控制一个Fragment
 */
public interface DetailedPoetryView {
    void toPoetryFragment(List<Object> list);
}
