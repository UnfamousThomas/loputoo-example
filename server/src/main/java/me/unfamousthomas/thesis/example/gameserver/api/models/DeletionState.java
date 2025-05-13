package me.unfamousthomas.thesis.example.gameserver.api.models;

import com.google.gson.annotations.SerializedName;

public class DeletionState {
  @SerializedName("allowed")
  private boolean deleteAllowed;

  public DeletionState(boolean deleteAllowed) {
    this.deleteAllowed = deleteAllowed;
  }

  public boolean isDeleteAllowed() {
    return deleteAllowed;
  }
}
