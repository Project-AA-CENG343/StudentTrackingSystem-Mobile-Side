package com.example.birdaha.Helper;

public class ProfilePictureChangeEvent {
    private boolean profilePictureChanged;
    private boolean profilePictureDeleted;

    public ProfilePictureChangeEvent(boolean profilePictureChanged, boolean profilePictureDeleted) {
        this.profilePictureChanged = profilePictureChanged;
        this.profilePictureDeleted = profilePictureDeleted;
    }

    public boolean isProfilePictureChanged() {
        return profilePictureChanged;
    }

    public void setProfilePictureChanged(boolean profilePictureChanged) {
        this.profilePictureChanged = profilePictureChanged;
    }

    public boolean isProfilePictureDeleted() {
        return profilePictureDeleted;
    }

    public void setProfilePictureDeleted(boolean profilePictureDeleted) {
        this.profilePictureDeleted = profilePictureDeleted;
    }
}
