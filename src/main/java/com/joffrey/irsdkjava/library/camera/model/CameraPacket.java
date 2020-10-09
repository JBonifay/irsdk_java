package com.joffrey.irsdkjava.library.camera.model;

import com.joffrey.irsdkjava.library.yaml.irsdkyaml.CamerasGroupsYaml;
import com.joffrey.irsdkjava.library.yaml.irsdkyaml.DriverInfoYaml;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CameraPacket {

    private List<CamerasGroupsYaml> cameraInfoList;
    private List<DriverInfoYaml>    cameraDriverPacketsList;

}
