# TallerApp

This is an app that displays thumbnails from the tops of Reddit with a title right under that thumbnail. If the user wants to make a manual call, I put a SwipeRefreshLayout to reload the information from the reddit api! When the user reaches the end of the list, the app automatically searches for a new "shipment" of items.
If you click in a picture, the app will save it in the gallery of the app.
I could have done more features (like the other ones suggested) but I chose to do unit tests in the viewModel.

Stack that was used:

- Kotlin
- Android DataBinding
- Koin
- AndroidX
- Retrofit2.0
- RxJava2
- RecycleView
- MVVM
- LiveData
- Glide
- Android ViewModel
- Mockk
- Android JetPack
- SwipeRefreshLayout
- Endless Scrolling
