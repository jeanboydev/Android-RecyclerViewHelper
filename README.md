##RecyclerViewHelper

------

##介绍

RecyclerView的工具类，更快的实现分页加载，刚方便的实现Adapter。

##使用

* 导入lib-RecyclerViewHelper
* Adapter继承CommonAdapter<T>
```java
	public class ListAdapter extends CommonAdapter<String> {

	    public ListAdapter(Context context, List<String> dataList, int layoutId) {
	        super(context, dataList, layoutId);
	    }
	
	    @Override
	    public void convert(ViewHolder holder, String s, int position) {
	        //
	
	        holder.setText(R.id.tv_title,"设置标题");
	        holder.setImageResource(R.id.iv_thumb,R.drawable.bg);
	        //...
	    }
	}
```

* 具体实现
```java
    public class MainActivity extends AppCompatActivity {


    private RecyclerView list_container;

    private List<String> dataList;

    private ListAdapter listAdapter;


    private RecyclerViewHelper loadMoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list_container = (RecyclerView) findViewById(R.id.list_container);
        dataList = new ArrayList<>();
        listAdapter = new ListAdapter(this, dataList, R.layout.item_list);

        //设置RecyclerView的layoutManager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        loadMoreHelper = RecyclerViewHelper.build(this, list_container, layoutManager, listAdapter);

        //不传layoutManager默认为LinearLayoutManager
        loadMoreHelper = RecyclerViewHelper.build(this, list_container, listAdapter);

        loadMoreHelper.addLoadMoreListener(new RecyclerViewHelper.LoadMoreCallback() {
            @Override
            public void loadMore() {
                loadNext();
            }
        });

        initData();
    }

    private void loadNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            dataList.add(String.valueOf(i));
                        }
                        loadMoreHelper.loadComplete();//通知helper加载完成
                        loadMoreHelper.notifyDataSetChanged();//刷新数据
                        loadMoreHelper.hasNext(true);//是否还有下一页
                    }
                });
            }
        }).start();

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            dataList.add(String.valueOf(i));
        }
        listAdapter.notifyDataSetChanged();
    }
}
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

  [1]: https://github.com/freekite/Android-RecyclerViewHelper/blob/master/resource/ScreenShot.jpg
