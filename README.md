# SectionedRecyclerView
[ ![Download](https://api.bintray.com/packages/zhukic/maven/SectionedRecyclerView/images/download.svg) ](https://bintray.com/zhukic/maven/SectionedRecyclerView/_latestVersion)

A simple Android library that allows you to easily split your RecyclerView into expanding/collapsing sections with headers.

![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/name.png?raw=true)
![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/decade.png?raw=true)

# Setup
The Gradle dependency is available via [jCenter](https://bintray.com/zhukic/maven/SectionedRecyclerView). 

Add this to your `build.gradle` file.
```gradle
compile 'com.github.zhukic:sectioned-recyclerview:1.2.0'
```

# How it works
The main idea of the library is to determine whether to place a subheader between a pair of neighboring items. To do this, you need to implement the `onPlaceSubheaderBetweenItems` method, which returns `true` when you need to place a subheader between two neighboring items, `false` - otherwise.
```java
@Override
public boolean onPlaceSubheaderBetweenItems(int position) {
    final char movieTitleFirstCharacter = movieList.get(position).getTitle().charAt(0);
    final char nextMovieTitleFirstCharacter = movieList.get(position + 1).getTitle().charAt(0);
    
    //The subheader will be placed between two neighboring items if the first characters in movie titles are different.
    return movieTitleFirstCharacter != nextMovieTitleFirstCharacter;
}
```
# Sample
Please try out [sample app](https://github.com/zhukic/Sectioned-RecyclerView/tree/master/sample) for more details.

# Modify data
```java

notifyDataChanged();

notifyItemInsertedAtPosition(int);

notifyItemChangedAtPosition(int);

notifyItemRemovedAtPosition(int);

```
# Expanding and Collapsing Sections
```java

expandSection(int sectionIndex);

expandAllSections();

collapseSection(int sectionIndex);

collapseAllSections();

isSectionExpanded(int sectionIndex);

```
# Other methods
```java

boolean isSubheaderAtPosition(int adapterPosition);

int getSectionIndex(int adapterPosition);

int getItemPositionInSection(int adapterPosition);

boolean isFirstItemInSection(int adapterPosition);

boolean isLastItemInSection(int adapterPosition);

int getSectionSize(int sectionIndex);

int getSectionSubheaderPosition(int sectionIndex);

int getSectionsCount();

```
# Grid
If you are using a ```GridLayoutManager``` set it to adapter.
```java
GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
sectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
```
![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/grid.png?raw=true)

# License

    Copyright 2016 Vladislav Zhukov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
