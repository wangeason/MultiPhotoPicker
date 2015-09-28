package io.github.wangeason.multiphotopicker.entity;

/**
 * Created by donglua on 15/6/30.
 * Modified by wangeason on 15/9/24
 */
public class Photo {

  private int id;
  private String path;
    private int selectedTimes;

  public Photo(int id, String path) {
    this.id = id;
    this.path = path;
  }

  public Photo() {
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Photo)) return false;

    Photo photo = (Photo) o;

    return id == photo.id;
  }

  @Override public int hashCode() {
    return id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

    public int getSelectedTimes() {
        return selectedTimes;
    }

    public void setSelectedTimes(int selectedTimes) {
        this.selectedTimes = selectedTimes;
    }

    public void add(){
        selectedTimes ++;
    }

    public void del(){
        if(selectedTimes>0){
            selectedTimes --;
        }
    }
}
