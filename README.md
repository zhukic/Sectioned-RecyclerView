# SectionedRecyclerView
[ ![Download](https://api.bintray.com/packages/zhukic/maven/SectionedRecyclerView/images/download.svg) ](https://bintray.com/zhukic/maven/SectionedRecyclerView/_latestVersion)

A simple Android library that allows you to easily split your data into expanding/collapsing sections with headers.

![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/name.png?raw=true)
![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/decade.png?raw=true)

# Setup
The Gradle dependency is available via [jCenter](https://bintray.com/zhukic/maven/SectionedRecyclerView). 

Add this to your `build.gradle` file.
```gradle
compile 'com.github.zhukic:sectioned-recyclerview:1.1.0'
```

# Usage
The main idea is to tell the adapter whether you want to place subheader between two neighboring items.
```java
@Override
public boolean onPlaceSubheaderBetweenItems(int position) {
    final Movie movie = movieList.get(position);
    final Movie nextMovie = movieList.get(position + 1);
    
    //The subheader will be placed between two neighboring items if the first movie characters are different.
    return !movie.getName().substring(0, 1).equals(nextMovie.getName().substring(0, 1));
}
```
# Sample adapter
```java
public class Adapter extends SectionedRecyclerViewAdapter<SubheaderViewHolder, ItemViewHolder> {

    public static class SubheaderViewHolder extends RecyclerView.ViewHolder {

        public SubheaderViewHolder(View itemView) {
            super(itemView);
            //Setup holder
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
            //Setup holder
        }
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        //return true if you want to place subheader between two neighboring items
    }

    @Override
    public SubheaderViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    public MovieViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }
     
    @Override
    public void onBindSubheaderViewHolder(SubheaderViewHolder subheaderViewHolder, int nextItemPosition) {
        //Setup subheader view
        //nextItemPosition - position of the first item in the section to which this subheader belongs
        final Movie nextMovie = movieList.get(nextItemPosition);
        subheaderHolder.subheaderText.setText(nextMovie.getName().substring(0, 1));
    }
    
    @Override
    public void onBindItemViewHolder(MovieViewHolder holder, int position) {
        //Setup item view
    }

    @Override
    public int getCount() {
        //return the total number of items
    }
}
```
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
