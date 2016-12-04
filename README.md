# SectionedRecyclerView
[ ![Download](https://api.bintray.com/packages/zhukic/maven/SectionedRecyclerView/images/download.svg) ](https://bintray.com/zhukic/maven/SectionedRecyclerView/_latestVersion)

A simple library that allows you to easily delineate sections of a list or grid list.

![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/name.png?raw=true)
![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/genre.png?raw=true)

#Setup
The Gradle dependency is available via [jCenter](https://bintray.com/zhukic/maven/SectionedRecyclerView). 

Add this to your `build.gradle` file.
```gradle
compile 'com.github.zhukic:sectioned-recyclerview:0.2.2'
```

#Usage
The main idea is to tell the adapter whether you want to place subheader between two neighboring items.
```java
@Override
public boolean onPlaceSubheaderBetweenItems(int itemPosition, int nextItemPosition) {
    final Movie movie1 = movieList.get(itemPosition);
    final Movie movie2 = movieList.get(nextItemPosition);

    //The subheader will be placed between two neighboring items if movie genres are different.
    return !movie1.getGenre().equals(movie2.getGenre());
}    
```
Also your items should be sorted. 
```java
Collections.sort(movieList, new Comparator<Movie>() {
    @Override
    public int compare(Movie o1, Movie o2) {
        return o1.getGenre().compareTo(o2.getGenre());
    }
});      
sectionedRecyclerAdapter = new Adapter(movieList);
```
Otherwise you will get something like this:

![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/notSortedItems.png?raw=true)
###Sample adapter
```java
public class Adapter extends SectionedRecyclerAdapter<SubheaderViewHolder, ItemViewHolder> {

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
    public boolean onPlaceSubheaderBetweenItems(int itemPosition, int nextItemPosition) {
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
        //nextItemPosition - position of item that placed after subheader
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
#Grid
If you are using a ```GridLayoutManager``` set it to adapter.
```java
GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
sectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
```
![Screenshots](https://github.com/zhukic/Sectioned-RecyclerView/blob/master/art/grid.png?raw=true)

#License

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
