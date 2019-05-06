# FabMenu

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/kiathee/maven/fabmenu/images/download.svg) ](https://bintray.com/kiathee/maven/fabmenu/_latestVersion)

This library can fast and easy develop floating action button menu function.
* Sub menu easy to add

ScreenShot
----------------
![ScreenShot](https://github.com/cheekiat/FabMenu/blob/master/appscreenshort.gif)

Kotlin Code
----------------
        fabMenu.addItem(R.drawable.ic_add_a_photo_black_24dp, android.R.color.holo_orange_light)
        fabMenu.addItem(R.drawable.ic_call_black_24dp, android.R.color.holo_orange_light)
        fabMenu.addItem(R.drawable.ic_content_copy_black_24dp, android.R.color.holo_orange_light)

        fabMenu.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {

                Toast.makeText(this@MainActivity, "position " + position, Toast.LENGTH_SHORT).show()
            }
        })

        
Xml Code
----------------
```
   <com.cheekiat.fabmenu.FabMenu
            android:id="@+id/fabMenu"
            android:layout_height="match_parent"
            android:layout_width="wrap_content"
            app:collapseIcon="@drawable/ic_close_white_24dp"
            app:expandIcon="@drawable/ic_add_white_24dp"
            app:space="16dp"
            app:fabBackgroundColor="#ff00ff"/>
```
        
How to use?
----------------
### Java
| Public methods | Description |
| ------------- | ------------- |
| addItem(resId: Int, backgroundColor: Int) | Add sub menu button. |
| setOnItemClickListener(listener: OnItemClickListener) | Add sub button on click listener. |
| setDuration(duration: Long) | Set sub menu display animation duration. |

### Xml
| XML attributes | Description |
| ------------- | ------------- |
| app:fabBackgroundColor | Set fab background color. |
| app:collapseIcon | Set fab collapse icon. |
| app:expandIcon | Set fab expand icon. |
| app:space | Add sub menu space. |

Download
----------------
```
repositories {
  mavenCentral() // jcenter() works as well because it pulls from Maven Central
}

dependencies {
 implementation 'com.cheekiat:fabmenu:1.1'
}
```
