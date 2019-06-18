# minimap-view

A minimap view library for Android. The miniature map is usually placed in the corner of the screen, to help the user in orienting himself in a screen with a big Recycler View. Check the example by cloning the repo and starting the example app.

## Screenshots

Example app (parking lot) showing different scenarios and sizes:

![big parking lot](docs/screenshots/Screenshot_20181212-231326.png)
![small parking lot](docs/screenshots/Screenshot_20181212-231340.png)

## Download   [![Release](https://jitpack.io/v/eu.acolombo/minimap-view.svg)](https://jitpack.io/#eu.acolombo/minimap-view)
Add JitPack in your root `build.gradle` if you haven't done so already
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency in your app  `build.gradle`
```
implementation 'eu.acolombo:minimap-view:0.1.1'
```

This library is AndroidX only, if you are still using Support libraries you can either migrate your app to AndroidX or you can contribute by downgrading the dependencies and subitting a pull-request, which will be merged in a different branch.

### Get started
Add the minimap in your layout customizing the properties you need
```
<eu.acolombo.minimap.MinimapView
    android:id="@+id/minimapView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="@dimen/activity_margin"
    app:minimapBackgroundColor="@color/colorMinimap"
    app:minimapBorderWidth="3dp"
    app:minimapCornerRadius="5dp"
    app:minimapMaxSize="160dp"/>
```
Then link it to your RecyclerView
```
recyclerView.minimap(minimapView)
```
Or, if you're using Java, do it this way
```
MinimapView minimapView = findViewById(R.id.minimapView)
minimapView.setRecyclerView(recyclerView)
```

For the library to work properly, the LayoutManager used in your RecyclerView should implement computeHorizontalScrollRange and computeVerticalScrollRange, not all of them do by default.

## Docs

The MinimapView indicator matches the size of the visible area of the RecycleView, while its background matches the size of the scrollable area of the RecyclerView. Both parts of the MinimapView will auto-update when the RecyclerView visible size or scrollable size will change. The calculations to have the indicator and the background match all the possible cases are not that trivial. To keep ratios and positions correct, there are many different scenarios you have to think about, for example when the size of the RecyclerView is bigger than its scrollable area. So I made a scheme with all the measurement names
![scheme](docs/scheme.svg)

For the moment the library lets you select one max size, and the MinimapView width or height will have to stay inside that size, keeping its form factor.

### Credits
- [Devunwired's FixedGridLayoutManager](https://github.com/devunwired/recyclerview-playground) - Layout Manager used in the example app
- [Freepik's Car Vector](https://www.freepik.com/free-vector/top-view-of-flat-cars-on-parking-lot_1349624.htm) - Vector graphics used in the example app

### Projects using minimap-view
- [mywellness](https://play.google.com/store/apps/details?id=com.technogym.mywellness) : [dark](docs/screenshots/Screenshot_20181212-000709.png) , [light](docs/screenshots/Screenshot_1544703153.png)
