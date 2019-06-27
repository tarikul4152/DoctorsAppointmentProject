package com.tarikul4152.doctorsappointment;

import android.Manifest;

public class PermissionGroup
{
    String[] CameraGroup={Manifest.permission.CAMERA};
    String[] StorageGroup={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] ContactGroup={Manifest.permission.READ_CONTACTS};
    String[] LocationGroup={Manifest.permission.ACCESS_FINE_LOCATION};

    public String[] getCameraGroup() {
        return CameraGroup;
    }

    public String[] getStorageGroup() {
        return StorageGroup;
    }

    public String[] getContactGroup() {
        return ContactGroup;
    }

    public String[] getLocationGroup() {
        return LocationGroup;
    }
}
