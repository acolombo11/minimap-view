# minimap-view

A minimap view library for Android RecyclerView.

The miniature map is usually placed in the corner of the screen, to help the user in orienting himself in a screen with a big scrolling view.
Check the example by cloning the repo and starting the demo app, or by downloading the [Release](https://github.com/acolombo25/minimap-view/releases) APK.

The example shown in the demo app is a resizable parking lot, showing a few scenarios of the library responding to resizes:

<img src="docs/screenshots/ex-1.jpg" width="360"> <img src="docs/screenshots/ex-2.jpg" width="360">

## Download &nbsp; [![Release](https://jitpack.io/v/eu.acolombo/minimap-view.svg)](https://jitpack.io/#eu.acolombo/minimap-view)

Add the dependency in your app  `build.gradle` with the current version number:
```gradle
implementation 'eu.acolombo:minimap-view:1.0.3'
```

Add JitPack in your root `build.gradle`:
```gradle
allprojects {
    repositories {
        ..
        maven { url 'https://jitpack.io' }
    }
}
```

Another option is to copy the single file [MinimapView.kt](minimap-view/src/main/java/eu/acolombo/minimap/MinimapView.kt) directly in your project.

## Usage
Add the minimap in your layout customizing the properties you need:
> XML
```xml
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
Then link it to your RecyclerView:
> Kotlin
```kotlin
recyclerView.minimap(minimapView)
```
> Java
```java
MinimapView minimapView = findViewById(R.id.minimapView)
minimapView.setRecyclerView(recyclerView)
```

For the library to work properly, the <mark>LayoutManager used in your RecyclerView should implement computeHorizontalScrollRange and computeVerticalScrollRange</mark>, not all LayoutManagers do by default.

## Docs

The MinimapView indicator matches the size of the visible area of the RecycleView, while its background matches the size of the scrollable area of the RecyclerView. Both parts of the MinimapView will auto-update when the RecyclerView visible size or scrollable size will change. The calculations to have the indicator and the background match all the possible cases are not that trivial. To keep ratios and positions correct, there are many different scenarios you have to think about, for example when the size of the RecyclerView is bigger than its scrollable area. So I made a scheme with all the measurement names:

<img src="docs/scheme.svg" width="600">

### State

For the moment the library lets you select one max size, and the MinimapView width or height will have to stay inside that size, keeping its form factor. Instead of one max size, having both a max-height and a max-width would be ideal. Also it would be cool to support not only RecyclerView but other scrolling views.

## Thanks
- [Devunwired](https://github.com/devunwired/recyclerview-playground) for the Layout Manager used in the example app
- [Freepik](https://www.freepik.com/free-vector/top-view-of-flat-cars-on-parking-lot_1349624.htm) for the vector graphics used in the example app

### Projects using minimap-view
- [mywellness](https://play.google.com/store/apps/details?id=com.technogym.mywellness): [Dark theme](docs/screenshots/mw-b-1.png), [Light theme](docs/screenshots/mw-w-1.png)
- Let me know if you're using it in you project 
