## RecyclerViewHelper

------

## 介绍

方便快捷的 RecyclerView 工具类，支持添加自定义的 header，footer 布局，加载 tips，分页加载。

## 使用

* 导入lib-RecyclerViewHelper
* 具体实现
```java
    
		//使用helper实现分页加载和加载的Tips
        recyclerViewHelper = new RecyclerViewHelper(list_container, listAdapter);

        //设置没有数据的Tips
        recyclerViewHelper.setTipsEmptyView(R.layout.view_data_empty);
        //设置加载中的Tips
        recyclerViewHelper.setTipsLoadingView(R.layout.view_data_loading);
        //设置加载失败的Tips
        recyclerViewHelper.setTipsErrorView(R.layout.view_data_error);
        //设置header
        recyclerViewHelper.setHeaderView(R.layout.view_header);

        //默认加载更多 footer 也可自定义
        recyclerViewHelper.useDefaultFooter();

        //加载失败，没有数据时Tips的接口
        recyclerViewHelper.setTipsListener(new TipsListener() {
            @Override
            public void retry() {
                //重新加载操作
            }
        });

        //加载更多的接口
        recyclerViewHelper.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                //加载下一页操作
            }
        });

		recyclerViewHelper.setOnViewBindListener(new OnViewBindListener() {
            @Override
            public void onBind(RecyclerView.ViewHolder holder, int viewType) {
                Log.d(MainActivity.class.getName(), "==============onBind============");
                if (ViewType.TYPE_HEADER == viewType) {
                    // TODO: 2017/7/13 header view bind
                } else if (ViewType.TYPE_FOOTER == viewType) {
                    // TODO: 2017/7/13 footer view bind
                }
            }
        });

        recyclerViewHelper.setFooterChangeListener(new OnFooterChangeListener() {
            @Override
            public void onChange(RecyclerView.ViewHolder holder, int state) {
                Log.d(MainActivity.class.getName(), "==============onChange============");
                if (FooterState.LOADING == state) {
                    // TODO: 2017/7/13 加载中
                } else if (FooterState.ERROR == state) {
                    // TODO: 2017/7/13 加载失败
                } else if (FooterState.NO_MORE == state) {
                    // TODO: 2017/7/13 加载完成
                }
            }
        });

		/*加载成功
		 *
		 *1. 若没有数据则显示 tips 没有数据
		 *2. 若已有数据且 hasMore=true 刷新 adapter
		 *3. 若已有数据且 hasMore=fale 刷新 footer 显示加载完成
		 */
        recyclerViewHelper.loadComplete(hasMore);
		
		/*加载失败
		 *
		 *1. 若没有数据则显示 tips 加载失败
		 *2. 若已有数据则显示加载失败
		 */
        recyclerViewHelper.loadError();

		//
		/*手动设置加载中（极少使用）
		 *
		 *1. 若没有数据则显示 tips 加载中...
		 *2. 若已有数据则显示 footer 加载中...
		 */
        recyclerViewHelper.loadStart();

```




## Demo

![演示][1]

## 感谢

* [CymChad / BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* [cundong / HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)

## 关于我

* Mail: jeanboy@foxmail.com

## License

    Copyright 2015 jeanboy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  [1]: https://github.com/freekite/Android-RecyclerViewHelper/blob/master/resource/ScreenRecord.gif
