##RecyclerViewHelper

------

##介绍

RecyclerView的工具类，更方便的实现Adapter，item点击事件，更快的实现加载提示，分页加载。

##使用

* 导入lib-RecyclerViewHelper
* Adapter继承CommonAdapter<T>
```java
	public class ListAdapter extends CommonAdapter<String> {

	    public ListAdapter(List<String> dataList) {
	        super(dataList, R.layout.item_list);//设置item layout
	    }
	
	    @Override
	    public void convert(ViewHolder holder, String s, int position) {
	        //
	
	        holder.setText(R.id.tv_title,"设置标题");
	        holder.setImageResource(R.id.iv_thumb,R.drawable.bg);

			or

			holder.setText(R.id.tv_title,"设置标题")
				  .setImageResource(R.id.iv_thumb,R.drawable.bg);
	        //...
	    }
	}
```

* 具体实现
```java
    
		//使用helper实现分页加载和加载的Tips
        recyclerViewHelper = new RecyclerViewHelper(recyclerView, listAdapter);

        //设置没有数据的Tips
        recyclerViewHelper.setTipsEmptyView(R.layout.view_data_empty);
        //设置加载中的Tips
        recyclerViewHelper.setTipsLoadingView(R.layout.view_data_loading);
        //设置加载失败的Tips
        recyclerViewHelper.setTipsErrorView(R.layout.view_data_error);

        //加载失败，没有数据时Tips的接口
        recyclerViewHelper.setTipsListener(new TipsListener() {
            @Override
            public void retry() {
                //重新加载操作
            }
        });

        //设置header
		recyclerViewHelper.setHeaderView(R.layout.view_header);

        //加载更多的接口
        recyclerViewHelper.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                //加载下一页操作
            }
        });



		//首次加载数据成功
        recyclerViewHelper.loadTipsComplete();
        //首次数据记载失败
        recyclerViewHelper.loadTipsError();


		//分页数据加载失败
        recyclerViewHelper.loadMoreError();
		//分页数据加载成功，还有下一页
        recyclerViewHelper.loadMoreFinish(true);
        //分页数据加载成功，没有更多。即全部加载完成
        recyclerViewHelper.loadMoreFinish(false);

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
