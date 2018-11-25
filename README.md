# minimap-view

A minimap view library for Android. The miniature map is usually placed in the corner of the screen, to help the user in orientig himself in a screen with a big Recycler View. Check the example by cloning the repo and starting the example app.

## Screenshots

## How to use
The library use androidX dependencies, if you do too use this
```
implementation 'eu.acolombo:minimap:1.0.0'
```
If you still use support libraries and have not transitioned yet to Android X, I got you covered:
```
implementation 'eu.acolombo:minimap-support:1.0.0'
```

### Get started
Add the minimap in your layout customizing the properties you want:
```
<com.fusaimoe.minimap.MinimapView
    android:id="@+id/minimapView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="@dimen/activity_margin"
    app:minimapBackgroundColor="@color/colorMinimap"
    app:minimapBorderWidth="3dp"
    app:minimapCornerRadius="5dp"
    app:minimapMaxSize="160dp"/>
```
Then add it to your Recycler view in this way:
```
recyclerView.minimap(minimapView)
```
If the minimap extension method is not found, add this in your imports:
```
import com.fusaimoe.minimap.MinimapView.Companion.minimap
```

## Docs
![scheme](docs/scheme.svg)

### Credits
- [Devunwired's FixedGridLayoutManager](https://github.com/devunwired/recyclerview-playground) - Layout Manager used in the example
- [Freepik's Car Vector](https://www.freepik.com/free-vector/top-view-of-flat-cars-on-parking-lot_1349624.htm) - Vector graphics used in the example
