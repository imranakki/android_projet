# Android Mobile Development Course Outline

## Chapter 1: Introduction & Environment
* [cite_start]**Mobile Development Overview:** The course focuses on mobile programming using Android, covering lifecycle, UI, intents, web services, and distributed architectures[cite: 680, 681, 692, 693].
* [cite_start]**Mobile Constraints:** Developing for mobile requires constant optimization regarding CPU, battery life, interactive interfaces, memory, storage, and network resources[cite: 777, 778, 780, 784, 786].
* [cite_start]**Android OS Architecture:** Android is based on a Linux kernel and utilizes a hardware abstraction layer (HAL), native libraries (like SQLite and OpenGL ES), an application framework, and a virtual machine (historically Dalvik) designed for mobile optimization[cite: 819, 820, 821, 822, 823, 825].
* [cite_start]**Development Environment:** Android Studio is the official IDE, replacing the older Eclipse+ADT plugin[cite: 876, 884]. 
* [cite_start]**Project Build:** Projects rely on Gradle for automating the build process, managing dependencies, and specifying the target/minimum SDK versions[cite: 1074, 1075, 1076, 1078].

## Chapter 2: Project Structure & Lifecycles
* [cite_start]**Manifest File (`AndroidManifest.xml`):** Declares essential app information, including the package name, required permissions (e.g., internet, camera), and all application components[cite: 468, 469, 470, 471, 473].
* [cite_start]**Application Components:** Apps are built using Activities (screens), Services (background tasks), Content Providers (data sharing), Broadcast Receivers (listening to system/app messages), and Intents (communication)[cite: 497, 498, 499, 501, 502].
* [cite_start]**Resource Management:** Static elements are stored in `res/` (e.g., `drawable` for images, `layout` for UI, `values` for strings and colors) and are accessed in Java via the generated `R` static class[cite: 484, 487, 488, 492, 529, 530].
* [cite_start]**Internationalization:** Multi-language support is achieved by creating localized resource folders (e.g., `values-fr`, `values-ar`) for strings[cite: 550, 551, 553].
* [cite_start]**Activity Lifecycle:** Activities transition through specific states managed by methods: `onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()`, `onDestroy()`, and `onRestart()`[cite: 610, 612, 613, 615].
* [cite_start]**State Preservation:** The `Bundle` object in `onCreate()` and the `onSaveInstanceState()` method are used to save and restore UI data if the OS unloads the activity to save memory[cite: 661, 662, 667].

## Chapter 3: Graphical Interfaces (UI)
* [cite_start]**Views & ViewGroups:** UI elements inherit from the `View` class (e.g., `TextView`, `EditText`, `Button`, `ImageView`), while containers inherit from `ViewGroup`[cite: 9, 18, 19, 21, 23, 26, 28].
* **Layout Managers (Gabarits):**
    * [cite_start]`LinearLayout`: Arranges elements horizontally or vertically[cite: 11].
    * [cite_start]`RelativeLayout`: Positions elements relative to each other or the parent[cite: 12, 70].
    * [cite_start]`FrameLayout`: Stacks elements on top of each other, anchoring to the top-left[cite: 13].
    * [cite_start]`ConstraintLayout`: Highly flexible layout optimized for the visual editor, using relative and circular constraints[cite: 14, 138, 142].
* [cite_start]**Layout Attributes:** Sizing is controlled via `match_parent` (fills parent container) and `wrap_content` (takes minimum necessary space)[cite: 53, 57].
* [cite_start]**Measurement Units:** `dp` (Density-independent Pixels) is used for layout dimensions to adapt to different physical screen densities, while `sp` (Scale-independent Pixels) is used for fonts[cite: 297, 298, 301].
* [cite_start]**Layout Reusability:** The `<include>` tag allows embedding one layout inside another, and the `<merge>` tag prevents redundant nested view groups during inclusion[cite: 345, 346, 382, 384].

## Chapter 4: Web Services & Client/Server Architecture
* [cite_start]**Application Types:** Outlines the differences between Native apps, Web apps (Mobile Friendly UI), and Hybrid apps (HTML/CSS/JS wrapped in a native container)[cite: 1190, 1191, 1201, 1203].
* [cite_start]**REST Architecture:** A stateless architecture where the client handles display and the server handles data logic[cite: 1251, 1254, 1255]. [cite_start]REST services typically return lightweight JSON payloads to optimize speed on mobile networks[cite: 1265, 1267].
* [cite_start]**Consuming REST Services:** Can be done using native `HttpURLConnection` or external libraries like Volley[cite: 1339, 1369, 1373]. [cite_start]Network operations must not run on the main thread, requiring asynchronous processing[cite: 1363, 1365].
* [cite_start]**Data Parsing:** Covers JSON parsing (`JSONObject`, `JSONArray`) and XML parsing (using DOM or `XmlPullParser`)[cite: 1388, 1396, 1414, 1418, 1434].
* **SOAP Architecture:** An architecture requiring tight coupling between client and server via a WSDL (Web Services Description Language) document; [cite_start]Android apps typically use the third-party `kSOAP2` library to interface with these[cite: 1461, 1464, 1467, 1468].

## Chapter 5: Advanced UI (Fragments & Lists)
* [cite_start]**Fragments:** Independent, reusable UI components with their own lifecycle that run within the context of a host Activity, making it easier to build responsive layouts for both phones and tablets[cite: 1519, 1520, 1521, 1523].
* [cite_start]**Fragment Management:** Handled by the `FragmentManager`, which can retain fragment states and manage navigation backstacks[cite: 1571, 1573, 1574].
* [cite_start]**List Principles:** Handled by combining a `ListView` (or similar container) with an `Adapter` to map data into UI row layouts[cite: 1626, 1633, 1640].
* [cite_start]**Advanced Lists:** Complex data requires extending `ArrayAdapter` or `BaseAdapter` and overriding the `getView()` method to map custom objects to complex row views[cite: 1686, 1693].
* [cite_start]**Optimization (ViewHolder Pattern):** To save CPU cycles and prevent lag, row views are recycled (using the `convertView` parameter), and view lookups (`findViewById`) are cached using a static `ViewHolder` class[cite: 1722, 1723, 1745, 1750, 1751].
* [cite_start]**Alternative List Views:** `GridView` (matrix display), `RecyclerView` (optimized modern list), `ExpandableListView`, and `CardView`[cite: 1761, 1777, 1778, 1784].

## Chapter 6: Intents (Intentions)
* [cite_start]**Intent Principle:** Objects used to delegate actions or pass messages between components, activities, or completely different applications[cite: 1805, 1806].
* [cite_start]**Explicit vs. Implicit:** Explicit Intents target a specific internal class directly, while Implicit Intents provide an Action and a Data URI, relying on the OS to find a suitable app[cite: 1821, 1825, 1826].
* [cite_start]**Passing Data:** Information is passed via key/value pairs using `putExtra()` and retrieved via `getExtras()` in a `Bundle`[cite: 1836, 1841].
* **Activity Results:** `startActivityForResult()` is used to launch an activity and wait for a return code. [cite_start]The child activity uses `setResult()` before finishing, and the parent intercepts the data in `onActivityResult()`[cite: 1848, 1849, 1871, 1876].
* [cite_start]**Intent Characteristics:** Built using standard System Actions (e.g., `ACTION_VIEW`, `ACTION_DIAL`) and Categories (e.g., `DEFAULT`, `BROWSABLE` for web links)[cite: 1881, 1897, 1907, 1912].

## Chapter 7: Broadcasts & Pending Intents
* [cite_start]**Broadcasting Information:** `sendBroadcast()` pushes an Intent outward so multiple applications/receivers can intercept it and read the `Extras`[cite: 1925, 1930, 1932].
* [cite_start]**System Broadcasts:** The OS broadcasts standard events like `ACTION_BOOT_COMPLETED`, `ACTION_SCREEN_ON`, or `ACTION_POWER_CONNECTED`[cite: 1938, 1939, 1940].
* [cite_start]**Filtering Intents:** Receivers declare an `<intent-filter>` in the Manifest, specifying the `<action>`, `<category>`, and `<data>` (scheme, host, mimeType) they care about[cite: 1949, 1951, 1952, 1953, 1958].
* [cite_start]**Broadcast Receivers:** Implementations must extend `BroadcastReceiver` and override `onReceive()` to handle incoming messages[cite: 1993, 2002].
* **Security Restrictions:** On Android 7.0+, many implicit broadcast filters in the manifest are restricted. [cite_start]Developers must dynamically register receivers using `registerReceiver()` via an active Context[cite: 2012, 2013, 2014].
* [cite_start]**Pending Intents:** A wrapper around an Intent that grants a foreign application (like the Android NotificationManager or AlarmManager) the right to execute that Intent on your application's behalf at a later time[cite: 2021, 2022, 2024, 2026].