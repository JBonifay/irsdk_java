package com.joffrey.irsdkjava.camera.model;

import com.joffrey.irsdkjava.yaml.irsdkyaml.CamerasGroupsYaml;
import com.joffrey.irsdkjava.yaml.irsdkyaml.DriverInfoYaml;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CameraPacket {

    private List<CamerasGroupsYaml> cameraInfoList;
    private List<DriverInfoYaml>    cameraDriverPacketsList;

}
