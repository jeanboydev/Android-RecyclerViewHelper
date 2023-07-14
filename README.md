## RecyclerViewHelper

## 介绍

方便快捷的 RecyclerView 工具类，支持添加自定义的 header，footer 布局，加载 tips，分页加载。

## 使用

-  导入 component_pagination
- 具体使用

```java
    
// 使用 helper 实现分页加载和加载中的提示
paginationHelper = new PaginationHelper(list_container, listAdapter);
// 设置加载中 View
paginationHelper.setMaskLayout(new MaskLayout());
// 设置加载更多 View
paginationHelper.setLoadMoreLayout(new FooterLayout());

// 设置刷新的接口
paginationHelper.setRefreshListener(new LoadListener() {
    @Override
    public void onLoad(boolean isError) {
        if (isError) {
            toLoadData();
        } else {
            toLoadData();
        }
    }
});

// 加载中
paginationHelper.setLoading();
// 加载失败
paginationHelper.setLoadError();
// 加载成功，是否还有下一页，没有下一页则显示为：没有更多数据
paginationHelper.setLoadCompleted(true);
```


## Demo

![演示][1]

## 感谢

* [CymChad / BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* [cundong / HeaderAndFooterRecyclerView](https://github.com/cundong/HeaderAndFooterRecyclerView)

## 关于我

如果对你有帮助，请 star 一下，然后 follow 我，给我增加一下分享动力，谢谢！

如果你有什么疑问或者问题，可以提交 issue 和 request，发邮件给我 jeanboy@foxmail.com 。



[1]: https://github.com/freekite/Android-RecyclerViewHelper/blob/master/resource/ScreenRecord.gif
